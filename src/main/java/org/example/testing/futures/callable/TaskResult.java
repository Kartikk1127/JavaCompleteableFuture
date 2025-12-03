package org.example.testing.futures.callable;

public class TaskResult {
    public String name;
    public final int secs;

    public TaskResult(String name, int sec) {
        this.name = name;
        this.secs = sec;
    }
}
