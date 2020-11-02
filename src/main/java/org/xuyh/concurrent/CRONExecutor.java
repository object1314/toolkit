/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

import java.util.LinkedList;
import java.util.UUID;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;

/**
 * Executor to execute task in CRON expression trigger. It's necessary to hold
 * this executor and {@link #shutdown()} it when it will be never used again.
 * Here is a demo on this executor.
 *
 * <pre>
 *     CRONExecutor executor = new CRONExecutor();
 *     try {
 *         Cancellable cancellable = executor.execute("0/10 * * * * ? *", () -> System.out.println("Hello World!"));
 *         Thread.sleep(30_000);
 *         cancellable.cancel();
 *         <i>blocking coding here...</i>
 *     } finally {
 *         executor.shutdown();
 *     }
 * </pre>
 * 
 * @author XuYanhang
 * @since 2020-10-30
 *
 */
public final class CRONExecutor {

	/**
	 * Scheduler on this executor
	 */
	private final Scheduler scheduler;

	/**
	 * Locker to lock different expressions
	 */
	private final ObjectLockManager lockManager;

	/**
	 * Create an instance of {@link CRONExecutor}. Remember to shutdown it when
	 * never use it again
	 * 
	 * @throws IllegalStateException on any exception happens
	 */
	public CRONExecutor() {
		super();
		// Runnable the executor name
		String id = UUID.randomUUID().toString().replace("-", "");
		// Create factory
		DirectSchedulerFactory factory = DirectSchedulerFactory.getInstance();
		// Create thread pool
		SimpleThreadPool threadPool = new SimpleThreadPool(10, Thread.NORM_PRIORITY);
		threadPool.setInstanceId(id);
		threadPool.setInstanceName(id);
		threadPool.setMakeThreadsDaemons(false);
		threadPool.setThreadNamePrefix(getClass().getSimpleName() + "_" + id);
		// Create scheduler
		try {
			factory.createScheduler(id, id, threadPool, new RAMJobStore(), null, null, 0, -1, -1, false, null);
			scheduler = factory.getScheduler(id);
		} catch (Exception e) {
			threadPool.shutdown(false);
			throw new IllegalStateException(e);
		}
		// Start scheduler
		try {
			scheduler.start();
		} catch (Exception e) {
			try {
				scheduler.shutdown(false);
			} catch (Exception ignore) {
			}
			throw new IllegalStateException(e);
		}
		lockManager = new ObjectLockManager();
	}

	/**
	 * Add a task into this executor
	 * 
	 * @param expression CRON expression
	 * @param task       runnable task
	 * @return a {@link Cancellable} to cancel this task for repeat running
	 * @throws IllegalArgumentException when the expression is illegal
	 * @throws IllegalStateException    when the executor has been shutdown
	 */
	public Cancellable execute(final String expression, final Runnable task) {
		if (null == expression || null == task)
			throw new NullPointerException();
		final JobKey jobKey = new JobKey(expression);
		final TriggerKey triggerKey = new TriggerKey(expression);
		final CronScheduleBuilder scheduleBuilder;
		try {
			scheduleBuilder = CronScheduleBuilder.cronSchedule(new CronExpression(expression));
		} catch (Exception e) {
			// Happens when this expression is illegal
			throw new IllegalArgumentException(e);
		}
		Cancellable cancellable;
		// Lock
		final ObjectLock lock = lockManager.lock(expression);
		try {
			if (scheduler.checkExists(jobKey)) {
				cancellable = appendTask(scheduler.getJobDetail(jobKey), task);
			} else {
				// Build trigger
				CronTrigger trigger = TriggerBuilder.newTrigger() // Create trigger
						.withSchedule(scheduleBuilder).withIdentity(triggerKey) // Key on expression
						.startNow() // start from now
						.build();
				// Build job detail
				JobDetail jobDetail = JobBuilder.newJob() // Create jobbuilder
						.ofType(ExecutorJob.class) // Job class map
						.withIdentity(jobKey) // Key on expression
						.build();
				jobDetail.getJobDataMap().put(Tasks.DATA_KEY, new Tasks());
				cancellable = appendTask(jobDetail, task);
				scheduler.scheduleJob(jobDetail, trigger);
			}
		} catch (Exception e) {
			try {
				if (scheduler.isShutdown()) {
					throw new IllegalStateException(e);
				}
			} catch (Exception ignore) {
			}
			return () -> {
			};
		} finally {
			lock.unlock();
		}
		return cancellable;
	}

	/**
	 * Add a task into job detail data
	 */
	private Cancellable appendTask(JobDetail jobDetail, Runnable runTask) {
		final Task task = new Task(runTask);
		((Tasks) jobDetail.getJobDataMap().get(Tasks.DATA_KEY)).put(task);
		return new TaskCancellable(jobDetail.getKey(), task);
	}

	/**
	 * Shutdown this executor. Never throws any {@link Exception} here.
	 */
	public void shutdown() {
		try {
			scheduler.shutdown(true);
		} catch (Exception ignore) {
		}
	}

	/**
	 * A {@link Cancellable} implementation on tasks.
	 * 
	 * @author XuYanhang
	 *
	 */
	private class TaskCancellable implements Cancellable {

		final JobKey jobKey;
		final Task task;

		/**
		 * Initialize this {@link Cancellable}
		 */
		TaskCancellable(JobKey jobKey, Task task) {
			super();
			this.jobKey = jobKey;
			this.task = task;
		}

		/**
		 * @see Cancellable#cancel()
		 */
		@Override
		public void cancel() {
			// Check shutdown and exists
			try {
				if (scheduler.isShutdown() || !scheduler.checkExists(jobKey)) {
					return;
				}
			} catch (Exception e) {
				return;
			}
			// Lock on id and expression
			ObjectLock lock = lockManager.lock(jobKey.getName());
			try {
				// Fetch tasks
				final Tasks tasks;
				try {
					if (scheduler.checkExists(jobKey)) {
						tasks = (Tasks) scheduler.getJobDetail(jobKey).getJobDataMap().get(Tasks.DATA_KEY);
					} else {
						return;
					}
				} catch (Exception e) {
					return;
				}
				// Delete
				tasks.del(task);
				if (tasks.isEmpty()) {
					try {
						if (!scheduler.isShutdown()) {
							scheduler.deleteJob(jobKey);
						}
					} catch (Exception ignore) {
					}
				}
			} finally {
				// Release lock
				lock.unlock();
			}
		}

	}

	/**
	 * Shouldn't be held on client. An inner implementation on {@link Job} to run
	 * tasks.
	 * 
	 * @author XuYanhang
	 *
	 */
	public static final class ExecutorJob implements Job {

		/**
		 * Initialize in thread pool
		 */
		public ExecutorJob() {
			super();
		}

		/**
		 * @see Job#execute(JobExecutionContext)
		 */
		@Override
		public void execute(JobExecutionContext context) {
			Task[] tasks = ((Tasks) context.getJobDetail().getJobDataMap().get(Tasks.DATA_KEY)).get();
			for (int i = 0, l = tasks.length; i < l; i++)
				tasks[i].task.run();
		}

	}

	/**
	 * Tasks data wrapper mapped for each CRON expression on each
	 * {@link CRONExecutor}.
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class Tasks {

		final static String DATA_KEY = "tasks";

		final LinkedList<Task> tasks;

		/**
		 * Initialize this class
		 */
		Tasks() {
			super();
			this.tasks = new LinkedList<>();
		}

		/**
		 * Fetch all tasks to run
		 */
		Task[] get() {
			synchronized (tasks) {
				return tasks.toArray(new Task[tasks.size()]);
			}
		}

		/**
		 * Add a task
		 */
		void put(Task task) {
			synchronized (tasks) {
				tasks.add(task);
			}
		}

		/**
		 * Remove a task
		 */
		void del(Task task) {
			synchronized (tasks) {
				tasks.remove(task);
			}
		}

		/**
		 * Analyze if this tasks is empty
		 */
		boolean isEmpty() {
			synchronized (tasks) {
				return tasks.isEmpty();
			}
		}

	}

	/**
	 * Task data wrapper on a {@link Runnable} task but hold the equals and hashCode
	 * with no relation with the origin task.
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class Task {

		/**
		 * Original task
		 */
		final Runnable task;

		/**
		 * Initialize this task
		 */
		Task(Runnable task) {
			super();
			this.task = task;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public final int hashCode() {
			return super.hashCode();
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public final boolean equals(Object obj) {
			return this == obj;
		}

	}

}
