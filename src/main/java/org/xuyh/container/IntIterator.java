package org.xuyh.container;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntConsumer;

/**
 * An iterator for lists that allows the programmer to traverse the list in
 * either direction, modify the list during iteration, and obtain the iterator's
 * current position in the list. An {@code IntIterator} has no current element;
 * its <I>cursor position</I> always lies between the element that would be
 * returned by a call to {@code prev()} and the element that would be returned
 * by a call to {@code next()}. An iterator for a list of length {@code n} has
 * {@code n+1} possible cursor positions, as illustrated by the carets
 * ({@code ^}) below:
 * 
 * <PRE>
 *                      Element(0)   Element(1)   Element(2)   ... Element(n-1)
 * cursor positions:  ^            ^            ^            ^                  ^
 * </PRE>
 * 
 * Note that the {@link #remove()} and {@link #set(int)} methods are <i>not</i>
 * defined in terms of the cursor position; they are defined to operate on the
 * last element returned by a call to {@link #next()} or {@link #prev()}.
 * 
 * @see java.util.ListIterator
 * @author XuYanhang
 * @since 2020-10-06
 *
 */
public interface IntIterator {

	/**
	 * Returns {@code true} if this list iterator has more elements when traversing
	 * the list in the forward direction. (In other words, returns {@code true} if
	 * {@link #next} would return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the list iterator has more elements when traversing
	 *         the list in the forward direction
	 */
	public boolean hasNext();

	/**
	 * Returns the next element in the list and advances the cursor position. This
	 * method may be called repeatedly to iterate through the list, or intermixed
	 * with calls to {@link #prev} to go back and forth. (Note that alternating
	 * calls to {@code next} and {@code prev} will return the same element
	 * repeatedly.)
	 *
	 * @return the next element in the list
	 * @throws NoSuchElementException if the iteration has no next element
	 */
	public int next();

	/**
	 * Returns {@code true} if this list iterator has more elements when traversing
	 * the list in the reverse direction. (In other words, returns {@code true} if
	 * {@link #prev} would return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the list iterator has more elements when traversing
	 *         the list in the reverse direction
	 */
	public boolean hasPrev();

	/**
	 * Returns the previous element in the list and moves the cursor position
	 * backwards. This method may be called repeatedly to iterate through the list
	 * backwards, or intermixed with calls to {@link #next} to go back and forth.
	 * (Note that alternating calls to {@code next} and {@code prev} will return the
	 * same element repeatedly.)
	 *
	 * @return the previous element in the list
	 * @throws NoSuchElementException if the iteration has no previous element
	 */
	public int prev();

	/**
	 * Returns the index of the element that would be returned by a subsequent call
	 * to {@link #next}. (Returns list size if the list iterator is at the end of
	 * the list.)
	 *
	 * @return the index of the element that would be returned by a subsequent call
	 *         to {@code next}, or list size if the list iterator is at the end of
	 *         the list
	 */
	public int nextIndex();

	/**
	 * Returns the index of the element that would be returned by a subsequent call
	 * to {@link #prev}. (Returns -1 if the list iterator is at the beginning of the
	 * list.)
	 *
	 * @return the index of the element that would be returned by a subsequent call
	 *         to {@code prev}, or -1 if the list iterator is at the beginning of
	 *         the list
	 */
	public int prevIndex();

	/**
	 * Removes from the list the last element that was returned by {@link #next} or
	 * {@link #prev} (optional operation). This call can only be made once per call
	 * to {@code next} or {@code prev}. It can be made only if {@link #add} has not
	 * been called after the last call to {@code next} or {@code prev}.
	 *
	 * @throws UnsupportedOperationException if the {@code remove} operation is not
	 *                                       supported by this list iterator
	 * @throws IllegalStateException         if neither {@code next} nor
	 *                                       {@code prev} have been called, or
	 *                                       {@code remove} or {@code add} have been
	 *                                       called after the last call to
	 *                                       {@code next} or {@code prev}
	 */
	default public void remove() {
		throw new UnsupportedOperationException("remove");
	}

	/**
	 * Replaces the last element returned by {@link #next} or {@link #prev} with the
	 * specified element (optional operation). This call can be made only if neither
	 * {@link #remove} nor {@link #add} have been called after the last call to
	 * {@code next} or {@code prev}.
	 *
	 * @param e the element with which to replace the last element returned by
	 *          {@code next} or {@code prev}
	 * @throws UnsupportedOperationException if the {@code set} operation is not
	 *                                       supported by this list iterator
	 * @throws IllegalArgumentException      if some aspect of the specified element
	 *                                       prevents it from being added to this
	 *                                       list
	 * @throws IllegalStateException         if neither {@code next} nor
	 *                                       {@code prev} have been called, or
	 *                                       {@code remove} or {@code add} have been
	 *                                       called after the last call to
	 *                                       {@code next} or {@code prev}
	 */
	default public void set(int e) {
		throw new UnsupportedOperationException("set");
	}

	/**
	 * Inserts the specified element into the list (optional operation). The element
	 * is inserted immediately before the element that would be returned by
	 * {@link #next}, if any, and after the element that would be returned by
	 * {@link #prev}, if any. (If the list contains no elements, the new element
	 * becomes the sole element on the list.) The new element is inserted before the
	 * implicit cursor: a subsequent call to {@code next} would be unaffected, and a
	 * subsequent call to {@code prev} would return the new element. (This call
	 * increases by one the value that would be returned by a call to
	 * {@code nextIndex} or {@code prevIndex}.)
	 *
	 * @param e the element to insert
	 * @throws UnsupportedOperationException if the {@code add} method is not
	 *                                       supported by this list iterator
	 * @throws IllegalArgumentException      if some aspect of this element prevents
	 *                                       it from being added to this list
	 */
	default public void add(int e) {
		throw new UnsupportedOperationException("add");
	}

	/**
	 * Performs the given action for each remaining element until all elements have
	 * been processed or the action throws an exception. Actions are performed in
	 * the order of iteration, if that order is specified. Exceptions thrown by the
	 * action are relayed to the caller.
	 *
	 * @implSpec
	 *           <p>
	 *           The default implementation behaves as if:
	 * 
	 *           <pre>
	 * {@code
	 *     while (hasNext())
	 *         action.accept(next());
	 * }
	 *           </pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 */
	default public void forEachRemaining(IntConsumer action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(next());
	}

}
