package edu.multithreading.classicmultithreading;
/*
Understanding ThreadLocal<>:

ThreadLocal<> in Java is a mechanism that allows you to create variables that can only be read and written by the same
thread. Even if two threads are executing the same code and access the same ThreadLocal<> variable, they each will have
their own, independent copy of that variable.

Advantages of ThreadLocal<>:

	1.	Thread Confinement:
	    •	ThreadLocal<> is useful when you want to avoid using synchronization mechanisms like synchronized or Locks
	        but still need to ensure that a variable is thread-safe.
	    •	Each thread accessing the ThreadLocal<> variable gets its own isolated copy, so no synchronization is needed.
	2.	Simplifies Code:
	    •	It simplifies the code when dealing with variables that are meant to be thread-specific, as you don’t have
	        to manage multiple instances manually.
	3.	Performance:
	    •	Reduces contention between threads, especially when compared to synchronized blocks or methods, which can
	        improve performance in highly concurrent environments.
  */
public class ThreadLocalDemoMain {
    // Create a ThreadLocal variable for each thread to hold its own copy of a variable
    private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " initial value: " + threadLocalValue.get());
            threadLocalValue.set(threadLocalValue.get() + 1);
            System.out.println(Thread.currentThread().getName() + " updated value: " + threadLocalValue.get());
        };

        Thread thread1 = new Thread(task, "Thread 1");
        Thread thread2 = new Thread(task, "Thread 2");

        thread1.start();
        thread2.start();
    }
}

/*
Explanation of the Code:

	•	ThreadLocal<Integer> threadLocalValue:
	•	This variable is specific to each thread, meaning that each thread will see its own version of threadLocalValue.
	•	withInitial(() -> 1) sets the initial value of the ThreadLocal variable to 1 for each thread that accesses it
	    for the first time.
	•	Output:
	    •	Thread 1 will output:
	    •	Thread 2 will output:
	•	Both threads have independent values for threadLocalValue.
  */


/*
	•	Instance Variables vs. ThreadLocal<>:
	    •	Typically, instance variables are shared among all threads accessing an instance of a class. This can lead to
	        race conditions, where multiple threads modify the variable concurrently.
	    •	ThreadLocal<> is used when you want each thread to have its own separate instance of a variable, even though
	        the class might be shared across threads.
	•	Benefits of Using ThreadLocal<> for Instance Variables:
	    •	Thread Safety Without Synchronization: When you use ThreadLocal<> for instance variables, you can avoid
	                      synchronization, which can be costly in terms of performance.
	    •	Reduced Complexity: It reduces the need for locks and makes the code easier to reason about, as you don’t have
	                            to worry about different threads interfering with each other’s data.
	    •	Improved Performance: Since there’s no contention between threads, performance can improve in scenarios with
	                              high concurrency.
 */
//Understanding Instance vs ThreadLocal using a code:


class UserContext {
    // Use ThreadLocal to store user information specific to each thread
    private static ThreadLocal<String> currentUser = ThreadLocal.withInitial(() -> "Unknown");

    public void setCurrentUser(String user) {
        currentUser.set(user);
    }

    public String getCurrentUser() {
        return currentUser.get();
    }

    public static void main(String[] args) {
        UserContext context = new UserContext();

        Runnable task1 = () -> {
            context.setCurrentUser("Alice");
            System.out.println(Thread.currentThread().getName() + " current user: " + context.getCurrentUser());
        };

        Runnable task2 = () -> {
            context.setCurrentUser("Bob");
            System.out.println(Thread.currentThread().getName() + " current user: " + context.getCurrentUser());
        };

        Thread thread1 = new Thread(task1, "Thread 1");
        Thread thread2 = new Thread(task2, "Thread 2");

        thread1.start();
        thread2.start();
    }
}

/*
Explanation:

	•	currentUser is a ThreadLocal variable that stores the current user for each thread.
	•	Each thread can set and get its own user without affecting the other threads.
	•	Thread 1 and Thread 2 will each see their own values (Alice and Bob, respectively), and there will be no interference.

Conclusion: ThreadLocal<> is a powerful tool for handling thread-specific data in Java. It provides a simple and effective
            way to achieve thread confinement, improve performance, and reduce complexity in multithreaded applications
            by allowing each thread to have its own independent copy of a variable.
 */