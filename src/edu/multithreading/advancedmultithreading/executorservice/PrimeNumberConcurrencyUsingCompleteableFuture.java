package edu.multithreading.advancedmultithreading.executorservice;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PrimeNumberConcurrencyUsingCompleteableFuture {
    public static void main(String[] args) {

        while(true){
            System.out.println("Running by "+Thread.currentThread().getName() +": I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) break;
//            Runnable runnable = new Runnable() {
//                public void run() {
//                    int number = PrimeNumberUtil.calculatePrime(n);
//                    System.out.println("Result: ");
//                    System.out.println("\nValue of "+n+"th prime: "+number);
//                }
//            };
//            new Thread(runnable).start();

            //Using the Completeable Future API: using the supplyAsync() static method of CompletableFuture.
            //Async() method spawns a separate thread, and gives a supplier lambda and then once that returns a value,
            // a new lambda is called on that value:

            //"hello" is being returned by the supplyAsync() and then this return value is sent to thenAccept which
            // accepts a consumer lambda.
            //This way I don't have to check when the supplyAsync()'s gonna return a value, as soon as it returns a value,
            //it is sent to thenAccept() and thenAccept() does the remaining thing.
            // This way I don't need to wait like previously for a Future object to get value return from Callable.


            //CompletableFuture.supplyAsync(()->"hello").thenAccept(s -> System.out.println(s));

            CompletableFuture.supplyAsync(() -> PrimeNumberUtil.calculatePrime(n))
                    .thenAccept(getPrime -> System.out.println(n +"th prime number is: " + getPrime+ " and printed by "+Thread.currentThread().getName()));



        }
    }
}

class PrimeNumberConcurrencyUsingCompleteableFutureAndThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        while(true){
            System.out.println("Running by "+Thread.currentThread().getName() +": I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) break;

            //Suppose I want CompleteableFuture to leverage my threadpool, I need to pass executorService, as second param to
            //the suuplyAsync() method, that way I can control in which thread the supplier lambda runs.
            CompletableFuture.supplyAsync(() -> PrimeNumberUtil.calculatePrime(n), executorService)
                    .thenAccept(getPrime -> System.out.println(n +"th prime number is: " + getPrime+ " and printed by "+Thread.currentThread().getName()));

            //if we don't define the pool where my lambda's gonna run, then by default it will run inside a common threadpool
            //named as the ForkJoinPool.commonPool-worker-(workerNo).

        }
    }
}
