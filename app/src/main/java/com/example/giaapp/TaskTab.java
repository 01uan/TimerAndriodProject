package com.example.giaapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskTab extends Fragment implements TaskAdapter.onItemClickListener {

    private TaskDBHelper db;

    private EditText etChunks, etTaskName;
    private ArrayList<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;
    private Button btnForm, btnDelete;
    private int currentTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

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

    public void refreshData() {
        Cursor cursor = db.getAllTasks();
        tasks.clear();
        while (cursor.moveToNext()) {
            Task task = new Task(cursor.getString(1), cursor.getInt(2));
            task.setId(cursor.getLong(0));
            tasks.add(task);
        }
        adapter.notifyDataSetChanged();
    }

    public void handleBtn(View view) {
        String name = etTaskName.getText().toString();
        int chunks = etChunks.getText().toString().isEmpty() ? -1 : Integer.parseInt(etChunks.getText().toString());

        if (btnForm.getText().toString().equals("Add")) {
            //error check for input
            if (chunks == -1 || name.isEmpty()) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(name, chunks);
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
    }

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

    @Override
    public void onItemClick(View view, int position) {
        currentTask = position;
        etTaskName.setText(tasks.get(position).getName());
        etChunks.setText(String.valueOf(tasks.get(position).getChunks()));
        btnForm.setText("Update");
        btnDelete.setVisibility(View.VISIBLE);
    }

}