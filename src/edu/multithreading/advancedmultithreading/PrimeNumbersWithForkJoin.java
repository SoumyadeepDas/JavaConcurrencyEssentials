package edu.multithreading.advancedmultithreading;

import edu.multithreading.advancedmultithreading.executorservice.PrimeNumberUtil;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

//Accept n input array of numbers
//Calculate nth prime (where n is value of i in that array arr[i])
//Add all the nth primes.

class CalculatePrimeTask extends RecursiveTask<Integer> {
    int[] array;
    int start;
    int end;
    
    public CalculatePrimeTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Integer compute() {
        if (start == end) {
            System.out.println(array[start]+" :"+PrimeNumberUtil.calculatePrime(array[start]));
            return PrimeNumberUtil.calculatePrime(array[start]);
        }

        if (end - start == 1){
            System.out.println(array[start]+" :"+PrimeNumberUtil.calculatePrime(array[start]));
            System.out.println(array[end]+" :"+PrimeNumberUtil.calculatePrime(array[end]));
            return PrimeNumberUtil.calculatePrime(array[start])+PrimeNumberUtil.calculatePrime(array[end]);
        }

        int mid = (start+end)/2;
        CalculatePrimeTask subTask1 = new CalculatePrimeTask(array, start, mid);
        CalculatePrimeTask subTask2 = new CalculatePrimeTask(array, mid + 1, end);
        invokeAll(subTask1, subTask2);
        return subTask1.join() + subTask2.join();

    }
}

public class PrimeNumbersWithForkJoin {
    public void runForkJoinExample(){
        int[] inputNumbers = {2,3,4,5,6,7,8,9,10};
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        CalculatePrimeTask calculatePrimeTask = new CalculatePrimeTask(inputNumbers, 0, inputNumbers.length - 1);
        Integer result = forkJoinPool.invoke(calculatePrimeTask);
        System.out.println("Sum of prime numbers: " + result);
    }

    public static void main(String[] args) {
        new PrimeNumbersWithForkJoin().runForkJoinExample();
    }
}
