package edu.multithreading.classicmultithreading;
//Runnable is an interface that has a SAM, "public void run()", it doesn't provide anything related to multi-threading,
//it just allows us to run anything, that's it

class myRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Just doing something");
    }
}
public class UnderstandingRunnable {
    public static void main(String[] args) {
        Runnable runnable = new myRunnable();
        runnable.run();
    }
}
