/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

/**
 * This integer list is a list that permits only integer values. Some behavior
 * is simular with {@link ArrayList} when some of these implements are from
 * {@link ArrayList} while some is different.
 *
 * @author XuYanhang
 * @since 2020-10-06
 */
public class IntArrayList implements IntList, java.util.RandomAccess, Cloneable, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4545675543813079249L;

	/**
	 * Default initial capacity.
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * Shared empty array instance used for empty instances.
	 */
	private static final int[] EMPTY_DATA = {};

	/**
	 * The maximum size of array to allocate. Some VMs reserve some header words in
	 * an array. Attempts to allocate larger arrays may result in OutOfMemoryError:
	 * Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * The array buffer into which the elements of the List are stored. The capacity
	 * of the List is the length of this array buffer. Any empty List with
	 * elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA will be expanded to
	 * DEFAULT_CAPACITY when the first element is added.
	 */
	private transient int[] data; // non-private to simplify nested class access

	/**
	 * The size of the List (the number of elements it contains).
	 */
	private transient int size;

	/**
	 * The number of times this list has been <i>structurally modified</i>.
	 * Structural modifications are those that change the size of the list, or
	 * otherwise perturb it in such a fashion that iterations in progress may yield
	 * incorrect results.
	 */
	protected transient int modCount = 0;

	/**
	 * Create an empty list.
	 */
	public IntArrayList() {
		super();
		this.data = EMPTY_DATA;
		this.size = 0;
	}

	/**
	 * Create a list in specified array. The list is "safe" when any modification
	 * has no effect on the origin array.
	 * 
	 * @throws NullPointerException if the array is <code>null</code>
	 */
	public IntArrayList(int[] array) {
		super();
		this.data = array.length == 0 ? EMPTY_DATA : Arrays.copyOf(array, array.length);
		this.size = array.length;
	}

	/**
	 * Create a list in specified collection. The list is "safe" when any
	 * modification has no effect on the origin collection.
	 * 
	 * @throws NullPointerException if the collection is <code>null</code> or any of
	 *                              the integer value is <code>null</code>
	 */
	public IntArrayList(Collection<? extends Number> c) {
		super();
		this.data = collectionToArray(c);
		this.size = this.data.length;
	}

	/**
	 * Create a list in specified integer list. The list is "safe" when any
	 * modification has no effect on the origin list.
	 * 
	 * @throws NullPointerException if the list is <code>null</code>
	 */
	public IntArrayList(IntList o) {
		super();
		this.data = o.toArray();
		this.size = this.data.length;
		if (this.size == 0)
			this.data = EMPTY_DATA;
	}

	/**
	 * Increases the capacity of this List, if necessary, to ensure that it can hold
	 * at least the number of elements specified by the minimum capacity argument.
	 *
	 * @param minCapacity the desired minimum capacity
	 * @return this
	 */
	public IntArrayList ensureCapacity(int minCapacity) {
		if (minCapacity < 0 || minCapacity > MAX_ARRAY_SIZE)
			throw new OutOfMemoryError();
		if (data == EMPTY_DATA)
			minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
		if (minCapacity > data.length) {
			int oldCapacity = data.length;
			int newCapacity = oldCapacity + (oldCapacity >> 1);
			if (newCapacity > MAX_ARRAY_SIZE) {
				newCapacity = MAX_ARRAY_SIZE;
			}
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			// minCapacity is usually close to size, so this is a win:
			data = Arrays.copyOf(data, newCapacity);
		}
		return this;
	}

	/**
	 * Trims the capacity of this <tt>ArrayList</tt> instance to be the list's
	 * current size. An application can use this operation to minimize the storage
	 * of an <tt>ArrayList</tt> instance.
	 * 
	 * @return this
	 */
	public IntArrayList trimToSize() {
		if (size < data.length)
			data = (size == 0) ? EMPTY_DATA : Arrays.copyOf(data, size);
		return this;
	}

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return the number of elements in this list
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 *
	 * @return <tt>true</tt> if this list contains no elements
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 *
	 * @param value element whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean contains(int value) {
		return indexOf(value) >= 0;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 */
	public int indexOf(int value) {
		for (int i = 0; i < size; i++)
			if (value == data[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 */
	public int lastIndexOf(int value) {
		for (int i = size - 1; i > -1; i--)
			if (value == data[i])
				return i;
		return -1;
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public int get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(index + " out of [0," + size + ")");
		return data[index];
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 *
	 * @param index index of the element to replace
	 * @param value element to be stored at the specified position
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public IntArrayList set(int index, int value) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(index + " out of [0," + size + ")");
		data[index] = value;
		return this;
	}

	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param value element to be appended to this list
	 * @return this
	 */
	public IntArrayList add(int value) {
		this.modCount++;
		ensureCapacity(size + 1); // Increments modCount!!
		data[size++] = value;
		return this;
	}

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param values elements to be appended to this list
	 * @return this
	 * @throws NullPointerException when the input value array is <code>null</code>
	 */
	public IntArrayList addAll(int... values) {
		if (null == values)
			throw new NullPointerException("values");
		if (0 == values.length)
			return this;
		this.modCount++;
		ensureCapacity(size + values.length); // Increments modCount!!
		System.arraycopy(values, 0, data, size, values.length);
		size += values.length;
		return this;
	}

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param c elements to be appended to this list but in collection way
	 * @return this
	 * @throws NullPointerException when the input collection is <code>null</code>
	 *                              or any element in it is <code>null</code>
	 */
	public IntArrayList addAll(Collection<? extends Number> c) {
		return addAll(collectionToArray(c));
	}

	/**
	 * Appends the specified elements to the end of this list.
	 *
	 * @param o elements to be appended to this list but in list way
	 * @return this
	 * @throws NullPointerException when the input list is <code>null</code>
	 */
	public IntArrayList addAll(IntList o) {
		return addAll(o.toArray());
	}

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
	public IntArrayList insert(int index, int value) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException(index + " out of [0," + size + "]");
		ensureCapacity(size + 1); // Increments modCount!!
		System.arraycopy(data, index, data, index + 1, size - index);
		data[index] = value;
		size++;
		return this;
	}

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
	public IntArrayList insertAll(int index, int... values) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException(index + " out of [0," + size + "]");
		if (0 == values.length)
			return this;
		ensureCapacity(size + values.length); // Increments modCount!!
		System.arraycopy(data, index, data, index + values.length, size - index);
		System.arraycopy(values, 0, data, index, values.length);
		size += values.length;
		return this;
	}

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
	public IntArrayList insertAll(int index, Collection<? extends Number> c) {
		return insertAll(index, collectionToArray(c));
	}

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
	public IntArrayList insertAll(int index, IntList o) {
		return insertAll(index, o.toArray());
	}

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 *
	 * @param index the index of the element to be removed
	 * @return this
	 * @throws IndexOutOfBoundsException when the index is out
	 */
	public IntArrayList del(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(index + " out of [0," + size + ")");
		this.modCount++;
		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(data, index + 1, data, index, numMoved);
		data[--size] = 0;
		return this;
	}

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
	public IntArrayList delIf(IntPredicate filter) {
		if (null == filter)
			throw new NullPointerException("filter");
		final int expectedModCount = modCount;
		final int size = this.size;
		final BitSet removeSet = new BitSet(size);
		int removeCount = 0;
		for (int i = 0; expectedModCount == modCount && i < size; i++) {
			if (filter.test(data[i])) {
				removeSet.set(i);
				removeCount++;
			}
		}
		if (modCount != expectedModCount) {
			throw new ConcurrentModificationException();
		}

		// shift surviving elements left over the spaces left by removed elements
		final boolean anyToRemove = removeCount > 0;
		if (anyToRemove) {
			final int newSize = size - removeCount;
			for (int i = 0, j = 0; (i < size) && (j < newSize); i++, j++) {
				i = removeSet.nextClearBit(i);
				data[j] = data[i];
			}
			for (int k = newSize; k < size; k++) {
				data[k] = 0;
			}
			this.size = newSize;
			modCount++;
		}
		return this;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after this
	 * call returns.
	 */
	public IntArrayList clear() {
		if (size != 0) {
			this.modCount++;
			for (int i = 0; i < size; i++)
				data[i] = 0;
			size = 0;
		}
		return this;
	}

	/**
	 * Sort this list in ASC way. For example, unsorted array of [3, 1, 2, 0] would
	 * be changed as [0, 1, 2, 3] after sort action.
	 * 
	 * @see Arrays#sort(int[], int, int)
	 * @return this
	 */
	public IntArrayList sort() {
		this.modCount++;
		Arrays.sort(data, 0, size);
		return this;
	}

	/**
	 * Reverse all elements in this list. For example, if the original list is [0,
	 * 1, 2]. After reverse operation, it is changed as [2, 1, 0].
	 * 
	 * @return this
	 */
	public IntArrayList reverse() {
		this.modCount++;
		int mid = size >> 1;
		for (int i = 0; i < mid; i++) {
			int t = data[size - 1 - i];
			data[size - 1 - i] = data[i];
			data[i] = t;
		}
		return this;
	}

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
	public IntArrayList subList(int fromIndex, int toIndex) {
		if (toIndex < -1 || toIndex > size)
			throw new IndexOutOfBoundsException("toIndex=" + toIndex);
		if (fromIndex == toIndex)
			return new IntArrayList();
		if (fromIndex < 0 || fromIndex >= size)
			throw new IndexOutOfBoundsException("fromIndex=" + fromIndex);
		IntArrayList sub = new IntArrayList();
		if (fromIndex < toIndex) {
			sub.size = toIndex - fromIndex;
			sub.data = Arrays.copyOfRange(data, fromIndex, toIndex);
		} else {
			sub.size = fromIndex - toIndex;
			sub.data = new int[fromIndex - toIndex];
			int i = fromIndex, c = 0;
			while (i != toIndex) {
				sub.data[c++] = data[i--];
			}
		}
		return sub;
	}

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
	public int[] toArray() {
		return 0 == size ? EMPTY_DATA : Arrays.copyOf(data, size);
	}

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
	public Integer[] toRefArray() {
		Integer[] array = new Integer[size];
		for (int i = 0; i < size; i++) {
			array[i] = Integer.valueOf(data[i]);
		}
		return array;
	}

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
	public ArrayList<Integer> toRefList() {
		ArrayList<Integer> list = new ArrayList<>(this.size);
		for (int i = 0; i < size; i++)
			list.add(Integer.valueOf(data[i]));
		return list;
	}

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
	public IntIterator intIterator(int index) {

		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index);

		return new IntIterator() {

			int cursor = index;
			int lastRet = -1;
			int expectedModCount = IntArrayList.this.modCount;

			@Override
			public boolean hasNext() {
				return this.cursor < IntArrayList.this.size;
			}

			@Override
			public int next() {
				if (this.cursor >= IntArrayList.this.size)
					throw new NoSuchElementException("next");
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				return IntArrayList.this.data[lastRet = cursor++];
			}

			@Override
			public boolean hasPrev() {
				return this.cursor != 0;
			}

			@Override
			public int prev() {
				if (this.cursor == 0)
					throw new NoSuchElementException("prev");
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				return IntArrayList.this.data[lastRet = --cursor];
			}

			@Override
			public int nextIndex() {
				return cursor;
			}

			@Override
			public int prevIndex() {
				return cursor - 1;
			}

			@Override
			public void remove() {
				if (this.lastRet < 0)
					throw new IllegalStateException();
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				del(this.lastRet);
				this.cursor = this.lastRet;
				this.lastRet = -1;
				this.expectedModCount = IntArrayList.this.modCount;
			}

			@Override
			public void set(int e) {
				if (lastRet < 0)
					throw new IllegalStateException();
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				IntArrayList.this.set(lastRet, e);
				this.expectedModCount = IntArrayList.this.modCount;
			}

			@Override
			public void add(int e) {
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				IntArrayList.this.insert(cursor, e);
				cursor++;
				lastRet = -1;
				expectedModCount = IntArrayList.this.modCount;
			}

		};
	}

	/**
	 * Returns an iterator over elements of type {@link Integer}.
	 *
	 * @return an Iterator.
	 */
	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {

			int cursor = 0;
			int lastRet = -1;
			int expectedModCount = IntArrayList.this.modCount;

			@Override
			public boolean hasNext() {
				return this.cursor < IntArrayList.this.size;
			}

			@Override
			public Integer next() {
				if (this.cursor >= IntArrayList.this.size)
					throw new NoSuchElementException("next");
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				return IntArrayList.this.data[lastRet = cursor++];
			}

			@Override
			public void remove() {
				if (this.lastRet < 0)
					throw new IllegalStateException();
				if (this.expectedModCount != IntArrayList.this.modCount)
					throw new ConcurrentModificationException();
				del(this.lastRet);
				this.cursor = this.lastRet;
				this.lastRet = -1;
				this.expectedModCount = IntArrayList.this.modCount;
			}
		};
	}

	/**
	 * Returns a copy of this List instance.
	 *
	 * @return a clone of this List instance
	 */
	public IntArrayList clone() {
		try {
			IntArrayList v = (IntArrayList) super.clone();
			v.data = Arrays.copyOf(data, size);
			return v;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e);
		}
	}

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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof IntList))
			return false;
		IntList other = (IntList) o;
		if (this.size != other.size())
			return false;
		return true;
	}

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
	public int hashCode() {
		int hashCode = 1;
		for (int i = 0; i < size; i++)
			hashCode = 31 * hashCode + data[i];
		return hashCode;
	}

	/**
	 * Create the string of this list. Cast this list to string as [1,2,3,4,5].
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++) {
			sb.append(data[i]);
			if (i != size - 1) {
				sb.append(',');
			}
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Save the state of the list instance to a stream (that is, serialize it).
	 *
	 * @serialData The size and the data in order.
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {

		// Write out element count, and any hidden stuff
		s.defaultWriteObject();

		// Write out size as capacity for behavioural compatibility with clone()
		s.writeInt(size);

		// Write out all elements in the proper order.
		for (int i = 0; i < size; i++) {
			s.writeInt(data[i]);
		}
	}

	/**
	 * Reconstitute the List instance from a stream (that is, deserialize it).
	 */
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {

		// Read in any hidden stuff
		s.defaultReadObject();

		// Read in size
		size = s.readInt();

		// Read in data
		data = EMPTY_DATA;

		if (size > 0) {
			// be like clone(), allocate array based upon size not capacity
			ensureCapacity(size);

			// Read in all elements in the proper order.
			for (int i = 0; i < size; i++) {
				data[i] = s.readInt();
			}
		}
	}

	/**
	 * Cast a collection to an array in primitive integer type.
	 * 
	 * @param c collection to cast array from
	 * @return an array in primitive integer way.
	 * @throws NullPointerException if the collection is <code>null</code> or any
	 *                              element is <code>null</code>
	 */
	private static int[] collectionToArray(Collection<? extends Number> c) {
		Number[] ns = c.toArray(new Number[c.size()]);
		if (0 == ns.length) {
			return EMPTY_DATA;
		}
		int[] vs = new int[ns.length];
		for (int i = 0; i < vs.length; i++) {
			vs[i] = ns[i].intValue();
		}
		return vs;
	}

}
