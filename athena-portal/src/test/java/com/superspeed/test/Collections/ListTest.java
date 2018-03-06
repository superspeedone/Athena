package com.superspeed.test.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * ArrayList Vector LinkedList Stack 实现原理和异同
 *
 * @author xc.yanww
 * @date 2017/10/25 9:27
 */
public class ListTest {

    /**
     * 线性表 链表 哈希表 是常用的数据结构
     * JDK提供了一系列相应的类来实现数据结构，这些类均在java.util包中
     *
     * 集合类存放的都是对象的引用，而非对象本身，出于表达上的便利，我们称集合中的对象就是指集合中对象的引用（reference)。
     *
     * Collection<--List<--Vector<--Stack  (同步)　有序
     * Collection<--List<--ArrayList (非同步)　有序
     * Collection<--List<--LinkedList (非同步)　有序
     *
     * ArrayList是实现了基于动态数组的数据结构，LinkedList基于链表的数据结构。
     * ArrayList 和Vector是采用数组方式存储数据，此数组元素数大于实际存储的数据以便增加和插入元素，都允许直接序号索引元素，
     * 但是插入数据要涉及到数组元素移动等内存操作，所以索引数据快，插入数据慢，Vector由于使用了synchronized方法（线程安全）
     * 所以性能上比ArrayList要差，LinkedList使用双向链表实现存储，按序号索引数据需要进行向前或向后遍历，但是插入数据时只需要
     * 记录本项的前后项即可，所以插入数度较快。
     *
     * Vector是同步的，当一个Iterator被创建而且正在被使用，另一个线程改变了Vector的状态（例如，添加或删除了一些元素），
     * 这时调用Iterator的方法时将抛出ConcurrentModificationException，因此必须捕获该异常。
     *
     * vector增长率为目前数组长度的100%，而arraylist增长率为目前数组长度的50%。
     * 如果在集合中使用数据量比较大的数据，用vector有一定的优势。
     *
     * ArrayList插入时间复杂度为O(n), LinkedList插入时间复杂度为O(1)
     *
     * Stack继承自Vector，实现一个后进先出的堆栈。Stack提供5个额外的方法使得Vector得以被当作堆栈使用。基本的push和pop方法，
     * 还有peek方法得到栈顶的元素，empty方法测试堆栈是否为空，search方法检测一个元素在堆栈中的位置。Stack刚创建后是空栈。
     *
     */

    public static void main(String[] args) {

        /**
         * Collection接口继承Iterator接口，所以，所有Collection的子类都支持一个iterator()方法
         * 该方法返回一个迭代子，使用该迭代子可以逐一访问集合中的每一个元素
         * List还提供了一个listIterator()方法，允许添加、删除、设定元素，还能往前后往后遍历
         */
        List<Object> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        ListIterator listiterator = list.listIterator();
        while (listiterator.hasNext()) {
            Object o = listiterator.next();
            System.out.println("List->" + o);
        }
        while (listiterator.hasPrevious()) {
            Object o = listiterator.previous();
            System.out.println("List->" + o);
        }



    }

}
