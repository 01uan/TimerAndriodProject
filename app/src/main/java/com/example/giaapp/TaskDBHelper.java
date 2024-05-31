package com.example.giaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * TaskDBHelper class to manage database creation and version management.
 * This class provides methods for CRUD operations on the tasks database.
 */
public class TaskDBHelper extends SQLiteOpenHelper {

    // Initialize database name and version
    private static final String DB_NAME = "tasks.db";
    private static final int DB_VERSION = 1;

    // Initialize columns
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CHUNKS = "chunks";
    private static final String COLUMN_COMPLETED = "completed";

    private SQLiteDatabase sqlDB;

    /**
     * Constructor for TaskDBHelper.
     * @param context The context of the calling activity.
     */
    public TaskDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Connects to the database file for reading and writing.
     */
    public void open() {
        sqlDB = getWritableDatabase();
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        sqlDB.close();
    }

    /**
     * Creates the table in the database.
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CHUNKS + " INTEGER, " +
                COLUMN_COMPLETED + " BOOLEAN);";
        sqLiteDatabase.execSQL(createTable);
    }

    /**
     * Deletes the database.
     * @param context The context of the calling activity.
     */
    public void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }

    /**
     * Upgrades the database if the database version is higher than the database file's version.
     * Deletes the existing table and calls onCreate to recreate everything.
     * @param sqLiteDatabase The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Delete the table if it exists and recreate it
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Creates a new task in the database.
     * @param task The task to be created.
     * @return The ID of the created task.
     */
    public long createTask(Task task) {
        ContentValues cvs = new ContentValues();
        cvs.put(COLUMN_NAME, task.getName());
        cvs.put(COLUMN_CHUNKS, task.getChunks());
        cvs.put(COLUMN_COMPLETED, task.isCompleted());

        // Run the insert and retrieve the generated key
        long autoID = sqlDB.insert(TABLE_NAME, null, cvs);
        task.setId(autoID);
        return autoID;
    }

    /**
     * Updates an existing task in the database.
     * @param task The task to be updated.
     * @return True if the task was successfully updated, false otherwise.
     */
    public boolean updateTask(Task task) {
        ContentValues cvs = new ContentValues();
        cvs.put(COLUMN_NAME, task.getName());
        cvs.put(COLUMN_CHUNKS, task.getChunks());
        cvs.put(COLUMN_COMPLETED, task.isCompleted());
        return sqlDB.update(TABLE_NAME, cvs, COLUMN_ID + "=" + task.getId(), null) > 0;
    }

    /**
     * Deletes a task from the database.
     * @param task The task to be deleted.
     * @return True if the task was successfully deleted, false otherwise.
     */
    public boolean deleteTask(Task task) {
        return sqlDB.delete(TABLE_NAME, COLUMN_ID + "=" + task.getId(), null) > 0;
    }

    /**
     * Retrieves all tasks from the database.
     * @return A cursor containing all tasks.
     */
    public Cursor getAllTasks() {
        Cursor cursor = sqlDB.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CHUNKS, COLUMN_COMPLETED},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Retrieves all tasks from the database as an ArrayList.
     * @return An ArrayList containing all tasks.
     */
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

    /**
     * Retrieves a specific task from the database.
     * @param id The ID of the task.
     * @return A cursor containing the task.
     */
    public Cursor getTask(long id) {
        Cursor cursor = sqlDB.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_CHUNKS, COLUMN_COMPLETED},
                COLUMN_ID + "=" + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Deletes all tasks from the database.
     */
    public void deleteAllTasks() {
        sqlDB.execSQL("DELETE FROM " + TABLE_NAME);
    }

    /**
     * Retrieves all tasks from the database using raw SQL query.
     * @return A cursor containing all tasks.
     */
    public Cursor getAllTasksWithSQL() {
        return sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
