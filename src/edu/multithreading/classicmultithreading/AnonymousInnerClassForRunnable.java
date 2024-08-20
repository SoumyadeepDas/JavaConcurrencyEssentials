package edu.multithreading.classicmultithreading;

public class AnonymousInnerClassForRunnable {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
        System.out.println(Thread.currentThread().getName());
    }
}
