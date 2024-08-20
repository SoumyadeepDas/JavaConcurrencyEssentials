package edu.multithreading.advancedmultithreading.executorservice;

import edu.multithreading.nthprime.PrimeNumberUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

//When does an application end?
//Typical ans: When the last thread ends.
//But in case of concurrency, the application will end when the last thread that has been spawned ends that means,
//the main's going to wait until the last thread finishes, then the main's going to stop its execution.
public class PrimeNumberConcurrencyUsingCallableAndFuture {
    public static void main(String[] args) {
        List<Future<Integer>> futures = new ArrayList<>(); // Creating array-list of Future objects to hold the value
        //of the prime numbers returned from the Callable.

        while(true){
            System.out.println("I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) break;
            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return PrimeNumberUtil.calculatePrime(n);
                }
            };
            ExecutorService executorService = Executors.newCachedThreadPool();
            Future<Integer> primeNumberFuture = executorService.submit(callable);
            futures.add(primeNumberFuture); //adding the returned primeNumbers from the Callable to ArrayList of futures.
//            try{
//                System.out.println(n + "th prime number is "+primeNumberFuture.get()); //This line will technically
//                // block the execution of the thread if we put in a large number to calculate prime as, every prime
//                // number is being returned to the Future<Integer> primeNumberFuture object, and the callable couldn't
//                // finish the calculation and return the result of the prime number to the future object, the get() won't
//                // work as until the Future object is populated. So, if a large value of n is given, it's technically
//                // going to block the flow until the calculation gets complete.
//            }
//            catch (InterruptedException | ExecutionException e){ //syntax for multi catch block introduced in Java 7
//                System.out.println(e.getMessage());
//            }
//            finally {
//                executorService.shutdown();
//            }
            Iterator<Future<Integer>> iterator = futures.iterator();
            while(iterator.hasNext()) {
                Future<Integer> future = iterator.next();
                if (future.isDone()) {
                    try {
                        System.out.println(n + "th prime number is " + future.get());
                        futures.remove(future);
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
