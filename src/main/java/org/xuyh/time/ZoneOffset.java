/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

import java.util.TimeZone;

import static org.xuyh.time.Time.MINUTES_PER_HOUR;
import static org.xuyh.time.Time.SECONDS_PER_HOUR;
import static org.xuyh.time.Time.SECONDS_PER_MINUTE;
import static org.xuyh.time.Time.MILLIS_PER_SECOND;

/**
 * A time-zone offset from Greenwich/UTC, such as {@code +0800}.
 * <p>
 * A time-zone offset is the amount of time that a time-zone differs from
 * Greenwich/UTC. This is usually a fixed number of hours and minutes.
 * <p>
 * In 2008, time-zone offsets around the world extended from -1200 to +1400. To
 * prevent any problems with that range being extended, yet still provide
 * validation, the range of offsets is restricted from -1800 to +1800 inclusive.
 * <p>
 * 
 * @author XuYanhang
 * @since 2020-08-13
 *
 */
public final class ZoneOffset implements java.io.Serializable, Comparable<ZoneOffset> {

	/**
	 * The UTC time-zone offset.
	 */
	public static final ZoneOffset UTC;

	/**
	 * The local time-zone offset.
	 */
	public static final ZoneOffset LOCAL;

	/**
	 * The maximum time-zone offset
	 */
	public static final ZoneOffset MAX;

	/**
	 * The minimum time-zone offset
	 */
	public static final ZoneOffset MIN;

	/**
	 * The maximum seconds for time-zone offset.
	 */
	public static final int MAX_SECONDS = 18 * SECONDS_PER_HOUR;

	/**
	 * The minimum seconds for time-zone offset.
	 */
	public static final int MIN_SECONDS = -MAX_SECONDS;

	static {
		UTC = new ZoneOffset(0);
		MAX = new ZoneOffset(MAX_SECONDS);
		MIN = new ZoneOffset(MIN_SECONDS);
		int localSeconds = TimeZone.getDefault().getRawOffset() / MILLIS_PER_SECOND;
		if (0 == localSeconds)
			LOCAL = UTC;
		else if (MAX_SECONDS <= localSeconds)
			LOCAL = MAX;
		else if (MIN_SECONDS >= localSeconds)
			LOCAL = MIN;
		else
			LOCAL = new ZoneOffset(localSeconds);
	}

	/**
	 * Create a instance of the {@link ZoneOffset} from offset hours.
	 * 
	 * @param hours time-zone offset hours
	 * @return result zone offset
	 */
	public static ZoneOffset fromHours(int hours) {
		return from(0, hours, 0, 0);
	}

	/**
	 * Create a instance of the {@link ZoneOffset} from offset hours and
	 * minutes.
	 * 
	 * @param sign    time-zone offset sign as 1, 0 or -1 where 1 is positive (most
	 *                as east zone), 0 is zero (most as UTC) and -1 is negative
	 *                (most as west zone)
	 * @param hours   time-zone offset hours
	 * @param minutes time-zone offset minutes
	 * @return result zone offset
	 */
	public static ZoneOffset from(int sign, int hours, int minutes) {
		return from(sign, hours, minutes, 0);
	}

	/**
	 * Create a instance of the {@link ZoneOffset} from offset hours, minutes
	 * and seconds.
	 * 
	 * @param sign    time-zone offset sign as 1, 0 or -1 where 1 is positive (most
	 *                as east zone), 0 is zero (most as UTC) and -1 is negative
	 *                (most as west zone)
	 * @param hours   time-zone offset hours
	 * @param minutes time-zone offset minutes
	 * @param seconds time-zone offset seconds
	 * @return result zone offset
	 */
	public static ZoneOffset from(int sign, int hours, int minutes, int seconds) {
		int totalSeconds = seconds;
		totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(minutes, SECONDS_PER_MINUTE));
		totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(hours, SECONDS_PER_HOUR));
		if (sign < 0) {
			totalSeconds = Math.negateExact(totalSeconds);
		}
		return fromTotalSeconds(totalSeconds);
	}

	/**
	 * Create a instance of the {@link ZoneOffset} from total seconds.
	 * 
	 * @param totalSeconds The total time-zone offset in seconds
	 * @return time-zone offset result
	 * @throws IllegalArgumentException if the total seconds out of range
	 */
	public static ZoneOffset fromTotalSeconds(int totalSeconds) {
		if (totalSeconds == 0)
			return UTC;
		if (totalSeconds == LOCAL.totalSeconds)
			return LOCAL;
		if (totalSeconds < MIN_SECONDS || totalSeconds > MAX_SECONDS)
			throw new IllegalArgumentException("time-zone offset out of range");
		return new ZoneOffset(totalSeconds);
	}

	/**
	 * The total offset in seconds.
	 */
	private final int totalSeconds;

	/**
	 * Constructor, previously validated.
	 * 
	 * @param totalSeconds the total time-zone offset in seconds, from
	 *                     {@value #MIN_SECONDS} to {@value #MAX_SECONDS}
	 */
	private ZoneOffset(int totalSeconds) {
		super();
		this.totalSeconds = totalSeconds;
	}

	/**
	 * Get the sign field of the offset of 1, 0 or -1 where 1 is positive (most as
	 * east zone), 0 is zero (most as UTC) and -1 is negative (most as west zone)
	 * 
	 * @return the sign field of the offset of 1, 0 or -1.
	 */
	public int getSign() {
		if (totalSeconds == 0) {
			return 0;
		}
		return totalSeconds < 0 ? -1 : 1;
	}

	/**
	 * Get the hour field of the offset.
	 * 
	 * @return the hour field of the offset
	 */
	public int getHours() {
		int t = totalSeconds < 0 ? -totalSeconds : totalSeconds;
		return t / SECONDS_PER_HOUR;
	}

	/**
	 * Get the minute-of-hour field of the offset.
	 * 
	 * @return the minute-of-hour field of the offset
	 */
	public int getMinutes() {
		int t = totalSeconds < 0 ? -totalSeconds : totalSeconds;
		return (t / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR;
	}

	/**
	 * Get the second-of-minute field of the offset.
	 * 
	 * @return the second-of-minute field of the offset
	 */
	public int getSeconds() {
		int t = totalSeconds < 0 ? -totalSeconds : totalSeconds;
		return t % SECONDS_PER_MINUTE;
	}

	/**
	 * Get the total offset in seconds.
	 * 
	 * @return the total offset in seconds
	 */
	public int getTotalSeconds() {
		return totalSeconds;
	}

	/**
	 * Compares this {@link ZoneOffset offset} to another one. A positive value
	 * means this {@link ZoneOffset offset} is larger than another one; A
	 * negative value means this {@link ZoneOffset offset} is smaller than
	 * another one; A zero means the both {@link ZoneOffset offsets} are a same
	 * time-zone offset point.
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param other the other time-zone offset to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(ZoneOffset other) {
		return this.totalSeconds - other.totalSeconds;
	}

	/**
	 * A hash code for this offset.
	 *
	 * @see java.lang.Object#hashCode()
	 * @return a suitable hash code
	 */
	@Override
	public int hashCode() {
		return totalSeconds;
	}

	/**
	 * Checks if this offset is equal to another offset.
	 * <p>
	 * The comparison is based on the amount of the offset in seconds.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj the object to check, null returns false
	 * @return true if this is equal to the other offset
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return this.totalSeconds == ((ZoneOffset) obj).totalSeconds;
	}

	/**
	 * Outputs this time-zone offset as a {@code String} formatted like
	 * {@code 1970-01-01T08:00:00.000000000+0800}.
	 * 
	 * @see java.lang.Object#toString()
	 * @return a string representation of this time-zone offset
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(7);
		// Sign
		sb.append(totalSeconds < 0 ? '-' : '+');
		// Hours
		int t = totalSeconds < 0 ? -totalSeconds : totalSeconds;
		int hours = t / SECONDS_PER_HOUR;
		if (hours < 10)
			sb.append('0');
		sb.append(hours);
		// Minutes
		t -= hours * SECONDS_PER_HOUR;
		int minutes = t / SECONDS_PER_MINUTE;
		if (minutes < 10)
			sb.append('0');
		sb.append(minutes);
		// Seconds
		t -= minutes * SECONDS_PER_MINUTE;
		if (t != 0) {
			int seconds = t;
			if (seconds < 10)
				sb.append('0');
			sb.append(seconds);
		}
		return sb.toString();
	}

	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = -8462013718729667406L;

}
