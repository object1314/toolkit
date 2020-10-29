/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Tools to do with {@link Runnable}s.
 * 
 * @author XuYanhang
 * @since 2020-10-30
 *
 */
public final class Runnables {

	/**
	 * Execute a {@link Runnable}
	 * 
	 * @param run the {@link Runnable} to run
	 * @see Runnable#run()
	 */
	public static void execute(final Runnable run) {
		run.run();
	}

	/**
	 * Execute an function in asynchronous way. This function can be cancel before
	 * it has run.
	 * 
	 * @param run         the {@link Runnable} to run
	 * @param delayMillis the milliseconds delay to run this function
	 * @return a {@link Cancellable} who can cancel this task when it hasn't run
	 *         after this delay time
	 */
	public static Cancellable executeAsync(final Runnable run, long delayMillis) {
		if (null == run)
			throw new NullPointerException();
		if (delayMillis < 0)
			throw new IllegalArgumentException();
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					run.run();
				} finally {
					timer.cancel();
				}
			}
		}, delayMillis);
		return timer::cancel;
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
