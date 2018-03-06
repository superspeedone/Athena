package com.superspeed.test.Collections;

import java.util.*;

/**
 * HashMap LinkedHashMap TreeMap HashTable WeakHashMap IdentityHashMap
 *
 * @author xc.yanww
 * @date 2017/10/25 11:02
 */
public class MapTest {

    /**
     *
     * 使用key-value来映射和存储数据，key必须唯一，value可以重复
     *
     * 1. HashMap (非同步)　无序  根据键的HashCode值存储数据，根据键可以直接获取它的值，
     *         具有很快的访问速度，遍历时，取得数据的顺序是完全随机的。因为键对象不可以重复，
     *         所以HashMap最多只允许一条记录的键为Null，允许多条记录的值为Null，是非同步的
     * 2. HashTable (线程安全, 不允许记录的键或者值为null)
     *         Hashtable与HashMap类似，是HashMap的线程安全版，它支持线程的同步，即任一
     *         时刻只有一个线程能写Hashtable，因此也导致了Hashtale在写入时会比较慢，它继承
     *         自Dictionary类，不同的是它的key和value都不允许为null，同时效率较低。
     * 3. ConcurrentHashMap 线程安全，并且锁分离。
     *         ConcurrentHashMap内部使用段(Segment)来表示这些不同的部分，每个段其实就是
     *         一个小的hash table，它们有自己的锁。只要多个修改操作发生在不同的段上，它们就可以并发进行。
     * 4. LinkedHashMap LinkedHashMap保存了记录的插入顺序，在用Iteraor遍历LinkedHashMap时，
     *         先得到的记录肯定是先插入的，在遍历的时候会比HashMap慢，有HashMap的全部特性，非同步的。
     * 5. TreeMap  (非同步)　有序(用二叉排序树)
     *          TreeMap实现SortMap接口，能够把它保存的记录根据键排序，默认是按键值的升序排序（自然顺序），
     *          也可以指定排序的比较器，当用Iterator遍历TreeMap时，得到的记录是排过序的。不允许key值为空，非同步的。
     *
     *
     */

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<String, Object>();

        // HashMap  线程非安全，所有方法都是非同步(可以通过Collections.synchronizedMap(Map<K,V> m)返回一个同步的Map对象)，
        // null可以作为键，这样的键只能存在一个。
        map.put(null, "null1");
        map.put(null, "null2");
        System.out.println(map.get(null));

        // HashMap 中可以有一个或多个键对应的值为null,当get(key)去除的值为空时，可能不存在该键，也有可能该键对应的值为null
        // 因此HashMap 不能用get()方法来判断键是否存在，必须用map.containsKey()来判断
        map.put("key1", null);
        map.put("key2", null);
        System.out.println(map.get("key1"));
        System.out.println(map.get("key3"));
        Set<String> keys = map.keySet();
        System.out.println(Arrays.toString(keys.toArray()));

        // map的遍历
        // 第一种：KeySet()
        //      将Map中所有的键存入到set集合中。因为set具备迭代器。所有可以迭代方式取出所有的键，再根据get方法。
        //       获取每一个键对应的值。 keySet():迭代后只能通过get()取key 。取到的结果会乱序，是因为取得数据行主键的时候，
        //      使用了HashMap.keySet()方法，而这个方法返回的Set结果，里面的数据是乱序排放的。
        // 第二种：entrySet()
        //      Set<Map.Entry<K,V>> entrySet() //返回此映射中包含的映射关系的 Set 视图。（一个关系就是一个键-值对），
        //      就是把(key-value)作为一个整体一对一对地存放到Set集合当中的。Map.Entry表示映射关系。entrySet()：
        //      迭代后可以e.getKey()，e.getValue()两种方法来取key和value。返回的是Entry接口。
        //
        // 推荐使用第二种方式，即entrySet()方法，效率较高。
        // 对于keySet其实是遍历了2次，一次是转为iterator，一次就是从HashMap中取出key所对应的value(get方法源码，循环遍历取值)。
        // 而entryset只是遍历了第一次，它把key和value都放到了entry中，所以快了。两种遍历的遍历时间相差还是很明显的。


        HashMap<Person, String> map2 = new HashMap<Person, String>();
        Person person = new Person(1234, "乔峰");
        //put到hashmap中去
        map2.put(person, "天龙八部");
        //get取出，从逻辑上讲应该能输出“天龙八部”
        System.out.println("结果:" + map2.get(new Person(1234, "乔峰")));

    }

    private static class Person {

        int idCard;
        String name;

        public Person(int idCard, String name) {
            this.idCard = idCard;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }
            Person person = (Person) o;
            //两个对象是否等值，通过idCard来确定
            return this.idCard == person.idCard;
        }

       /* @Override
        public int hashCode() {
            int result = idCard;
            result = 31 * result + name.hashCode();
            return result;
        }*/
    }

}
