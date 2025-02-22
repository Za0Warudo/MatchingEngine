package main;

import java.util.Iterator;

/**
 * A FIFO linked list with O(1) complexity for the removal operation if the node to be removed is previously known.
 * @author Zawarudo (Vinicius)
 * @version 1.0
 * @since 2025-02-20
 * @param <T> data type saved in Node
 */
public class LinkedList<T> implements Iterable<T>{

    private Node<T> head;
    private Node<T> tail;
    int size;

    /**
     * Return true if LinkedList is empty, false otherwise.
     * @return {@code boolean} true or false
     */
    public boolean isEmpty() {return size == 0;}

    /**
     * Create a new node that saved {@code info} and append it in the list.
     * @param info data to be saved.
     * @return {@code Node<T>} node where information has been saved.
     * @see Node
     */
    public Node<T> push (T info) { // O(1)
        Node<T> newNode = new Node<>(info);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        size++;
        return newNode;
    }

    /**
     * Removes the first Node of the list. This operation is O(1).
     */
    public void popf () { // O(1)
        if (isEmpty()) return;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            if (head != null) head.prev = null;
        }
        size--;
    }

    /**
     * Removes the last Node of the list. This operation is O(1).
     */
    public void popl () {
        if (isEmpty()) return;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            if (tail != null) tail.next = null;
        }
        size--;
    }

    /**
     * Removes the given node from the list. This operation is O(1).
     * @param node a node to be removed from the list.
     * @see Node
     */
    public void remove (Node<T> node) { // O(1)
        if (isEmpty()) return;
        if (node == null) return;
        if (node == head) {
            popf();
            return;
        }
        if (node == tail) {
            popl();
            return;
        }
        if (node.prev != null) node.prev.next = node.next;
        if (node.next != null) node.next.prev = node.prev;
        size--;
    }

    /**
     * Return the first node in the list.
     * @return {@code Node} the first node in the list.
     * @see Node
     */
    public Node<T> getHead() {return head;}

    /**
     * Return the size of the list.
     * @return {@code size} an integer number greater or equal to zero.
     */
    public int size() {return size;}

    /**
     * Iterator for the list
     * @return {@code iterator} an Iterator object.
     * @see Iterator
     */
    public Iterator<T> iterator() {return new linkedListIterator();}

    /**
     * Create the iterator object
     * @see Iterator
     */
    private class linkedListIterator implements Iterator<T> {
        private Node<T> current = head;

        @Override
        public boolean hasNext() {return current != null;}

        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
        @Override
        public void remove() {throw new UnsupportedOperationException();}
    }
}
