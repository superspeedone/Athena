package com.superspeed.test.Collections;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet LinkedHashSet TreeSet
 * @author xc.yanww
 * @date 2017/10/25 10:59
 */
public class SetTest {

    /**
     * Collection<--Set<--HashSet (非同步)　无序
     * Collection<--Set<--HashSet<--LinkedHashSet　(非同步) 有序
     * Collection<--Set<--SortedSet<--TreeSet　(非同步)　有序(用二叉排序树)
     */

    /**
     *  HashSet元素不能重复的实现原理
     *  HashSet底层是基于HashMap实现的，先通过HashCode取一个模，这样一下子就固定到某个位置了，
     *  如果这个位置上没有元素，那么就可以肯定HashSet中必定没有和新添加的元素equals的元素，就可以
     *  直接存放了，都不需要比较；如果这个位置上有元素了，逐一比较，比较的时候先比较HashCode，
     *  HashCode都不同接下去都不用比了，肯定不一样，HashCode相等，再equals比较，不相同的元素
     *  就存，有相同的元素就不存。忽略不插入重复元素。
     *
     *  必须小心操作可变对象（Mutable Object）。如果一个Set中的可变元素改变了自身状态导致Object.equals(Object)=true将导致一些问题。
     *
     */

    public static void main(String[] args) {

        Set<Person> set = new HashSet<Person>();

        Person a = new Person("David", 21);
        Person b = new Person("David", 21);

        set.add(a);
        set.add(b);

        System.out.println("a.hashCode=" + a.hashCode());
        System.out.println("b.hashCode=" + b.hashCode());
        System.out.println("set.size=" + set.size());
        System.out.println("a==b?" + (a ==b));


    }

    static  class Person {

        private String name;

        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + age;
            return result;
        }
    }


}
