package com.example.giaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedView extends ViewModel {
    private final MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>();

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks.setValue(tasks);
    }

    public LiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }
}
