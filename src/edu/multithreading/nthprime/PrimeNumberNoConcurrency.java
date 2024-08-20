package edu.multithreading.nthprime;

import java.util.Scanner;

public class PrimeNumberNoConcurrency {
    public static void main(String[] args) {
        int number;

        while(true){
            System.out.println("I can tell you the nth prime number. Just Enter the value of n: ");
            int n = new Scanner(System.in).nextInt();
            if (n==0) break;
            number = PrimeNumberUtil.calculatePrime(n);
            System.out.println("\nValue of "+n+"th prime: "+number);
        }
    }
}
