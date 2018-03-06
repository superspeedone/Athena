package com.superspeed.test.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectTest {

    private static final Integer age = 20;

    private static String name = "David";

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        Field ageField = ReflectTest.class.getDeclaredField("age");

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(ageField, ageField.getModifiers() & ~Modifier.FINAL);

        ageField.setAccessible(true);
        ageField.set(null , 30);

        System.out.println(age);



    }

}
