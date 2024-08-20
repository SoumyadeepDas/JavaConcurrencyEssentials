package edu.multithreading.advancedmultithreading;

import edu.multithreading.nthprime.PrimeNumberUtil;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class SemaphoresDemoMain {
    public static void main(String[] args) {
        //The difference between semaphore and lock is that, Lock allows only one thread to acquire it and get inside
        //the code while Semaphore can be used by multiple threads simultaneously, well not technically unlimited but
        //the number of threads that we define in it's constructor when creating the object of Semaphore.
        //Semaphore is kinda "permitted access"
        Semaphore semaphore = new Semaphore(3, true);
        //public Semaphore(int permits, boolean fair) Another constructor for Semaphore:
        //1. Semaphore Overview:
        //
        //	•	A Semaphore is a concurrency utility in Java that controls access to a resource by multiple threads.
        //  	It maintains a set of permits, and each thread that wants to access the resource must acquire a permit.
        //  	If no permits are available, the thread will wait until one becomes available.
        //	•	The Semaphore class is part of the java.util.concurrent package, which provides high-level concurrency
        //  	utilities.
        //
        //2. Constructor Parameters:
        //
        //	•	int permits: This parameter specifies the number of permits that the Semaphore will manage.
        //	•	For example, if you create a Semaphore with 3 permits, up to 3 threads can acquire a permit at the same
        //  	time. If all permits are taken, additional threads will block until a permit is released by another thread.
        //	•	boolean fair: This parameter specifies whether the Semaphore should use a fair or non-fair ordering policy.
        //	•	Fair Semaphore: If fair is set to true, the Semaphore ensures that threads acquire permits in the order
        //  	they requested them (first-come, first-served). This fairness guarantees that no thread will be starved while waiting for a permit.
        //	•	Non-Fair Semaphore: If fair is set to false, the Semaphore does not guarantee any particular order in
        //  	which threads will acquire permits. This can be more efficient in terms of throughput, but it can lead to thread starvation, where some threads may wait indefinitely if other threads continuously acquire permits.
        //
        //3. How the Constructor Works:
        //
        //	•	When you call new Semaphore(int permits, boolean fair), you are creating a new Semaphore object with a
        //  	specific number of permits and an optional fairness policy.

        while(true){
                System.out.println("I can tell you the nth prime number. Just Enter the value of n: ");
                int n = new Scanner(System.in).nextInt();
                if (n == 0) break;
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            semaphore.acquire(); //semaphore.acquireUninterruptibly();: It will only acquire if there are
                            // no interrupts, so it doesn't need a try-catch block.
                            //semaphore.acquire(2); We can acquire and release multiple permits
                            System.out.println("Now calculating for n: "+n);
                            int number = PrimeNumberUtil.calculatePrime(n);
                            System.out.println("\nValue of " + n + "th prime: " + number);
                        }
                        catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        finally {
                            semaphore.release();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
        }
    }
}
