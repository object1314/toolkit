package org.xuyh.container;

/**
 * It's just a simple implements on {@link SinglyLinkedNode}.
 * 
 * @author XuYanhang
 * @since 2020-08-22
 *
 * @param <T> Generic type
 */
public class SimpleSinglyLinkedNode<T> implements SinglyLinkedNode<T> {

	/**
	 * The value this node wraps. A <code>null</code> value is permitted.
	 */
	private T value;

	/**
	 * The node this node refers to. A <code>null</code> value is permitted.
	 */
	private SinglyLinkedNode<T> next;

	/**
	 * Create this node in single with no next node and any value.
	 */
	public SimpleSinglyLinkedNode() {
		super();
	}

	/**
	 * Create this node in single with no next node but a value.
	 * 
	 * @param value the {@link #value} to initial
	 */
	public SimpleSinglyLinkedNode(T value) {
		super();
		this.value = value;
	}

	/**
	 * Create this node with next node and a value.
	 * 
	 * @param value the {@link #value} to initial
	 * @param next  the {@link #next} to initial
	 */
	public SimpleSinglyLinkedNode(T value, SinglyLinkedNode<T> next) {
		super();
		this.value = value;
		this.next = next;
	}

	/**
	 * @param value the {@link #value} to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @param next the {@link #next} to set
	 */
	public void setNext(SinglyLinkedNode<T> next) {
		this.next = next;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public SinglyLinkedNode<T> getNext() {
		return next;
	}

}
