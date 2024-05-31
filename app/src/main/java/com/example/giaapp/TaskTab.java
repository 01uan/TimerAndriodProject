package com.example.giaapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * TaskTab class that handles the task management functionality within the application.
 * It provides methods for adding, updating, and deleting tasks, as well as displaying the list of tasks.
 */
public class TaskTab extends Fragment implements TaskAdapter.OnItemClickListener {

    private SharedView sharedView;
    private TaskDBHelper db;
    private EditText etChunks, etTaskName;
    private ArrayList<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;
    private Button btnForm, btnDelete;
    private int currentTask;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedView = new ViewModelProvider(requireActivity()).get(SharedView.class);
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etChunks = view.findViewById(R.id.etChunks);
        etTaskName = view.findViewById(R.id.etTaskName);
        RecyclerView rvTasks = view.findViewById(R.id.rvTasks);
        btnForm = view.findViewById(R.id.btnForm);
        btnDelete = view.findViewById(R.id.btnDelete);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(getContext(), tasks);
        adapter.setClickListener(this);
        rvTasks.setAdapter(adapter);

        db = new TaskDBHelper(getContext());
        db.open();
        refreshData();

        btnForm.setOnClickListener(this::handleBtn);
        btnDelete.setOnClickListener(this::handleDelete);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * Refreshes the task data and updates the UI.
     */
    public void refreshData() {
        Cursor cursor = db.getAllTasks();
        ArrayList<Task> newTasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getInt(2), cursor.getInt(3) != 0);
                task.setId(cursor.getLong(0));
                newTasks.add(task);
            } while (cursor.moveToNext());
        }
        tasks.clear();
        tasks.addAll(newTasks);
        sharedView.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    /**
     * Handles the button click for adding or updating a task.
     * @param view The view that was clicked.
     */
    public void handleBtn(View view) {
        String name = etTaskName.getText().toString();
        int chunks = etChunks.getText().toString().isEmpty() ? -1 : Integer.parseInt(etChunks.getText().toString());

        if (btnForm.getText().toString().equals("Add")) {
            // Error check for input
            if (chunks == -1 || name.isEmpty()) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(name, chunks, false);
            tasks.add(task);
            db.createTask(task);

        } else {
            Task task = tasks.get(currentTask);
            task.setName(name);
            task.setChunks(chunks);
            db.updateTask(task);
            btnForm.setText("Add");
        }

        adapter.notifyDataSetChanged();
        etChunks.setText("");
        etTaskName.setText("");
        btnDelete.setVisibility(View.GONE);
    }

    /**
     * Handles the button click for deleting a task.
     * @param view The view that was clicked.
     */
    public void handleDelete(View view) {
        Task task = tasks.get(currentTask);
        db.deleteTask(task);
        tasks.remove(currentTask);
        adapter.notifyDataSetChanged();
        etChunks.setText("");
        etTaskName.setText("");
        btnForm.setText("Add");
        btnDelete.setVisibility(View.GONE);
    }

    /**
     * Called when a task item is clicked.
     * @param view The view that was clicked.
     * @param position The position of the clicked item.
     */
    @Override
    public void onItemClick(View view, int position) {
        currentTask = position;
        etTaskName.setText(tasks.get(position).getName());
        etChunks.setText(String.valueOf(tasks.get(position).getChunks()));
        btnForm.setText("Update");
        btnDelete.setVisibility(View.VISIBLE);
    }
}
