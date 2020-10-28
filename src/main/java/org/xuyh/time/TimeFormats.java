/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * The time tool to format milliseconds time value to readable time expression
 * or parse the readable time expression as milliseconds time value.
 * 
 * @author XuYanhang
 * @since 2020-10-28
 * @see System#currentTimeMillis()
 */
public final class TimeFormats {

	/**
	 * UTC time zone
	 */
	private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	/**
	 * Returns the current time in milliseconds. It's the difference, measured in
	 * milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 * 
	 * @see System#currentTimeMillis()
	 */
	public static long now() {
		return System.currentTimeMillis();
	}

	/**
	 * Convert a time to time expression as format according the timeZone. The input
	 * time is the difference, measured in milliseconds, between the current time
	 * and midnight, January 1, 1970 UTC. If the method is failed, especially when
	 * the format string is invalid then the result is <code>null</code>.
	 * 
	 * @param epochMillis the difference, measured in milliseconds, between the
	 *                    current time and midnight, January 1, 1970 UTC
	 * @param pattern     the target result's time pattern
	 * @param zoneId      the target result's timeZone ID matches
	 *                    {@link TimeZone#getTimeZone(String)}
	 * 
	 * @return The formated time as the parameters or <code>null</code> when failed.
	 * 
	 * @see #parse(String, String, String)
	 */
	public static String format(long epochMillis, String pattern, String zoneId) {
		return format0(epochMillis, pattern, TimeZone.getTimeZone(zoneId));
	}

	/**
	 * Formats a time to local time expression as pattern
	 * 
	 * @see #format(long, String, String)
	 */
	public static String formatLocal(long epochMillis, String pattern) {
		return format0(epochMillis, pattern, TimeZone.getDefault());
	}

	/**
	 * Formats a time to UTC time expression as pattern
	 * 
	 * @see #format(long, String, String)
	 */
	public static String formatUTC(long epochMillis, String pattern) {
		return format0(epochMillis, pattern, UTC);
	}

	/**
	 * Time formats method. Returns <code>null</code> format failed.
	 */
	private static String format0(long epochMillis, String pattern, TimeZone zone) {
		final SimpleDateFormat formatter = FORMATTER_CACHE.get(pattern);
		if (null != zone)
			formatter.setTimeZone(zone);
		String result = formatter.format(new java.util.Date(epochMillis));
		FORMATTER_CACHE.put(pattern, formatter);
		return result;
	}

	/**
	 * Convert a time expression as format to milliseconds time according the
	 * timeZone. The result time is the difference, measured in milliseconds,
	 * between the current time and midnight, January 1, 1970 UTC. If the parse
	 * method is failed when the format is invalid or the formated time is parsed
	 * failed then the <code>null</code> is returned.
	 * 
	 * @param formatTime The formated time
	 * @param pattern    The source time's format pattern
	 * @param zoneId     The target time's's timeZone matches
	 *                   {@link TimeZone#getTimeZone(String)}
	 * 
	 * @return the difference, measured in milliseconds, between the current time
	 *         and midnight, January 1, 1970 UTC or <code>null</code> when failed
	 * 
	 * @see #format(long, String, String)
	 */
	public static Long parse(String formatTime, String pattern, String zoneId) {
		return parse0(formatTime, pattern, TimeZone.getTimeZone(zoneId));
	}

	/**
	 * Parses a local time expression as milliseconds time value.
	 * 
	 * @see #parse(String, String, String)
	 */
	public static Long parseLocal(String formatTime, String pattern) {
		return parse0(formatTime, pattern, TimeZone.getDefault());
	}

	/**
	 * Parses a UTC time expression as milliseconds time value.
	 * 
	 * @see #parse(String, String, String)
	 */
	public static Long parseUTC(String formatTime, String pattern) {
		return parse0(formatTime, pattern, UTC);
	}

	/**
	 * Time parses method. Returns <code>null</code> on any exception happens.
	 */
	private static Long parse0(final String time, final String pattern, final TimeZone zone) {
		final SimpleDateFormat formatter = FORMATTER_CACHE.get(pattern);
		if (null != zone)
			formatter.setTimeZone(zone);
		java.util.Date date = null;
		try {
			date = formatter.parse(time);
		} catch (Exception e) {
		}
		FORMATTER_CACHE.put(pattern, formatter);
		return null == date ? null : Long.valueOf(date.getTime());
	}

	/**
	 * Cache to cache formatters
	 */
	private static final TimeFormatterCache FORMATTER_CACHE = new TimeFormatterCache();

	/**
	 * Pool to cache {@link SimpleDateFormat} in hashMap way of pattern map and
	 * array way of formatters for each pattern
	 * 
	 * @author XuYanhang
	 *
	 */
	private final static class TimeFormatterCache {

		final ConcurrentHashMap<String, TimeFormatterPool> caches;

		private TimeFormatterCache() {
			super();
			caches = new ConcurrentHashMap<>();
		}

		SimpleDateFormat get(final String pattern) {
			TimeFormatterPool pool = caches.get(pattern);
			if (null == pool) {
				pool = new TimeFormatterPool(new SimpleDateFormat(pattern, Locale.ROOT),
						() -> new SimpleDateFormat(pattern, Locale.ROOT), 10);
				caches.put(pattern, pool);
			}
			return pool.poll();
		}

		void put(final String pattern, final SimpleDateFormat formatter) {
			TimeFormatterPool pool = caches.get(pattern);
			if (null != pool && null != formatter)
				pool.push(formatter);
		}

	}

	/**
	 * Pool to cache {@link SimpleDateFormat} in array way
	 * 
	 * @author XuYanhang
	 *
	 */
	private final static class TimeFormatterPool {

		final Supplier<SimpleDateFormat> factory;
		final SimpleDateFormat[] items;
		int cursor;

		TimeFormatterPool(SimpleDateFormat first, Supplier<SimpleDateFormat> factory, int capacity) {
			super();
			this.factory = factory;
			this.items = new SimpleDateFormat[capacity];
			this.items[0] = first;
			this.cursor = 1;
		}

		SimpleDateFormat poll() {
			SimpleDateFormat formatter = null;
			synchronized (items) {
				if (cursor > 0) {
					formatter = items[--cursor];
					items[cursor] = null;
				}
			}
			if (null == formatter)
				formatter = factory.get();
			return formatter;
		}

		void push(SimpleDateFormat formatter) {
			synchronized (items) {
				if (cursor < items.length)
					items[cursor++] = formatter;
			}
		}

	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private TimeFormats() {
		super();
	}

}
