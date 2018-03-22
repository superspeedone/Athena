package com.superspeed.test.jvm;

public class DeadLock {

    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLock().deadLock();
    }



    private void deadLock() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.printf("thread1 [%d] \n", Thread.currentThread().getId());

                synchronized (A) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (B) {

                    }

                }

            }
        }, "thread1");


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.printf("thread2 [%d] \n", Thread.currentThread().getId());

                synchronized (B) {
                    synchronized (A) {

                    }
                }
            }
        }, "thread2");

        thread1.start();
        thread2.start();
    }

}

