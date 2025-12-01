package org.example.testing.futures.callable;

public class TaskResult {
    private String name;
    private final int secs;

    TaskResult(String name, int sec) {
        this.name = name;
        this.secs = sec;
    }
}
