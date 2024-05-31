package com.example.giaapp;

/**
 * Task class represents a task with a name, number of chunks, and completion status.
 */
public class Task {
    private long id;
    private String name;
    private int chunks;
    private boolean completed;

    /**
     * Constructs a new Task with the specified name, number of chunks, and completion status.
     * @param name The name of the task.
     * @param chunks The number of chunks (units of work) for the task.
     * @param completed The completion status of the task.
     */
    public Task(String name, int chunks, boolean completed) {
        this.name = name;
        this.chunks = chunks;
        this.completed = completed;
    }

    /**
     * Returns the number of chunks for the task.
     * @return The number of chunks.
     */
    public int getChunks() {
        return chunks;
    }

    /**
     * Returns the name of the task.
     * @return The task name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the ID of the task.
     * @param id The task ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the name of the task.
     * @param name The task name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the number of chunks for the task.
     * @param chunks The number of chunks.
     */
    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    /**
     * Returns the ID of the task.
     * @return The task ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the completion status of the task.
     * @param completed The completion status.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Returns the completion status of the task.
     * @return True if the task is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }
}
