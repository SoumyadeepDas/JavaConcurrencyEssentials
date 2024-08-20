package edu.multithreading.advancedmultithreading;
/*
Understanding tryLock(long time, TimeUnit unit) in Java

The tryLock(long time, TimeUnit unit) method is part of the ReentrantLock class in the java.util.concurrent.locks package.
This method attempts to acquire the lock within the specified waiting time. If the lock is available, the method returns
true and the lock is acquired. If the lock is not available, the thread waits for the specified time before giving up, and
returns false.

Use Cases

	1.	Timeout for Lock Acquisition: When you want to try to acquire a lock but don’t want to wait indefinitely, tryLock
	    allows you to specify a timeout. This is useful in scenarios where you want to avoid deadlocks or where waiting too
	    long to acquire a lock might be unacceptable.
	2.	Resource Management: In resource-constrained environments, where threads need to access shared resources, tryLock
	    can help manage resource contention by giving up after a certain time, freeing up the thread to do other tasks.
	3.	Graceful Degradation: If a thread fails to acquire a lock within the given time, it can gracefully degrade by
	    either retrying later or taking an alternative action, thus maintaining the application’s responsiveness.

 */


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class TimedDemo{
    private final ReentrantLock lock = new ReentrantLock();

    public void performTask() {
        try {
            System.out.println(Thread.currentThread().getName() + " is attempting to acquire the lock.");
            if (lock.tryLock(2, TimeUnit.SECONDS)) { // Try to acquire the lock within 2 seconds
                try {
                    System.out.println(Thread.currentThread().getName() + " has acquired the lock.");
                    // Simulate some work with the lock
                    Thread.sleep(3000);
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " has released the lock.");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire the lock within the specified time.");
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }
    }
}

public class TimedTryLockDemo {
    public static void main(String[] args) {
        TimedDemo demo = new TimedDemo();

        Runnable task = demo::performTask;

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();
    }
}

/*
Explanation

	1.	Two Threads: The code creates two threads, Thread-1 and Thread-2, both attempting to acquire the same lock.
	2.	tryLock with Timeout: Thread-1 tries to acquire the lock first and holds it for 3 seconds. Thread-2 tries to
	    acquire the lock using tryLock(2, TimeUnit.SECONDS), meaning it will wait up to 2 seconds to acquire the lock.
	3.	Timeout Handling: Since Thread-1 holds the lock for 3 seconds, Thread-2 will fail to acquire the lock within
	    its 2-second timeout and will print a message indicating it couldn’t acquire the lock.
	4.	Output: You’ll see that Thread-1 acquires and releases the lock, while Thread-2 attempts to acquire the lock
	    but fails after 2 seconds and continues with its execution.

This example demonstrates how tryLock(long time, TimeUnit unit) can be used to manage lock acquisition with a timeout,
allowing threads to avoid waiting indefinitely and handle scenarios where the lock cannot be acquired within a reasonable
time.
 */