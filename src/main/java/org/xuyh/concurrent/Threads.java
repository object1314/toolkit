/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * Tools to do with {@link Thread}s.
 *
 * @author XuYanhang
 * @since 2020-11-02
 */
public final class Threads {
    /**
     * Replace the {@link Thread#sleep(long)} but not clear interrupted status and
     * no interrupted exceptions.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Replace the {@link Thread#join(long)} but not clear interrupted status and no
     * interrupted exceptions.
     */
    public static void join(Thread thread, long millis) {
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Replace the {@link Thread#join()} but not clear interrupted status and no
     * interrupted exceptions.
     */
    public static void join(Thread thread) {
        try {
            thread.join(0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the root thread group in the system
     *
     * @return The root thread group in the system
     */
    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = group.getParent()) != null) group = parent;
        return group;
    }

    /**
     * Creates a {@link ThreadFactory} used a given thread name prefix.
     *
     * @param threadNamePrefix the given thread name prefix
     * @return The new thread factory
     */
    public static ThreadFactory newThreadFactory(String threadNamePrefix) {
        return new NamedThreadFactory(threadNamePrefix);
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Threads() {
        super();
    }
}
