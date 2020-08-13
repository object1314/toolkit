/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

/**
 * {@link Time} is an immutable date-time object that represents a time,
 * often viewed as hour-minute-second. Time is represented to nanosecond
 * precision. For example, the value "18:28:38.123456789" can be stored in a
 * {@link Time}.
 * <p>
 * This class does not store or represent a date or time-zone. Instead, it is a
 * description of the local time as seen on a wall clock. It's for time-of-day,
 * from 00:00:00.000000000 to 23:59:59.999999999.
 * <p>
 * 
 * @author XuYanhang
 * @since 2020-08-13
 *
 */
public final class Time implements java.io.Serializable, Comparable<Time> {

	/**
	 * The maximum supported {@link Time} as 23:59:59.999999999. It's the end of
	 * the day.
	 */
	public static final Time MAX;

	/**
	 * The maximum supported {@link Time} as 00:00:00.000000000. It's the begin
	 * of the day.
	 */
	public static final Time MIN;

	/**
	 * The time of midnight at the start of the day as 00:00:00.000000000. Neither
	 * P.M. nor A.M..
	 */
	public static final Time MIDNIGHT;

	/**
	 * The time of noon at the middle of the day as 12:00:00.000000000. Neither P.M.
	 * nor A.M..
	 */
	public static final Time NOON;

	/**
	 * Cached time constants for each hours.
	 */
	private static final Time[] HOURS;

	/**
	 * Hours per day.
	 */
	public static final int HOURS_PER_DAY = 24;

	/**
	 * Minutes per hour.
	 */
	public static final int MINUTES_PER_HOUR = 60;

	/**
	 * Seconds per minute.
	 */
	public static final int SECONDS_PER_MINUTE = 60;

	/**
	 * Seconds per hour.
	 */
	public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

	/**
	 * Seconds per day.
	 */
	public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

	/**
	 * Milliseconds per second.
	 */
	public static final int MILLIS_PER_SECOND = 1000;

	/**
	 * Milliseconds per hour.
	 */
	public static final int MILLIS_PER_HOUR = SECONDS_PER_HOUR * MILLIS_PER_SECOND;

	/**
	 * Milliseconds per day.
	 */
	public static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * HOURS_PER_DAY;

	/**
	 * Nanoseconds per second.
	 */
	public static final int NANOS_PER_SECOND = 1_000_000_000;

	/**
	 * Nanoseconds per millisecond.
	 */
	public static final int NANOS_PER_MILLIS = NANOS_PER_SECOND / MILLIS_PER_SECOND;

	/**
	 * Nanoseconds per day.
	 */
	public static final long NANOS_PER_DAY = ((long) NANOS_PER_SECOND) * SECONDS_PER_DAY;

	static {
		HOURS = new Time[HOURS_PER_DAY];
		for (int h = 0; h < HOURS_PER_DAY; h++)
			HOURS[h] = new Time(h, 0, 0, 0);
		MIDNIGHT = HOURS[0];
		NOON = HOURS[12];
		MIN = HOURS[0];
		MAX = new Time(23, 59, 59, 999_999_999);
	}

	/**
	 * Create a instance of the {@link Time} from hour-of-day field.
	 * 
	 * @param hour hour-of-day field
	 * @return result time
	 */
	public static Time fromHour(int hour) {
		int h = hour;
		if (h < 0 || h >= HOURS_PER_DAY)
			h = (h % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
		return HOURS[h];
	}

	/**
	 * Create a instance of the {@link Time} from these time fields.
	 * 
	 * @param hour   hour-of-day field
	 * @param minute minute-of-hour field
	 * @param second second-of-minute field
	 * @return result time
	 */
	public static Time from(int hour, int minute, int second) {
		return from(hour, minute, second, 0);
	}

	/**
	 * Create a instance of the {@link Time} from all time fields.
	 * 
	 * @param hour          hour-of-day field
	 * @param minute        minute-of-hour field
	 * @param second        second-of-minute field
	 * @param nanosOfSecond nanosecond-of-second field
	 * @return result time
	 */
	public static Time from(int hour, int minute, int second, int nanosOfSecond) {
		int h = hour;
		int m = minute;
		int s = second;
		int n = nanosOfSecond;
		int temp;
		if (h < 0 || h >= HOURS_PER_DAY) {
			h = (h % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
		}
		if (m < 0 || m >= MINUTES_PER_HOUR) {
			temp = (m % MINUTES_PER_HOUR + MINUTES_PER_HOUR) % MINUTES_PER_HOUR;
			h += (m - temp) / MINUTES_PER_HOUR;
			m = temp;
		}
		if (s < 0 || s >= SECONDS_PER_MINUTE) {
			temp = (s % SECONDS_PER_MINUTE + SECONDS_PER_MINUTE) % SECONDS_PER_MINUTE;
			m += (s - temp) / SECONDS_PER_MINUTE;
			s = temp;
		}
		if (n < 0 || n >= NANOS_PER_SECOND) {
			temp = (n % NANOS_PER_SECOND + NANOS_PER_SECOND) % NANOS_PER_SECOND;
			s += (n - temp) / NANOS_PER_SECOND;
			n = temp;
		}
		if (s < 0 || s >= SECONDS_PER_MINUTE) {
			temp = (s % SECONDS_PER_MINUTE + SECONDS_PER_MINUTE) % SECONDS_PER_MINUTE;
			m += (s - temp) / SECONDS_PER_MINUTE;
			s = temp;
		}
		if (m < 0 || m >= MINUTES_PER_HOUR) {
			temp = (m % MINUTES_PER_HOUR + MINUTES_PER_HOUR) % MINUTES_PER_HOUR;
			h += (m - temp) / MINUTES_PER_HOUR;
			m = temp;
		}
		if (h < 0 || h >= HOURS_PER_DAY) {
			h = (h % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
		}
		return make(h, m, s, n);
	}

	/**
	 * Create a instance of the {@link Time} from the milliseconds of the day.
	 * 
	 * @param millisOfDay MilliSeconds in the day.
	 * @return the result time
	 */
	public static Time fromMillisOfDay(int millisOfDay) {
		long nanosOfDay = ((long) millisOfDay) * NANOS_PER_MILLIS;
		return fromNanosOfDay(nanosOfDay);
	}

	/**
	 * Create a instance of the {@link Time} from the nanoseconds of the day.
	 * 
	 * @param nanosOfDay NanoSeconds in the day.
	 * @return the result time
	 */
	public static Time fromNanosOfDay(long nanosOfDay) {
		long ns = nanosOfDay;
		if (ns < 0 || ns >= NANOS_PER_DAY)
			ns = (nanosOfDay % NANOS_PER_DAY + NANOS_PER_DAY) % NANOS_PER_DAY;
		int n = (int) (ns % NANOS_PER_SECOND);
		int t = (int) (ns / NANOS_PER_SECOND);
		int s = t % SECONDS_PER_MINUTE;
		t = t / SECONDS_PER_MINUTE;
		int m = t % MINUTES_PER_HOUR;
		t = t / MINUTES_PER_HOUR;
		int h = t;
		return make(h, m, s, n);
	}

	/**
	 * Make an {@link Time} which will try to get from {@link #HOURS} cache.
	 * Values previously validated.
	 * 
	 * @param hour       hour-of-day field
	 * @param minute     minute-of-hour field
	 * @param second     second-of-minute field
	 * @param nanoSecond nanosecond-of-second field
	 * @return a time
	 */
	private static Time make(int hour, int minute, int second, int nanoSecond) {
		if ((minute | second | nanoSecond) == 0)
			return HOURS[hour];
		return new Time(hour, minute, second, nanoSecond);
	}

	/**
	 * The hour of the time. The value if from 0 to 23.
	 */
	private final byte hour;

	/**
	 * The minute of the time. The value if from 0 to 59.
	 */
	private final byte minute;

	/**
	 * The second of the time. The value if from 0 to 50.
	 */
	private final byte second;

	/**
	 * The nanoSecond of the time. The value if from 0 to 999,999,999.
	 */
	private final int nanoSecond;

	/**
	 * Constructor, previously validated.
	 *
	 * @param hour       the hour-of-day to represent, validated from 0 to 23
	 * @param minute     the minute-of-hour to represent, validated from 0 to 59
	 * @param second     the second-of-minute to represent, validated from 0 to 59
	 * @param nanoSecond the nanosecond-of-second to represent, validated from 0 to
	 *                   999,999,999
	 */
	private Time(int hour, int minute, int second, int nanoSecond) {
		super();
		this.hour = (byte) hour;
		this.minute = (byte) minute;
		this.second = (byte) second;
		this.nanoSecond = nanoSecond;
	}

	/**
	 * To get the hour-of-day field.
	 *
	 * @return the hour-of-day, from 0 to 23
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * To get the minute-of-hour field.
	 *
	 * @return the minute-of-hour, from 0 to 59
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * To get the second-of-minute field.
	 *
	 * @return the second-of-minute, from 0 to 59
	 */
	public int getSecond() {
		return second;
	}

	/**
	 * To get the millisecond-of-second field.
	 *
	 * @return the millisecond-of-second, from 0 to 999
	 */
	public int getMillisOfSecond() {
		return nanoSecond / 1_000_000;
	}

	/**
	 * To get the nanosecond-of-second field.
	 *
	 * @return the nanosecond-of-second
	 */
	public int getNanosOfSecond() {
		return nanoSecond;
	}

	/**
	 * To get the second-of-day field, from {@code 0} to {@code 24 * 60 * 60 - 1}.
	 *
	 * @return the second-of-day
	 */
	public int getSecondOfDay() {
		int t = this.hour;
		t = t * MINUTES_PER_HOUR + minute;
		t = t * SECONDS_PER_MINUTE + second;
		return t;
	}

	/**
	 * To get the millisecond-of-day field, from {@code 0} to
	 * {@code 24 * 60 * 60 * 1,000 - 1}.
	 *
	 * @return the millisecond-of-day
	 */
	public int getMillisOfDay() {
		int t = this.hour;
		t = t * MINUTES_PER_HOUR + minute;
		t = t * SECONDS_PER_MINUTE + second;
		t = t * MILLIS_PER_SECOND + nanoSecond / 1_000_000;
		return t;
	}

	/**
	 * To get the nanosecond-of-day field, from {@code 0} to
	 * {@code 24 * 60 * 60 * 1,000,000,000 - 1}.
	 *
	 * @return the nanosecond-of-day equivalent to this time
	 */
	public long getNanosOfDay() {
		long t = this.hour;
		t = t * MINUTES_PER_HOUR + minute;
		t = t * SECONDS_PER_MINUTE + second;
		t = t * NANOS_PER_SECOND + nanoSecond;
		return t;
	}

	/**
	 * Reset the hour-of-day field value.
	 * 
	 * @param hour the hour-of-day field value to reset
	 * @return the time result
	 */
	public Time resetHour(int hour) {
		if (hour == this.hour)
			return this;
		if (hour < 0 || hour >= HOURS_PER_DAY)
			throw new IllegalArgumentException("hour-of-day field out of range");
		return make(hour, minute, second, nanoSecond);
	}

	/**
	 * Reset the minute-of-hour field value.
	 * 
	 * @param minute the minute-of-hour field value to reset
	 * @return the time result
	 */
	public Time resetMinute(int minute) {
		if (minute == this.minute)
			return this;
		if (minute < 0 || minute >= MINUTES_PER_HOUR)
			throw new IllegalArgumentException("minute-of-hour field out of range");
		return make(hour, minute, second, nanoSecond);
	}

	/**
	 * Reset the second-of-minute field value.
	 * 
	 * @param second the second-of-minute field value to reset
	 * @return the time result
	 */
	public Time resetSecond(int second) {
		if (second == this.second)
			return this;
		if (second < 0 || second >= SECONDS_PER_MINUTE)
			throw new IllegalArgumentException("second-of-minute field out of range");
		return make(hour, minute, second, nanoSecond);
	}

	/**
	 * Reset the millisecond-of-second field value.
	 * 
	 * @param millisOfSecond the millisecond-of-second field value to reset
	 * @return the time result
	 */
	public Time resetMillisOfSecond(int millisOfSecond) {
		if (millisOfSecond < 0 || millisOfSecond >= MILLIS_PER_SECOND)
			throw new IllegalArgumentException("millisecond-of-second field out of range");
		int nanosOfSecond = NANOS_PER_MILLIS * millisOfSecond;
		if (nanosOfSecond == this.nanoSecond)
			return this;
		return make(hour, minute, second, nanosOfSecond);
	}

	/**
	 * Reset the nanosecond-of-second field value.
	 * 
	 * @param nanosOfSecond the nanosecond-of-second field value to reset
	 * @return the time result
	 */
	public Time resetNanosOfSecond(int nanosOfSecond) {
		if (nanosOfSecond == this.nanoSecond)
			return this;
		if (nanosOfSecond < 0 || nanosOfSecond >= NANOS_PER_SECOND)
			throw new IllegalArgumentException("nanosecond-of-second field out of range");
		return make(hour, minute, second, nanosOfSecond);
	}

	/**
	 * Add some hours.
	 * 
	 * @param hours the hour count to add
	 * @return the time result
	 */
	public Time addHours(int hours) {
		if (hours < 0 || hours >= HOURS_PER_DAY)
			hours = (hours % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
		if (hours == 0)
			return this;
		return from(hours + hour, minute, second, nanoSecond);
	}

	/**
	 * Add some minutes.
	 * 
	 * @param minutes the minute count to add
	 * @return the time result
	 */
	public Time addMinutes(int minutes) {
		int minutesPerDay = MINUTES_PER_HOUR * HOURS_PER_DAY;
		if (minutes < 0 || minutes >= minutesPerDay)
			minutes = (minutes % minutesPerDay + minutesPerDay) % minutesPerDay;
		if (minutes == 0)
			return this;
		return from(hour, minute + minutes, second, nanoSecond);
	}

	/**
	 * Add some seconds.
	 * 
	 * @param seconds the second count to add
	 * @return the time result
	 */
	public Time addSeconds(int seconds) {
		if (seconds < 0 || seconds >= SECONDS_PER_DAY)
			seconds = (seconds % SECONDS_PER_DAY + SECONDS_PER_DAY) % SECONDS_PER_DAY;
		if (seconds == 0)
			return this;
		return from(hour, minute, second + seconds, nanoSecond);
	}

	/**
	 * Add some milliSeconds.
	 * 
	 * @param milliSeconds the milliSecond count to add
	 * @return the time result
	 */
	public Time addMilliSeconds(int milliSeconds) {
		if (milliSeconds < 0 || milliSeconds >= MILLIS_PER_DAY)
			milliSeconds = (milliSeconds % MILLIS_PER_DAY + MILLIS_PER_DAY) % MILLIS_PER_DAY;
		if (milliSeconds == 0)
			return this;
		long nanosOfDay = getNanosOfDay() + (long) milliSeconds * NANOS_PER_MILLIS;
		return fromNanosOfDay(nanosOfDay);
	}

	/**
	 * Add some nanoSeconds.
	 * 
	 * @param nanoSeconds the nanoSecond count to add
	 * @return the time result
	 */
	public Time addNanoSeconds(long nanoSeconds) {
		if (nanoSeconds < 0 || nanoSeconds >= NANOS_PER_DAY)
			nanoSeconds = (nanoSeconds % NANOS_PER_DAY + NANOS_PER_DAY) % NANOS_PER_DAY;
		if (nanoSeconds == 0)
			return this;
		long nanosOfDay = getNanosOfDay() + nanoSeconds;
		return fromNanosOfDay(nanosOfDay);
	}

	/**
	 * Compare the {@link Time time} with another one. A positive value means
	 * this {@link Time time} is later than another one; A negative value means
	 * this {@link Time time} is earlier than another one; A zero value means
	 * the both {@link Time times} are a same time point.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param other the other time to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(Time other) {
		// Fast compare if same time
		if (this == other)
			return 0;
		// Compare hour
		if (this.hour != other.hour)
			return this.hour > other.hour ? 1 : -1;
		// Compare minute
		if (this.minute != other.minute)
			return this.minute > other.minute ? 1 : -1;
		// Compare second
		if (this.second != other.second)
			return this.second > other.second ? 1 : -1;
		// Compare nanoSecond
		if (this.nanoSecond != other.nanoSecond)
			return this.nanoSecond > other.nanoSecond ? 1 : -1;
		return 0;
	}

	/**
	 * A hash code for this time.
	 *
	 * @see java.lang.Object#hashCode()
	 * @return a suitable hash code
	 */
	@Override
	public int hashCode() {
		int h = hour; // 0-23 (2^5-1)
		int m = minute; // 0-59 (2^6-1)
		int s = second; // 0-59 (2^6-1)
		int n = nanoSecond; // 0-999,999,999 (2^30-1)
		return (h + (m << 5) + (s << 11) + (n << 17)) ^ ((n << 2) & 0Xfffe0000);
	}

	/**
	 * Checks if this time is equal to another time.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj the object to check, null returns false
	 * @return true if this is equal to the other time
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time other = (Time) obj;
		return this.hour == other.hour && this.minute == other.minute && this.second == other.second
				&& this.nanoSecond == other.nanoSecond;
	}

    /**
	 * Outputs this time as a {@code String}, such as {@code 12:34:56}.
	 * <p>
	 * The output will be one of the following ISO-8601 formats:
	 * <ul>
	 * <li>{@code HH:mm:ss}</li>
	 * <li>{@code HH:mm:ss.SSS}</li>
	 * <li>{@code HH:mm:ss.SSSSSS}</li>
	 * <li>{@code HH:mm:ss.SSSSSSSSS}</li>
	 * </ul>
	 * The format used will be the shortest that outputs the full value of the time
	 * where the omitted parts are implied to be zero.
	 *
	 * @see java.lang.Object#toString()
	 * @return a string representation of this time
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(18);
		// Hour
		if (hour < 10)
			sb.append('0');
		sb.append(hour);
		// Minute
		sb.append(':');
		if (minute < 10)
			sb.append('0');
		sb.append(minute);
		// Second
		sb.append(':');
		if (second < 10)
			sb.append('0');
		sb.append(second);
		// NanoSecond
		if (nanoSecond > 0) {
			sb.append('.');
			if (nanoSecond % 1_000_000 == 0) {
				if (nanoSecond >= 100_000_000)
					sb.append(nanoSecond / 1_000_000);
				else
					sb.append(100 + nanoSecond / 1_000_000).setCharAt(sb.length() - 3, '0');
			} else if (nanoSecond % 1_000 == 0) {
				if (nanoSecond >= 100_000_000)
					sb.append(nanoSecond / 1_000);
				else
					sb.append(100_000 + nanoSecond / 1_000).setCharAt(sb.length() - 6, '0');
			} else {
				if (nanoSecond >= 100_000_000)
					sb.append(nanoSecond);
				else
					sb.append(100_000_000 + nanoSecond).setCharAt(sb.length() - 9, '0');
			}
		}
		return sb.toString();
	}

	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = -4241235020675199017L;

}
