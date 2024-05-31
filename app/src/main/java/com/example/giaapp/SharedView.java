package com.example.giaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * SharedView is a ViewModel that holds shared data for the application.
 * It contains a list of tasks that can be observed and updated by various components of the app.
 */
public class SharedView extends ViewModel {

    private final MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>();

    /**
     * Sets the list of tasks.
     * @param tasks The new list of tasks to be set.
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks.setValue(tasks);
    }

    /**
     * Returns the current list of tasks as LiveData.
     * @return A LiveData object containing the list of tasks.
     */
    public LiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }
}
