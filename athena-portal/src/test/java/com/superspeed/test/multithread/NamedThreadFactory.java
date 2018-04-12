package com.superspeed.test.multithread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * NamedThreadFactory
 *
 * Provide some settings for thread, such as namingService, counter, priority, daemonSet and so on.
 * When thread occurs unknown exception, we can find the problem easily by threadName.
 *
 * Created by yanweiwen on 2018/4/12.
 */
public class NamedThreadFactory implements ThreadFactory {

    // 线程计数器
    private final AtomicLong threadCounter;
    /** 自义定ThreadFactory，覆盖默认的线程工厂 {@link Executors#defaultThreadFactory()} */
    private final ThreadFactory wrappedFactory;
    // 未知异常处理器
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    // 线程命名表达式
    private final String namingPattern;
    // 优先级
    private final Integer priority;
    // 是否为守护线程
    /**
     * public final static int MIN_PRIORITY = 1; // 最低优先级
     * public final static int NORM_PRIORITY = 5;// 干活线程建立默认优先级
     * public final static int MAX_PRIORITY = 10;// 最高优先级
     */
    private final Boolean daemonFlag;

    private NamedThreadFactory(NamedThreadFactory.Builder builder) {
        if(builder.wrappedFactory == null) {
            this.wrappedFactory = Executors.defaultThreadFactory();
        } else {
            this.wrappedFactory = builder.wrappedFactory;
        }

        this.namingPattern = builder.namingPattern;
        this.priority = builder.priority;
        this.daemonFlag = builder.daemonFlag;
        this.uncaughtExceptionHandler = builder.exceptionHandler;
        this.threadCounter = new AtomicLong();
    }

    public final ThreadFactory getWrappedFactory() {
        return this.wrappedFactory;
    }

    public final String getNamingPattern() {
        return this.namingPattern;
    }

    public final Boolean getDaemonFlag() {
        return this.daemonFlag;
    }

    public final Integer getPriority() {
        return this.priority;
    }

    public final UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.uncaughtExceptionHandler;
    }

    public long getThreadCount() {
        return this.threadCounter.get();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = this.getWrappedFactory().newThread(r);
        this.initializeThread(t);
        return t;
    }

    private void initializeThread(Thread t) {
        if(this.getNamingPattern() != null) {
            Long count = Long.valueOf(this.threadCounter.incrementAndGet());
            t.setName(String.format(this.getNamingPattern(), new Object[]{count}));
        }

        if(this.getUncaughtExceptionHandler() != null) {
            t.setUncaughtExceptionHandler(this.getUncaughtExceptionHandler());
        }

        if(this.getPriority() != null) {
            t.setPriority(this.getPriority().intValue());
        }

        if(this.getDaemonFlag() != null) {
            t.setDaemon(this.getDaemonFlag().booleanValue());
        }

    }

    public static class Builder implements com.superspeed.test.multithread.Builder<NamedThreadFactory> {
        private ThreadFactory wrappedFactory;
        private UncaughtExceptionHandler exceptionHandler;
        private String namingPattern;
        private Integer priority;
        private Boolean daemonFlag;

        public Builder() {
        }

        public NamedThreadFactory.Builder wrappedFactory(ThreadFactory factory) {
            if(factory == null) {
                throw new NullPointerException("Wrapped ThreadFactory must not be null!");
            } else {
                this.wrappedFactory = factory;
                return this;
            }
        }

        public NamedThreadFactory.Builder namingPattern(String pattern) {
            if(pattern == null) {
                throw new NullPointerException("Naming pattern must not be null!");
            } else {
                this.namingPattern = pattern;
                return this;
            }
        }

        public NamedThreadFactory.Builder daemon(boolean f) {
            this.daemonFlag = Boolean.valueOf(f);
            return this;
        }

        public NamedThreadFactory.Builder priority(int prio) {
            this.priority = Integer.valueOf(prio);
            return this;
        }

        public NamedThreadFactory.Builder uncaughtExceptionHandler(UncaughtExceptionHandler handler) {
            if(handler == null) {
                throw new NullPointerException("Uncaught exception handler must not be null!");
            } else {
                this.exceptionHandler = handler;
                return this;
            }
        }

        public void reset() {
            this.wrappedFactory = null;
            this.exceptionHandler = null;
            this.namingPattern = null;
            this.priority = null;
            this.daemonFlag = null;
        }

        public NamedThreadFactory build() {
            NamedThreadFactory factory = new NamedThreadFactory(this);
            this.reset();
            return factory;
        }
    }




}

interface Builder<T> {
    T build();
}