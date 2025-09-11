package com.example.renshuuhandsoff;

public class PausableThread extends Thread {
    protected final Object lock = new Object();
    protected volatile boolean running = true;

    public void wakeUp() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void stopThread() {
        running = false;
        wakeUp();
    }
}
// SYNTAXE OVERRIDE
/*
worker = new ControlledThread() {
    @Override
    public void run() {
        while (running) {
            synchronized (lock) {
                try {
                    lock.wait(3000); // wait 3s or until notified
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // ✅ Here I can use `running`, `lock`, wakeUp(), etc. directly
            System.out.println("Doing work in custom run()");
        }
    }
};

        worker.start();




// autre chose
  @Override
    protected void onStop() {
        super.onStop();
        worker.stopThread();
    }
 */