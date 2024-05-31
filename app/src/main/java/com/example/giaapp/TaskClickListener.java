package com.example.giaapp;

/**
 * TaskClickListener interface to handle click events on Task objects.
 */
public interface TaskClickListener {
    /**
     * Called when a Task is clicked.
     * @param task The Task that was clicked.
     */
    void onTaskClick(Task task);
}
