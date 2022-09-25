/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.UnaryOperator;

/**
 * Toolkits on operation who is not supported but required for
 * {@link ConcurrentMap}.
 *
 * @author XuYanhang
 * @since 2022-09-26
 * @see ConcurrentMap
 */
public class ConcurrentMaps {
	/**
	 * Creates a new {@link ConcurrentMap}.
	 *
	 * @return an instance of {@link ConcurrentMap}.
	 */
	public static <K,V> ConcurrentMap<K, V> create() {
		return new ConcurrentHashMap<>();
	}

	/**
	 * Compare-And-Set(CAS) for a {@link ConcurrentMap}. In a map, both key and value shouldn't be
	 * <code>null</code> when a <code>null</code> value means not exist. So in this CAS operation, a
	 * <code>null</code> old value means we'll try to add a new value when the old value not set (key
	 * not exists), and a <code>null</code> new value means a remove operation. The new value will be
	 * set success only when the old value is compared same as expected.
	 *
	 * @param map an instance of {@link ConcurrentMap}, nonnull
	 * @param key operation-key, nonnull
	 * @param oldVal old value, nullable
	 * @param newVal new value, nullable
	 * @return <code>true</code> when set success or <code>false</code> when fail
	 */
	public static <K, V> boolean compareAndSet(ConcurrentMap<K, V> map, K key, V oldVal, V newVal) {
		// Special status especially when both new value and old one are null
		if (oldVal == newVal) {
			return Objects.equals(map.get(key), oldVal);
		}
		// Old value is null but new one not, then create new
		if (oldVal == null) {
			return map.putIfAbsent(key, newVal) == null;
		}
		// New value is null but old one not, then remove old
		if (newVal == null) {
			return map.remove(key, oldVal);
		}
		// Both new value and old one not null, then set to new on old
		return map.replace(key, oldVal, newVal);
	}

	/**
	 * Swaps the value for a key in a {@link ConcurrentMap} and returns the old value.
	 *
	 * @param map an instance of {@link ConcurrentMap}, nonnull
	 * @param key key operation-key, nonnull
	 * @param swapper operator to switch old value to new value
	 * @return old value after switch
	 */
	public static <K, V> V getAndSwap(ConcurrentMap<K, V> map, K key, UnaryOperator<V> swapper) {
		do {
			V oldVal = map.get(key);
			V newVal = swapper.apply(oldVal);
			if (compareAndSet(map, key, oldVal, newVal)) {
				return oldVal;
			}
		} while (true);
	}

	/**
	 * Swaps the value for a key in a {@link ConcurrentMap} and returns the new value.
	 *
	 * @param map an instance of {@link ConcurrentMap}, nonnull
	 * @param key key operation-key, nonnull
	 * @param swapper operator to switch old value to new value
	 * @return new value after switch
	 */
	public static <K, V> V swapAndGet(ConcurrentMap<K, V> map, K key, UnaryOperator<V> swapper) {
		do {
			V oldVal = map.get(key);
			V newVal = swapper.apply(oldVal);
			if (compareAndSet(map, key, oldVal, newVal)) {
				return newVal;
			}
		} while (true);
	}
}
