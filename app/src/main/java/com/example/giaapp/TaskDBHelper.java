package com.example.giaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TaskDBHelper extends SQLiteOpenHelper {

    //initialize database name and version
    private static final String DB_NAME = "tasks.db";
    private static final int DB_VERSION = 1;

    //initialize columns
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CHUNKS = "chunks";
    private static final String COLUMN_COMPLETED = "completed";

    private SQLiteDatabase sqlDB;

    public TaskDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * connects to the database file for reading and writing
     */
    public void open() {
        sqlDB = getWritableDatabase();
    }

    public void close() {
        sqlDB.close();
    }


    /**
     * creates the table in the database
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CHUNKS + " INTEGER, " +
                COLUMN_COMPLETED + " BOOLEAN);";
        sqLiteDatabase.execSQL(createTable);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //occurs if the db version is higher than the db file's version
        //deleting the table and call onCreate to recreate everything
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long createTask(Task task) {
        ContentValues cvs = new ContentValues();
        cvs.put(COLUMN_NAME, task.getName());
        cvs.put(COLUMN_CHUNKS, task.getChunks());
        cvs.put(COLUMN_COMPLETED, task.isCompleted());

        //run the insert and retrieve the generated key
        long autoID = sqlDB.insert(TABLE_NAME, null, cvs);
        task.setId(autoID);
        return autoID;
    }

    public boolean updateTask(Task task) {
        ContentValues cvs = new ContentValues();
        cvs.put(COLUMN_NAME, task.getName());
        cvs.put(COLUMN_CHUNKS, task.getChunks());
        cvs.put(COLUMN_COMPLETED, task.isCompleted());
        return sqlDB.update(TABLE_NAME, cvs, COLUMN_ID + "=" + task.getId(), null) > 0;
    }

    public boolean deleteTask(Task task) {
        return sqlDB.delete(TABLE_NAME, COLUMN_ID + "=" + task.getId(), null) > 0;
    }

    public Cursor getAllTasks() {
        Cursor cursor = sqlDB.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CHUNKS, COLUMN_COMPLETED},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public ArrayList<Task> getAllTasksList() {
        Cursor cursor = getAllTasks();

        ArrayList<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getInt(2), cursor.getInt(3) != 0);
                task.setId(cursor.getLong(0));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        return tasks;
    }

    public Cursor getTask(long id) {
        Cursor cursor = sqlDB.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CHUNKS, COLUMN_COMPLETED},
                COLUMN_ID + "=" + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void deleteAllTasks() {
        sqlDB.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public Cursor getAllTasksWithSQL() {
        return sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
