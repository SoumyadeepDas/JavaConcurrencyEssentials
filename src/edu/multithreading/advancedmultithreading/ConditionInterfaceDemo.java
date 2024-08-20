package edu.multithreading.advancedmultithreading;
/*
What is Condition?

The Condition interface in Java provides a way to handle complex thread synchronization using explicit locks (Lock), rather
than the intrinsic monitor methods (wait(), notify(), and notifyAll()) of the Object class. It offers more control and
flexibility.

Key Methods of Condition:

	1.	await(): Causes the current thread to wait until it is signaled or interrupted. Releases the associated lock while
	             waiting and re-acquires it before returning.
	2.	signal(): Wakes up one of the threads that are waiting on this condition. The choice of which thread to wake up is
	              not specified.
	3.	signalAll(): Wakes up all threads that are waiting on this condition.
	4.	awaitNanos(long nanosTimeout): Waits for the given time in nanoseconds or until signaled or interrupted. Returns
	                                   the remaining time if the wait was not satisfied.
	5.	awaitUntil(Date deadline): Waits until the specified deadline or until signaled or interrupted.

Creating a Condition with newCondition()

	•	Purpose: newCondition() creates a new Condition instance bound to the current Lock object. This allows you to
	             create multiple conditions for different scenarios within the same lock.
	•	Usage: Useful for implementing more complex synchronization strategies beyond what synchronized blocks and
	           wait/notify can offer.

Example Scenario

Let’s consider a classic producer-consumer problem where a producer thread generates data and a consumer thread processes
it. The shared resource is a bounded buffer (queue) that limits the number of items it can hold. We’ll use Condition to
handle the synchronization between producer and consumer threads.
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ProducerConsumerExample {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 10;  // Maximum capacity of the queue
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    // Producer method
    public void produce(int item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await(); // Wait until there is space in the queue
            }
            queue.add(item);
            System.out.println("Produced: " + item);
            notEmpty.signal(); // Notify that the queue is not empty
        } finally {
            lock.unlock();
        }
    }

    // Consumer method
    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // Wait until there is an item to consume
            }
            int item = queue.poll();
            System.out.println("Consumed: " + item);
            notFull.signal(); // Notify that the queue is not full
        } finally {
            lock.unlock();
        }
    }
}
public class ConditionInterfaceDemo {
    public static void main(String[] args) {
        ProducerConsumerExample pc = new ProducerConsumerExample();

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.produce(i);
                    Thread.sleep(100); // Simulate time taken to produce
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.consume();
                    Thread.sleep(150); // Simulate time taken to consume
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}

/*
Explanation:

	1.	Lock and Conditions:
	    •	lock: A ReentrantLock used to ensure that only one thread can access the critical section at a time.
	    •	notFull: A Condition object used to signal that the queue has space available.
	    •	notEmpty: A Condition object used to signal that the queue is not empty.
	2.	Producer:
        •	Acquires the lock and checks if the queue is full.
        •	If full, it calls notFull.await(), which releases the lock and waits.
        •	When space becomes available, it adds the item to the queue and signals notEmpty to wake up any waiting consumers.
	3.	Consumer:
        •	Acquires the lock and checks if the queue is empty.
        •	If empty, it calls notEmpty.await(), which releases the lock and waits.
        •	When items become available, it removes an item from the queue and signals notFull to wake up any waiting producers.
	4.	Synchronization:
        •	await() releases the lock and waits for a signal.
        •	signal() wakes up one waiting thread.
        •	signalAll() wakes up all waiting threads (not used in this example but useful in other scenarios).

Summary

	•	Condition Interface: Provides advanced thread synchronization with more flexibility than wait()/notify().
	•	newCondition() Method: Creates a new Condition instance associated with a Lock.
	•	Usage: Useful for complex synchronization scenarios, such as managing bounded buffers in producer-consumer problems.
 */
