package edu.multithreading.classicmultithreading;
//This approach is not recommended.
class MyThread extends Thread {
    public void run() {
        System.out.println("MyThread "+Thread.currentThread().getName() +" is running");
    }
}

public class ExtendingThreadClassMain {
    public static void main(String[] args) {
        //Since MyThread is extending the Thread class, it itself is becoming eligible for running so we don't have to
        //pass any instance of runnable we can directly invoke the start() method on the MyThread instance.
        Thread t1 = new MyThread();
        t1.start();
        System.out.println(Thread.currentThread().getName() +" is running");
    }
}
