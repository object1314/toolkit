/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

/**
 * Sometimes, we need a lock but only for same object on
 * {@link Object#equals(Object) equals} as <code>true</code> instead of
 * equivalence relation on objects as <code>obj1==obj2</code> in some situation.
 * Here we provide a lock on these same objects. We can use it like this:
 * 
 * <pre>
 *     ObjectLock lock = ObjectLock.lock("myLock");
 *     try {
 *         <i>blocking coding here...</i>
 *     } finally {
 *         lock.unlock();
 *     }
 * </pre>
 * 
 * Or this:
 * 
 * <pre>
 *     ObjectLock lock = ObjectLock.lockx("myLockk1", "mylockk2");
 *     try {
 *         <i>blocking coding here...</i>
 *     } finally {
 *         lock.unlock();
 *     }
 * </pre>
 * 
 * Of course, the used like this is permitted, while these locks are same one:
 * 
 * <pre>
 *     ObjectLock lock1 = ObjectLock.lock("myLock");
 *     try {
 *         <i>blocking coding here...</i>
 *         ObjectLock lock2 = ObjectLock.lock("myLock");
 *         try {
 *             <i>blocking coding here...</i>
 *         } finally {
 *             lock2.unlock();
 *         }
 *         <i>blocking coding here...</i>
 *     } finally {
 *         lock1.unlock();
 *     }
 * </pre>
 * 
 * But never forget to unlock a lock after do lock it each time.
 * 
 * @author XuYanhang
 * @since 2020-08-15
 * @see ObjectLockManager
 *
 */
public interface ObjectLock {

	/**
	 * Lock on plural objects and get the lock. The result lock is an unfair lock.
	 * All objects are expected to be unchangeable on the results of
	 * {@link #equals(Object)} and {@link #hashCode()} ( for example, {@link String}
	 * ). The lock defined by and only by all of the object values and their order.
	 * Two input keys are same like <code>("myLockk1", "mylockk2")</code> and
	 * <code>(new Object[] { "myLockk1", "mylockk2" })</code>. But the usage of
	 * <code>((Object)(new Object[] { "myLockk1", "mylockk2" })) is considered as a
	 * key of single object, just as call {@link #lock(Object)} directly.
	 * 
	 * 
	 * @param lockKey the key of the lock who maybe be in plural values
	 * @return the {@link ObjectLock lock} from the key
	 * @see #lock(Object)
	 */
	public static ObjectLock lockx(Object... lockKey) {
		return ObjectLockManager.GLOBAL_MANAGER.lockx(lockKey);
	}

	/**
	 * Lock on an object and get the lock. The result lock is an unfair lock. The
	 * object is expected to be unchangeable on the results of
	 * {@link #equals(Object)} and {@link #hashCode()} ( for example, {@link String}
	 * ).
	 * 
	 * @param lockKey the key of the lock
	 * @return the {@link ObjectLockImp lock} from the key
	 */
	public static ObjectLock lock(Object lockKey) {
		return ObjectLockManager.GLOBAL_MANAGER.lock(lockKey);
	}

	/**
	 * Release the lock. The unlock operation must be one-to-one for each lock
	 * operation, and the current unlock thread must be the locking thread or an
	 * {@link IllegalMonitorStateException} thrown.
	 * 
	 * @throws IllegalMonitorStateException if the current thread does not hold this
	 *                                      lock
	 */
	public void unlock();

}
