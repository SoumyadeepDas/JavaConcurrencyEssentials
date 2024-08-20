package edu.multithreading.advancedmultithreading.executorservice;

import java.util.concurrent.*;

public class CallableDemo {
    public static void main(String[] args) throws ExecutionException {
        Runnable runnable = new Runnable() {
            public void run() {
                System.out.println("Printed from Runnable");
            }
        };
        //The problem with Runnable is that it only contains the public void run() method. It doesn't have any concurrency
        //techniques on it's own. We can pass a runnable to another thread or an ExecutorService which in turn, runs the
        //run method.
        // However, there's no way to get back, anything from the run() method as it's void method does not return.

        //The solution to the Runnable problem is Callable.
        /*
        1.	Generic Return Type:
	        •	Unlike Runnable, which cannot return a result (its run() method has a void return type), Callable allows
	            you to return a result after the task completes. The result can be of any type, and the return type is
	            specified using generics.
	        •	The call() method in Callable is defined as: V call() throws Exception; where V is the generic type parameter.
	    2.	Exception Handling:
	        •	The call() method can throw checked exceptions, giving you more flexibility in handling errors that might
	            occur during task execution. This is different from Runnable, where the run() method cannot throw checked
	            exceptions.
	    3.	Usage with ExecutorService:
	        •	When you submit a Callable task to an ExecutorService, it returns a Future<V> object, which represents the
	            result of the asynchronous computation.
	        •	You can use the Future.get() method to retrieve the result of the Callable once the computation is complete.

         */
        //The generic type parameters tell us the return type of our Callable.
        Callable<String> callable = new Callable<>() {
            @Override
            public String call() throws Exception {
                System.out.println("Printed from Callable: ");
                Thread.sleep(3000);
                return "Value from Callable";
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(runnable);
        //executorService.submit(callable);
        //String ret = executorService.submit(callable); we can't do this,
        // this is basically stopping the flow of our program as the ret variable is waiting for the return and the
        // main thread gets blocked waiting for the same.
        //We can only use the submit method to pass a callable.

        //So how do you get what the callable is returning?
        //It's by using a Future object. In Java, a Future<V> is an interface in the java.util.concurrent package that
        //represents the result of an asynchronous computation. When you submit a task to an ExecutorService using
        //either a Callable<V> or a Runnable task (via the submit() method), the method returns a Future<V> object,
        // which can be used to retrieve the result of the computation, check if the computation is complete, or cancel
        // the task.

        // Well, think of the future object as an empty box,
        // which will hold what callable will be returning in the future.

        //String ret = executorService.submit(callable); we can't do this, instead of this, we do this:

        Future<String> retFromFuture = executorService.submit(callable);
        System.out.println("Executing things in main thread:");
        System.out.println("While waiting for the return from Callable");
        //While we wait for the return from callable, we can process some other tasks in between.
        //And well since the get() method doesn't immediately return anything, it has to be surrounded by interrupted
        //exception as we know, when something waits, it has to be surrounded by an interrupted exception.
        try {
            System.out.println(retFromFuture.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // what the gate method actually does is that it will wait for the Callable to finish processing and to return
        // the value into that empty box.
        // Then it will extract the value from the box and will print it.
    }
}
