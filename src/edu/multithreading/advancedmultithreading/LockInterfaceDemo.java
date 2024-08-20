package edu.multithreading.advancedmultithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Counter implements Runnable {
    private int value = 0;

    private Lock l = new ReentrantLock(); //Creating my own ReentrantLock using the built in Lock Interface
    //A ReentrantLock is a type of lock provided in the java.util.concurrent.locks package that offers more advanced
    //locking mechanisms than the built-in synchronized keyword. As the name suggests, it is reentrant, meaning that the
    // thread that currently holds the lock can acquire it again without causing a deadlock.
    public void increment() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value++;
    }

    public void decrement() {
        value--;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void run() {
        //synchronized (this) { //This refers to the monitor lock! Instance based locking OBJECT LEVEL LOCK.
        l.lock(); //calling the lock() method to acquire a lock on this object.
        try {
            this.increment();
            System.out.println(Thread.currentThread().getName() + " increments: " + this.getValue());
            this.decrement();
            System.out.println(Thread.currentThread().getName() + " decrements: " + this.getValue());
        }
        finally {
            l.unlock(); //releasing the lock acquired by invoking the unlock() method.
            //This should be in finally block as,
            // even if my code in side the try block throws an exception, it will still release the lock
            // and continue. We could have used try with resources too.
        }

    }
}


public class LockInterfaceDemo {
    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(counter,"One").start();
        new Thread(counter,"Two").start();
        new Thread(counter,"Three").start();
        new Thread(counter,"Four").start();

        Counter counter2 = new Counter();
        new Thread(counter2,"Counter 2(Second instance) Thread").start();

    }
}
