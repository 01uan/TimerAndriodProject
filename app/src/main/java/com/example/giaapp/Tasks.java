package com.example.giaapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Tasks extends Fragment {

    EditText etChunks;
    ArrayList<Task> tasks;
    RecyclerView rvTasks;
    TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etChunks = view.findViewById(R.id.etChunks);
        rvTasks = view.findViewById(R.id.rvTasks);
        tasks = new ArrayList<>();

        tasks.add(new Task("Task 1", 5));
        tasks.add(new Task("Task 2", 2));
        tasks.add(new Task("Task 3", 4));
        tasks.add(new Task("Task 3", 4));
        tasks.add(new Task("Task 3", 4));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(getContext(), tasks);
        rvTasks.setAdapter(adapter);


    }
}