package edu.multithreading.classicmultithreading;

public class LambdasToRunThread {
    public static void main(String[] args) {
        //Runnable takes in a consumer lambda.
        Runnable runnable = () -> System.out.println(Thread.currentThread().getName());
        //runnable.run();

        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println(Thread.currentThread().getName());
    }
}

class LambdasToRunThreadMoreCompactWay {
    public static void main(String[] args) {
        //Runnable runnable = () -> System.out.println(Thread.currentThread().getName());
        //runnable.run();
        //Thread thread = new Thread(() -> System.out.println(Thread.currentThread().getName()));

        //thread.start();
        new Thread(() -> System.out.println(Thread.currentThread().getName())).start();//thread 0

        System.out.println(Thread.currentThread().getName()); // main thread
    }
}
