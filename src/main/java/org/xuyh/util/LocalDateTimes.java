/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Toolkits on {@link LocalDateTime}.
 *
 * @author XuYanhang
 * @since 2022-10-03
 */
public final class LocalDateTimes {
	/**
	 * Formatter to format a {@link LocalDateTime} in default ways like 2022-10-03 12:00:00
	 */
	private static final DateTimeFormatter DEFAULT_FORMATTER;

	/**
	 * Parser to parse time element in a time stamp {@link String} to a {@link LocalDateTime}
	 */
	private static final Pattern ELEMENT_PARSE_PATTERN;

	static {
		DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
		ELEMENT_PARSE_PATTERN = Pattern.compile("[0-9]{1,9}");
	}

	/**
	 * Returns a default format time stamp with format of {@link #DEFAULT_FORMATTER}
	 *
	 * @param time date and time, not null
	 * @return a time with format of {@link #DEFAULT_FORMATTER}
	 */
	public static String format(LocalDateTime time) {
		return DEFAULT_FORMATTER.format(time);
	}

	/**
	 * Parses a time stamp to a {@link LocalDateTime}. The time stamp required to be like these:<br>
	 * yyyy-MM-dd --> yyyy-MM-dd 00:00:00<br>
	 * yyyy-MM-dd HH --> yyyy-MM-dd HH:00:00<br>
	 * yyyy-MM-dd HH:mm --> yyyy-MM-dd HH:mm:00<br>
	 * yyyy-MM-dd HH:mm:ss<br>
	 * yyyy-MM-dd HH:mm:ss.SSS<br>
	 * yyyy-MM-dd HH:mm:ss.SSSSSS<br>
	 * yyyy-MM-dd HH:mm:ss.SSSSSSSSS<br>
	 *
	 * @param time time stamp, not null
	 * @return a {@link LocalDateTime}
	 * @throws IllegalArgumentException if the time can't be parsed
	 */
	public static LocalDateTime parse(String time) {
		if (time.length() > 64) {
			throw new IllegalArgumentException("Can't parse");
		}
		Matcher matcher = ELEMENT_PARSE_PATTERN.matcher(time);
		int[] result = new int[7];
		for (int cur = 0; cur < result.length && matcher.find(); ++cur) {
			result[cur] = Integer.parseInt(matcher.group());
		}
		if (result[6] != 0) {
			int exp = 9 - matcher.group().length();
			result[6] *= (int) Math.pow(10, exp);
		}
		try {
			return LocalDateTime.of(result[0], result[1], result[2],
					result[3], result[4], result[5], result[6]);
		} catch (DateTimeException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Splits a period of time to groups by month
	 *
	 * @param begin begin time, required not later than end
	 * @param end end time, required not earlier than begin
	 */
	public static List<PeriodGroup<YearMonth>> splitPeriodByMonth(LocalDateTime begin, LocalDateTime end) {
		if (begin.isAfter(end)) {
			throw new IllegalArgumentException("begin>end");
		}
		YearMonth beginMonth = YearMonth.from(begin);
		YearMonth endMonth = YearMonth.from(end);
		int size = Math.toIntExact(beginMonth.until(endMonth, ChronoUnit.MONTHS) + 1L);
		return Collections2.formulaList(size, (index) -> {
			YearMonth curMonth = beginMonth.plusMonths(index);
			return new PeriodGroup<>(curMonth,
					curMonth.equals(beginMonth) ? begin : LocalDateTime.of(curMonth.atDay(1), LocalTime.MIN),
					curMonth.equals(endMonth) ? end : LocalDateTime.of(curMonth.atEndOfMonth(), LocalTime.MAX));
		});
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private LocalDateTimes() {
		super();
	}

	/**
	 * PeriodGroup is a period of time. The time group includes the period from begin time to end.
	 *
	 * @author XuYanhang
	 * @since 2022-10-03
	 *
	 * @param <T> generic type to tag on group
	 */
	public static class PeriodGroup<T extends Comparable<T>>
			implements Comparable<PeriodGroup<T>>, java.io.Serializable {
		/**
		 * Period group, included both begin time and end time, Not null
		 */
		public final T group;

		/**
		 * Period begin time, Not null and not later than end
		 */
		public final LocalDateTime begin;

		/**
		 * Period end time, Not null and not earlier than begin
		 */
		public final LocalDateTime end;

		/**
		 * Create by full fields
		 *
		 * @param group period group
		 * @param begin begin time
		 * @param end end time
		 */
		private PeriodGroup(T group, LocalDateTime begin, LocalDateTime end) {
			super();
			this.group = group;
			this.begin = begin;
			this.end = end;
		}

		@Override
		public int compareTo(PeriodGroup<T> other) {
			return group.compareTo(other.group);
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + group.hashCode();
			result = prime * result + begin.hashCode();
			result = prime * result + end.hashCode();
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			PeriodGroup<?> other = (PeriodGroup<?>) obj;
			return group.equals(other.group) && begin.equals(other.begin) && end.equals(other.end);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format(Locale.ROOT, "PeriodGroup[%s]:%s-->%s",
					group, DEFAULT_FORMATTER.format(begin), DEFAULT_FORMATTER.format(end));
		}

		/**
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = 3431994859011598266L;
	}
}
