package edu.multithreading.advancedmultithreading;
/*
The lockInterruptibly() method is part of the ReentrantLock class in the java.util.concurrent.locks package.
It allows a thread to acquire a lock, but it can be interrupted while waiting to acquire that lock. This is useful
in scenarios where you want to avoid a thread getting stuck indefinitely when it might be more important for the thread
to respond to an interrupt (e.g., to perform cleanup, exit, or handle other critical tasks).

Use Cases

	1.	Responsive Applications: In applications where threads need to remain responsive to interrupts, such as in UI
	    applications or responsive servers, using lockInterruptibly() ensures that the thread can be interrupted if it’s
	    taking too long to acquire a lock.
	2.	Graceful Shutdown: In long-running operations or server applications, using lockInterruptibly() allows you to
	    implement a graceful shutdown. If the application is asked to stop, any waiting threads can be interrupted, allowing
	    them to release resources and exit cleanly.
	3.	Deadlock Prevention: By using lockInterruptibly(), you can avoid deadlocks by ensuring that a thread can back out
	    if it cannot acquire a lock due to some unexpected situation.

 */

import java.util.concurrent.locks.ReentrantLock;

class LockInterruptibly {
    private final ReentrantLock lock = new ReentrantLock();

    public void performTask() {
        try {
            System.out.println(Thread.currentThread().getName() + " is attempting to acquire the lock.");
            lock.lockInterruptibly();  // Try to acquire the lock
            try {
                System.out.println(Thread.currentThread().getName() + " has acquired the lock.");
                // Simulate some work with the lock
                Thread.sleep(2000);
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " has released the lock.");
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted while waiting for the lock.");
        }
    }
}
public class LockInterruptiblyDemo {
    public static void main(String[] args) {
        LockInterruptibly demo = new LockInterruptibly();

        Runnable task = demo::performTask;

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();

        // Interrupt Thread-2 after 1 second
        try {
            Thread.sleep(1000);
            t2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
