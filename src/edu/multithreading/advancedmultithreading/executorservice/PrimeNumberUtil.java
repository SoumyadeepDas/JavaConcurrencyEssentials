package edu.multithreading.advancedmultithreading.executorservice;

public class PrimeNumberUtil {
    public static int calculatePrime(int n){
        int number, numberOfPrimesFound, i;
        number = 1; numberOfPrimesFound = 0;

        while(numberOfPrimesFound<n){
            number++;
            for (i = 2; i<= number && number%i !=0; i++){

            }
            if (i == number){
                numberOfPrimesFound++;
            }
        }
        return number;
    }
}
