/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Immutable arbitrary-precision irreducible fraction numbers like
 * <code>a/b</code> or <code>-a/b</code>. All operations behave as if
 * BigFractions were represented in two's-complement notation (like Java's
 * primitive number types). BigFraction provides analogues to all of Java's
 * primitive number operators of modular arithmetic, and all relevant methods
 * from java.lang.Math.
 * 
 * @author XuYanhang
 * @since 2020-11-05
 * @see BigInteger
 *
 */
public final class BigFraction extends Number implements Comparable<BigFraction> {

	/**
	 * Creates a {@link BigFraction} from an integer value. Result signum is the
	 * signum of the number, numerator is the absolute value of the number and the
	 * denominator is <code>ONE</code> forever
	 * 
	 * @param num an integer value to construct for this {@link BigFraction}
	 */
	public static BigFraction of(long num) {
		return of(BigInteger.valueOf(num));
	}

	/**
	 * Creates a {@link BigFraction} from an integer value. Result signum is the
	 * signum of the number, numerator is the absolute value of the number and the
	 * denominator is <code>ONE</code> forever
	 * 
	 * @param num an integer value to construct for this {@link BigFraction}
	 */
	public static BigFraction of(BigInteger num) {
		if (num.signum() == 0)
			return new BigFraction(0, BigInteger.ZERO, BigInteger.ONE);
		return new BigFraction(num.signum(), num.abs(), BigInteger.ONE);
	}

	/**
	 * Creates a {@link BigFraction} from two numbers on its numerator and
	 * denominator. These parameters would be checked when the result signum is
	 * depended on the {@code numerator} and {@code denomitor}.
	 * 
	 * @param numerator   the numerator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @param denominator the denominator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @return a {@link BigFraction} from given parameters
	 * @throws ArithmeticException if {@code denominator} is zero
	 */
	public static BigFraction of(long numerator, long denominator) {
		return of(0, BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a {@link BigFraction} from two numbers on its numerator and
	 * denominator. These parameters would be checked when the result signum is
	 * depended on the {@code numerator} and {@code denomitor}.
	 * 
	 * @param numerator   the numerator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @param denominator the denominator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @return a {@link BigFraction} from given parameters
	 * @throws ArithmeticException if {@code denominator} is zero
	 */
	public static BigFraction of(BigInteger numerator, BigInteger denominator) {
		return of(0, numerator, denominator);
	}

	/**
	 * Creates a {@link BigFraction} from its signum, numerator and denominator.
	 * These parameters would be checked when the result signum is depended on the
	 * {@code numerator} and {@code denomitor}. The {@code signum} works only when
	 * this is not a zero fraction and {@code signum} is a negative value.
	 * 
	 * @param signum      the signum of the {@link BigFraction} who would be checked
	 *                    on the {@code numerator} and {@code denominator}
	 * @param numerator   the numerator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @param denominator the denominator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @return a {@link BigFraction} from given parameters
	 * @throws ArithmeticException if {@code denominator} is zero
	 */
	public static BigFraction of(int signum, long numerator, long denominator) {
		return of(signum, BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a {@link BigFraction} from its signum, numerator and denominator.
	 * These parameters would be checked when the result signum is depended on the
	 * {@code numerator} and {@code denomitor}. The {@code signum} works only when
	 * this is not a zero fraction and {@code signum} is a negative value.
	 * 
	 * @param signum      the signum of the {@link BigFraction} who would be checked
	 *                    on the {@code numerator} and {@code denominator}
	 * @param numerator   the numerator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @param denominator the denominator of the {@link BigFraction} when signum
	 *                    checked and absolute value converted
	 * @return a {@link BigFraction} from given parameters
	 * @throws ArithmeticException if {@code denominator} is zero
	 */
	public static BigFraction of(int signum, BigInteger numerator, BigInteger denominator) {
		if (denominator.signum() == 0)
			throw new ArithmeticException("zero denominator");
		signum = signum >= 0 ? 1 : -1;
		signum = signum * numerator.signum() * denominator.signum();
		if (signum == 0)
			return new BigFraction(0, BigInteger.ZERO, BigInteger.ONE);
		numerator = numerator.abs();
		denominator = denominator.abs();
		BigInteger gcd = numerator.gcd(denominator);
		return new BigFraction(signum, numerator.divide(gcd), denominator.divide(gcd));
	}

	/**
	 * Translates the decimal fraction String representation of a
	 * {@link BigFraction} into a {@link BigFraction}. The String representation
	 * consists of an optional minus sign followed by a sequence of one or more
	 * decimal digits, a fraction sign divided decimal digits besides. The
	 * character-to-digit mapping is provided by {@code Character.digit}. The String
	 * may not contain any extraneous characters but whitespace besides numerator
	 * and denominator.
	 *
	 * @param value decimal fraction String representation of {@link BigFraction}
	 * @return a {@link BigFraction} represented this decimal fraction String
	 * @throws NumberFormatException if {@code value} is not a valid representation
	 *                               of a {@link BigFraction}
	 * @throws ArithmeticException   if the denominator of the fraction represented
	 *                               by {@code value} is zero(NaN)
	 * @see Character#digit
	 */
	public static BigFraction parse(String value) {
		value = value.trim();
		if (value.length() == 0)
			throw new NumberFormatException("Zero length Number");
		int sig = 1;
		int off = 0;
		if (value.charAt(0) == '-') {
			sig = -1;
			off = 1;
		} else if (value.charAt(0) == '+') {
			off = 1;
		}
		BigInteger nume;
		BigInteger deno;
		int i = value.indexOf('/', off);
		if (i < 0) {
			nume = new BigInteger(value.substring(off).trim());
			sig = sig * nume.signum();
			nume = nume.abs();
			deno = BigInteger.ONE;
		} else {
			nume = new BigInteger(value.substring(off, i).trim());
			deno = new BigInteger(value.substring(i + 1).trim());
			if (deno.signum() == 0)
				throw new ArithmeticException("zero denominator");
			sig = nume.signum() * deno.signum() * sig;
			if (sig == 0) {
				nume = BigInteger.ZERO;
				deno = BigInteger.ONE;
			} else {
				nume = nume.abs();
				deno = deno.abs();
				BigInteger gcd = nume.gcd(deno);
				nume = nume.divide(gcd);
				deno = deno.divide(gcd);
			}
		}
		return new BigFraction(sig, nume, deno);
	}

	/**
	 * The signum of this {@link BigFraction}: -1 for negative, 0 for zero, or 1 for
	 * positive. Note that the {@link BigFraction} zero <i>must</i> have a signum of
	 * 0. This is necessary to ensures that there is exactly one representation for
	 * each {@link BigFraction} value.
	 */
	private final int signum;
	/**
	 * The numerator of this {@link BigFraction}, is an integer of zero or positive
	 */
	private final BigInteger numerator;
	/**
	 * The denominator of this {@link BigFraction}, is an integer of positive
	 */
	private final BigInteger denominator;

	/**
	 * Create a {@link BigFraction} in full parameters without check
	 */
	private BigFraction(int signum, BigInteger numerator, BigInteger denominator) {
		super();
		this.signum = signum;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * Returns the signum function of this {@link BigFraction}
	 *
	 * @return -1, 0 or 1 as the value of this {@link BigFraction} is negative, zero
	 *         or positive
	 */
	public int signum() {
		return this.signum;
	}

	/**
	 * Returns the numerator function of this {@link BigFraction}
	 *
	 * @return numerator of this {@link BigFraction}, zero or positive
	 */
	public BigInteger numerator() {
		return this.numerator;
	}

	/**
	 * Returns the denominator function of this {@link BigFraction}
	 *
	 * @return denominator of this {@link BigFraction}, positive
	 */
	public BigInteger denominator() {
		return this.denominator;
	}

	/**
	 * Returns if this {@link BigFraction} is a standard integer number when the
	 * {@link #denominator() denominator} of it is <code>ONE</code>
	 * 
	 * @return true when the {@link BigFraction} is an integer
	 */
	public boolean isInteger() {
		return denominator.equals(BigInteger.ONE);
	}

	/**
	 * Returns if this {@link BigFraction} is a standard real fraction number when
	 * the {@link #numerator() numerator} is smaller than the {@link #denominator()
	 * denominator}
	 * 
	 * @return true when the {@link BigFraction} is a real fraction who is between
	 *         -1(excluded) and 1(excluded)
	 */
	public boolean isRealFraction() {
		return numerator.compareTo(denominator) < 0;
	}

	/**
	 * Returns if this {@link BigFraction} is a circulating decimal number when the
	 * {@link #denominator() denominator} has the prime factor besides 2 and 5.
	 * 
	 * @return true when the {@link BigFraction} is a circulating decimal
	 */
	public boolean isCirculatingDecimal() {
		BigInteger deno = denominator;
		if (deno.equals(BigInteger.ONE))
			return false;
		while (!deno.testBit(0))
			deno = deno.shiftRight(1);
		final BigInteger FIVE = BigInteger.valueOf(5L);
		while (deno.mod(FIVE).signum() == 0)
			deno = deno.divide(FIVE);
		return !deno.equals(BigInteger.ONE);
	}

	/**
	 * Returns if this {@link BigFraction} is a pure circulating decimal number when
	 * the {@link #denominator() denominator} only has the prime factor but 2 or 5.
	 * 
	 * @return true when the {@link BigFraction} is a pure circulating decimal
	 */
	public boolean isPureCirculatingDecimal() {
		BigInteger deno = denominator;
		if (deno.equals(BigInteger.ONE))
			return false;
		return deno.testBit(0) && deno.mod(BigInteger.valueOf(5L)).signum() != 0;
	}

	/**
	 * Returns the non-circulating period length behind the decimal point and before
	 * the circulating period if exists. Returns zero when this is an integer or a
	 * pure circulating decimal.
	 * 
	 * @return the non-circulating period length of this decimal fraction
	 */
	public int getNoncirculatingPeriodLength() {
		BigInteger deno = denominator;
		if (deno.equals(BigInteger.ONE))
			return 0;
		int off = 0;
		while (deno.mod(BigInteger.TEN).signum() == 0) {
			deno = deno.divide(BigInteger.TEN);
			off++;
		}
		while (!deno.testBit(0)) {
			deno = deno.shiftRight(1);
			off++;
		}
		final BigInteger FIVE = BigInteger.valueOf(5L);
		while (deno.mod(FIVE).signum() == 0) {
			deno = deno.divide(FIVE);
			off++;
		}
		return off;
	}

	/**
	 * Returns the circulating period length behind the decimal point. Returns zero
	 * when it's not a circulating decimal.
	 * 
	 * @return the circulating period length of this decimal fraction
	 */
	public int getCirculatingPeriodLength() {
		BigInteger deno = denominator;
		BigInteger nume = numerator.mod(deno);
		if (deno.equals(BigInteger.ONE))
			return 0;
		while (deno.mod(BigInteger.TEN).signum() == 0) {
			deno = deno.divide(BigInteger.TEN);
			nume = nume.mod(deno);
		}
		final BigInteger FIVE = BigInteger.valueOf(5L);
		while (!deno.testBit(0)) {
			deno = deno.shiftRight(1);
			nume = nume.multiply(FIVE).mod(deno);
		}
		while (deno.mod(FIVE).signum() == 0) {
			deno = deno.divide(FIVE);
			nume = nume.shiftLeft(1).mod(deno);
		}
		if (deno.equals(BigInteger.ONE))
			return 0;
		BigInteger begin = nume;
		int counter = 1;
		while (true) {
			nume = nume.multiply(BigInteger.TEN).mod(deno);
			if (nume.equals(begin))
				return counter;
			counter++;
		}
	}

	/**
	 * Converts this {@link BigFraction} to an {@code int}. This conversion is
	 * analogous to the <i>narrowing primitive conversion</i> from {@code double} to
	 * {@code short} as defined in section 5.1.3 of <cite>The Java&trade; Language
	 * Specification</cite>: any fractional part of this {@link BigFraction} will be
	 * discarded, and if the resulting "{@code BigInteger}" is too big to fit in an
	 * {@code int}, only the low-order 32 bits are returned. Note that this
	 * conversion can lose information about the overall magnitude and precision of
	 * this {@link BigFraction} value as well as return a result with the opposite
	 * sign.
	 *
	 * @return this {@link BigFraction} converted to an {@code int}
	 */
	@Override
	public int intValue() {
		return toBigInteger().intValue();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code long}. This conversion is
	 * analogous to the <i>narrowing primitive conversion</i> from {@code double} to
	 * {@code short} as defined in section 5.1.3 of <cite>The Java&trade; Language
	 * Specification</cite>: any fractional part of this {@link BigFraction} will be
	 * discarded, and if the resulting "{@code BigInteger}" is too big to fit in a
	 * {@code long}, only the low-order 64 bits are returned. Note that this
	 * conversion can lose information about the overall magnitude and precision of
	 * this {@link BigFraction} value as well as return a result with the opposite
	 * sign.
	 *
	 * @return this {@link BigFraction} converted to a {@code long}
	 */
	@Override
	public long longValue() {
		return toBigInteger().longValue();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code float}. This conversion is
	 * similar to the <i>narrowing primitive conversion</i> from {@code double} to
	 * {@code float} as defined in section 5.1.3 of <cite>The Java&trade; Language
	 * Specification</cite>: if this {@link BigFraction} has too great a magnitude
	 * to represent as a {@code float}, it will be converted to
	 * {@link Float#NEGATIVE_INFINITY} or {@link Float#POSITIVE_INFINITY} as
	 * appropriate. Note that even when the return value is finite, this conversion
	 * can lose information about the precision of the {@link BigFraction} value.
	 *
	 * @return this {@link BigFraction} converted to a {@code float}
	 */
	@Override
	public float floatValue() {
		return toBigDecimal().floatValue();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code double}. This conversion is
	 * similar to the <i>narrowing primitive conversion</i> from {@code double} to
	 * {@code float} as defined in section 5.1.3 of <cite>The Java&trade; Language
	 * Specification</cite>: if this {@link BigFraction} has too great a magnitude
	 * represent as a {@code double}, it will be converted to
	 * {@link Double#NEGATIVE_INFINITY} or {@link Double#POSITIVE_INFINITY} as
	 * appropriate. Note that even when the return value is finite, this conversion
	 * can lose information about the precision of the {@link BigFraction} value.
	 *
	 * @return this {@link BigFraction} converted to a {@code double}
	 */
	@Override
	public double doubleValue() {
		return toBigDecimal().doubleValue();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code BigInteger}. This conversion is
	 * analogous to the <i>narrowing primitive conversion</i> from {@code double} to
	 * {@code long} as defined in section 5.1.3 of <cite>The Java&trade; Language
	 * Specification</cite>: any fractional part of this {@link BigFraction} will be
	 * discarded. Note that this conversion can lose information about the precision
	 * of the {@link BigFraction} value.
	 *
	 * @return this {@link BigFraction} converted to a {@code BigInteger}
	 */
	public BigInteger toBigInteger() {
		if (signum == 0)
			return BigInteger.ZERO;
		BigInteger nume = signum > 0 ? numerator : numerator.negate();
		return denominator.equals(BigInteger.ONE) ? nume : nume.divide(denominator);
	}

	/**
	 * Converts this {@link BigFraction} to a {@code BigDecimal}. This is also a
	 * call of <code>toBigDecimal(255, BigDecimal.ROUND_DOWN)</code>.
	 *
	 * @return this {@link BigFraction} converted to a {@code BigDecimal}
	 * @see #toBigDecimal(int, int)
	 */
	public BigDecimal toBigDecimal() {
		return toBigDecimal(255, BigDecimal.ROUND_DOWN);
	}

	/**
	 * Converts this {@link BigFraction} to a {@code BigDecimal}. Some fractional
	 * part of this {@link BigFraction} might be discarded when this keeps no more
	 * than {@code scaleLimit} decimals after decimal point. Note that this
	 * conversion can lose information about the precision of the
	 * {@link BigFraction} value.
	 *
	 * @param scaleLimit   maximum scale of the {@code BigDecimal} quotient to be
	 *                     returned.
	 * @param roundingMode rounding mode to apply.
	 * @return this {@link BigFraction} converted to a {@code BigDecimal}
	 * @throws ArithmeticException      if {@code roundingMode==ROUND_UNNECESSARY}
	 *                                  and the specified {@link scaleLimit} is
	 *                                  insufficient to represent the result of the
	 *                                  division exactly
	 * @throws IllegalArgumentException if {@code scaleLimit} is a negative value or
	 *                                  {@code roundingMode} does not represent a
	 *                                  valid rounding mode
	 * @see BigDecimal#ROUND_UP
	 * @see BigDecimal#ROUND_DOWN
	 * @see BigDecimal#ROUND_CEILING
	 * @see BigDecimal#ROUND_FLOOR
	 * @see BigDecimal#ROUND_HALF_UP
	 * @see BigDecimal#ROUND_HALF_DOWN
	 * @see BigDecimal#ROUND_HALF_EVEN
	 * @see BigDecimal#ROUND_UNNECESSARY
	 */
	public BigDecimal toBigDecimal(int scaleLimit, int roundingMode) {
		if (scaleLimit < 0)
			throw new IllegalArgumentException("Illegal scaleLimit");
		if (roundingMode < BigDecimal.ROUND_UP || roundingMode > BigDecimal.ROUND_UNNECESSARY)
			throw new IllegalArgumentException("Illegal roundingMode");
		if (signum == 0)
			return BigDecimal.ZERO;
		BigDecimal nume = new BigDecimal(signum > 0 ? numerator : numerator.negate());
		int nonCir = getNoncirculatingPeriodLength();
		int cir = getCirculatingPeriodLength();
		int len = cir != 0 ? scaleLimit : Math.min(scaleLimit, nonCir);
		return denominator.equals(BigInteger.ONE) ? nume : nume.divide(new BigDecimal(denominator), len, roundingMode);
	}

	/**
	 * Converts this {@link BigFraction} to a {@code BigFraction}. Any integer part
	 * of this {@link BigFraction} will be discarded. Note that this conversion can
	 * lose information about the precision of the {@link BigFraction} value.
	 *
	 * @return this {@link BigFraction} converted to a real fraction in range(-1,1)
	 */
	public BigFraction toRealFraction() {
		if (denominator.equals(BigInteger.ONE))
			return new BigFraction(0, BigInteger.ZERO, BigInteger.ZERO);
		return new BigFraction(signum, numerator.mod(denominator), denominator);
	}

	/**
	 * Converts this {@link BigFraction} to a {@code long}, checking for lost
	 * information. If the integer value of this {@link BigFraction} is out of the
	 * range of the {@code long} type, then an {@code ArithmeticException} is
	 * thrown.
	 *
	 * @return this {@link BigFraction} converted to a {@code long}.
	 * @throws ArithmeticException if the integer value of {@code this} will not
	 *                             exactly fit in a {@code long}.
	 * @see BigFraction#longValue()
	 */
	public long longValueExact() {
		return toBigInteger().longValueExact();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code int}, checking for lost
	 * information. If the integer value of this {@link BigFraction} is out of the
	 * range of the {@code int} type, then an {@code ArithmeticException} is thrown.
	 *
	 * @return this {@link BigFraction} converted to a {@code int}.
	 * @throws ArithmeticException if the integer value of {@code this} will not
	 *                             exactly fit in a {@code int}.
	 * @see BigFraction#intValue()
	 */
	public int intValueExact() {
		return toBigInteger().intValueExact();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code short}, checking for lost
	 * information. If the integer value of this {@link BigFraction} is out of the
	 * range of the {@code short} type, then an {@code ArithmeticException} is
	 * thrown.
	 *
	 * @return this {@link BigFraction} converted to a {@code short}.
	 * @throws ArithmeticException if the integer value of {@code this} will not
	 *                             exactly fit in a {@code short}.
	 * @see BigFraction#shortValue()
	 */
	public short shortValueExact() {
		return toBigInteger().shortValueExact();
	}

	/**
	 * Converts this {@link BigFraction} to a {@code byte}, checking for lost
	 * information. If the integer value of this {@link BigFraction} is out of the
	 * range of the {@code byte} type, then an {@code ArithmeticException} is
	 * thrown.
	 *
	 * @return this {@link BigFraction} converted to a {@code byte}.
	 * @throws ArithmeticException if the integer value of {@code this} will not
	 *                             exactly fit in a {@code byte}.
	 * @see BigFraction#intValue()
	 */
	public byte byteValueExact() {
		return toBigInteger().byteValueExact();
	}

	/**
	 * Returns a {@link BigFraction} whose value is the absolute value of this
	 * {@link BigFraction}.
	 *
	 * @return {@code abs(this)}
	 */
	public BigFraction abs() {
		return signum >= 0 ? this : new BigFraction(-signum, numerator, denominator);
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (-this)}.
	 *
	 * @return {@code -this}
	 */
	public BigFraction negate() {
		return new BigFraction(-signum, numerator, denominator);
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (1/this)}.
	 *
	 * @return {@code 1/this}
	 */
	public BigFraction reciprocal() {
		if (0 == signum)
			throw new ArithmeticException("zero denominator");
		return new BigFraction(signum, denominator, numerator);
	}

	/**
	 * Returns the minimum of this {@link BigFraction} and {@code num}.
	 *
	 * @param num value with which the minimum is to be computed
	 * @return the {@link BigFraction} whose value is the lesser of this
	 *         {@link BigFraction} and {@code num}. If they are equal, either may be
	 *         returned
	 */
	public BigFraction min(BigFraction num) {
		return (compareTo(num) < 0 ? this : num);
	}

	/**
	 * Returns the maximum of this {@link BigFraction} and {@code num}.
	 *
	 * @param num value with which the maximum is to be computed
	 * @return the {@link BigFraction} whose value is the greater of this
	 *         {@link BigFraction} and {@code num}. If they are equal, either may be
	 *         returned
	 */
	public BigFraction max(BigFraction num) {
		return (compareTo(num) > 0 ? this : num);
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (this + num)}.
	 *
	 * @param num value to be added to this {@link BigFraction}
	 * @return {@code this + num}
	 */
	public BigFraction add(BigFraction num) {
		if (num.signum == 0)
			return this;
		if (this.signum == 0)
			return num;
		BigInteger gcd = this.denominator.gcd(num.denominator);
		BigInteger nume1 = this.numerator.multiply(num.denominator.divide(gcd));
		BigInteger nume2 = num.numerator.multiply(this.denominator.divide(gcd));
		BigInteger nume;
		int sig = this.signum;
		if (num.signum == this.signum) {
			nume = nume1.add(nume2);
		} else {
			nume = nume1.subtract(nume2);
		}
		if (nume.signum() == 0)
			return new BigFraction(0, BigInteger.ZERO, BigInteger.ONE);
		sig *= nume.signum();
		nume = nume.abs();
		BigInteger deno = this.denominator.divide(gcd).multiply(num.denominator);
		gcd = nume.gcd(deno);
		return new BigFraction(sig, nume.divide(gcd), deno.divide(gcd));
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (this - num)}.
	 *
	 * @param num value to be subtracted from this {@link BigFraction}
	 * @return {@code this - num}
	 */
	public BigFraction subtract(BigFraction num) {
		return add(num.negate());
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (this * num)}.
	 *
	 * @param num value to be multiplied by this {@link BigFraction}
	 * @return {@code this * num}
	 */
	public BigFraction multiply(BigFraction num) {
		if (num.signum == 0)
			return num;
		if (this.signum == 0)
			return this;
		BigInteger gcd1 = this.numerator.gcd(num.denominator);
		BigInteger gcd2 = num.numerator.gcd(this.denominator);
		BigInteger nume = (this.numerator.divide(gcd1)).multiply(num.numerator.divide(gcd2));
		BigInteger deno = (this.denominator.divide(gcd2)).multiply(num.denominator.divide(gcd1));
		BigInteger gcd = nume.gcd(deno);
		return new BigFraction(this.signum * num.signum, nume.divide(gcd), deno.divide(gcd));
	}

	/**
	 * Returns a {@link BigFraction} whose value is {@code (this / num)}.
	 *
	 * @param num value by which this {@link BigFraction} is to be divided
	 * @return {@code this / num}
	 * @throws ArithmeticException if {@code num} is zero.
	 */
	public BigFraction divide(BigFraction num) {
		if (num.signum == 0)
			throw new ArithmeticException("divide by zero");
		return multiply(num.reciprocal());
	}

	/**
	 * Returns a {@link BigFraction} whose value is
	 * <tt>(this<sup>exponent</sup>)</tt>. Note that {@code exponent} is a 32-bits
	 * integer.
	 *
	 * @param exponent exponent to which this {@link BigFraction} is to be raised
	 * @return <tt>this<sup>exponent</sup></tt>
	 * @throws ArithmeticException if {@code exponent} is not positive and this is
	 *                             zero or the exponent is {@link Integer#MIN_VALUE}
	 */
	public BigFraction pow(int exponent) {
		if (this.signum == 0 && exponent <= 0)
			throw new ArithmeticException("0^" + exponent);
		if (this.signum == 0)
			return this;
		if (exponent == 0)
			return new BigFraction(1, BigInteger.ONE, BigInteger.ONE);
		if (exponent == 1)
			return this;
		int sig = this.signum;
		BigInteger nume = exponent > 0 ? this.numerator : this.denominator;
		BigInteger deno = exponent > 0 ? this.denominator : this.numerator;
		if (sig < 0)
			sig = (exponent & 1) == 0 ? -sig : sig;
		if (exponent == -1)
			return new BigFraction(sig, nume, deno);
		nume = nume.equals(BigInteger.ONE) ? BigInteger.ONE
				: nume.pow(exponent > 0 ? exponent : Math.negateExact(exponent));
		deno = deno.equals(BigInteger.ONE) ? BigInteger.ONE
				: deno.pow(exponent > 0 ? exponent : Math.negateExact(exponent));
		return new BigFraction(sig, nume, deno);
	}

	/**
	 * Compares this {@link BigFraction} with the specified {@link BigFraction}.
	 * This method is provided in preference to individual methods for each of the
	 * six boolean comparison operators ({@literal <}, ==, {@literal >},
	 * {@literal >=}, !=, {@literal <=}). The suggested idiom for performing these
	 * comparisons is: {@code
	 * (x.compareTo(y)} &lt;<i>op</i>&gt; {@code 0)}, where &lt;<i>op</i>&gt; is one
	 * of the six comparison operators.
	 *
	 * @param other {@link BigFraction} to which this {@link BigFraction} is to be
	 *              compared.
	 * @return -1, 0 or 1 as this {@link BigFraction} is numerically less than,
	 *         equal to, or greater than {@code other}.
	 */
	@Override
	public int compareTo(BigFraction other) {
		if (this.signum == other.signum) {
			if (signum == 0)
				return 0;
			BigInteger gcd = this.denominator.gcd(other.denominator);
			BigInteger nume1 = this.numerator.multiply(other.denominator.divide(gcd));
			BigInteger nume2 = other.numerator.multiply(this.denominator.divide(gcd));
			return signum > 0 ? nume1.compareTo(nume2) : nume2.compareTo(nume1);
		}
		return this.signum > other.signum ? 1 : -1;
	}

	/**
	 * Compares this {@link BigFraction} with the specified Object for equality.
	 *
	 * @param obj Object to which this {@link BigFraction} is to be compared
	 * @return {@code true} if and only if the specified Object is a
	 *         {@link BigFraction} whose value is numerically equal to this
	 *         {@link BigFraction}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof BigFraction))
			return false;
		BigFraction other = (BigFraction) obj;
		return this.signum == other.signum && this.numerator.equals(other.numerator)
				&& this.denominator.equals(other.denominator);
	}

	/**
	 * Returns the hash code for this {@link BigFraction}.
	 *
	 * @return hash code for this {@link BigFraction}
	 */
	@Override
	public int hashCode() {
		return (numerator.hashCode() + 31 * denominator.hashCode()) * signum;
	}

	/**
	 * Returns the decimal fraction String representation of this
	 * {@link BigFraction}. The digit-to-character mapping provided by
	 * {@code Character.forDigit} is used, a minus sign is prepended if appropriate,
	 * and a fraction sign of '/' is prepended is this fraction can't be exactly
	 * mapped as an integer. (This representation is compatible with the
	 * {@link #parse(String)} method, and allows for String concatenation with
	 * Java's + operator.)
	 *
	 * @return decimal fraction String representation of this {@link BigFraction}
	 * @see Character#forDigit
	 * @see #parse(String)
	 */
	@Override
	public String toString() {
		if (this.signum == 0)
			return "0";
		String nume = numerator.toString(10);
		if (denominator.equals(BigInteger.ONE))
			return signum > 0 ? nume : "-" + nume;
		String deno = denominator.toString(10);
		return signum > 0 ? (nume + "/" + deno) : ("-" + nume + "/" + deno);
	}

	/**
	 * Serializes this number is allowed
	 */
	private static final long serialVersionUID = -701767633538608873L;

}
