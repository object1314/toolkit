/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.container;

/**
 * An analyzer to analyze the singly linked lists. Any mislead or error result
 * might be returned in a concurrent modifying environment if there is no more
 * concurrent strategy in the caller.
 *
 * @author XuYanhang
 * @see SinglyLinkedNode
 * @since 2020-08-22
 */
public class SinglyLinkedListAnalyzer {
    /**
     * Returns the circle entrance if there is any circle on the singly linked list
     * or <code>null</code> if the singly linked list isn't a circular one.
     *
     * @param head the head of the singly linked list
     * @return the circle entrance of the circular singly linked list or
     * <code>null</code> if it's not a circular one
     */
    public static <T> SinglyLinkedNode<T> getCircleEntrance(SinglyLinkedNode<T> head) {
        // Happens on empty list
        if (isNilNode(head)) return null;
        SinglyLinkedNode<T> first;
        SinglyLinkedNode<T> second;
        // Fast node stepped by two and slow node stepped by one
        for (first = second = head; ; ) {
            first = first.getNext();
            // Happens on the tail
            if (isNilNode(first)) return null;
            first = first.getNext();
            // Happens on the tail
            if (isNilNode(first)) return null;
            second = second.getNext();
            if (first == second) break;
        }
        // From the meet node and head node, for the next meeting node
        for (second = head; ; first = first.getNext(), second = second.getNext())
            if (first == second) break;
        return first;
    }

    /**
     * Returns the size of the singly linked list. It's also the length or nodes
     * count of the list.
     *
     * @param head the head of the singly linked list
     * @return the size of the singly linked list
     */
    public static <T> int getSize(SinglyLinkedNode<T> head) {
        // Happens on empty list
        if (isNilNode(head)) return 0;
        // Get circle entrance
        SinglyLinkedNode<T> circleEntrance = getCircleEntrance(head);
        int size = 0;
        // Happens when the list is not a circular one
        if (null == circleEntrance) {
            for (SinglyLinkedNode<T> n = head; !isNilNode(n); n = n.getNext())
                size++;
            return size;
        }
        // Happens when the list is a circular one
        // The size from head to circle entrance (exclude)
        for (SinglyLinkedNode<T> n = head; n != circleEntrance; n = n.getNext())
            size++;
        // Count the circle entrance
        size++;
        // The size of the circle but the circle entrance
        for (SinglyLinkedNode<T> n = circleEntrance; (n = n.getNext()) != circleEntrance; )
            size++;
        return size;
    }

    /**
     * Returns the intersection node of two singly linked lists, or <code>null<code>
     * when they don't have one. Normally, just the intersection node returns. But
     * if the two lists intersect on the circle, then the circle entrance of the
     * second list returns.
     *
     * @param ahead the head of the first singly linked list
     * @param bhead the head of the second singly linked list
     * @return the intersection node of two singly linked lists, or <code>null<code>
     * when they don't intersect
     */
    public static <T> SinglyLinkedNode<T> getIntersection(SinglyLinkedNode<T> ahead, SinglyLinkedNode<T> bhead) {
        if (isNilNode(ahead) || isNilNode(bhead)) return null;
        SinglyLinkedNode<T> a = getCircleEntrance(ahead);
        SinglyLinkedNode<T> b = getCircleEntrance(bhead);
        // Happens when one has circle and another not
        if (isNilNode(a) && !isNilNode(b)) return null;
        if (isNilNode(b) && !isNilNode(a)) return null;
        // Happens when they intersect on the circle entrance
        if (!isNilNode(a) && a == b) return a;
        SinglyLinkedNode<T> first;
        SinglyLinkedNode<T> second;
        // Happens case 1 when they intersect on the circle but entrance
        // Happens case 2 on no intersection when they are circular ones
        if (a != b) {
            for (first = a, second = b; ; first = first.getNext(), second = second.getNext()) {
                if (first == b || second == a) return a;
                if (first == a || second == b) return null;
            }
        }
        // Set aHead as a tail when a list is not a circular one
        if (null == a) for (a = ahead; !isNilNode(a.getNext()); a = a.getNext());
        // Find the meeting point when redirect a list tail to a list head
        for (first = second = bhead; ; ) {
            first = first == a ? ahead : first.getNext();
            // Happens on the tail
            if (isNilNode(first)) return null;
            first = first == a ? ahead : first.getNext();
            // Happens on the tail
            if (isNilNode(first)) return null;
            second = second == a ? ahead : second.getNext();
            if (first == second) break;
        }
        // Find the intersection
        for (second = bhead; ; ) {
            if (first == second) break;
            first = first == a ? ahead : first.getNext();
            second = second == a ? ahead : second.getNext();
        }
        return first;
    }

    /**
     * Returns if a linked node is a nil one.
     *
     * @param node the node to check
     * @return true when the node is a nil node or false when not
     */
    private static <T> boolean isNilNode(SinglyLinkedNode<T> node) {
        return null == node || node.isNil();
    }
}
