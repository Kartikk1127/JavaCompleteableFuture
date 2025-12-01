package org.example;

public class Main {
    public static void main(String[] args) {
//        Thread thread = new SimpleThread("simple", 5);
//        thread.start();

//        Runnable r = new SimpleRunnable();
//        Thread thread = new Thread(r);
//        thread.start();

        Runnable r = new SimpleRunnable();
        Thread.ofPlatform().name("simple").daemon(false).start(r);


    }
}