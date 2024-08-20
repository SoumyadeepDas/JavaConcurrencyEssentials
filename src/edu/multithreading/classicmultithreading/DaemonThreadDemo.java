package edu.multithreading.classicmultithreading;
//Daemon threads are low-powered threads. They will end their execution if the main thread ends. They will do their
//work until the main thread is running.
//Example: Monitoring something in an application. If the main application ends, the daemon thread also needs to end.
import edu.multithreading.nthprime.PrimeNumberUtil;

import java.util.Scanner;

public class DaemonThreadDemo {
    public static void main(String[] args) {
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
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
