/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Tools to do with {@link Runnable}s.
 * 
 * @author XuYanhang
 * @since 2020-10-30
 * @see Runnable
 * @see Callable
 *
 */
public final class Runnables {

	/**
	 * Scheduler handle in this class
	 */
	private static final AutoPoolScheduler HANDLE_SCHEDULER = new AutoPoolScheduler(1);

	/**
	 * Execute a {@link Runnable} in current thread
	 * 
	 * @param task the {@link Runnable} to run
	 * @see Runnable#run()
	 */
	public static void execute(final Runnable task) {
		task.run();
	}

	/**
	 * Execute a {@link Callable} in current thread
	 * 
	 * @param      <V> the result type of method {@code Callable#call}
	 * 
	 * @param task the {@link Callable} to run
	 * @return the execute result
	 * @throws Exception - if unable to compute a result
	 * @see Callable#call()
	 */
	public static <V> V execute(final Callable<V> task) throws Exception {
		return task.call();
	}

	/**
	 * Execute an function in asynchronous way. This function can be cancel before
	 * it runs and can also wait the task finishes.
	 * 
	 * @param task the {@link Runnable} to run
	 * @return a Future representing pending completion of the task
	 * @throws NullPointerException if the task is null
	 */
	public static Future<Void> executeAsync(final Runnable task) {
		return HANDLE_SCHEDULER.submit(task);
	}

	/**
	 * Execute an function in asynchronous way. This function can be cancel before
	 * it runs and can also wait the task finishes.
	 * 
	 * @param task the {@link Callable} to run
	 * @param      <T> the type of the task's result
	 * @return a Future representing pending completion of the task
	 * @throws NullPointerException if the task is null
	 */
	public static <V> Future<V> executeAsync(final Callable<V> task) {
		return HANDLE_SCHEDULER.submit(task);
	}

	/**
	 * Execute an function in asynchronous way after specified delay milliseconds.
	 * This function can be cancel before it runs and can also wait the task
	 * finishes.
	 * 
	 * @param task        the {@link Runnable} to run
	 * @param delayMillis the milliseconds delay to run this function
	 * @return a Future representing pending completion of the task and whose
	 *         {@code get()} method will return {@code null} upon completion
	 * @throws NullPointerException if command is null
	 */
	public static Future<Void> executeAsync(final Runnable task, long delayMillis) {
		return HANDLE_SCHEDULER.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Execute an function in asynchronous way after specified delay milliseconds.
	 * This function can be cancel before it runs and can also wait the task
	 * finishes.
	 * 
	 * @param task        the {@link Callable} to run
	 * @param delayMillis the milliseconds delay to run this function
	 * @param             <V> the type of the callable's result
	 * @return a Future that can be used to extract result or cancel
	 * @throws NullPointerException if task is null
	 */
	public static <V> Future<V> executeAsync(final Callable<V> task, long delayMillis) {
		return HANDLE_SCHEDULER.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given period; that is
	 * executions will commence after {@code initialDelayMillis} then
	 * {@code initialDelayMillis+periodMillis}, then
	 * {@code initialDelayMillis + 2 * periodMillis}, and so on. If any execution of
	 * the task encounters an exception, subsequent executions are continued when
	 * the exception is silent. The task will only terminate via cancellation. If
	 * any execution of this task takes longer than its period, then subsequent
	 * executions may start late, but will not concurrently execute.
	 *
	 * @param task               the task to execute
	 * @param initialDelayMillis the time in milliseconds to delay first execution
	 * @param periodMillis       the period milliseconds between successive
	 *                           executions
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command is null
	 * @throws IllegalArgumentException if period less than or equal to zero
	 */
	public static Cancellable scheduleFixedRate(Runnable task, long initialDelayMillis, long periodMillis) {
		return HANDLE_SCHEDULER.scheduleAtFixedRate(task, initialDelayMillis, periodMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given delay between the
	 * termination of one execution and the commencement of the next. If any
	 * execution of the task encounters an exception, subsequent executions are
	 * continued when the exception is silent. The task will only terminate via
	 * cancellation.
	 *
	 * @param task               the task to execute
	 * @param initialDelayMillis the time in milliseconds to delay first execution
	 * @param delayMillis        the delay milliseconds between the termination of
	 *                           one execution and the commencement of the next
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command is null
	 * @throws IllegalArgumentException if delay less than or equal to zero
	 */
	public static Cancellable scheduleFixedDelay(Runnable task, long initialDelayMillis, long delayMillis) {
		return HANDLE_SCHEDULER.scheduleWithFixedDelay(task, initialDelayMillis, delayMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Combine two {@link Runnable} functions as one when they will do
	 * {@link Runnable#run()} one bye one. Any exception from first run with stop
	 * all behind.
	 * 
	 * @param run1 first {@link Runnable}
	 * @param run2 second {@link Runnable}
	 * @return {@link Runnable} to run these two {@link Runnable} functions
	 */
	public static Runnable combine(Runnable run1, Runnable run2) {
		if (null == run1 || null == run2)
			throw new NullPointerException();
		Runnable[] runs1 = null;
		Runnable[] runs2 = null;
		int size = 0;
		if (run1 instanceof DuplicatedRunnable) {
			runs1 = ((DuplicatedRunnable) run1).runs;
			size += runs1.length;
		} else {
			size++;
		}
		if (run2 instanceof DuplicatedRunnable) {
			runs2 = ((DuplicatedRunnable) run2).runs;
			size += runs2.length;
		} else {
			size++;
		}
		Runnable[] runs = new Runnable[size];
		int cursor = 0;
		if (null == runs1) {
			runs[cursor++] = run1;
		} else {
			System.arraycopy(runs1, 0, runs, cursor, runs1.length);
			cursor += runs1.length;
		}
		if (null == runs2) {
			runs[cursor++] = run2;
		} else {
			System.arraycopy(runs2, 0, runs, cursor, runs2.length);
			cursor += runs2.length;
		}
		return new DuplicatedRunnable(runs);
	}

	/**
	 * Runnable in duplicated way to run batch functions.
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class DuplicatedRunnable implements Runnable {

		/**
		 * Batch Runnable
		 */
		final Runnable[] runs;

		/**
		 * Initialize this class in duplicated Runnables
		 */
		DuplicatedRunnable(Runnable[] runs) {
			super();
			this.runs = runs;
		}

		/**
		 * @see Runnable#run()
		 */
		@Override
		public void run() {
			for (int i = 0, l = runs.length; i < l; i++)
				if (null != runs[i])
					runs[i].run();
		}

	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Runnables() {
		super();
	}

}
