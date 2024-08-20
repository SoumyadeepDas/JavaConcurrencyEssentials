package edu.multithreading.classicmultithreading;


class Counter implements Runnable{
    private int value = 0;

    public void increment() {
        try{
            Thread.sleep(20);
        }
        catch (InterruptedException e) {
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
    public void run(){
        //What synchronized is doing?
        //synchronized is a special keyword in java whose work is to acquire the object-specific lock when an object ref is
        //passed as an argument.
        // Think of the lock as the key of a specific room in a hotel.
        // When a customer occupies the
        //hotel room, he takes with him the key and no one else has the access of that room until the customer checks out.
        //Now when a thread hits the synchronized block, it acquires the lock(key), the all other threads are not allowed to
        //get inside the block and the thread does it's work. After doing the work, the thread releases the lock(key) that it
        //has acquired. Now any other thread is allowed to enter the block. Once again when the other thread tries to enter the
        //block, it again acquires the lock so that no other threads are allowed to enter as long as the current thread is doing
        //it's task. This is the work of the "synchronized" block.

        //Steps are:
        //1. Thread tries to get access to the monitor lock.
        //2. If thread gets it, it executes the block
        //3. After execution, it releases the monitor lock when exiting the block.
        //4. Any other threads needs to wait as long as the present executing thread doesn't release the lock.

        synchronized (this) { //This refers to the monitor lock! Instance based locking OBJECT LEVEL LOCK.
            this.increment();
            System.out.println(Thread.currentThread().getName() + " increments: " + this.getValue());
            this.decrement();
            System.out.println(Thread.currentThread().getName() + " decrements: " + this.getValue());
        }

        //The code inside synchronized block is known as the critical section.

    }
}

public class SynchronisationDemoMain {
    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(counter,"One").start();
        new Thread(counter,"Two").start();
        new Thread(counter,"Three").start();
        new Thread(counter,"Four").start();

        Counter counter2 = new Counter();
        new Thread(counter2,"Counter 2 Thread").start();

    }
}

/*In the SynchronisationDemoMain code, the synchronization mechanism works per object instance, not across multiple instances.
Let me explain why counter2 does not synchronize with counter.

Understanding Synchronization:

	•	Object-Level Lock: When you use synchronized (this) in the Counter class, it locks the current instance of the
	    Counter object (i.e., this). This means that the synchronized block ensures that only one thread at a time can
	    execute the block on that particular instance of Counter.

	•	Instance-Specific: The lock is specific to the instance of the object. So, if two threads are working on the same
	    instance (like counter), they will synchronize with each other, ensuring that only one thread can enter the
	    synchronized block at a time.


Why counter2 Doesn’t Synchronize with counter:

	•	Different Instances, Different Locks: counter and counter2 are two separate instances of the Counter class. Each
	    instance has its own lock.
	•	When a thread executes the synchronized block on counter, it acquires the lock for the counter instance. Another
	    thread working on counter2 will acquire the lock for the counter2 instance. These locks are independent of each other.
	•	As a result, there is no synchronization between threads operating on counter and those operating on counter2.
	    They do not need to wait for each other because they are working on different locks.

Code Behavior:

	•	Threads on counter: The four threads (One, Two, Three, Four) are all working on the same Counter instance (counter).
	    These threads will synchronize with each other, ensuring that only one thread can execute the synchronized block
	    at a time.
	•	Thread on counter2: The thread Counter 2 Thread operates on a different Counter instance (counter2). It has its
	    own lock, so it doesn’t synchronize with the other four threads working on counter.

Key Takeaway:

	•	Synchronization is Per Instance: Since counter and counter2 are separate instances, their synchronization
	    mechanisms are independent. If you want synchronization across multiple instances, you would need to use a common
	    lock object shared across those instances or synchronize on a class-level lock (e.g., synchronized (Counter.class)).
	     */


/*      synchronized (Counter.class) {  // Synchronizes on the class itself, making it global
        this.increment();
        System.out.println(Thread.currentThread().getName() + " increments: " + this.getValue());
        this.decrement();
        System.out.println(Thread.currentThread().getName() + " decrements: " + this.getValue());
} */

//Synchronized keyword can also be used for a method. Example: public synchronized increment().
//This would work in the same way as we were synchronizing the block. It also translates to synchronizing this. The method
//becomes synchronized for object level lock.

/*What does synchronization achieves?
* Ans: 2 things:
*           i. Mutex (Mutual exclusion): Means only a single thread can operate on any piece of code at given time.
*          ii. Visibility: It ensures that, before entering the synchronized block "Value is read from memory before
*                          block execution. And after execution value is written into the main memory.  */

/* Synchronisation (synchronized) block provides STRUCTURED LOCK meaning, synchronizing a block ensures that the lock
* is acquired and released implicitly, we as devs, don't have to worry about anything.
* Benefit: Exception causing control to exit: Lock auto released, we don't have to do anything. */

/*How to handle concurrency issues? Or potentially when can be your code be called thread safe?
* Ans: Three things: 1. Not having a shared state
*                    2. Share only immutable values.
*                    3. Use synchronization */

/*Disadvantages of using a synchronized block:
* 1. Performance issue
* 2. we have to use carefully
* 3. we need to choose the right object for the lock
* 4. The bare minimum code required to be sunchronized
* 5. Choosing the wrong lock.
* 6. Extreme synchronisation results in non concurrent (serial) code.
* 7. Liveness */

/*What the heck is this Liveness?
* Liveness in the context of an application refers to the property that ensures the application remains responsive and
  continues to make progress in executing its tasks. It is a key aspect of concurrent and distributed systems, where
  multiple threads or processes are involved.

Key Concepts Related to Liveness:

	1.	Responsiveness:
	    •	Liveness ensures that the application responds to user inputs or external events within a reasonable time frame.
	    •	For example, in a GUI application, liveness means the interface remains interactive, and the application
	        doesn’t freeze.
	2.	Progress:
	    •	The application must continue to make progress towards completing its tasks or operations.
	    •	For instance, a background task like file processing should eventually complete rather than being stuck
	        indefinitely.
	3.	Avoidance of Liveness Issues:
	•	Deadlock: A situation where two or more threads are blocked forever, waiting for each other to release resources.
	              Deadlock is a liveness issue because no further progress can be made.
	•	Livelock: A situation where two or more threads continuously change their state in response to each other without
	              making any progress. Although the threads are not blocked, they are still not making any meaningful progress.
	•	Starvation: A situation where a thread is perpetually denied access to resources, preventing it from completing
	                its task. While other threads may be making progress, the starved thread is not, leading to a liveness
	                problem.

Liveness in Concurrency:

In a concurrent application, ensuring liveness involves careful management of thread synchronization, resource allocation,
and error handling to prevent scenarios where the application becomes unresponsive or fails to make progress.

For example:

	•	Locks: Using locks to manage resource access between threads must be done carefully to avoid deadlock, which
	           would prevent liveness.
	•	Resource Management: Ensuring that threads have fair access to shared resources can prevent starvation and ensure
	                         all parts of the application can progress.

Liveness vs. Safety:

	•	Liveness is about ensuring the application continues to do something useful and makes progress.
	•	Safety is about ensuring that the application doesn’t do anything wrong or inconsistent (e.g., data corruption).

In summary, liveness is a crucial property for the smooth operation of an application, particularly in systems where
concurrency or parallelism is involved. Ensuring liveness helps in maintaining responsiveness and overall application
stability. */

/*Livelock: A naive solution to deadlock
*           Try to get lock 1.
*           Try to get Lock 2.
*           If lock 2 not required in x ms, release lock 1
*           Try again after sometime
*
* Think of it like this two Persons, walking opposite to each other one person,
* seeing the other steps to the right so that they don't collide
* and the other person steps to the left so that he does not collide with Person one now again,
* the first person steps to the left, after seeing the second person stepping to the left,
* the first person when he stepped to the left, the second person stepped to the right and they again kinda collide.
* They did not want to, so this is kind of a situation is known as LiveLock.
* Example of livelock: Stalemate in chess*/

//NO JVM features to avoid these type of problems.
