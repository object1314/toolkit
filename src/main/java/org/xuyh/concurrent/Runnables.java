package org.xuyh.concurrent;

public class Runnables {

	public static Runnable order(Runnable run1, Runnable run2) {
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
			cursor+=runs1.length;
		}
		if (null == runs2) {
			runs[cursor++] = run2;
		} else {
			System.arraycopy(runs2, 0, runs, cursor, runs2.length);
			cursor+=runs2.length;
		}
		return new DuplicatedRunnable(runs);
	}

	private static class DuplicatedRunnable implements Runnable {

		final Runnable[] runs;

		/**
		 * @param runs
		 */
		DuplicatedRunnable(Runnable[] runs) {
			super();
			this.runs = runs;
		}

		@Override
		public void run() {
			for (int i = 0, l = runs.length ; i < l; i++)
				runs[i].run();
		}

	}

}
