/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.time;

/**
 * The {@link Date} is an immutable date-time object that represents a date,
 * often viewed as year-month-day. The field day-of-week can also be accessed.
 * For example, the value "23th January 1970" can be stored in an {@link Date}.
 * <p>
 * This class does not store or represent a time or time-zone. Instead, it is a
 * description of the date, as used for birthdays.
 * <p>
 * 
 * @author XuYanhang
 * @since 2020-08-13
 *
 */
public final class Date implements java.io.Serializable, Cloneable, Comparable<Date> {

	/**
	 * The minimum year value allowed for the year field.
	 */
	public final static int YEAR_MIN = -999_999_999;

	/**
	 * The maximum year value allowed for the year field.
	 */
	public final static int YEAR_MAX = 999_999_999;

	/**
	 * Value of the month-of-year field indicating the first month of the year.
	 */
	public final static int JANUARY = 1;

	/**
	 * Value of the month-of-year field indicating the second month of the year.
	 */
	public final static int FEBRUARY = 2;

	/**
	 * Value of the month-of-year field indicating the third month of the year.
	 */
	public final static int MARCH = 3;

	/**
	 * Value of the month-of-year field indicating the fourth month of the year.
	 */
	public final static int APRIL = 4;

	/**
	 * Value of the month-of-year field indicating the fifth month of the year.
	 */
	public final static int MAY = 5;

	/**
	 * Value of the month-of-year field indicating the sixth month of the year.
	 */
	public final static int JUNE = 6;

	/**
	 * Value of the month-of-year field indicating the seventh month of the year.
	 */
	public final static int JULY = 7;

	/**
	 * Value of the month-of-year field indicating the eighth month of the year.
	 */
	public final static int AUGUST = 8;

	/**
	 * Value of the month-of-year field indicating the ninth month of the year.
	 */
	public final static int SEPTEMBER = 9;

	/**
	 * Value of the month-of-year field indicating the tenth month of the year.
	 */
	public final static int OCTOBER = 10;

	/**
	 * Value of the month-of-year field indicating the eleventh month of the year.
	 */
	public final static int NOVEMBER = 11;

	/**
	 * Value of the month-of-year field indicating the twelfth month of the year.
	 */
	public final static int DECEMBER = 12;

	/**
	 * Value of the day-of-week field indicating Sunday.
	 */
	public final static int SUNDAY = 0;

	/**
	 * Value of the day-of-week field indicating Monday.
	 */
	public final static int MONDAY = 1;

	/**
	 * Value of the day-of-week field indicating Tuesday.
	 */
	public final static int TUESDAY = 2;

	/**
	 * Value of the day-of-week field indicating Wednesday.
	 */
	public final static int WEDNESDAY = 3;

	/**
	 * Value of the day-of-week field indicating Thursday.
	 */
	public final static int THURSDAY = 4;

	/**
	 * Value of the day-of-week field indicating Friday.
	 */
	public final static int FRIDAY = 5;

	/**
	 * Value of the day-of-week field indicating Saturday.
	 */
	public final static int SATURDAY = 6;

	/**
	 * List the days in each month for LEAP year.
	 */
	private static final int[] LEAP_YEAR_MONTH_DAYS = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * List the days in each month for UNLEAP year.
	 */
	private static final int[] UNLEAP_YEAR_MONTH_DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * Create a instance of the {@link Date} from year, month-of-year and
	 * day-of-month fields.
	 * 
	 * @param year       year field
	 * @param month      month-of-year field
	 * @param dayOfMonth day-of-month field
	 * @return result date
	 */
	public static Date from(int year, int month, int dayOfMonth) {
		long y = year;
		long m = (long) month - 1;
		long d = (long) dayOfMonth - 1;
		if (m < 0 || m >= 12) {
			long temp = m < 0 ? m % 12 + 12 : m % 12;
			y = Math.addExact(y, (m - temp) / 12);
			m = temp;
		}
		while (d < 0) {
			if (m == 0) {
				m = 11;
				y--;
			}
			d += _isLeapYear(y) ? LEAP_YEAR_MONTH_DAYS[(int) m] : UNLEAP_YEAR_MONTH_DAYS[(int) m];
		}
		int mds = _isLeapYear(y) ? LEAP_YEAR_MONTH_DAYS[(int) m] : UNLEAP_YEAR_MONTH_DAYS[(int) m];
		while (d >= mds) {
			d -= mds;
			m++;
			if (m == 12) {
				y++;
				m = 0;
			}
			mds = _isLeapYear(y) ? LEAP_YEAR_MONTH_DAYS[(int) m] : UNLEAP_YEAR_MONTH_DAYS[(int) m];
		}
		return make(y, m + 1, d + 1);
	}

	/**
	 * Create a instance of the {@link Date} from the epoch days.
	 * 
	 * @param epochDays the Epoch Days to convert, based on the epoch 1970-01-01
	 * @return result date.
	 */
	public static Date fromEpochDays(long epochDays) {
		long y = 1970;
		long exd = epochDays;
		// Estimate year
		while (true) {
			long sy = exd / 366;
			if (0 == sy) {
				if (exd == 365 && !_isLeapYear(y)) {
					y++;
					exd = 0;
				} else if (exd == -365 && !_isLeapYear(y - 1)) {
					y--;
					exd = 0;
				}
				break;
			}
			long ty = y + sy;
			exd -= _getDaysBetweenYearFirstDay(y, ty);
			y = ty;
		}
		// Fast Confirm
		if (exd == 0L)
			return make(y, JANUARY, 1);
		// Locate the year
		int m = 0;
		if (exd < 0) {
			y--;
			m = 11;
		}
		// Locate the month and day
		int[] mds = _isLeapYear(y) ? LEAP_YEAR_MONTH_DAYS : UNLEAP_YEAR_MONTH_DAYS;
		if (exd < 0) {
			do {
				exd += mds[m];
				if (exd >= 0) {
					break;
				}
				m--;
			} while (true);
		} else {
			do {
				if (exd < mds[m]) {
					break;
				}
				exd -= mds[m];
				m++;
			} while (true);
		}
		// Value copy
		return make(y, m + 1, (int) (exd + 1));
	}

	/**
	 * Make an {@link Date} but in a year range checking way. Values previously
	 * validated.
	 * 
	 * @param year  year field
	 * @param month month-of-year field
	 * @param day   day-of-month field
	 * @return a date
	 * @throws IllegalArgumentException if the year field too large or too small
	 */
	private static Date make(long year, long month, long day) {
		if (year < YEAR_MIN || year > YEAR_MAX)
			throw new IllegalArgumentException("year field out of range");
		return new Date((int) year, (int) month, (int) day);
	}

	/**
	 * The date year part.
	 */
	private final int year;
	/**
	 * The date month part. Values range from 1 to 12.
	 */
	private final byte month;
	/**
	 * The date day part. Values range from 1 to 31.
	 */
	private final byte day;

	/**
	 * Constructor, previously validated.
	 *
	 * @param year  the year to represent, from {@value #YEAR_MIN} to
	 *              {@value #YEAR_MAX}
	 * @param month the month-of-year to represent from {@value #JANUARY} to
	 *              {@value #DECEMBER}
	 * @param day   the day-of-month to represent, valid for month-of-year, from 1
	 *              to 28, 29, 30 or 31
	 */
	private Date(int year, int month, int day) {
		super();
		this.year = year;
		this.month = (byte) month;
		this.day = (byte) day;
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
		final int y = this.year;
		return y == 0 || y % 4 == 0 && y % 100 != 0 || y % 400 == 0 && y % 3200 != 0;
	}

	/**
	 * To get the year field.
	 * 
	 * @return the year, a primitive {@code int} value for the year from
	 *         {@value #YEAR_MIN} to {@value #YEAR_MAX}.
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * To get the month-of-year field whose value is in:<br>
	 * {@link #JANUARY} is {@value #JANUARY}. <br>
	 * {@link #FEBRUARY} is {@value #FEBRUARY}. <br>
	 * {@link #MARCH} is {@value #MARCH}. <br>
	 * {@link #APRIL} is {@value #APRIL}. <br>
	 * {@link #MAY} is {@value #MAY}. <br>
	 * {@link #JUNE} is {@value #JUNE}. <br>
	 * {@link #JULY} is {@value #JULY}. <br>
	 * {@link #AUGUST} is {@value #AUGUST}. <br>
	 * {@link #SEPTEMBER} is {@value #SEPTEMBER}. <br>
	 * {@link #OCTOBER} is {@value #OCTOBER}. <br>
	 * {@link #NOVEMBER} is {@value #NOVEMBER}. <br>
	 * {@link #DECEMBER} is {@value #DECEMBER}. <br>
	 * 
	 * @return the month-of-year, a primitive {@code int} value for the
	 *         month-of-year from {@value #JANUARY} to {@value #DECEMBER}.
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * To get the day-of-month field.
	 * 
	 * @return the day-of-month, a primitive {@code int} value for the day-of-month
	 *         from 1 to 31.
	 */
	public int getDayOfMonth() {
		return this.day;
	}

	/**
	 * To get the day-of-year field.
	 * 
	 * @return the day-of-year, a primitive {@code int} value for the day-of-year
	 *         from 1 to 365, or 366 in a leap year.
	 */
	public int getDayOfYear() {
		int yearDay = day;
		int[] yearMonthDays = isLeapYear() ? LEAP_YEAR_MONTH_DAYS : UNLEAP_YEAR_MONTH_DAYS;
		for (int mi = 0, m = month - 1; mi < m; mi++)
			yearDay += yearMonthDays[mi];
		return yearDay;
	}

	/**
	 * To get the week-of-year field.
	 * 
	 * @return the week-of-year, a primitive {@code int} value for the week-of-year
	 *         from 1 to 54.
	 */
	public int getWeekOfYear() {
		final int y = year - 1;
		int yearBeginWeekDay = (1 + y + y / 4 - y / 100 + y / 400 - y / 3200) % 7;
		int yearDay = getDayOfYear() - 1;
		return (yearDay + yearBeginWeekDay) / 7;
	}

	/**
	 * To get the week-of-month field.
	 * 
	 * @return the week-of-month, a primitive {@code int} value for the
	 *         week-of-month from 1 to 6.
	 */
	public int getWeekOfMonth() {
		final int y = (month == 0 || month == 1) ? year - 1 : year;
		final int m = (month == 0 || month == 1) ? month + 12 : month;
		int monthBeginWeekDay = (2 + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400 - y / 3200) % 7;
		return (monthBeginWeekDay + day) / 7;
	}

	/**
	 * To get the day-of-week field whose value is in:<br>
	 * {@link #SUNDAY} is {@value #SUNDAY}. <br>
	 * {@link #MONDAY} is {@value #MONDAY}. <br>
	 * {@link #TUESDAY} is {@value #TUESDAY}. <br>
	 * {@link #WEDNESDAY} is {@value #WEDNESDAY}. <br>
	 * {@link #THURSDAY} is {@value #THURSDAY}. <br>
	 * {@link #FRIDAY} is {@value #FRIDAY}. <br>
	 * {@link #SATURDAY} is {@value #SATURDAY}. <br>
	 * <p>
	 * The calculate method is a change method based on Kim larsson calculation
	 * formula. <br>
	 * 1. The origin formula: W=(d+2*m+3*(m+1)/5+y+y/4-y/100+y/400+1)%7 <br>
	 * 2. We consider a year could be divisible by 3200 as not a leap year <br>
	 * 
	 * @see #isLeapYear()
	 * @return the day-of-week, a primitive {@code int} value for the day-of-week
	 *         from {@value #SUNDAY} to {@value #SATURDAY}.
	 */
	public int getDayOfWeek() {
		final int y = (month == 1 || month == 2) ? year - 1 : year;
		final int m = (month == 1 || month == 2) ? month : month;
		final int d = day;
		return (1 + d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400 - y / 3200) % 7;
	}

	/**
	 * Returns the length of the year represented by this date.
	 * <p>
	 * This returns the length of the year in days, either 365 or 366.
	 *
	 * @return 366 if the year is leap, 365 otherwise
	 */
	public int getDaysInYear() {
		return isLeapYear() ? 366 : 365;
	}

	/**
	 * Returns the length of the month represented by this date.
	 * <p>
	 * This returns the length of the month in days. For example, a date in January
	 * would return 31.
	 *
	 * @return the length of the month in days
	 */
	public int getDaysInMonth() {
		return isLeapYear() ? LEAP_YEAR_MONTH_DAYS[month - 1] : UNLEAP_YEAR_MONTH_DAYS[month - 1];
	}

	/**
	 * Converts this date to the Epoch Day.
	 * <p>
	 * The Epoch Days is a simple incrementing count of days where days 0 is
	 * 1970-01-01 (ISO). This definition is the same for all chronologies, enabling
	 * conversion.
	 *
	 * @return the Epoch Day equivalent to this date in a primitive {@code long}
	 *         type.
	 */
	public long toEpochDays() {
		final long y = year;
		long yearBeginDays = y * 365 - 719527L + y / 4 - y / 100 + y / 400 - y / 3200 - (y <= 0 ? 1 : 0);
		long yearDays = getDayOfYear() - 1;
		return yearBeginDays + yearDays;
	}

	/**
	 * Reset the year field value.
	 * 
	 * @param year the year field value to reset
	 * @return the date result
	 */
	public Date resetYear(int year) {
		if (year == this.year)
			return this;
		if (year < YEAR_MIN || year > YEAR_MAX)
			throw new IllegalArgumentException("year field out of range");
		int day = this.day;
		if (this.month == FEBRUARY && this.day == 29 && !_isLeapYear(year))
			day = 28;
		return new Date(year, month, day);
	}

	/**
	 * Reset the month-of-year field value.
	 * 
	 * @param month the month-of-year field value to reset
	 * @return the date result
	 */
	public Date resetMonth(int month) {
		if (month == this.month)
			return this;
		if (month < JANUARY || month > DECEMBER)
			throw new IllegalArgumentException("month-of-year field out of range");
		int[] monthDays = isLeapYear() ? LEAP_YEAR_MONTH_DAYS : UNLEAP_YEAR_MONTH_DAYS;
		int day = this.day;
		if (monthDays[month] < day)
			day = monthDays[month];
		return new Date(year, month, day);
	}

	/**
	 * Reset the day-of-month field value.
	 * 
	 * @param dayOfMonth the day-of-month field value to reset
	 * @return the date result
	 */
	public Date resetDayOfMonth(int dayOfMonth) {
		if (dayOfMonth == this.day)
			return this;
		if (dayOfMonth < 1 || dayOfMonth > this.getDaysInMonth())
			throw new IllegalArgumentException("day-of-month field out of range");
		return new Date(year, month, dayOfMonth);
	}

	/**
	 * Reset the day-of-year field value.
	 * 
	 * @param dayOfYear the day-of-year field value to reset
	 * @return the date result
	 */
	public Date resetDayOfYear(int dayOfYear) {
		if (dayOfYear < 1 || dayOfYear > this.getDaysInYear())
			throw new IllegalArgumentException("day-of-year field out of range");
		int month = 1;
		int day = 1;
		int[] yearMonthDays = isLeapYear() ? LEAP_YEAR_MONTH_DAYS : UNLEAP_YEAR_MONTH_DAYS;
		int temp = dayOfYear;
		for (int mi = 0; mi < 12; mi++) {
			if (temp - yearMonthDays[mi] < 1) {
				month = mi + 1;
				day = temp;
				break;
			}
			temp -= yearMonthDays[mi];
		}
		if (month == this.month && day == this.day)
			return this;
		return new Date(year, month, day);
	}

	/**
	 * Reset the day-of-week field value.
	 * 
	 * @param dayOfWeek the day-of-week field value to reset
	 * @return the date result
	 */
	public Date resetDayOfWeek(int dayOfWeek) {
		if (dayOfWeek < SUNDAY || dayOfWeek > SATURDAY)
			throw new IllegalArgumentException("day-of-week field out of range");
		int theDayOfWeek = getDayOfWeek();
		if (theDayOfWeek == dayOfWeek)
			return this;
		return from(year, month, day + (dayOfWeek - theDayOfWeek));
	}

	/**
	 * Add some years.
	 * 
	 * @param years the year count to add
	 * @return the date result
	 */
	public Date addYears(int years) {
		if (years == 0)
			return this;
		return from(Math.addExact(years, this.year), month, day);
	}

	/**
	 * Add some months.
	 * 
	 * @param months the month count to add
	 * @return the date result
	 */
	public Date addMonths(long months) {
		if (months == 0)
			return this;
		long years = 0;
		if (months < 0 || months >= 12) {
			long temp = (months % 12 + 12) % 12;
			years = Math.subtractExact(months, temp) / 12;
			months = temp;
		}
		long year = Math.addExact(this.year, years);
		if (year < Integer.MIN_VALUE || year > Integer.MAX_VALUE)
			throw new IllegalArgumentException("year field out of range");
		return from((int) year, (int) months + month, day);
	}

	/**
	 * Add some days.
	 * 
	 * @param days the day count to add
	 * @return the date result
	 */
	public Date addDays(long days) {
		if (days == 0L)
			return this;
		long epochDays = Math.addExact(days, toEpochDays());
		return fromEpochDays(epochDays);
	}

	/**
	 * Add some weeks.
	 * 
	 * @param weeks the week count to add
	 * @return the date result
	 */
	public Date addWeeks(long weeks) {
		if (weeks == 0L)
			return this;
		long days = Math.multiplyExact(weeks, 7);
		long epochDays = Math.addExact(days, toEpochDays());
		return fromEpochDays(epochDays);
	}

	/**
	 * Compare the {@link Date date} with another one. A positive value means this
	 * {@link Date date} is later than another one; A negative value means this
	 * {@link Date date} is earlier than another one; A zero value means the both
	 * {@link Date dates} are a same time point.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param other the other date to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(Date other) {
		// Fast compare if same date
		if (this == other)
			return 0;
		// Compare year
		if (this.year != other.year)
			return this.year > other.year ? 1 : -1;
		// Compare month
		if (this.month != other.month)
			return this.month > other.month ? 1 : -1;
		// Compare day
		if (this.day != other.day)
			return this.day > other.day ? 1 : -1;
		// Same date
		return 0;
	}

	/**
	 * A hash code for this date.
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return a suitable hash code
	 */
	@Override
	public int hashCode() {
		int y = year;
		int m = month - 1; // 0~11(2^4-1)
		int d = day - 1; // 0~30(2^5-1)
		return (y & 0Xfffffe00) ^ ((y << 9) + (m << 5) + d);
	}

	/**
	 * Checks if this date is equal to another date.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj the object to check, null returns false
	 * @return true if this is equal to the other date
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Date other = (Date) obj;
		return this.year == other.year && this.month == other.month && this.day == other.day;
	}

	/**
	 * Outputs this date as a {@code String}, such as {@code 1970-01-23}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		boolean bc = year <= 0;
		int y = bc ? -year : year;
		int m = month;
		int d = day;
		StringBuilder sb = new StringBuilder(12);
		// B.C. or A.D.
		if (bc)
			sb.append('-');
		// Year
		if (y >= 1000)
			sb.append(y);
		else
			sb.append(1000 + y).setCharAt(sb.length() - 4, '0');
		// Month
		sb.append('-');
		if (m < 10)
			sb.append('0');
		sb.append(m);
		// Day
		sb.append('-');
		if (d < 10)
			sb.append('0');
		sb.append(d);
		return sb.toString();
	}

	/**
	 * Analyze if the year is leap year.
	 * <p>
	 * For year B.C., the year value is the negative show value plus one. For
	 * example, for year 1BC then the year is 0.
	 * <p>
	 * One year is leap year when:<br />
	 * 1. 1BC is leap year. <br />
	 * 2. A year could be divided by 4 but 100 is a leap year. <br />
	 * 3. A year could be divided by 400 but 3200 is a leap year. <br />
	 * 4. Each in others years is not a leap year. <br />
	 * 
	 * @return If the year is a leap year
	 */
	private static boolean _isLeapYear(long y) {
		return y == 0 || y % 4 == 0 && y % 100 != 0 || y % 400 == 0 && y % 3200 != 0;
	}

	/**
	 * Calculate the leap days(February 29th) count between a year' January 1st with
	 * year 0(B.C. 1)'s January 1st.
	 * 
	 * @param year The target year.
	 * @return The leaps between the year's 01-01 with B.C.0001-01-01
	 */
	private static long _getLeapsBetweenYearFirstDayWithYear0(long year) {
		if (year > 0) {
			long temp = year - 1;
			return (temp / 4 - temp / 100 + temp / 400 - temp / 3200) + 1;
		} else {
			return year / 4 - year / 100 + year / 400 - year / 3200;
		}
	}

	/**
	 * Calculate the days count between two years' January 1st.
	 * 
	 * @param fromYear The from year.
	 * @param toYear   The to year.
	 * @return The days between the to year's 01-01 with the from year's 01-01.
	 */
	private static long _getDaysBetweenYearFirstDay(long fromYear, long toYear) {
		return (toYear - fromYear) * 365
				+ (_getLeapsBetweenYearFirstDayWithYear0(toYear) - _getLeapsBetweenYearFirstDayWithYear0(fromYear));
	}

	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = 5798700275147622539L;

}
