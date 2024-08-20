package edu.multithreading.classicmultithreading;
/* The volatile keyword in Java is used in the context of multithreading to ensure that the value of a variable is
always read from and written to the main memory. This keyword plays a crucial role in ensuring visibility of changes to
variables across threads, and it is also related to synchronization in terms of how threads interact with shared data.

Key Concepts of volatile:

	1.	Visibility:
	    •	In a multithreaded environment, each thread can have its own cache of variables. This means that when a thread
	        updates a variable, the change might not be immediately visible to other threads if they are reading from their
	        own cached version of that variable.
	    •	When you declare a variable as volatile, it ensures that any read or write to that variable is directly done in
	        the main memory, and not in the thread’s local cache. This ensures that when one thread updates the volatile
	        variable, all other threads see the updated value immediately.
	2.	Avoiding Caching:
	    •	Normally, to optimize performance, the Java Virtual Machine (JVM) might cache variables in CPU registers or
	        thread-local storage. This can lead to a situation where changes made by one thread are not visible to others.
	    •	The volatile keyword prevents such caching, making sure that the latest value is always read from main memory
	        and any update is immediately written back to main memory.
	3.	Relationship with Synchronization:
	    •	Synchronization in Java involves both mutual exclusion (ensuring only one thread can execute a block of code at
	        a time) and visibility (ensuring changes made by one thread are visible to others).
	    •	The volatile keyword only provides visibility guarantees. It does not provide mutual exclusion. This means that
	        while volatile ensures that all threads see the latest value of the variable, it does not prevent multiple
	        threads from modifying the variable concurrently, which could lead to race conditions.
	    •	volatile is lighter than synchronized, as it does not involve locking. However, if you need to both ensure
	        visibility and prevent race conditions, you should use synchronization (e.g., with synchronized blocks) instead
	        of or in addition to volatile.

When to Use volatile:

	•	Flags or Status Variables: volatile is often used for variables that act as flags or status indicators
	    (e.g., boolean isRunning), where one thread might set the flag, and others need to see the change immediately.
	•	Simple Counters or References: It can be used when you have a simple counter or a reference that multiple threads
	    read and write, but where race conditions aren’t a concern (e.g., a read-mostly scenario).
	•	In this example, one thread might be running the run() method, and another thread might call stop() to change flag
	    to false. Without volatile, the run() method might never see the updated value of flag because the value could be
	    cached in the thread’s local memory.

Limitations of volatile:

	•	No Atomicity: Operations like flag++ or flag = flag + 1 are not atomic, meaning they involve multiple steps
	    (read-modify-write). Even if flag is volatile, race conditions can still occur. In such cases, you should use
	    synchronized or atomic classes from java.util.concurrent.atomic (like AtomicInteger).

        In summary, the volatile keyword is a tool to ensure visibility of changes to variables across threads, preventing
        stale or inconsistent data being read by different threads. It is simpler and lighter than synchronization but is
        not a replacement for it when mutual exclusion is required.

Remember: IF 1 of the variable in a block is volatile for a thread, all other variables must also be volatile or
          effectively volatile(By default).
          YOU NEED TO BE CAREFUL ABOUT INSTRUCTION ORDERING WHEN USING VOLATILE.
	*/

public class VolatileKeywordDemoMain implements Runnable {
    // The volatile keyword ensures that changes to flag are visible to all threads.
    private volatile boolean flag = true;
    private int i = 1;

    @Override
    public void run() {
        System.out.println("Thread started: " + Thread.currentThread().getName());
        while (flag) {
            // Simulate some work
            try {
                Thread.sleep(100);// Sleep for 100 milliseconds
                System.out.println(Thread.currentThread().getName() + " is running, with iteration no: " + i++);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupted status
                System.out.println("Thread interrupted: " + Thread.currentThread().getName());
            }
        }
        System.out.println("Thread stopped: " + Thread.currentThread().getName());
    }

    // This method will stop the thread by setting flag to false
    public void stop() {
        flag = false;
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileKeywordDemoMain example = new VolatileKeywordDemoMain();

        // Start the thread that will run the 'run' method
        Thread thread = new Thread(example, "Worker Thread");
        thread.start();

        // Simulate some work in the main thread
        Thread.sleep(500);  // Main thread sleeps for 500 milliseconds

        System.out.println("Requesting stop...");
        // Stop the worker thread
        example.stop();

        // Wait for the worker thread to finish
        thread.join();
        System.out.println("Main thread finished.");
    }
}

/* Explanation:

	1.	Volatile Variable (flag):
	    •	The flag variable is declared as volatile. This ensures that when one thread changes the value of flag,
	        all other threads see the updated value immediately.
	2.	run() Method:
	    •	The run method is executed by the Worker Thread. It repeatedly checks the value of flag and performs some
	        work (simulated by Thread.sleep(100)).
	    •	The loop continues as long as flag remains true.
	3.	stop() Method:
	    •	The stop method sets the flag to false. This causes the loop in the run method to exit, which in turn stops
	        the thread.
	4.	Main Method (main):
	    •	The main method creates an instance of VolatileExample and starts a new thread that runs the run method.
	    •	After sleeping for 500 milliseconds, the main thread requests the worker thread to stop by calling example.stop().
	    •	The thread.join() ensures that the main thread waits for the worker thread to finish before printing
	        “Main thread finished.”

Behavior:

	•	The Worker Thread will start, and after approximately 500 milliseconds, the main thread will request it to stop
	    by setting flag to false.
	•	Because flag is volatile, the worker thread will immediately see this change, exit the loop, and stop its
	    execution. The program will then cleanly exit.

    This example illustrates how the volatile keyword ensures visibility across threads, making it suitable for cases
    where a simple flag is used to control the execution of threads. */
