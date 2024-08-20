package edu.multithreading.advancedmultithreading.executorservice;

import java.util.Scanner;
import java.util.concurrent.*;

/*
ExecutorService is an interface. Executors is a class that provides concrete implementations of the interface.

ExecutorService in Java is a part of the java.util.concurrent package and provides a higher-level replacement for working
with threads directly. It simplifies the process of managing a pool of worker threads to execute tasks concurrently,
offering a more flexible and powerful way to handle thread management.

Key Concepts and Features:

	1.	Task Submission:
	    •	Runnable and Callable: You can submit tasks to an ExecutorService using either Runnable (which does not return
	        a result) or Callable (which can return a result and throw exceptions).
	    •	Methods like submit(), invokeAll(), and invokeAny() allow you to submit tasks for execution.

	2.	Types of ExecutorService:
	    •	Fixed Thread Pool: A fixed number of threads are created and reused.
	    •	Cached Thread Pool: Creates new threads as needed and reuses previously created threads.
	    •	Single Thread Executor: A single worker thread executes tasks sequentially.
	    •	Scheduled Executor: Supports scheduling tasks to run after a delay or periodically.
	    •	Work Stealing Thread pool executor: Added later in Java 7, will discuss later.

	 3.	Managing Lifecycle:
	    •	Shutdown: You can gracefully shut down the ExecutorService using shutdown(), which stops accepting new tasks
	        but allows previously submitted tasks to complete.
	    •	Immediate Shutdown: If you need to stop all running tasks immediately, you can use shutdownNow().
	    •	Await Termination: After calling shutdown(), you can use awaitTermination() to block until all tasks have
	        completed or a timeout occurs.
	 4.	Handling Results:
	    •	Future: When you submit a Callable or Runnable to an ExecutorService, it returns a Future object, which
	        represents the pending result of the task. You can use methods like get() to retrieve the result or check
	        if the task is completed.
	 5.	Advantages:
	    •	Thread Management: It abstracts away the complex details of managing thread creation, execution, and termination.
	    •	Scalability: By using a thread pool, it can efficiently manage resources and scale up or down based on demand.
	    •	Ease of Use: Provides a more structured and easy-to-use API for concurrent programming.

 */
public class PrimeNumberConcurrencyUsingThreadPoolAndExecutorService {
    public static void main(String[] args) {

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);//This pool size of 1
        //is the initial size, doesn't mean it's restricted to this size only, if it needs, it'll increase the threadpool
        //size accordingly

        //ExecutorService executorService = Executors.newFixedThreadPool(3);
        //ExecutorService executorService = Executors.newCachedThreadPool(); //spawns thread as and when required.
        //ExecutorService executorService = Executors.newSingleThreadExecutor();
        //ExecutorService executorService = Executors.newFixedThreadPool(3);

        //Instead of using ExecutorService interface as a reference type,
        // I'm using ThreadPoolExecutor as a reference here because we're supposed to call the getActiveCount()
        // & getCompletedTaskCount() method on the reference,
        // and these methods can be found in ThreadPoolExecutor class. So we're taking the reference of the
        // ThreadPoolExecutor type and calling the newFixedThreadPool and casting it to ThreadPoolExecutor.
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        Runnable reporterRunnable = () -> {
            System.out.println("Running Report");
            System.out.println("Active threads : " + executorService.getActiveCount());
            System.out.println("Completed threads : " + executorService.getCompletedTaskCount());
        };

        scheduledExecutorService.scheduleAtFixedRate(reporterRunnable,1,5, TimeUnit.SECONDS);


        while(true){
            System.out.println("I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) break;
            Runnable runnable = new Runnable() {
                public void run() {
                    int number = PrimeNumberUtil.calculatePrime(n);
                    System.out.println("Result: ");
                    System.out.println("\nValue of "+n+"th prime: "+number);
                }
            };
            //new Thread(runnable).start();
            executorService.execute(runnable);

        }
    }
}
