package com.superspeed.test.jvm;

/**
 * 垃圾回收策略
 */
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC SAVE_HOCK = null;

    public void isAlive() {
        System.out.println("yes, i am still alive :)");
    }

    /**
     * Called by the garbage collector on an object when garbage collection
     * determines that there are no more references to the object.
     * A subclass overrides the {@code finalize} method to dispose of
     * system resources or to perform other cleanup.
     * <p>
     * @throws Throwable the {@code Exception} raised by this method
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.SAVE_HOCK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOCK = new FinalizeEscapeGC();
        SAVE_HOCK = null;
        System.gc();
        // finalize方法优先级比较低，所以暂停1s等待执行结束
        Thread.sleep(1000);
        if (SAVE_HOCK != null) {
            SAVE_HOCK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }

        SAVE_HOCK = null;
        System.gc();
        // finalize方法优先级比较低，所以暂停1s等待执行结束
        Thread.sleep(1000);
        if (SAVE_HOCK != null) {
            SAVE_HOCK.isAlive();
        } else {
            System.out.println("no, i am dead :(");
        }
    }
}

