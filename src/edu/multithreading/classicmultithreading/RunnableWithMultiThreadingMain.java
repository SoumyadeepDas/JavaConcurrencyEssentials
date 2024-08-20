package edu.multithreading.classicmultithreading;
/* Steps for activating multi-threading:
* Step 1: Identify the code that requires another thread other than the main thread to process it.
* Step 2: Write a class that implements the Runnable interface.
* Step 3: Override the run method and put your code inside the run method, that requires multi-threading.
* Step 4: Create a new thread and start it.  */
class myRun implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i<=10;i++){
            System.out.println(Thread.currentThread().getName() + " is executing "+i);
            System.out.println();
        }
    }
}


public class RunnableWithMultiThreadingMain {
    public static void main(String[] args) {
        Runnable runnable = new myRun();
        Thread thread = new Thread(runnable); //the thread says, give me something that I can run, so we
        //provide the instance of Runnable and provide it to the Thread class constructor and then we call the
        //start() on the thread reference.
        thread.start();
        for (int i = 1; i<=10;i++){
            System.out.println(Thread.currentThread().getName() + " is executing "+i);
            System.out.println();
        }
    }
}
//Note JVM doesn't run the thread that we've created. It asks the operating system to run the thread.
//The thread class only holds the metadata of the thread that the OS is executing, it keeps a track on the OS's
//thread, which is running the thread that we've started and that the JVM has passed to the OS for execution. We
//can enquire about the thread's state and the jvm's gonna poke the os's thread for the answer and it replys us back.
//The JVM acts as an interface


//When does the thread end?
//when the run method ends or when it throws an exception