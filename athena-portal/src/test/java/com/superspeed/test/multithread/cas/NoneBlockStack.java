package com.superspeed.test.multithread.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 非阻塞堆栈
 * @param <E>
 */
public class NoneBlockStack<E> {

    private AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();

    /**
     * 从栈顶添加节点
     */
    public void push(E item) {
        // 思路：添加新的节点作为栈顶节点，并且新节点的next指向旧的栈顶节点
        // 创建新的节点
        Node<E> newHead = new Node<E>(item);
        // 获取当前栈顶节点
        Node<E> oldHead = head.get();

        do {
            // 每次循环，oldHead指向当前栈顶节点，新的节点的next指向oldHead
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
    }

    /**
     * 从栈顶删除节点
     */
    public E pop() {
        //  思路： 把当前栈顶节点的下一个堆栈节点作为栈顶节点
        Node<E> newHead;
        Node<E> oldHead;
        do {
            oldHead = head.get();
            // 删除节点的时候，必须考虑栈顶节点有可能为空的情况
            if (oldHead == null) {
                return null;
            }
            newHead = head.get().next;
        } while (!head.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    /**
     * 节点数据结构
     * @param <E>
     */
    static class Node<E> {

        E item;
        Node<E> next;

        public Node(E item) {
            this.item = item;
        }

    }


}
