/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is a scheduler to schedule tasks who can release thread pool resources
 * by self when there is no more tasks to run. If uses a loop task, don't forget
 * to cancel it when stop it, or this scheduler won't release by itself.
 * 
 * @author XuYanhang
 * @since 2020-11-02
 * @see ScheduledThreadPoolExecutor
 *
 */
public class AutoPoolScheduler {

	/**
	 * Handler on the exception to support silent
	 */
	private static final Consumer<Throwable> SILENT_EXCEPTION_HANDLER = (e) -> {
	};

	/**
	 * Factory scheduler factory to generate a current
	 * {@link ScheduledThreadPoolExecutor executor} at {@link #executor}
	 */
	private final Supplier<ScheduledThreadPoolExecutor> executorFactory;
	/**
	 * Lock on this scheduler to schedule tasks and check {@link #counter} changed
	 */
	private final Object lock = new Object();

	/**
	 * Current {@link ScheduledThreadPoolExecutor executor} to execute tasks who
	 * will be dynamically created or destroyed
	 */
	private ScheduledThreadPoolExecutor executor;
	/**
	 * Current handle tasks count and monitor on {@link #executor} status
	 */
	private int counter;

	/**
	 * Creates a new {@code AutoCloseScheduler} with the given core pool size for
	 * the thread pool in this scheduler.
	 *
	 * @param corePoolSize the number of threads to keep in the thread pool in this
	 *                     scheduler, even if they are idle
	 * @throws IllegalArgumentException if {@code corePoolSize < 0}
	 */
	public AutoPoolScheduler(int corePoolSize) {
		super();
		if (corePoolSize < 0)
			throw new IllegalArgumentException();
		executorFactory = () -> new ScheduledThreadPoolExecutor(corePoolSize);
	}

	/**
	 * Creates a new {@code AutoCloseScheduler} with the given initial parameters.
	 *
	 * @param corePoolSize  the number of threads to keep in the thread pool in this
	 *                      scheduler, even if they are idle
	 * @param threadFactory the factory to use when the thread pool in this
	 *                      scheduler creates a new thread
	 * @throws IllegalArgumentException if {@code corePoolSize < 0}
	 * @throws NullPointerException     if {@code threadFactory} is null
	 */
	public AutoPoolScheduler(int corePoolSize, ThreadFactory threadFactory) {
		super();
		if (corePoolSize < 0)
			throw new IllegalArgumentException();
		if (null == threadFactory)
			throw new NullPointerException();
		executorFactory = () -> new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
	}

	/**
	 * Submits a Runnable task for execution and returns a Future representing that
	 * task. The Future's {@code get} method will return {@code null} upon
	 * <em>successful</em> completion.
	 *
	 * @param task the task to submit
	 * @return a Future representing pending completion of the task
	 * @throws NullPointerException if the task is null
	 * @see ScheduledThreadPoolExecutor#submit(Runnable)
	 */
	public Future<Void> submit(Runnable task) {
		return schedule0(wrapperTask(task), 0, TimeUnit.MILLISECONDS);
	}

	/**
	 * Submits a value-returning task for execution and returns a Future
	 * representing the pending results of the task. The Future's {@code get} method
	 * will return the task's result upon successful completion.
	 *
	 * <p>
	 * If you would like to immediately block waiting for a task, you can use
	 * constructions of the form {@code result = exec.submit(aCallable).get();}
	 *
	 * @param task the task to submit
	 * @param      <T> the type of the task's result
	 * @return a Future representing pending completion of the task
	 * @throws NullPointerException if the task is null
	 * @see ScheduledThreadPoolExecutor#submit(Callable)
	 */
	public <V> Future<V> submit(Callable<V> task) {
		return schedule0(wrapperTask(task), 0, TimeUnit.MILLISECONDS);
	}

	/**
	 * Creates and executes a one-shot action that becomes enabled after the given
	 * delay.
	 *
	 * @param task     the task to execute
	 * @param delay    the time from now to delay execution
	 * @param timeunit the time unit of the delay parameter
	 * @return a Future representing pending completion of the task and whose
	 *         {@code get()} method will return {@code null} upon completion
	 * @throws NullPointerException if command is null
	 * @see ScheduledThreadPoolExecutor#schedule(Runnable, long, TimeUnit)
	 */
	public Future<Void> schedule(Runnable task, long delay, TimeUnit timeunit) {
		return schedule0(wrapperTask(task), delay, timeunit);
	}

	/**
	 * Creates and executes a Future that becomes enabled after the given delay.
	 *
	 * @param task     the function to execute
	 * @param delay    the time from now to delay execution
	 * @param timeunit the time unit of the delay parameter
	 * @param          <V> the type of the callable's result
	 * @return a Future that can be used to extract result or cancel
	 * @throws NullPointerException if task is null
	 * @see ScheduledThreadPoolExecutor#schedule(Callable, long, TimeUnit)
	 */
	public <V> Future<V> schedule(Callable<V> task, long delay, TimeUnit timeunit) {
		return schedule0(wrapperTask(task), delay, timeunit);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given period; that is
	 * executions will commence after {@code initialDelay} then
	 * {@code initialDelay+period}, then {@code initialDelay + 2 * period}, and so
	 * on. If any execution of the task encounters an exception, subsequent
	 * executions are continued when the exception is silent. The task will only
	 * terminate via cancellation. If any execution of this task takes longer than
	 * its period, then subsequent executions may start late, but will not
	 * concurrently execute.
	 *
	 * @param task         the task to execute
	 * @param initialDelay the time to delay first execution
	 * @param period       the period between successive executions
	 * @param timeunit     the time unit of the initialDelay and period parameters
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command or exceptionHandler is null
	 * @throws IllegalArgumentException if period less than or equal to zero
	 * @see ScheduledThreadPoolExecutor#scheduleAtFixedRate(Runnable, long, long,
	 *      TimeUnit)
	 */
	public Cancellable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit timeunit) {
		return scheduleAtFixedRate(task, initialDelay, period, timeunit, SILENT_EXCEPTION_HANDLER);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given delay between the
	 * termination of one execution and the commencement of the next. If any
	 * execution of the task encounters an exception, subsequent executions are
	 * continued when the exception is silent. The task will only terminate via
	 * cancellation.
	 *
	 * @param task         the task to execute
	 * @param initialDelay the time to delay first execution
	 * @param delay        the delay between the termination of one execution and
	 *                     the commencement of the next
	 * @param timeunit     the time unit of the initialDelay and delay parameters
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command or exceptionHandler is null
	 * @throws IllegalArgumentException if delay less than or equal to zero
	 * @see ScheduledThreadPoolExecutor#scheduleWithFixedDelay(Runnable, long, long,
	 *      TimeUnit)
	 */
	public Cancellable scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeunit) {
		return scheduleWithFixedDelay(task, initialDelay, delay, timeunit, SILENT_EXCEPTION_HANDLER);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given period; that is
	 * executions will commence after {@code initialDelay} then
	 * {@code initialDelay+period}, then {@code initialDelay + 2 * period}, and so
	 * on. If any execution of the task encounters an exception, subsequent
	 * executions are continued when the exception is handled and no exception
	 * thrown from the handler, or interrupted when the handler throws any exception
	 * or error. The task will only terminate via cancellation or exception. If any
	 * execution of this task takes longer than its period, then subsequent
	 * executions may start late, but will not concurrently execute.
	 *
	 * @param task             the task to execute
	 * @param initialDelay     the time to delay first execution
	 * @param period           the period between successive executions
	 * @param timeunit         the time unit of the initialDelay and period
	 *                         parameters
	 * @param exceptionHandler handler to handle exceptions from this task running
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command or exceptionHandler is null
	 * @throws IllegalArgumentException if period less than or equal to zero
	 * @see ScheduledThreadPoolExecutor#scheduleAtFixedRate(Runnable, long, long,
	 *      TimeUnit)
	 */
	public Cancellable scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit timeunit,
			Consumer<? super RuntimeException> exceptionHandler) {
		Runnable wrapperTask = wrapperPeriodTask(task, exceptionHandler);
		Future<?> future;
		synchronized (lock) {
			checkInitial();
			try {
				future = executor.scheduleAtFixedRate(wrapperTask, initialDelay, period, timeunit);
			} catch (Throwable e) {
				checkDestroy();
				throw e;
			}
			counter++;
		}
		return wrapperCancellable(future);
	}

	/**
	 * Creates and executes a periodic action that becomes enabled first after the
	 * given initial delay, and subsequently with the given delay between the
	 * termination of one execution and the commencement of the next. If any
	 * execution of the task encounters an exception, subsequent executions are
	 * continued when the exception is handled and no exception thrown from the
	 * handler, or interrupted when the handler throws any exception or error. The
	 * task will only terminate via cancellation or exception.
	 *
	 * @param task             the task to execute
	 * @param initialDelay     the time to delay first execution
	 * @param delay            the delay between the termination of one execution
	 *                         and the commencement of the next
	 * @param timeunit         the time unit of the initialDelay and delay
	 *                         parameters
	 * @param exceptionHandler handler to handle exceptions from this task running
	 * @return a Cancellable who can cancel this command execution
	 * @throws NullPointerException     if command or exceptionHandler is null
	 * @throws IllegalArgumentException if delay less than or equal to zero
	 * @see ScheduledThreadPoolExecutor#scheduleWithFixedDelay(Runnable, long, long,
	 *      TimeUnit)
	 */
	public Cancellable scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit timeunit,
			Consumer<? super RuntimeException> exceptionHandler) {
		Runnable wrapperTask = wrapperPeriodTask(task, exceptionHandler);
		Future<?> future;
		synchronized (lock) {
			checkInitial();
			try {
				future = executor.scheduleWithFixedDelay(wrapperTask, initialDelay, delay, timeunit);
			} catch (Throwable e) {
				checkDestroy();
				throw e;
			}
			counter++;
		}
		return wrapperCancellable(future);
	}

	/**
	 * Real schedules a task.
	 */
	private <V> Future<V> schedule0(Callable<V> task, long delay, TimeUnit timeunit) {
		Future<V> future;
		synchronized (lock) {
			checkInitial();
			try {
				future = executor.schedule(task, delay, timeunit);
			} catch (Throwable e) {
				checkDestroy();
				throw e;
			}
			counter++;
		}
		return wrapperFuture(future);
	}

	/**
	 * Before each task scheduled, sure there exists an available executor
	 */
	private void checkInitial() {
		if (null == executor) {
			executor = executorFactory.get();
			counter = 0;
		}
	}

	/**
	 * Checks if there is no task here so that shutdown current executor
	 */
	private void checkDestroy() {
		if (counter <= 0 && null != executor) {
			executor.shutdown();
			executor = null;
			counter = 0;
		}
	}

	/**
	 * Wrappers the {@link Runnable task} so gets a new one of {@link Callable}
	 */
	private Callable<Void> wrapperTask(final Runnable task) {
		if (null == task)
			throw new NullPointerException();
		return new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					task.run();
				} finally {
					synchronized (lock) {
						counter--;
						checkDestroy();
					}
				}
				return null;
			}

		};
	}

	/**
	 * Wrappers the {@link Callable task} so gets a new one of {@link Callable}
	 */
	private <V> Callable<V> wrapperTask(final Callable<V> task) {
		if (null == task)
			throw new NullPointerException();
		return new Callable<V>() {

			@Override
			public V call() throws Exception {
				V v;
				try {
					v = task.call();
				} finally {
					synchronized (lock) {
						counter--;
						checkDestroy();
					}
				}
				return v;
			}

		};
	}

	/**
	 * Wrappers the {@link Future future} so gets a new one of {@link Future}
	 */
	private <V> Future<V> wrapperFuture(final Future<V> future) {
		return new Future<V>() {

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				boolean cancel = future.cancel(mayInterruptIfRunning);
				if (cancel) {
					synchronized (lock) {
						counter--;
						checkDestroy();
					}
				}
				return cancel;
			}

			@Override
			public boolean isCancelled() {
				return future.isCancelled();
			}

			@Override
			public boolean isDone() {
				return future.isDone();
			}

			@Override
			public V get() throws InterruptedException, ExecutionException {
				return future.get();
			}

			@Override
			public V get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return future.get(timeout, unit);
			}

		};
	}

	/**
	 * Wrappers the {@link Runnable task} so gets a new one of {@link Runnable}
	 */
	private Runnable wrapperPeriodTask(final Runnable task, Consumer<? super RuntimeException> exceptionHandler) {
		if (null == task || null == exceptionHandler)
			throw new NullPointerException();
		return new Runnable() {
			@Override
			public void run() {
				RuntimeException error = null;
				try {
					task.run();
				} catch (RuntimeException e) {
					error = e;
				} catch (Throwable e) {
					synchronized (lock) {
						counter--;
						checkDestroy();
					}
					throw e;
				}
				if (null != error)
					try {
						exceptionHandler.accept(error);
					} catch (Throwable e) {
						synchronized (lock) {
							counter--;
							checkDestroy();
						}
						throw e;
					}
			}
		};
	}

	/**
	 * Wrappers the {@link Future future} so gets a new one of {@link Cancellable}
	 */
	private Cancellable wrapperCancellable(final Future<?> future) {
		return new Cancellable() {
			@Override
			public void cancel() {
				if (future.cancel(false)) {
					synchronized (lock) {
						counter--;
						checkDestroy();
					}
				}
			}
		};
	}

}
