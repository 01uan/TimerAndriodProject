package com.example.giaapp;

public class Task {
    private long id;
    private String name;
    private int chunks;

    private boolean completed;

    public Task(String name, int chunks, boolean completed) {
        this.name = name;
        this.chunks = chunks;
        this.completed = completed;
    }

    public int getChunks() {
        return chunks;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public long getId() {
        return id;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
