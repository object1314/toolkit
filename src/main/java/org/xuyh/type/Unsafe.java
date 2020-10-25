/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.lang.reflect.Field;

/**
 * This class is a proxy on JDK's {@link sun.misc.Unsafe Unsafe} who should
 * provide access to low-level operations and its use should be limited to
 * trusted code. Fields can be accessed using memory addresses, with undefined
 * behaviour occurring if invalid memory addresses are given.
 *
 * @see sun.misc.Unsafe
 * @author XuYanhang
 * @since 2020-10-24
 *
 */
@SuppressWarnings("restriction")
public final class Unsafe {

	/**
	 * Singleton of the unsafe
	 */
	private static final Unsafe SINGLETON = new Unsafe();

	/**
	 * Provides the caller with the capability of performing unsafe operations.
	 * <p>
	 * The returned <code>Unsafe</code> object should be carefully guarded by the
	 * caller, since it can be used to read and write data at arbitrary memory
	 * addresses. It must never be passed to untrusted code.
	 * <p>
	 * Most methods in this class are very low-level, and correspond to a small
	 * number of hardware instructions (on typical machines). Compilers are
	 * encouraged to optimize these methods accordingly.
	 * <p>
	 * Here is a suggested idiom for using unsafe operations:
	 *
	 * <blockquote>
	 * 
	 * <pre>
	 * class MyTrustedClass {
	 *   private static final Unsafe unsafe = Unsafe.getUnsafe();
	 *   ...
	 *   private long myCountAddress = ...;
	 *   public int getCount() { return unsafe.getByte(myCountAddress); }
	 * }
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * (It may assist compilers to make the local variable be <code>final</code>.)
	 */
	public static Unsafe getUnsafe() {
		return SINGLETON;
	}

	/**
	 * Proxy an instance of {@link sun.misc.Unsafe Unsafe} in JDK
	 */
	private final sun.misc.Unsafe unsafe;

	/**
	 * Instantiate this class where is private for class
	 */
	private Unsafe() {
		try {
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (sun.misc.Unsafe) field.get((Object) null);
		} catch (Exception e) {
			throw new InternalError(e);
		}
	}

	/*
	 * These work on object fields in the Java heap. They will not work on elements
	 * of packed arrays.
	 */

	/**
	 * Fetches a value from a given Java variable. More specifically, fetches a
	 * field or array element within the given object <code>obj</code> at the given
	 * offset, or (if <code>obj</code> is null) from the memory address whose
	 * numerical value is the given offset.
	 * <p>
	 * The results are undefined unless one of the following cases is true:
	 * <ul>
	 * <li>The offset was obtained from {@link #objectFieldOffset} on the
	 * {@link Field} of some Java field and the object referred to by
	 * <code>obj</code> is of a class compatible with that field's class.
	 *
	 * <li>The offset and object reference <code>obj</code> (either null or
	 * non-null) were both obtained via {@link #staticFieldOffset} and
	 * {@link #staticFieldBase} (respectively) from the reflective {@link Field}
	 * representation of some Java field.
	 *
	 * <li>The object referred to by <code>obj</code> is an array, and the offset is
	 * an integer of the form <code>B+N*S</code>, where <code>N</code> is a valid
	 * index into the array, and <code>B</code> and <code>S</code> are the values
	 * obtained by {@link #arrayBaseOffset} and {@link #arrayIndexScale}
	 * (respectively) from the array's class. The value referred to is the
	 * <code>N</code><em>th</em> element of the array.
	 *
	 * </ul>
	 * <p>
	 * If one of the above cases is true, the call references a specific Java
	 * variable (field or array element). However, the results are undefined if that
	 * variable is not in fact of the type returned by this method.
	 * <p>
	 * This method refers to a variable by means of two parameters, and so it
	 * provides (in effect) a <em>double-register</em> addressing mode for Java
	 * variables. When the object reference is null, this method uses its offset as
	 * an absolute address. This is similar in operation to methods such as
	 * {@link #getInt(long)}, which provide (in effect) a <em>single-register</em>
	 * addressing mode for non-Java variables. However, because Java variables may
	 * have a different layout in memory from non-Java variables, programmers should
	 * not assume that these two addressing modes are ever equivalent. Also,
	 * programmers should remember that offsets from the double-register addressing
	 * mode cannot be portably confused with longs used in the single-register
	 * addressing mode.
	 *
	 * @param obj    Java heap object in which the variable resides, if any, else
	 *               null
	 * @param offset indication of where the variable resides in a Java heap object,
	 *               if any, else a memory address locating the variable statically
	 * @return the value fetched from the indicated Java variable
	 * @throws RuntimeException No defined exceptions are thrown, not even
	 *                          {@link NullPointerException}
	 */
	public Object getObject(Object obj, long offset) {
		return unsafe.getObject(obj, offset);
	}

	/**
	 * Stores a value into a given Java variable.
	 * <p>
	 * The first two parameters are interpreted exactly as with
	 * {@link #getInt(Object, long)} to refer to a specific Java variable (field or
	 * array element). The given value is stored into that variable.
	 * <p>
	 * The variable must be of the same type as the method parameter
	 * <code>value</code>.
	 * <p>
	 * Unless the reference <code>value</code> being stored is either null or
	 * matches the field type, the results are undefined. If the reference
	 * <code>obj</code> is non-null, car marks or other store barriers for that
	 * object (if the VM requires them) are updated.
	 *
	 * @param obj    Java heap object in which the variable resides, if any, else
	 *               null
	 * @param offset indication of where the variable resides in a Java heap object,
	 *               if any, else a memory address locating the variable statically
	 * @param value  the value to store into the indicated Java variable
	 * @throws RuntimeException No defined exceptions are thrown, not even
	 *                          {@link NullPointerException}
	 */
	public void putObject(Object obj, long offset, Object value) {
		unsafe.putObject(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public boolean getBoolean(Object obj, long offset) {
		return unsafe.getBoolean(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putBoolean(Object obj, long offset, boolean value) {
		unsafe.putBoolean(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public byte getByte(Object obj, long offset) {
		return unsafe.getByte(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putByte(Object obj, long offset, byte value) {
		unsafe.putByte(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public short getShort(Object obj, long offset) {
		return unsafe.getShort(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putShort(Object obj, long offset, short value) {
		unsafe.putShort(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public char getChar(Object obj, long offset) {
		return unsafe.getChar(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putChar(Object obj, long offset, char value) {
		unsafe.putChar(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public int getInt(Object obj, long offset) {
		return unsafe.getInt(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putInt(Object obj, long offset, int value) {
		unsafe.putInt(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public long getLong(Object obj, long offset) {
		return unsafe.getLong(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putLong(Object obj, long offset, long value) {
		unsafe.putLong(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public float getFloat(Object obj, long offset) {
		return unsafe.getFloat(obj, offset);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putFloat(Object obj, long offset, float value) {
		unsafe.putFloat(obj, offset, value);
	}

	/**
	 * @see #getObject(Object, long)
	 */
	public double getDouble(Object obj, long value) {
		return unsafe.getDouble(obj, value);
	}

	/**
	 * @see #putObject(Object, int, Object)
	 */
	public void putDouble(Object obj, long offset, double value) {
		unsafe.putDouble(obj, offset, value);
	}

	/**
	 * Fetches a reference value from a given Java variable, with volatile load
	 * semantics. Otherwise identical to {@link #getObject(Object, long)}
	 * 
	 * @param obj    the object containing the field to read.
	 * @param offset the offset of the object field within <code>obj</code>.
	 */
	public Object getObjectVolatile(Object obj, long offset) {
		return unsafe.getObjectVolatile(obj, offset);
	}

	/**
	 * Stores a reference value into a given Java variable, with volatile store
	 * semantics. Otherwise identical to {@link #putObject(Object, long, Object)}
	 * 
	 * @param obj    the object containing the field to modify.
	 * @param offset the offset of the object field within <code>obj</code>.
	 * @param value  the new value of the field.
	 * @see #putObject(Object,long,Object)
	 */
	public void putObjectVolatile(Object obj, long offset, Object value) {
		unsafe.putOrderedObject(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public boolean getBooleanVolatile(Object obj, long offset) {
		return unsafe.getBooleanVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putBooleanVolatile(Object obj, long offset, boolean value) {
		unsafe.putBooleanVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public byte getByteVolatile(Object obj, long offset) {
		return unsafe.getByteVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putByteVolatile(Object obj, long offset, byte value) {
		unsafe.putByteVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public short getShortVolatile(Object obj, long offset) {
		return unsafe.getShortVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putShortVolatile(Object obj, long offset, short value) {
		unsafe.putShortVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public char getCharVolatile(Object obj, long offset) {
		return unsafe.getCharVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putCharVolatile(Object obj, long offset, char value) {
		unsafe.putCharVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public int getIntVolatile(Object obj, long offset) {
		return unsafe.getIntVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putIntVolatile(Object obj, long offset, int value) {
		unsafe.putIntVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public long getLongVolatile(Object obj, long offset) {
		return unsafe.getLongVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putLongVolatile(Object obj, long offset, long value) {
		unsafe.putLongVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public float getFloatVolatile(Object obj, long offset) {
		return unsafe.getFloatVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putFloatVolatile(Object obj, long offset, float value) {
		unsafe.putFloatVolatile(obj, offset, value);
	}

	/**
	 * @see #getObjectVolatile(Object, long)
	 */
	public double getDoubleVolatile(Object obj, long offset) {
		return unsafe.getDoubleVolatile(obj, offset);
	}

	/**
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putDoubleVolatile(Object obj, long offset, double value) {
		unsafe.putDoubleVolatile(obj, offset, value);
	}

	/**
	 * Version of {@link #putObjectVolatile(Object, long, Object)} that does not
	 * guarantee immediate visibility of the store to other threads. This method is
	 * generally only useful if the underlying field is a Java volatile (or if an
	 * array cell, one that is otherwise only accessed using volatile accesses).
	 *
	 * @param obj    the object containing the field to modify.
	 * @param offset the offset of the object field within <code>obj</code>.
	 * @param value  the new value of the field.
	 * @see #putObjectVolatile(Object, long, Object)
	 */
	public void putOrderedObject(Object obj, long offset, Object value) {
		unsafe.putOrderedObject(obj, offset, value);
	}

	/**
	 * Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)}
	 * 
	 * @see #putIntVolatile(Object,long,int)
	 */
	public void putOrderedInt(Object obj, long offset, int value) {
		unsafe.putOrderedInt(obj, offset, value);
	}

	/**
	 * Ordered/Lazy version of {@link #putLongVolatile(Object, long, long)}
	 * 
	 * @see #putLongVolatile(Object,long,long)
	 */
	public void putOrderedLong(Object obj, long offset, long value) {
		unsafe.putOrderedLong(obj, offset, value);
	}

	/*
	 * The following contain CAS-based Java implementations used on platforms not
	 * supporting native instructions
	 */

	/**
	 * Atomically update Java variable to <tt>update</tt> if it is currently holding
	 * <tt>expected</tt>.
	 * <p>
	 * Compares the value of the object field at the specified offset in the
	 * supplied object with the given expected value, and updates it if they match.
	 * The operation of this method should be atomic, thus providing an
	 * uninterruptible way of updating an object field.
	 * 
	 * @param obj    the object containing the field to modify.
	 * @param offset the offset of the object field within <code>obj</code>.
	 * @param expect the expected value of the field.
	 * @param update the new value of the field if it equals <code>expect</code>.
	 * @return true if the field was changed.
	 */
	public boolean compareAndSwapObject(Object obj, long offset, Object expect, Object update) {
		return unsafe.compareAndSwapObject(obj, offset, expect, update);
	}

	/**
	 * Atomically update Java variable to <tt>update</tt> if it is currently holding
	 * <tt>expected</tt>.
	 * <p>
	 * Compares the value of the integer field at the specified offset in the
	 * supplied object with the given expected value, and updates it if they match.
	 * The operation of this method should be atomic, thus providing an
	 * uninterruptible way of updating an integer field.
	 * 
	 * @param obj    the object containing the field to modify.
	 * @param offset the offset of the integer field within <code>obj</code>.
	 * @param expect the expected value of the field.
	 * @param update the new value of the field if it equals <code>expect</code>.
	 * @return true if the field was changed.
	 */
	public boolean compareAndSwapInt(Object obj, long offset, int expect, int update) {
		return unsafe.compareAndSwapInt(obj, offset, expect, update);
	}

	/**
	 * Atomically update Java variable to <tt>update</tt> if it is currently holding
	 * <tt>expected</tt>.
	 * <p>
	 * Compares the value of the long field at the specified offset in the supplied
	 * object with the given expected value, and updates it if they match. The
	 * operation of this method should be atomic, thus providing an uninterruptible
	 * way of updating a long field.
	 * 
	 * @param obj    the object containing the field to modify.
	 * @param offset the offset of the long field within <code>obj</code>.
	 * @param expect the expected value of the field.
	 * @param update the new value of the field if it equals <code>expect</code>.
	 * @return true if the field was changed.
	 */
	public boolean compareAndSwapLong(Object obj, long offset, long expect, long update) {
		return unsafe.compareAndSwapLong(obj, offset, expect, update);
	}

	/**
	 * Atomically adds the given value to the current value of a field or array
	 * element within the given object <code>obj</code> at the given
	 * <code>offset</code>.
	 *
	 * @param obj    object/array to update the field/element in
	 * @param offset field/element offset
	 * @param delta  the value to add
	 * @return the previous value
	 */
	public int getAndAddInt(Object obj, long offset, int delta) {
		int v;
		do {
			v = unsafe.getIntVolatile(obj, offset);
		} while (!unsafe.compareAndSwapInt(obj, offset, v, v + delta));
		return v;
	}

	/**
	 * Atomically adds the given value to the current value of a field or array
	 * element within the given object <code>obj</code> at the given
	 * <code>offset</code>.
	 *
	 * @param obj    object/array to update the field/element in
	 * @param offset field/element offset
	 * @param delta  the value to add
	 * @return the previous value
	 */
	public long getAndAddLong(Object obj, long offset, long delta) {
		long v;
		do {
			v = unsafe.getLongVolatile(obj, offset);
		} while (!unsafe.compareAndSwapLong(obj, offset, v, v + delta));
		return v;
	}

	/**
	 * Atomically exchanges the given reference value with the current reference
	 * value of a field or array element within the given object <code>obj</code> at
	 * the given <code>offset</code>.
	 *
	 * @param obj      object/array to update the field/element in
	 * @param offset   field/element offset
	 * @param newValue new value
	 * @return the previous value
	 */
	public Object getAndSetObject(Object obj, long offset, Object newValue) {
		Object v;
		do {
			v = unsafe.getObjectVolatile(obj, offset);
		} while (!unsafe.compareAndSwapObject(obj, offset, v, newValue));
		return v;
	}

	/**
	 * Atomically exchanges the given value with the current value of a field or
	 * array element within the given object <code>obj</code> at the given
	 * <code>offset</code>.
	 *
	 * @param obj      object/array to update the field/element in
	 * @param offset   field/element offset
	 * @param newValue new value
	 * @return the previous value
	 */
	public int getAndSetInt(Object obj, long offset, int newValue) {
		int v;
		do {
			v = unsafe.getIntVolatile(obj, offset);
		} while (!unsafe.compareAndSwapInt(obj, offset, v, newValue));
		return v;
	}

	/**
	 * Atomically exchanges the given value with the current value of a field or
	 * array element within the given object <code>obj</code> at the given
	 * <code>offset</code>.
	 *
	 * @param obj      object/array to update the field/element in
	 * @param offset   field/element offset
	 * @param newValue new value
	 * @return the previous value
	 */
	public long getAndSetLong(Object obj, long offset, long newValue) {
		long v;
		do {
			v = unsafe.getLongVolatile(obj, offset);
		} while (!unsafe.compareAndSwapLong(obj, offset, v, newValue));
		return v;
	}

	/*
	 * These work on values in the C heap
	 */

	/**
	 * Fetches a value from a given memory address. If the address is zero, or does
	 * not point into a block obtained from {@link #allocateMemory}, the results are
	 * undefined.
	 *
	 * @param address the start memory address of the value
	 * @return the value from the specified memory address
	 * @see #putByte(long, byte)
	 * @see #allocateMemory
	 */
	public byte getByte(long address) {
		return unsafe.getByte(address);
	}

	/**
	 * Stores a value into a given memory address. If the address is zero, or does
	 * not point into a block obtained from {@link #allocateMemory}, the results are
	 * undefined.
	 *
	 * @param address the start memory address to store the value
	 * @param value   the value to store into the memory
	 * @see #getByte(long)
	 */
	public void putByte(long address, byte value) {
		unsafe.putByte(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public short getShort(long address) {
		return unsafe.getShort(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putShort(long address, short value) {
		unsafe.putShort(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public char getChar(long address) {
		return unsafe.getChar(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putChar(long address, char value) {
		unsafe.putChar(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public int getInt(long address) {
		return unsafe.getInt(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putInt(long address, int value) {
		unsafe.putInt(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public long getLong(long address) {
		return unsafe.getLong(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putLong(long address, long value) {
		unsafe.putLong(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public float getFloat(long address) {
		return unsafe.getFloat(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putFloat(long address, float value) {
		unsafe.putFloat(address, value);
	}

	/**
	 * @see #getByte(long)
	 */
	public double getDouble(long address) {
		return unsafe.getDouble(address);
	}

	/**
	 * @see #putByte(long, byte)
	 */
	public void putDouble(long address, double value) {
		unsafe.putDouble(address, value);
	}

	/**
	 * Fetches a native pointer from a given memory address. If the address is zero,
	 * or does not point into a block obtained from {@link #allocateMemory}, the
	 * results are undefined.
	 *
	 * <p>
	 * If the native pointer is less than 64 bits wide, it is extended as an
	 * unsigned number to a Java long. The pointer may be indexed by any given byte
	 * offset, simply by adding that offset (as a simple integer) to the long
	 * representing the pointer. The number of bytes actually read from the target
	 * address maybe determined by consulting {@link #addressSize}.
	 * 
	 * @param address memory address to fetch
	 * @return native pointer from the given memory address
	 *
	 * @see #allocateMemory
	 */
	public long getAddress(long address) {
		return unsafe.getAddress(address);
	}

	/**
	 * Stores a native pointer into a given memory address. If the address is zero,
	 * or does not point into a block obtained from {@link #allocateMemory}, the
	 * results are undefined.
	 *
	 * <p>
	 * The number of bytes actually written at the target address maybe determined
	 * by consulting {@link #addressSize}.
	 *
	 * @param address memory address to store
	 * @param value   native pointer to store
	 *
	 * @see #getAddress(long)
	 */
	public void putAddress(long address, long value) {
		unsafe.putAddress(address, value);
	}

	/*
	 * wrappers for malloc, realloc, free operations directly in C
	 */

	/**
	 * Allocates a new block of native memory, of the given size in bytes. The
	 * contents of the memory are uninitialized; they will generally be garbage. The
	 * resulting native pointer will never be zero, and will be aligned for all
	 * value types. Dispose of this memory by calling {@link #freeMemory}, or resize
	 * it with {@link #reallocateMemory}.
	 *
	 * @throws IllegalArgumentException if the size is negative or too large for the
	 *                                  native size_t type
	 *
	 * @throws OutOfMemoryError         if the allocation is refused by the system
	 *
	 * @see #getByte(long)
	 * @see #putByte(long, byte)
	 */
	public long allocateMemory(long bytes) {
		return unsafe.allocateMemory(bytes);
	}

	/**
	 * Resizes a new block of native memory, to the given size in bytes. The
	 * contents of the new block past the size of the old block are uninitialized;
	 * they will generally be garbage. The resulting native pointer will be zero if
	 * and only if the requested size is zero. The resulting native pointer will be
	 * aligned for all value types. Dispose of this memory by calling
	 * {@link #freeMemory}, or resize it with {@link #reallocateMemory}. The address
	 * passed to this method may be null, in which case an allocation will be
	 * performed.
	 *
	 * @throws IllegalArgumentException if the size is negative or too large for the
	 *                                  native size_t type
	 *
	 * @throws OutOfMemoryError         if the allocation is refused by the system
	 *
	 * @see #allocateMemory
	 */
	public long reallocateMemory(long address, long bytes) {
		return unsafe.reallocateMemory(address, bytes);
	}

	/**
	 * Sets all bytes in a given block of memory to a fixed value (usually zero).
	 * This provides a <em>single-register</em> addressing mode, as discussed in
	 * {@link #getInt(Object,long)}.
	 *
	 * <p>
	 * Equivalent to <code>setMemory(null, address, bytes, value)</code>.
	 */
	public void setMemory(long address, long bytes, byte value) {
		unsafe.setMemory(null, address, bytes, value);
	}

	/**
	 * Sets all bytes in a given block of memory to a fixed value (usually zero).
	 *
	 * <p>
	 * This method determines a block's base address by means of two parameters, and
	 * so it provides (in effect) a <em>double-register</em> addressing mode, as
	 * discussed in {@link #getInt(Object,long)}. When the object reference is null,
	 * the offset supplies an absolute base address.
	 *
	 * <p>
	 * The stores are in coherent (atomic) units of a size determined by the address
	 * and length parameters. If the effective address and length are all even
	 * modulo 8, the stores take place in 'long' units. If the effective address and
	 * length are (resp.) even modulo 4 or 2, the stores take place in units of
	 * 'int' or 'short'.
	 */
	public void setMemory(Object obj, long offset, long bytes, byte value) {
		unsafe.setMemory(obj, offset, bytes, value);
	}

	/**
	 * Sets all bytes in a given block of memory to a copy of another block. This
	 * provides a <em>single-register</em> addressing mode, as discussed in
	 * {@link #getInt(Object,long)}.
	 *
	 * Equivalent to
	 * <code>copyMemory(null, srcAddress, null, destAddress, bytes)</code>.
	 */
	public void copyMemory(long srcAddress, long destAddress, long bytes) {
		unsafe.copyMemory(null, srcAddress, null, destAddress, bytes);
	}

	/**
	 * Sets all bytes in a given block of memory to a copy of another block.
	 *
	 * <p>
	 * This method determines each block's base address by means of two parameters,
	 * and so it provides (in effect) a <em>double-register</em> addressing mode, as
	 * discussed in {@link #getInt(Object,long)}. When the object reference is null,
	 * the offset supplies an absolute base address.
	 *
	 * <p>
	 * The transfers are in coherent (atomic) units of a size determined by the
	 * address and length parameters. If the effective addresses and length are all
	 * even modulo 8, the transfer takes place in 'long' units. If the effective
	 * addresses and length are (resp.) even modulo 4 or 2, the transfer takes place
	 * in units of 'int' or 'short'.
	 */
	public void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
		unsafe.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
	}

	/**
	 * Disposes of a block of native memory, as obtained from
	 * {@link #allocateMemory} or {@link #reallocateMemory}. The address passed to
	 * this method may be null, in which case no action is taken.
	 *
	 * @see #allocateMemory
	 */
	public void freeMemory(long address) {
		unsafe.freeMemory(address);
	}

	/*
	 * random queries
	 */

	/**
	 * Report the size in bytes of a native pointer, as stored via
	 * {@link #putAddress}. This value will be either 4 or 8. Note that the sizes of
	 * other primitive types (as stored in native memory blocks) is determined fully
	 * by their information content.
	 * 
	 * @return the address size for the native pointer
	 */
	public int addressSize() {
		return unsafe.addressSize();
	}

	/**
	 * Report the size in bytes of a native memory page (whatever that is). This
	 * value will always be a power of two.
	 * 
	 * @return the page size for the native memory page
	 */
	public int pageSize() {
		return unsafe.pageSize();
	}

	/**
	 * Report the location of a given field in the storage allocation of its class.
	 * Do not expect to perform any sort of arithmetic on this offset; it is just a
	 * cookie which is passed to the unsafe heap memory accessors.
	 *
	 * <p>
	 * Any given field will always have the same offset and base, and no two
	 * distinct fields of the same class will ever have the same offset and base.
	 *
	 * <p>
	 * As of 1.4.1, offsets for fields are represented as long values, although the
	 * Sun JVM does not use the most significant 32 bits. However, JVM
	 * implementations which store static fields at absolute addresses can use long
	 * offsets and null base pointers to express the field locations in a form
	 * usable by {@link #getInt(Object,long)}. Therefore, code which will be ported
	 * to such JVMs on 64-bit platforms must preserve all bits of static field
	 * offsets.
	 * 
	 * @param field the field whose offset should be returned
	 * @return the offset of the given field
	 * @see #getInt(Object, long)
	 */
	public long staticFieldOffset(Field field) {
		return unsafe.staticFieldOffset(field);
	}

	/**
	 * Report the location of a given static field, in conjunction with
	 * {@link #staticFieldBase}.
	 * <p>
	 * Do not expect to perform any sort of arithmetic on this offset; it is just a
	 * cookie which is passed to the unsafe heap memory accessors.
	 *
	 * <p>
	 * Any given field will always have the same offset, and no two distinct fields
	 * of the same class will ever have the same offset.
	 *
	 * <p>
	 * As of 1.4.1, offsets for fields are represented as long values, although the
	 * Sun JVM does not use the most significant 32 bits. It is hard to imagine a
	 * JVM technology which needs more than a few bits to encode an offset within a
	 * non-array object, However, for consistency with other methods in this class,
	 * this method reports its result as a long value.
	 *
	 * @param field the field whose offset should be returned
	 * @return the offset of the given field
	 * @see #getInt(Object, long)
	 */
	public long objectFieldOffset(Field field) {
		return unsafe.objectFieldOffset(field);
	}

	/**
	 * Report the location of a given static field, in conjunction with
	 * {@link #staticFieldOffset}.
	 * <p>
	 * Fetch the base "Object", if any, with which static fields of the given class
	 * can be accessed via methods like {@link #getInt(Object, long)}. This value
	 * may be null. This value may refer to an object which is a "cookie", not
	 * guaranteed to be a real Object, and it should not be used in any way except
	 * as argument to the get and put routines in this class.
	 */
	public Object staticFieldBase(Field field) {
		return unsafe.staticFieldBase(field);
	}

	/**
	 * Ensure the given class has been initialized. This is often needed in
	 * conjunction with obtaining the static field base of a class.
	 */
	public void ensureClassInitialized(Class<?> clazz) {
		unsafe.ensureClassInitialized(clazz);
	}

	/**
	 * Detect if the given class may need to be initialized. This is often needed in
	 * conjunction with obtaining the static field base of a class.
	 * 
	 * @return false only if a call to {@code ensureClassInitialized} would have no
	 *         effect
	 */
	public boolean shouldBeInitialized(Class<?> clazz) {
		return unsafe.shouldBeInitialized(clazz);
	}

	/**
	 * Report the offset of the first element in the storage allocation of a given
	 * array class. If {@link #arrayIndexScale} returns a non-zero value for the
	 * same class, you may use that scale factor, together with this base offset, to
	 * form new offsets to access elements of arrays of the given class.
	 * 
	 * @param arrayClass the class for which the first element's address should be
	 *                   obtained.
	 * @return the offset of the first element of the array class.
	 * @see #arrayIndexScale(Class)
	 * @see #getInt(Object, long)
	 * @see #putInt(Object, long, int)
	 */
	public int arrayBaseOffset(Class<?> arrayClass) {
		return unsafe.arrayBaseOffset(arrayClass);
	}

	/**
	 * Report the scale factor for addressing elements in the storage allocation of
	 * a given array class. However, arrays of "narrow" types will generally not
	 * work properly with accessors like {@link #getByte(Object, int)}, so the scale
	 * factor for such classes is reported as zero.
	 * 
	 * @param arrayClass the class whose scale factor should be returned.
	 * @return the scale factor, or zero if not supported for this array class.
	 * @see #arrayBaseOffset(Class)
	 * @see #getInt(Object, long)
	 * @see #putInt(Object, long, int)
	 */
	public int arrayIndexScale(Class<?> arrayClass) {
		return unsafe.arrayIndexScale(arrayClass);
	}

	/*
	 * random trusted operations from JNI
	 */

	/**
	 * Tell the VM to define a class, without security checks. By default, the class
	 * loader and protection domain come from the caller's class.
	 */
	public Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader,
			java.security.ProtectionDomain protectionDomain) {
		return unsafe.defineClass(name, b, off, len, loader, protectionDomain);
	}

	/**
	 * Define a class but do not make it known to the class loader or system
	 * dictionary.
	 * <p>
	 * For each CP entry, the corresponding CP patch must either be null or have the
	 * a format that matches its tag:
	 * <ul>
	 * <li>Integer, Long, Float, Double: the corresponding wrapper object type from
	 * java.lang
	 * <li>Utf8: a string (must have suitable syntax if used as signature or name)
	 * <li>Class: any java.lang.Class object
	 * <li>String: any object (not just a java.lang.String)
	 * <li>InterfaceMethodRef: (NYI) a method handle to invoke on that call site's
	 * arguments
	 * </ul>
	 * 
	 * @param hostClass context for linkage, access control, protection domain, and
	 *                  class loader
	 * @param data      bytes of a class file
	 * @param cpPatches where non-null entries exist, they replace corresponding CP
	 *                  entries in data
	 */
	public Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
		return unsafe.defineAnonymousClass(hostClass, data, cpPatches);
	}

	/**
	 * Allocate an instance but do not run any constructor. Initializes the class if
	 * it has not yet been.
	 */
	@SuppressWarnings("unchecked")
	public <T> T allocateInstance(Class<? extends T> clazz) throws InstantiationException {
		return (T) unsafe.allocateInstance(clazz);
	}

	/*
	 * other necessary methods
	 */

	/**
	 * Block current thread, returning when a balancing <tt>unpark</tt> occurs, or a
	 * balancing <tt>unpark</tt> has already occurred, or the thread is interrupted,
	 * or, if time is not zero, the given time nanoseconds have elapsed, or
	 * spuriously (i.e., returning for no "reason"). Note: This operation is in the
	 * Unsafe class only because <tt>unpark</tt> is, so it would be strange to place
	 * it elsewhere.
	 * 
	 * @param nanoSeconds the number of nanoseconds to wait
	 */
	public void park(long nanoSeconds) {
		unsafe.park(false, nanoSeconds);
	}

	/**
	 * Block current thread, returning when a balancing <tt>unpark</tt> occurs, or a
	 * balancing <tt>unpark</tt> has already occurred, or the thread is interrupted,
	 * or, the given deadline in milliseconds since Epoch has passed, or spuriously
	 * (i.e., returning for no "reason"). Note: This operation is in the Unsafe
	 * class only because <tt>unpark</tt> is, so it would be strange to place it
	 * elsewhere.
	 * 
	 * @param epochMillis a time in milliseconds from the epoch to wait for
	 */
	public void parkUntil(long epochMillis) {
		unsafe.park(true, epochMillis);
	}

	/**
	 * Unblock the given thread blocked on <tt>park</tt> or <tt>parkUntil</tt>, or,
	 * if it is not blocked, cause the subsequent call to <tt>park</tt> not to
	 * block. Note: this operation is "unsafe" solely because the caller must
	 * somehow ensure that the thread has not been destroyed. Nothing special is
	 * usually required to ensure this when called from Java (in which there will
	 * ordinarily be a live reference to the thread) but this is not
	 * nearly-automatically so when calling from native code.
	 * 
	 * @param thread the thread to unpark
	 */
	public void unpark(Object thread) {
		unsafe.unpark(thread);
	}

	/**
	 * Ensures lack of reordering of loads before the fence with loads or stores
	 * after the fence.
	 */
	public void loadFence() {
		unsafe.loadFence();
	}

	/**
	 * Ensures lack of reordering of stores before the fence with loads or stores
	 * after the fence.
	 */
	public void storeFence() {
		unsafe.storeFence();
	}

	/**
	 * Ensures lack of reordering of loads or stores before the fence with loads or
	 * stores after the fence.
	 */
	public void fullFence() {
		unsafe.fullFence();
	}

	/**
	 * Gets the load average in the system run queue assigned to the available
	 * processors averaged over various periods of time. This method retrieves the
	 * given <tt>nelem</tt> samples and assigns to the elements of the given
	 * <tt>loadavg</tt> array. The system imposes a maximum of 3 samples,
	 * representing averages over the last 1, 5, and 15 minutes, respectively.
	 *
	 * @param loadavg an array of double of size nelems
	 * @param nelems  the number of samples to be retrieved and must be 1 to 3
	 *
	 * @return the number of samples actually retrieved; or -1 if the load average
	 *         is unobtainable
	 */
	public int getLoadAverage(double[] loadavg, int nelems) {
		return unsafe.getLoadAverage(loadavg, nelems);
	}

}
