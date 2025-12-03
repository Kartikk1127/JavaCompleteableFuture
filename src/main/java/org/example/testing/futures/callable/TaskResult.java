package org.example.testing.futures.callable;

public class TaskResult {
    private String name;
    public final int secs;

    public TaskResult(String name, int sec) {
        this.name = name;
        this.secs = sec;
    }
}
