package com.superspeed.test.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class DirectMemoryOOM {

    private static final long _1M = 1024*1024;

    public static void main(String[] args) {
        try {
            Field field = Unsafe.class.getDeclaredFields()[0];
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            while (true) {
                unsafe.allocateMemory(_1M);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
