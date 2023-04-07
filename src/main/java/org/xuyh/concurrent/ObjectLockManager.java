/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An {@link ObjectLockManager} is the manager to lock in one key equal or
 * duplicated keys equal in values but needn't <code>key1==key2</code> is
 * <code>true</code>. Different with {@link ObjectLock#lock(Object)} or
 * {@link ObjectLock#lockx(Object...)} where use locks in a global scope, you
 * can use this manager to do lock in no effects on each other.
 *
 * @author XuYanhang
 * @see ObjectLock
 * @since 2020-10-30
 */
public final class ObjectLockManager {
    /**
     * The manager in global scope
     */
    static final ObjectLockManager GLOBAL_MANAGER = new ObjectLockManager();

    /**
     * The storage on all locks. The storage is safe in concurrent operation and
     * provides some atomic operations.
     */
    private final ConcurrentHashMap<Object, ObjectLockImp> locks;

    /**
     * Create an instance
     */
    public ObjectLockManager() {
        super();
        locks = new ConcurrentHashMap<>();
    }

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
     * @param lockKey the key of the lock who maybe be in plural values
     * @return the {@link ObjectLock lock} from the key
     * @see #lock(Object)
     */
    public ObjectLock lockx(Object... lockKey) {
        if (null == lockKey || lockKey.length == 0)
            return lock(ObjectLockKey.EMPTY_KEY);
        if (lockKey.length == 1)
            return lock(lockKey[0]);
        return lock(new ObjectLockKey(lockKey));
    }

    /**
     * Lock on an object and get the lock. The result lock is an unfair lock. The
     * object is expected to be unchangeable on the results of
     * {@link #equals(Object)} and {@link #hashCode()} ( for example, {@link String}
     * ).
     *
     * @param lockKey the key of the lock
     * @return the {@link ObjectLock lock} from the key
     */
    public ObjectLock lock(Object lockKey) {
        if (null == lockKey)
            lockKey = ObjectLockKey.NULL_KEY;
        ObjectLockImp lock;
        while (true) {
            // Get a lock or create one if it doesn't exist
            while ((lock = locks.get(lockKey)) == null)
                locks.putIfAbsent(lockKey, new ObjectLockImp(lockKey));
            // When the lock is on a locked thread
            if (lock.lock.isHeldByCurrentThread()) {
                lock.holdCountCurrentThread++;
                return lock;
            }
            // When the lock is on a new thread
            lock.holdThreadsCount.getAndIncrement();
            if (!lock.tryDestroying)
                break;
            // The lock is trying to do destroy in another thread
            lock.holdThreadsCount.getAndDecrement();
        }
        // Try lock this thread and wait until the lock get
        lock.lock.lock();
        // Thread locked
        lock.holdCountCurrentThread = 1;
        return lock;
    }

    /**
     * A standard implements on {@link ObjectLock}
     *
     * @author XuYanhang
     */
    private final class ObjectLockImp implements ObjectLock {
        /*
         * Lock fields for lock bean
         */
        /**
         * The key of the lock. It might be a single any {@link Object} but never
         * <code>null</null> from {@link #lock(Object)}, or just an instance of
         * {@link ObjectLockKey} including all objects parameters from
         * {@link #lockx(Object[])}.
         */
        private final Object lockKey;

        /**
         * The reentrant lock on this lock. It provides the block action.
         */
        private final ReentrantLock lock;

        /**
         * A statistics value on threads count this lock is holding. It is concurrent
         * safe and provides some atomic operations.
         */
        private final AtomicInteger holdThreadsCount;

        /**
         * A statistics value on locking count this lock is holding in a locked thread.
         * The value changed only in the locking block when only one single thread can
         * visit it at same time.
         */
        private volatile int holdCountCurrentThread;

        /**
         * A flag on this lock if it is destroying, that's if it is removing from the
         * storage of {@link #locks}. It is concurrent safe and provides some atomic
         * operations.
         */
        private volatile boolean tryDestroying;

        /**
         * Initialize method but only private.
         *
         * @param lockKey the key of the lock.
         */
        private ObjectLockImp(Object lockKey) {
            super();
            this.lockKey = lockKey;
            this.lock = new ReentrantLock();
            this.holdThreadsCount = new AtomicInteger(0);
            this.holdCountCurrentThread = 0;
            this.tryDestroying = false;
        }

        /**
         * Release the lock. The unlock operation must be one-to-one for each lock
         * operation, and the current unlock thread must be the locking thread or an
         * {@link IllegalMonitorStateException} thrown.
         *
         * @throws IllegalMonitorStateException if the current thread does not hold this
         *                                      lock
         */
        @Override
        public void unlock() {
            // An unlocked thread try release the lock
            if (!lock.isHeldByCurrentThread())
                throw new IllegalMonitorStateException(Thread.currentThread().getName());
            // The thread locked more than once and need locked still
            if (--holdCountCurrentThread > 0)
                return;
            // Release the lock
            lock.unlock();
            if (holdThreadsCount.decrementAndGet() == 0) {
                tryDestroying = true;
                // When another thread is trying to grab the lock
                if (holdThreadsCount.get() > 0) {
                    tryDestroying = false;
                } else {
                    locks.remove(lockKey);
                }
            }
        }

    }

    /**
     * The plural object values as a lock key.
     *
     * @author XuYanhang
     * @since 2020-08-15
     */
    private static class ObjectLockKey implements java.io.Serializable {
        /**
         * Key of empty
         */
        static final ObjectLockKey EMPTY_KEY = new ObjectLockKey(new Object[0]);

        /**
         * Key of one value in <code>null</code>
         */
        static final ObjectLockKey NULL_KEY = new ObjectLockKey(new Object[]{null});

        /**
         * Real key values
         */
        private final Object[] objs;

        // Cached hash value
        private int hash = 0;

        /**
         * Constructor to create a lock key for plural objects.
         *
         * @param objs keys
         */
        ObjectLockKey(Object[] objs) {
            super();
            if (null == objs) throw new NullPointerException();
            this.objs = objs;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            if (hash != 0) return hash;
            int result = 1;
            for (Object o : this.objs) {
                result = 31 * result + (o == null ? 0 : o.hashCode());
            }
            hash = result;
            return result;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            ObjectLockKey other = (ObjectLockKey) obj;
            if (this.objs.length != other.objs.length)
                return false;
            for (int i = 0, l = this.objs.length; i < l; i++) {
                Object o1 = this.objs[i];
                Object o2 = other.objs[i];
                if (o1 == null ? o2 != null : !o1.equals(o2))
                    return false;
            }
            return true;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            if (objs.length == 0) return "[]";
            int last = objs.length - 1;
            StringBuilder sb = new StringBuilder().append('[');
            for (int cursor = 0; cursor < last; cursor++) {
                sb.append(objs[cursor]).append(',').append(' ');
            }
            return sb.append(objs[last]).append(']').toString();
        }

        private static final long serialVersionUID = 4435545659181658180L;
    }
}
