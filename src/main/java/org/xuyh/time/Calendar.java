/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

/**
 * {@link Calendar} is an immutable calendar object that represents both date
 * and time, as well as time-zone, often viewed as
 * year-month-day-hour-minute-second. The bean is represented to nanosecond
 * precision.
 * <p>
 * 
 * @author XuYanhang
 * @since 2020-08-13
 *
 */
public final class Calendar implements java.io.Serializable, Comparable<Calendar> {

	/**
	 * The minimum year value allowed for the year field.
	 */
	public final static int YEAR_MIN = Date.YEAR_MIN;

	/**
	 * The maximum year value allowed for the year field.
	 */
	public final static int YEAR_MAX = Date.YEAR_MAX;

	/**
	 * Value of the month-of-year field indicating the first month of the year.
	 */
	public final static int JANUARY = Date.JANUARY;

	/**
	 * Value of the month-of-year field indicating the second month of the year.
	 */
	public final static int FEBRUARY = Date.FEBRUARY;

	/**
	 * Value of the month-of-year field indicating the third month of the year.
	 */
	public final static int MARCH = Date.MARCH;

	/**
	 * Value of the month-of-year field indicating the fourth month of the year.
	 */
	public final static int APRIL = Date.APRIL;

	/**
	 * Value of the month-of-year field indicating the fifth month of the year.
	 */
	public final static int MAY = Date.MAY;

	/**
	 * Value of the month-of-year field indicating the sixth month of the year.
	 */
	public final static int JUNE = Date.JUNE;

	/**
	 * Value of the month-of-year field indicating the seventh month of the year.
	 */
	public final static int JULY = Date.JULY;

	/**
	 * Value of the month-of-year field indicating the eighth month of the year.
	 */
	public final static int AUGUST = Date.AUGUST;

	/**
	 * Value of the month-of-year field indicating the ninth month of the year.
	 */
	public final static int SEPTEMBER = Date.SEPTEMBER;

	/**
	 * Value of the month-of-year field indicating the tenth month of the year.
	 */
	public final static int OCTOBER = Date.OCTOBER;

	/**
	 * Value of the month-of-year field indicating the eleventh month of the year.
	 */
	public final static int NOVEMBER = Date.NOVEMBER;

	/**
	 * Value of the month-of-year field indicating the twelfth month of the year.
	 */
	public final static int DECEMBER = Date.DECEMBER;

	/**
	 * Value of the day-of-week field indicating Sunday.
	 */
	public final static int SUNDAY = Date.SUNDAY;

	/**
	 * Value of the day-of-week field indicating Monday.
	 */
	public final static int MONDAY = Date.MONDAY;

	/**
	 * Value of the day-of-week field indicating Tuesday.
	 */
	public final static int TUESDAY = Date.TUESDAY;

	/**
	 * Value of the day-of-week field indicating Wednesday.
	 */
	public final static int WEDNESDAY = Date.WEDNESDAY;

	/**
	 * Value of the day-of-week field indicating Thursday.
	 */
	public final static int THURSDAY = Date.THURSDAY;

	/**
	 * Value of the day-of-week field indicating Friday.
	 */
	public final static int FRIDAY = Date.FRIDAY;

	/**
	 * Value of the day-of-week field indicating Saturday.
	 */
	public final static int SATURDAY = Date.SATURDAY;

	/**
	 * Hours per day.
	 */
	public static final int HOURS_PER_DAY = Time.HOURS_PER_DAY;

	/**
	 * Minutes per hour.
	 */
	public static final int MINUTES_PER_HOUR = Time.MINUTES_PER_HOUR;

	/**
	 * Seconds per minute.
	 */
	public static final int SECONDS_PER_MINUTE = Time.SECONDS_PER_MINUTE;

	/**
	 * Seconds per hour.
	 */
	public static final int SECONDS_PER_HOUR = Time.SECONDS_PER_HOUR;

	/**
	 * Seconds per day.
	 */
	public static final int SECONDS_PER_DAY = Time.SECONDS_PER_DAY;

	/**
	 * Milliseconds per second.
	 */
	public static final int MILLIS_PER_SECOND = Time.MILLIS_PER_SECOND;

	/**
	 * Milliseconds per hour.
	 */
	public static final int MILLIS_PER_HOUR = Time.MILLIS_PER_HOUR;

	/**
	 * Milliseconds per day.
	 */
	public static final int MILLIS_PER_DAY = Time.MILLIS_PER_DAY;

	/**
	 * Nanoseconds per second.
	 */
	public static final int NANOS_PER_SECOND = Time.NANOS_PER_SECOND;

	/**
	 * Nanoseconds per millisecond.
	 */
	public static final int NANOS_PER_MILLIS = Time.NANOS_PER_MILLIS;

	/**
	 * Nanoseconds per day.
	 */
	public static final long NANOS_PER_DAY = Time.NANOS_PER_DAY;

	/**
	 * Create a instance of the {@link Calendar} for current date-time of a
	 * zone-offset.
	 * 
	 * @param zoneOffset the zone-offset field
	 * @return the result calendar
	 */
	public static Calendar now(ZoneOffset zoneOffset) {
		return fromEpochMillis(System.currentTimeMillis(), zoneOffset);
	}

	/**
	 * Create a instance of the {@link Calendar} from these date-time fields the
	 * zone-offset. The result instance represented to second precision.
	 * 
	 * @param year       the year field
	 * @param month      the month-of-year field
	 * @param dayOfMonth the day-of-month field
	 * @param hour       the hour-of-day field
	 * @param minute     the minute-of-hour field
	 * @param second     the second-of-minute field
	 * @param zoneOffset the zone-offset field
	 * @return the result calendar
	 */
	public static Calendar from(int year, int month, int dayOfMonth, int hour, int minute, int second,
			ZoneOffset zoneOffset) {
		return from(year, month, dayOfMonth, hour, minute, second, 0, zoneOffset);
	}

	/**
	 * Create a instance of the {@link Calendar} from all the date-time fields the
	 * zone-offset. The result instance represented to nanosecond precision.
	 * 
	 * @param year          the year field
	 * @param month         the month-of-year field
	 * @param dayOfMonth    the day-of-month field
	 * @param hour          the hour-of-day field
	 * @param minute        the minute-of-hour field
	 * @param second        the second-of-minute field
	 * @param nanosOfSecond the nanosecond-of-second field
	 * @param zoneOffset    the zone-offset field
	 * @return the result calendar
	 */
	public static Calendar from(int year, int month, int dayOfMonth, int hour, int minute, int second,
			int nanosOfSecond, ZoneOffset zoneOffset) {
		if (null == zoneOffset)
			throw new NullPointerException("zoneOffset");
		int h = hour;
		int m = minute;
		int s = second;
		int n = nanosOfSecond;
		int d = dayOfMonth;
		int temp;
		if (h < 0 || h >= HOURS_PER_DAY) {
			temp = (h % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
			d = Math.addExact(d, (h - temp) / HOURS_PER_DAY);
			h = temp;
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
			temp = (h % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
			d = Math.addExact(d, (h - temp) / HOURS_PER_DAY);
			h = temp;
		}
		Date date = Date.from(year, month, d);
		Time time = Time.from(h, m, s, n);
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Create a instance of the {@link Calendar} from the epoch seconds,
	 * nanosecond-of-second and the zone-offset. The result instance represented to
	 * nanosecond precision.
	 * 
	 * @param epochSeconds  the number of second from the epoch of
	 *                      1970-01-01T00:00:00+0000
	 * @param nanosOfSecond the nanosecond-of-second field value
	 * @param zoneOffset    the zone-offset part of the calendar, null not allowed
	 * @return the result calendar
	 */
	public static Calendar fromEpochSeconds(long epochSeconds, int nanosOfSecond, ZoneOffset zoneOffset) {
		if (null == zoneOffset)
			throw new NullPointerException("zoneOffset");
		if (nanosOfSecond < 0 || nanosOfSecond >= NANOS_PER_SECOND) {
			int temp = (nanosOfSecond % NANOS_PER_SECOND + NANOS_PER_SECOND) % NANOS_PER_SECOND;
			epochSeconds = Math.addExact(epochSeconds, (long) (nanosOfSecond - temp) / NANOS_PER_SECOND);
			nanosOfSecond = temp;
		}
		epochSeconds += zoneOffset.getTotalSeconds();
		long epochDays = epochSeconds / SECONDS_PER_DAY;
		long secondsOfDay = epochSeconds - epochDays * SECONDS_PER_DAY;
		if (secondsOfDay < 0) {
			epochDays--;
			secondsOfDay += SECONDS_PER_DAY;
		}
		Date date = Date.fromEpochDays(epochDays);
		Time time = Time.fromNanosOfDay(secondsOfDay * NANOS_PER_SECOND + nanosOfSecond);
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Create a instance of the {@link Calendar} from the epoch milliseconds and the
	 * zone-offset. The result instance represented to millisecond precision.
	 * 
	 * @param epochMillis the number of milliseconds from the epoch of
	 *                    1970-01-01T00:00:00.000+0000
	 * @param zoneOffset  the zone-offset part of the calendar, null not allowed
	 * @return the result calendar
	 */
	public static Calendar fromEpochMillis(long epochMillis, ZoneOffset zoneOffset) {
		if (null == zoneOffset)
			throw new NullPointerException("zoneOffset");
		epochMillis += zoneOffset.getTotalSeconds() * MILLIS_PER_SECOND;
		long epochDays = epochMillis / MILLIS_PER_DAY;
		long millisOfDay = epochMillis - epochDays * MILLIS_PER_DAY;
		if (millisOfDay < 0) {
			epochDays--;
			millisOfDay += MILLIS_PER_DAY;
		}
		Date date = Date.fromEpochDays(epochDays);
		Time time = Time.fromNanosOfDay(millisOfDay * NANOS_PER_MILLIS);
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Create a instance of the {@link Calendar} from the date, time and zone-offset
	 * parts.
	 * 
	 * @param date       the date part of the calendar, null not allowed
	 * @param time       the time part of the calendar, null not allowed
	 * @param zoneOffset the zone-offset part of the calendar, null not allowed
	 * @return the result calendar
	 */
	public static Calendar from(Date date, Time time, ZoneOffset zoneOffset) {
		if (null == date || null == time || null == zoneOffset)
			throw new NullPointerException();
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * The date part of the calendar
	 */
	private final Date date;

	/**
	 * The time part of the calendar
	 */
	private final Time time;

	/**
	 * The zone-offset of the calendar
	 */
	private final ZoneOffset zoneOffset;

	/**
	 * Constructor, previously validated.
	 * 
	 * @param date       the date part of the calendar, validated not null
	 * @param time       the time part of the calendar, validated not null
	 * @param zoneOffset the zone-offset part of the calendar, validated not null
	 */
	private Calendar(Date date, Time time, ZoneOffset zoneOffset) {
		super();
		this.date = date;
		this.time = time;
		this.zoneOffset = zoneOffset;
	}

	/**
	 * Analyze if the year is leap year.
	 * <p>
	 * For year B.C., the year value is the negative show value plus one. For
	 * example, for year 1BC then the year is 0.
	 * <p>
	 * One year is leap year when:<br />
	 * 1. 1BC is leap year. <br />
	 * 2. A year could be divisible by 4 but 100 is a leap year. <br />
	 * 3. A year could be divisible by 400 but 3200 is a leap year. <br />
	 * 4. Each in others years is not a leap year. <br />
	 * 
	 * @return If the year is a leap year
	 */
	public boolean isLeapYear() {
		return date.isLeapYear();
	}

	/**
	 * To get the year field.
	 * 
	 * @return the year, a primitive {@code int} value.
	 */
	public int getYear() {
		return date.getYear();
	}

	/**
	 * To get the month-of-year field.
	 * 
	 * @return the month-of-year, a primitive {@code int} value from 1 to 12.
	 */
	public int getMonth() {
		return date.getMonth();
	}

	/**
	 * To get the day-of-month field.
	 * 
	 * @return the day-of-month, a primitive {@code int} value for the day-of-month
	 *         from 1 to 31.
	 */
	public int getDayOfMonth() {
		return date.getDayOfMonth();
	}

	/**
	 * To get the day-of-year field.
	 * 
	 * @return the day-of-year, a primitive {@code int} value for the day-of-year
	 *         from 1 to 365, or 366 in a leap year.
	 */
	public int getDayOfYear() {
		return date.getDayOfYear();
	}

	/**
	 * To get the week-of-year field.
	 * 
	 * @return the week-of-year, a primitive {@code int} value for the week-of-year
	 *         from 1 to 54.
	 */
	public int getWeekOfYear() {
		return date.getWeekOfYear();
	}

	/**
	 * To get the week-of-month field.
	 * 
	 * @return the week-of-month, a primitive {@code int} value for the
	 *         week-of-month from 1 to 6.
	 */
	public int getWeekOfMonth() {
		return date.getWeekOfMonth();
	}

	/**
	 * To get the day-of-week field.
	 * <p>
	 * The calculate method is a change method based on Kim larsson calculation
	 * formula. <br>
	 * 1. The origin formula: W=(d+2*m+3*(m+1)/5+y+y/4-y/100+y/400+1)%7 <br>
	 * 2. We consider a year could be divisible by 3200 as not a leap year <br>
	 * 
	 * @see #isLeapYear()
	 * @return the day-of-week, a primitive {@code int} value for the day-of-week
	 *         from 0(Sunday) to 6(Saturday).
	 */
	public int getDayOfWeek() {
		return date.getDayOfWeek();
	}

	/**
	 * To get the hour-of-day field.
	 *
	 * @return the hour-of-day, from 0 to 23
	 */
	public int getHour() {
		return time.getHour();
	}

	/**
	 * To get the minute-of-hour field.
	 *
	 * @return the minute-of-hour, from 0 to 59
	 */
	public int getMinute() {
		return time.getMinute();
	}

	/**
	 * To get the second-of-minute field.
	 *
	 * @return the second-of-minute, from 0 to 59
	 */
	public int getSecond() {
		return time.getSecond();
	}

	/**
	 * To get the millisecond-of-second field.
	 *
	 * @return the millisecond-of-second, from 0 to 999
	 */
	public int getMillisOfSecond() {
		return time.getMillisOfSecond();
	}

	/**
	 * To get the nanosecond-of-second field.
	 *
	 * @return the nanosecond-of-second
	 */
	public int getNanosOfSecond() {
		return time.getNanosOfSecond();
	}

	/**
	 * To get the second-of-day field, from {@code 0} to {@code 24 * 60 * 60 - 1}.
	 *
	 * @return the second-of-day
	 */
	public int getSecondOfDay() {
		return time.getSecondOfDay();
	}

	/**
	 * To get the millisecond-of-day field, from {@code 0} to
	 * {@code 24 * 60 * 60 * 1,000 - 1}.
	 *
	 * @return the millisecond-of-day
	 */
	public int getMillisOfDay() {
		return time.getMillisOfDay();
	}

	/**
	 * To get the nanosecond-of-day field, from {@code 0} to
	 * {@code 24 * 60 * 60 * 1,000,000,000 - 1}.
	 *
	 * @return the nanosecond-of-day
	 */
	public long getNanosOfDay() {
		return time.getNanosOfDay();
	}

	/**
	 * Gets the zone offset, such as '+0800'. It's the offset of the date-time from
	 * UTC/Greenwich.
	 * 
	 * @return the zoneOffset
	 */
	public ZoneOffset getZoneOffset() {
		return zoneOffset;
	}

	/**
	 * Reset the year field value.
	 * 
	 * @param year the year field value to reset
	 * @return the calendar result
	 */
	public Calendar resetYear(int year) {
		Date date = this.date.resetYear(year);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the month-of-year field value.
	 * 
	 * @param month the month-of-year field value to reset
	 * @return the calendar result
	 */
	public Calendar resetMonth(int month) {
		Date date = this.date.resetMonth(month);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the day-of-month field value.
	 * 
	 * @param dayOfMonth the day-of-month field value to reset
	 * @return the calendar result
	 */
	public Calendar resetDayOfMonth(int dayOfMonth) {
		Date date = this.date.resetDayOfMonth(dayOfMonth);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the day-of-year field value.
	 * 
	 * @param dayOfYear the day-of-year field value to reset
	 * @return the calendar result
	 */
	public Calendar resetDayOfYear(int dayOfYear) {
		Date date = this.date.resetDayOfYear(dayOfYear);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the day-of-week field value.
	 * 
	 * @param dayOfWeek the day-of-week field value to reset
	 * @return the calendar result
	 */
	public Calendar resetDayOfWeek(int dayOfWeek) {
		Date date = this.date.resetDayOfWeek(dayOfWeek);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the hour-of-day field value.
	 * 
	 * @param hour the hour-of-day field value to reset
	 * @return the calendar result
	 */
	public Calendar resetHour(int hour) {
		Time time = this.time.resetHour(hour);
		if (time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the minute-of-hour field value.
	 * 
	 * @param minute the minute-of-hour field value to reset
	 * @return the calendar result
	 */
	public Calendar resetMinute(int minute) {
		Time time = this.time.resetMinute(minute);
		if (time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the second-of-minute field value.
	 * 
	 * @param second the second-of-minute field value to reset
	 * @return the calendar result
	 */
	public Calendar resetSecond(int second) {
		Time time = this.time.resetSecond(second);
		if (time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the millisecond-of-second field value.
	 * 
	 * @param millisOfSecond the millisecond-of-second field value to reset
	 * @return the calendar result
	 */
	public Calendar resetMillisOfSecond(int millisOfSecond) {
		Time time = this.time.resetMillisOfSecond(millisOfSecond);
		if (time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the nanosecond-of-second field value.
	 * 
	 * @param nanosOfSecond the nanosecond-of-second field value to reset
	 * @return the calendar result
	 */
	public Calendar resetNanosOfSecond(int nanosOfSecond) {
		Time time = this.time.resetNanosOfSecond(nanosOfSecond);
		if (time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Reset the zoneOffset field.
	 * 
	 * @param zoneOffset the zoneOffset
	 * @return the calendar result
	 */
	public Calendar resetZoneOffset(ZoneOffset zoneOffset) {
		if (zoneOffset.equals(this.zoneOffset))
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some years.
	 * 
	 * @param years the year count to add
	 * @return the calendar result
	 */
	public Calendar addYears(int years) {
		Date date = this.date.addYears(years);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some months.
	 * 
	 * @param months the month count to add
	 * @return the calendar result
	 */
	public Calendar addMonths(long months) {
		Date date = this.date.addMonths(months);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some days.
	 * 
	 * @param days the day count to add
	 * @return the calendar result
	 */
	public Calendar addDays(long days) {
		Date date = this.date.addDays(days);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some weeks.
	 * 
	 * @param weeks the week count to add
	 * @return the calendar result
	 */
	public Calendar addWeeks(long weeks) {
		Date date = this.date.addWeeks(weeks);
		if (date == this.date)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some hours.
	 * 
	 * @param hours the hour count to add
	 * @return the calendar result
	 */
	public Calendar addHours(long hours) {
		if (hours == 0L)
			return this;
		long days = 0L;
		if (hours < 0 || hours >= HOURS_PER_DAY) {
			long temp = (hours % HOURS_PER_DAY + HOURS_PER_DAY) % HOURS_PER_DAY;
			days = Math.subtractExact(hours, temp) / HOURS_PER_DAY;
			hours = temp;
		}
		Time time = this.time.addHours((int) hours);
		if (time.compareTo(this.time) < 0)
			days = Math.incrementExact(days);
		Date date = this.date.addDays(days);
		if (date == this.date && time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some minutes.
	 * 
	 * @param minutes the minute count to add
	 * @return the calendar result
	 */
	public Calendar addMinutes(long minutes) {
		if (minutes == 0L)
			return this;
		long days = 0L;
		int minutesPerDay = MINUTES_PER_HOUR * HOURS_PER_DAY;
		if (minutes < 0 || minutes >= minutesPerDay) {
			long temp = (minutes % minutesPerDay + minutesPerDay) % minutesPerDay;
			days = Math.subtractExact(minutes, temp) / minutesPerDay;
			minutes = temp;
		}
		Time time = this.time.addMinutes((int) minutes);
		if (time.compareTo(this.time) < 0)
			days = Math.incrementExact(days);
		Date date = this.date.addDays(days);
		if (date == this.date && time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some seconds.
	 * 
	 * @param seconds the second count to add
	 * @return the calendar result
	 */
	public Calendar addSeconds(long seconds) {
		if (seconds == 0L)
			return this;
		long days = 0L;
		if (seconds < 0 || seconds >= SECONDS_PER_DAY) {
			long temp = (seconds % SECONDS_PER_DAY + SECONDS_PER_DAY) % SECONDS_PER_DAY;
			days = Math.subtractExact(seconds, temp) / SECONDS_PER_DAY;
			seconds = temp;
		}
		Time time = this.time.addSeconds((int) seconds);
		if (time.compareTo(this.time) < 0)
			days = Math.incrementExact(days);
		Date date = this.date.addDays(days);
		if (date == this.date && time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some milliSeconds.
	 * 
	 * @param milliSeconds the milliSecond count to add
	 * @return the calendar result
	 */
	public Calendar addMilliSeconds(long milliSeconds) {
		if (milliSeconds == 0L)
			return this;
		long days = 0L;
		if (milliSeconds < 0 || milliSeconds >= MILLIS_PER_DAY) {
			long temp = (milliSeconds % MILLIS_PER_DAY + MILLIS_PER_DAY) % MILLIS_PER_DAY;
			days = Math.subtractExact(milliSeconds, temp) / MILLIS_PER_DAY;
			milliSeconds = temp;
		}
		Time time = this.time.addMilliSeconds((int) milliSeconds);
		if (time.compareTo(this.time) < 0)
			days = Math.incrementExact(days);
		Date date = this.date.addDays(days);
		if (date == this.date && time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Add some nanoSeconds.
	 * 
	 * @param nanoSeconds the nanoSecond count to add
	 * @return the calendar result
	 */
	public Calendar addNanoSeconds(long nanoSeconds) {
		if (nanoSeconds == 0L)
			return this;
		long days = 0L;
		if (nanoSeconds < 0 || nanoSeconds >= NANOS_PER_DAY) {
			long temp = (nanoSeconds % NANOS_PER_DAY + NANOS_PER_DAY) % NANOS_PER_DAY;
			days = Math.subtractExact(nanoSeconds, temp) / NANOS_PER_DAY;
			nanoSeconds = temp;
		}
		Time time = this.time.addNanoSeconds(nanoSeconds);
		if (time.compareTo(this.time) < 0)
			days = Math.incrementExact(days);
		Date date = this.date.addDays(days);
		if (date == this.date && time == this.time)
			return this;
		return new Calendar(date, time, zoneOffset);
	}

	/**
	 * Converts this date-time to the number of seconds from the epoch of
	 * 1970-01-01T00:00:00+0000.
	 * 
	 * @return the number of seconds from the epoch of 1970-01-01T00:00:00+0000
	 */
	public long toEpochSeconds() {
		return date.toEpochDays() * SECONDS_PER_DAY + time.getSecondOfDay() - zoneOffset.getTotalSeconds();
	}

	/**
	 * Converts this date-time to the number of milliseconds from the epoch of
	 * 1970-01-01T00:00:00.000+0000.
	 * 
	 * @return the number of milliseconds from the epoch of
	 *         1970-01-01T00:00:00.000+0000
	 */
	public long toEpochMillis() {
		return toEpochSeconds() * MILLIS_PER_SECOND + time.getNanosOfSecond() / NANOS_PER_MILLIS;
	}

	/**
	 * Compare the {@link Calendar date-time} with another one. A positive value
	 * means this {@link Calendar date-time} is later than another one; A negative
	 * value means this {@link Calendar date-time} is earlier than another one; A
	 * zero value means the both {@link Calendar date-times} are a same time point.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Calendar other) {
		// Fast compare if same instance
		if (this == other)
			return 0;
		// Compare in seconds
		int c = Long.compare(this.toEpochSeconds(), other.toEpochSeconds());
		// Compare on nanoseconds
		if (c == 0)
			c = Integer.compare(this.time.getNanosOfSecond(), other.time.getNanosOfSecond());
		return c;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return date.hashCode() ^ time.hashCode() ^ zoneOffset.hashCode();
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
		Calendar other = (Calendar) obj;
		return this.date.equals(other.date) && this.time.equals(other.time) && this.zoneOffset.equals(other.zoneOffset);
	}

	/**
	 * Outputs this date-time as a {@code String}, such as
	 * {@code 1970-01-23T12:34:56.123456789+0800}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return date.toString() + 'T' + time.toString() + zoneOffset.toString();
	}

	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = -7583098833409126882L;
}
