package edu.multithreading.advancedmultithreading;
/*
tryLock() is a method provided by the ReentrantLock class in Java’s java.util.concurrent.locks package. It is used to
attempt to acquire a lock without waiting indefinitely. This method is particularly useful when you want to attempt to
perform some task that requires a lock, but only if the lock is immediately available, without blocking the thread.

How tryLock() Works:

	•	Immediate Acquisition: When a thread calls tryLock(), it tries to acquire the lock immediately. If the lock is
	                           available, the thread acquires it and the method returns true.
	•	Non-blocking: If the lock is not available (i.e., another thread is holding it), tryLock() returns false immediately
	                  without blocking the thread. This allows the thread to continue with other tasks or try again later.

Advantages of tryLock():

	1.	Avoid Deadlocks: By using tryLock(), you can avoid situations where a thread might block indefinitely, waiting for a
	                     lock, leading to potential deadlocks.
	2.	Time-Sensitive Operations: It is useful in scenarios where a thread needs to perform an operation only if it can
	                               acquire the lock immediately, such as in real-time systems.
	3.	Flexibility: It provides more control over lock acquisition and allows the program to decide what to do if the lock
	                 is not available (e.g., try again later, perform a different task, etc.).

 */

import java.util.concurrent.locks.ReentrantLock;

class TryLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    private int value = 0;

    public void increment() {
        try {
            Thread.sleep(50); // Simulate some work
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

    public void run() {
        if (lock.tryLock()) {  // Attempt to acquire the lock without waiting
            try {
                increment();
                System.out.println(Thread.currentThread().getName() + " increments: " + getValue());
                decrement();
                System.out.println(Thread.currentThread().getName() + " decrements: " + getValue());
            } finally {
                lock.unlock();  // Always release the lock
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " could not acquire the lock, doing other work.");
        }
    }
}
public class TryLockDemo {
    public static void main(String[] args) {
        TryLockExample example = new TryLockExample();
        new Thread(example::run, "Thread 1").start();
        new Thread(example::run, "Thread 2").start();

        /*
        Explanation of the Code:

	•	lock.tryLock(): Each thread tries to acquire the lock. If the lock is available, it enters the try block and
	                    performs the increment and decrement operations.
	•	else: If the lock is not available, the thread simply prints a message and does something else (in this example,
	          just skips the critical section).

Benefits:

	•	No Waiting: If Thread 1 is holding the lock, Thread 2 will not wait; instead, it can continue with other tasks.
	•	Efficiency: This can improve the efficiency of the program by allowing threads to avoid waiting and keep the CPU
	                busy with other tasks.
         */
    }
}
