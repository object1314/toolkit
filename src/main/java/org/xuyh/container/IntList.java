/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.container;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * This integer list is a list that permits only integer values. Some behavior
 * is simular with {@link List} while some is different.
 *
 * @author XuYanhang
 * @since 2020-10-06
 */
public interface IntList extends Iterable<Integer> {

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return the number of elements in this list
	 */
	public int size();

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 *
	 * @return <tt>true</tt> if this list contains no elements
	 */
	public boolean isEmpty();

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 *
	 * @param value element whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean contains(int value);

	/**
	 * Returns the index of the first occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 */
	public int indexOf(int value);

	/**
	 * Returns the index of the last occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 */
	public int lastIndexOf(int value);

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public int get(int index);

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 *
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public IntList set(int index, int value);

	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param value element to be appended to this list
	 * @return this
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	public IntList add(int value);

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param values elements to be appended to this list
	 * @return this
	 * @throws NullPointerException when the input value array is <code>null</code>
	 */
	public IntList addAll(int... values);

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param c elements to be appended to this list but in collection way
	 * @return this
	 * @throws NullPointerException when the input collection is <code>null</code>
	 *                              or any element in it is <code>null</code>
	 */
	public IntList addAll(Collection<? extends Number> c);

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param o elements to be appended to this list but in list way
	 * @return this
	 * @throws NullPointerException when the input list is <code>null</code>
	 */
	public IntList addAll(IntList o);

	/**
	 * Inserts the specified element at the specified position in this list. Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param value element to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public IntList insert(int index, int value);

	/**
	 * Inserts the specified elements at the specified position in this list. Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 *
	 * @param index  index at which the specified element is to be inserted
	 * @param values elements to be inserted
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 * @throws NullPointerException      when the input value array is
	 *                                   <code>null</code>
	 */
	public IntList insertAll(int index, int... values);

	/**
	 * Inserts the specified elements at the specified position in this list. Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param c     elements to be inserted but in in collection way
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 * @throws NullPointerException      when the input collection is
	 *                                   <code>null</code> or any number in it is
	 *                                   null
	 */
	public IntList insertAll(int index, Collection<? extends Number> c);

	/**
	 * Inserts the specified elements at the specified position in this list. Shifts
	 * the element currently at that position (if any) and any subsequent elements
	 * to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param o     elements to be inserted but in in list way
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 * @throws NullPointerException      when the input list is <code>null</code>
	 */
	public IntList insertAll(int index, IntList o);

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 *
	 * @param index the index of the element to be removed
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public IntList del(int index);

	/**
	 * Removes all of the elements of this list that satisfy the given predicate.
	 * Errors or runtime exceptions thrown during iteration or by the predicate are
	 * relayed to the caller.
	 *
	 * @param filter a predicate which returns {@code true} for elements to be
	 *               removed
	 * @return this
	 * @throws NullPointerException if the specified filter is <code>null</code>
	 */
	public IntList delIf(IntPredicate filter);

	/**
	 * Removes all of the elements from this list. The list will be empty after this
	 * call returns.
	 * 
	 * @return this
	 */
	public IntList clear();

	/**
	 * Sort this list in ASC way. For example, unsorted array of [3, 1, 2, 0] would
	 * be changed as [0, 1, 2, 3] after sort action.
	 * 
	 * @return this
	 */
	public IntList sort();

	/**
	 * Reverse all elements in this list. For example, if the original list is [0,
	 * 1, 2]. After reverse operation, it is changed as [2, 1, 0].
	 * 
	 * @return this
	 */
	public IntList reverse();

	/**
	 * Create a sub list from a slice in the list. Specially, You can use
	 * fromIndex=size and toIndex=size to create an empty list or parameter of
	 * fromIndex>toIndex to slice this list in a reverse way. This way has no effect
	 * on the origin list of this.
	 * 
	 * @Param fromIndex The from element index to slice the list. Range from -1 to
	 *        size on step 1 when fromIndex==toIndex or 0 to size-1 on step 1 when
	 *        fromIndex!=toIndex. Included.
	 * @Param toIndex The to element index to slice the list or size of the list
	 *        when not set. Range from -1 to size on step 1. Excluded.
	 * @return The sub slice list.
	 * @throws IndexOutOfBoundsException when the from index or to index out of
	 *                                   range.
	 */
	public IntList subList(int fromIndex, int toIndex);

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element).
	 *
	 * <p>
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this list. (In other words, this method must allocate a new array). The
	 * caller is thus free to modify the returned array.
	 *
	 * <p>
	 * This method acts as bridge between array-based and int-list-based APIs.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *         sequence
	 */
	public int[] toArray();

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element).
	 *
	 * <p>
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this list. (In other words, this method must allocate a new array). The
	 * caller is thus free to modify the returned array.
	 *
	 * <p>
	 * This method acts as bridge between array-based and int-list-based APIs.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *         sequence
	 */
	public Integer[] toRefArray();

	/**
	 * Cast this list with primitive integer type as reference type of integer.
	 *
	 * <p>
	 * The returned list will be "safe" in that no references to it are maintained
	 * by this list. (In other words, this method must allocate a new memory). The
	 * caller is thus free to modify the returned list.
	 * 
	 * <p>
	 * This method acts as bridge between int-list-based and collection-based APIs.
	 * 
	 * @return a list in integer type containing all of the elements in this list in
	 *         proper sequence
	 */
	public List<Integer> toRefList();

	/**
	 * Returns an {@link IntIterator} over elements in this list, , starting at the
	 * specified position in the list.The specified index indicates the first
	 * element that would be returned by an initial call to {@link IntIterator#next
	 * next}. An initial call to {@link IntIterator#prev prev} would return the
	 * element with the specified index minus one.
	 * 
	 * @param index the starting index of the target iterator
	 * @return an {@link IntIterator}.
	 * @throws IndexOutOfBoundsException when the index is out of range
	 */
	public IntIterator intIterator(int index);

	/**
	 * Returns an iterator over elements of type {@link Integer}.
	 *
	 * @return an Iterator.
	 */
	public Iterator<Integer> iterator();

	/**
	 * Compares the specified object with this list for equality. Returns
	 * {@code true} if and only if the specified object is also a list, both lists
	 * have the same size, and all corresponding pairs of elements in the two lists
	 * are <i>equal</i>. In other words, two lists are defined to be equal if they
	 * contain the same elements in the same order.
	 * <p>
	 *
	 * @param o the object to be compared for equality with this list
	 * @return {@code true} if the specified object is equal to this list
	 */
	@Override
	public boolean equals(Object o);

	/**
	 * Returns the hash code value for this list. The hash code of a list is defined
	 * to be the result of the following calculation:
	 * 
	 * <pre>
	 * {
	 * 	int hashCode = 1;
	 * 	for (Integer e : list)
	 * 		hashCode = 31 * hashCode + e;
	 * }
	 * </pre>
	 * 
	 * This ensures that <tt>list1.equals(list2)</tt> implies that
	 * <tt>list1.hashCode()==list2.hashCode()</tt> for any two lists, <tt>list1</tt>
	 * and <tt>list2</tt>, as required by the general contract of
	 * {@link Object#hashCode}.
	 *
	 * @return the hash code value for this list
	 * @see Object#equals(Object)
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode();

}
