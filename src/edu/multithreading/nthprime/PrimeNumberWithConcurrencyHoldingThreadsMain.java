package edu.multithreading.nthprime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrimeNumberWithConcurrencyHoldingThreadsMain {
    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        //Interrupt in java is soft interrupt.
        Runnable statusReporter = () -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    printThreads(threads);
                }
                catch (InterruptedException e) {
                    System.out.println("Status report interrupted. Ending status updates.");
                    break;
                }
                //printThreads(threads); //The status reporter thread was not stopping because after invoking the
                // interrupt() method on statusReporterThread, the catch block is catching the interrupted exception but
                //since the printThreads(threads) method is outside the try and catch block, and the loop is an infinite loop
                //it goes on calling the printThreads method with the same old value. To mitigate this issue, we need to
                //put this printThreads(threads) inside the try block.
            }

            /*How the Status Reporter Stops:

	        1.	Interrupting the Thread:
	            •	When you enter 0, the program calls statusReporterThread.interrupt(). This sets an interrupt flag on
	                the thread, which causes it to be interrupted if it is sleeping or waiting.
	        2.	Catching the InterruptedException:
	            •	If the thread is interrupted during the Thread.sleep(5000);, an InterruptedException is thrown.
	            •	The catch (InterruptedException e) block catches this exception.
	        3.	Exiting the Loop with break;:
	            •	Inside the catch block, the break; statement is executed. This immediately exits the infinite while
	                (true) loop.
	            •	Exiting the loop means that the thread stops running the Runnable and finishes its execution.
	        4.	Thread Termination:
	            •	After breaking out of the loop, the thread will finish executing and terminate. No further status
	                updates will be printed. */


            /*  Interrupting a Thread Not Sleeping or Waiting

                When you interrupt a thread by calling statusReporterThread.interrupt(), it sets the thread’s interrupt
                flag to true. However, the behavior of the thread upon being interrupted depends on what the thread is
                currently doing:

	            1.	Thread is Sleeping or Waiting:
	                •	If the thread is currently in a sleeping state (Thread.sleep()), waiting (Object.wait()), or
	                    blocked on I/O, the InterruptedException is immediately thrown.
	                •	The thread jumps to the catch block where the exception is handled.
	            2.	Thread is Not Sleeping or Waiting:
	                •	If the thread is not in a sleeping or waiting state when interrupt() is called, the thread does
	                    not immediately throw an InterruptedException.
	                •	Instead, the interrupt flag is simply set to true. The thread continues executing whatever code
	                    it was running at the moment.
	                •	The thread will not automatically jump to the catch block just because the interrupt flag is set.

                Impact on the Status Reporter Example

                In your statusReporter code:

	            •	If the Thread is Not Sleeping:
	                    •	If the thread is executing printThreads(threads); or any other code that doesn’t involve
	                        sleeping or waiting when interrupt() is called, it won’t throw an InterruptedException.
	                    •	The thread will continue to execute normally until it hits the next Thread.sleep(5000);.
	            •	When the Thread Hits Thread.sleep(5000) Again:
	                    •	If the thread is interrupted, the next time it calls Thread.sleep(5000);, the
	                        InterruptedException will be thrown immediately, because the interrupt flag was set.
	                    •	The thread then jumps to the catch block, prints the message, and exits the loop via break;.

                Summary:

	            •	If the thread is sleeping or waiting when interrupted: It throws an InterruptedException and
	                immediately enters the catch block.
	            •	If the thread is not sleeping or waiting when interrupted: The interrupt flag is set, but no
	                exception is thrown. The thread continues running until it encounters a sleep or wait operation,
	                at which point the InterruptedException will be thrown. */

        };
        Thread statusReporterThread = new Thread(statusReporter);
        //statusReporterThread.setDaemon(true); Since interrupt is called if user enters 0, we don't need to set this
        // thread as a daemon thread anymore.
        statusReporterThread.start();
        //What is barrier synchronization?
        /* Ans: Barrier synchronization is a technique used in concurrent programming to coordinate the execution of
                multiple threads, ensuring that all participating threads reach a certain point (the “barrier”) before
                any of them can proceed further. This is particularly useful in parallel computing scenarios where you
                want to ensure that all threads have completed a certain phase of their computation before moving on to
                the next phase.

            Key Concepts of Barrier Synchronization:

	            1.	Barrier:
	                •	A barrier is a synchronization point where threads must wait until all other participating
	                    threads reach this point. Only when all threads have reached the barrier will they all be allowed
	                    to continue execution.
	            2.	Phased Execution:
	                •	In some programs, work is divided into phases, and all threads need to complete one phase before
	                    moving on to the next. A barrier helps ensure that no thread starts the next phase until all threads
	                    have finished the current phase.
	            3.	Implementation:
	                •	Barrier synchronization can be implemented using various concurrency constructs like semaphores,
	                    locks, or specific barrier objects provided by concurrency libraries.
	                     Join() kinda does barrier synchronization. */

        while(true){
            System.out.println("I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) {

                // When the user will enter 0, the reporter thread gets interrupted,
                // in other words, it stops the execution and does all the necessary cleanups. This is a way to stop a
                // thread. Well, technically you cannot stop a thread directly, there used to be a method in Java 1.2,
                // public void stop(), which used to stop the thread directly but it was deprecated. Now, what the
                // interrupt does is that, it requests the thread to do all the cleanups and stop.

                System.out.println("Waiting for all the threads to finish.");
                waitForThreads(threads);
                System.out.println("Done! "+threads.size()+" primes calculated");
                statusReporterThread.interrupt();
                //statusReporterThread.interrupt();
                break;

            }
            Runnable runnable = new Runnable() {
                public void run() {
                    int number = PrimeNumberUtil.calculatePrime(n);
                    System.out.println("Result is: ");
                    System.out.println("Value of "+n+"th prime: "+number);
                }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            threads.add(thread);
            thread.start();
        }
    }

    private static void printThreads(List<Thread> threads) {
        System.out.println("\nThread Status: ");
        threads.forEach(thread -> System.out.println(thread.getState()+" "+thread.getName()));
        System.out.println();
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}