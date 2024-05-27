package com.example.giaapp;

public class Task {
    private long id;
    private String name;
    private int chunks;

    public Task(String name, int chunks) {
        this.name = name;
        this.chunks = chunks;
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
}
