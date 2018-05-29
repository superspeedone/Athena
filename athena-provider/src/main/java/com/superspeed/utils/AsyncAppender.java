package com.superspeed.utils;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * log4j日志异步存储
 */
public class AsyncAppender extends AppenderSkeleton implements AppenderAttachable {

    private static final int DEFAULT_QUEUE_SIZE = 256;
    private int bufferSize = DEFAULT_QUEUE_SIZE;

    private long lastDiscartTime = System.currentTimeMillis();
    /**
     * 缓存日志队列
     */
    private BlockingQueue<LoggingEvent> queue;
    private BlockingQueue<LoggingEvent> errorQueue = new ArrayBlockingQueue<LoggingEvent>(bufferSize);

    private AtomicInteger discardCount = new AtomicInteger(0);
    private final AppenderAttachableImpl appenders = new AppenderAttachableImpl();
    private Thread dispather;

    public AsyncAppender() {
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (discardCount.get() > 10000 || (System.currentTimeMillis() - lastDiscartTime > 300000 && discardCount.get() > 0)) {
            logDiscardCount();
        }
        loggingEvent.getNDC();
        loggingEvent.getThreadName();
        loggingEvent.getMDCCopy();
        if (!queue.offer(loggingEvent)) {
            discardCount.incrementAndGet();
        }
    }

    private void logDiscardCount() {
        int discardCountSnapshort = 0;
        long timeRange = 0;
        synchronized (discardCount) {
            if (System.currentTimeMillis() - lastDiscartTime < 10000) {
                return;
            }
            discardCountSnapshort = discardCount.getAndSet(0);
            timeRange = (System.currentTimeMillis() - lastDiscartTime) / 1000;
            lastDiscartTime = System.currentTimeMillis();
        }
        LoggingEvent loggingEvent = new LoggingEvent(null, Logger.getLogger(this.getClass()), Level.ERROR,
                "Log4j discard " + discardCountSnapshort + " message last " + timeRange + "s", null);
        loggingEvent.getNDC();
        loggingEvent.getThreadName();
        loggingEvent.getMDCCopy();
        errorQueue.offer(loggingEvent);
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    /**
     * add appender
     *
     * @param appender
     */
    @Override
    public void addAppender(Appender appender) {
        appenders.addAppender(appender);
    }

    /**
     * Get iterator over attached appenders
     *
     * @return
     */
    @Override
    public Enumeration getAllAppenders() {
        return appenders.getAllAppenders();
    }

    /**
     * Get appender by name
     *
     * @param name
     * @return matching appender
     */
    @Override
    public Appender getAppender(String name) {
        return appenders.getAppender(name);
    }

    /**
     * Determines if speciefied appender is attached.
     *
     * @param appender
     * @return
     */
    @Override
    public boolean isAttached(Appender appender) {
        return appenders.isAttached(appender);
    }

    /**
     * remove and closes all attached appenders.
     */
    @Override
    public void removeAllAppenders() {
        appenders.removeAllAppenders();
    }

    @Override
    public void removeAppender(Appender appender) {
        appenders.removeAppender(appender);
    }

    @Override
    public void removeAppender(String name) {
        appenders.removeAppender(name);
    }

    @Override
    public void close() {
        if (discardCount.get() > 0) {
            logDiscardCount();
        }
        if (queue.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
        Enumeration enumeration = appenders.getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object next = enumeration.nextElement();
                if (next instanceof Appender) {
                    ((Appender) next).close();
                }
            }
        }
    }

    public void setBufferSize(final int buffSize) {
        this.bufferSize = (buffSize < 1) ? DEFAULT_QUEUE_SIZE : buffSize;
    }

    private class Dispatcher implements Runnable {

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (true) {
                try {
                    LoggingEvent loggingEvent = queue.take();
                    appenders.appendLoopOnAppenders(loggingEvent);
                    loggingEvent = errorQueue.poll();
                    if (loggingEvent != null) {
                        appenders.appendLoopOnAppenders(loggingEvent);
                    }
                } catch (InterruptedException ie) {
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
