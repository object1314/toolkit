/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link ThreadFactory threadFactory} to generate threads with custom thread
 * name prefix.
 * 
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public class NamedThreadFactory implements ThreadFactory {

	/**
	 * Thread name prefix in a fix string
	 */
	private final String threadNamePrefix;

	/**
	 * Thread name suffix in an ordered number
	 */
	private final AtomicInteger threadNo;

	/**
	 * Thread group to generate threads
	 */
	private final ThreadGroup threadGroup;

	/**
	 * Create factory with the name prefix.
	 * 
	 * @param threadNamePrefix the created threads' name prefix
	 */
	public NamedThreadFactory(String threadNamePrefix) {
		super();
		if (null == threadNamePrefix)
			throw new NullPointerException("threadNamePrefix");
		this.threadNamePrefix = threadNamePrefix;
		this.threadNo = new AtomicInteger(1);
		SecurityManager s = System.getSecurityManager();
		this.threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		if (null == threadGroup)
			throw new NullPointerException("threadGroup");
	}

	/**
	 * Create factory with the name prefix and the thread group.
	 * 
	 * @param threadNamePrefix the created threads' name prefix
	 * @param threadGroup      the belonged thread factory
	 */
	public NamedThreadFactory(String threadNamePrefix, ThreadGroup threadGroup) {
		super();
		if (null == threadNamePrefix)
			throw new NullPointerException("threadNamePrefix");
		this.threadNamePrefix = threadNamePrefix;
		this.threadNo = new AtomicInteger(1);
		if (null == threadGroup)
			throw new NullPointerException("threadGroup");
		this.threadGroup = threadGroup;
	}

	/**
	 * @see ThreadFactory#newThread(Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(threadGroup, r, threadNamePrefix + "-" + threadNo.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}

}
