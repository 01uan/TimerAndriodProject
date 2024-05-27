package com.example.giaapp;

public class Task {
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
}
