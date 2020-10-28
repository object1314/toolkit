/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * According <a href="https://tools.ietf.org/html/rfc2616">RFC 2616</a>, HTTP
 * applications have historically allowed three different formats for the
 * representation of date/time stamps: <br>
 * "<code>Sun, 06 Nov 1994 08:49:37 GMT</code>"
 * <a href="https://tools.ietf.org/html/rfc822">RFC 822</a>, updated by
 * <a href="https://tools.ietf.org/html/rfc1123">RFC 1123</a> <br>
 * "<code>Sunday, 06-Nov-94 08:49:37 GMT</code>"
 * <a href="https://tools.ietf.org/html/rfc850">RFC 850</a>, obsoleted by
 * <a href="https://tools.ietf.org/html/rfc1036">RFC 1036</a> <br>
 * "<code>Sun Nov 6 08:49:37 1994</code>" ANSI C's asctime() format <br>
 * The first format is preferred as an Internet standard and represents a
 * fixed-length subset of that defined
 * <a href="https://tools.ietf.org/html/rfc1123">by RFC 1123</a> (an update to
 * <a href="https://tools.ietf.org/html/rfc822">RFC 822</a>). The second format
 * is in common use, but is based on the obsolete
 * <a href="https://tools.ietf.org/html/rfc850">RFC 850</a> date format and
 * lacks a four-digit year. HTTP/1.1 clients and servers that parse the date
 * value MUST accept all three formats (for compatibility with HTTP/1.0), though
 * they MUST only generate the <a href="https://tools.ietf.org/html/rfc1123">RFC
 * 1123</a> format for representing HTTP-date values in header fields.
 *
 * @author XuYanhang
 * @since 2020-10-28
 *
 */
public final class HttpTime implements java.io.Serializable, Cloneable, Comparable<HttpTime> {

	/**
	 * Parse a formatted HTTP time String and get the {@link HttpTime} or
	 * <code>null</code> if failed.
	 * 
	 * @return current HttpTime or <code>null</code> if parse failed
	 */
	public static HttpTime of(String timestamp) {
		if (null == timestamp)
			return null;
		// Fetch the formats and for-each test parse.
		Formatter[] formats = Formatter.values();
		for (int index = 0; index < formats.length; index++) {
			try {
				return new HttpTime(formats[index].parse(timestamp), formats[index]);
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Create a {@link HttpTime} at current with formatter of
	 * {@link Formatter#RFC_822}
	 * 
	 * @return current HttpTime
	 */
	public static HttpTime now() {
		return new HttpTime();
	}

	/**
	 * The time in epoch milliseconds.
	 */
	private long epochMillis;

	/**
	 * The time formatter.
	 */
	private Formatter formatter;

	/**
	 * Create a current {@link HttpTime}
	 * 
	 * @see System#currentTimeMillis()
	 * @see Formatter#RFC_822
	 */
	public HttpTime() {
		super();
		this.epochMillis = System.currentTimeMillis();
		this.formatter = Formatter.RFC_822;
	}

	/**
	 * Create a {@link HttpTime} use a given epoch milliseconds
	 * 
	 * @see Formatter#RFC_822
	 * @param epochMillis the time to set in epoch milliSeconds
	 */
	public HttpTime(long epochMillis) {
		super();
		this.epochMillis = epochMillis;
		this.formatter = Formatter.RFC_822;
	}

	/**
	 * Create a {@link HttpTime} in given epoch milliseconds and formatter
	 * 
	 * @param time      the time to set in epoch milliSeconds
	 * @param formatter the formatter of the time
	 */
	public HttpTime(long time, Formatter formatter) {
		super();
		this.epochMillis = time;
		this.formatter = null == formatter ? Formatter.RFC_822 : formatter;
	}

	/**
	 * Reset the epoch milliseconds of this time
	 * 
	 * @param epochMillis the {@link #epochMillis} to set
	 * @return this
	 */
	public HttpTime setEpochMillis(long epochMillis) {
		this.epochMillis = epochMillis;
		return this;
	}

	/**
	 * Reset the formatter of this time to specified value
	 * 
	 * @param formatter the {@link #formatter} to set
	 * @return this
	 */
	public HttpTime setFormatter(Formatter formatter) {
		this.formatter = null == formatter ? Formatter.RFC_822 : formatter;
		return this;
	}

	/**
	 * Returns the epoch milliseconds of this time
	 * 
	 * @return the {@link #epochMillis}
	 */
	public long getEpochMillis() {
		return epochMillis;
	}

	/**
	 * Returns this formatter of this time
	 * 
	 * @return the {@link #formatter}
	 */
	public Formatter getFormatter() {
		return formatter;
	}

	/**
	 * Compare two {@link HttpTime}s. If the time compare to another one get a
	 * positive value, the time is bigger than another one then it means the time is
	 * later than another one.
	 */
	@Override
	public int compareTo(HttpTime o) {
		return Long.compare(epochMillis, o.epochMillis);
	}

	/**
	 * Clone the {@link HttpTime}.
	 */
	@Override
	public HttpTime clone() {
		try {
			return (HttpTime) super.clone();
		} catch (CloneNotSupportedException e) {
			// Should never happen when it's Cloneable.
			throw new InternalError(e);
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formatter == null) ? 0 : formatter.hashCode());
		result = prime * result + (int) (epochMillis ^ (epochMillis >>> 32));
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpTime other = (HttpTime) obj;
		if (formatter != other.formatter)
			return false;
		if (epochMillis != other.epochMillis)
			return false;
		return true;
	}

	/**
	 * Cast the {@link HttpTime} bean as date string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return formatter.format(epochMillis);
	}

	/**
	 * The formatter for HTTP Time. For some reason, here only supports the GMT as
	 * zone and {@link Formatter#RFC_850} can only support the year in 21-century.
	 * 
	 * @author XuYanhang
	 *
	 */
	public static enum Formatter {

		// RFC 822
		RFC_822("EEE, dd MMM yyyy HH:mm:ss zzz"),
		// RFC 850
		RFC_850("EEEE, dd-MMM-yy HH:mm:ss zzz"),
		// ANSI C's asctime()
		ANSI_C("EEE MMM d HH:mm:ss yyyy");

		/**
		 * The time format pattern String
		 */
		private final String pattern;

		/**
		 * The formatter from the pattern
		 */
		private final DateTimeFormatter formatter;

		/**
		 * Initialize the formatter
		 */
		private Formatter(String pattern) {
			this.pattern = pattern;
			this.formatter = DateTimeFormatter.ofPattern(pattern, Locale.ROOT).withZone(ZoneId.of("GMT"));
		}

		/**
		 * Format a time in epoch milliseconds as time string.
		 */
		public String format(long epochMillis) {
			return formatter.format(Instant.ofEpochMilli(epochMillis));
		}

		/**
		 * Parse a time string as time in epoch milliseconds. If parse failed then get
		 * an <code>Exception</code>.
		 */
		public long parse(String timestamp) {
			return formatter.parse(timestamp).getLong(ChronoField.INSTANT_SECONDS) * 1000;
		}

		/**
		 * Returns the pattern of this formatter
		 * 
		 * @return the {@link #pattern}
		 */
		public String getPattern() {
			return pattern;
		}

	}

	/**
	 * This class can be serialized
	 */
	private static final long serialVersionUID = 2335437663581851345L;

}
