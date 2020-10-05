package org.xuyh.util;

import java.util.Arrays;

/**
 * The toolkit to perform common basic numeric operations on math that not
 * afford in JDK {@link Math}.
 * 
 * @see Math
 * @author XuYanhang
 * @since 2020-10-06
 *
 */
public final class Maths {

	/**
	 * Returns <code>true</code> if a number is an even number or <code>false</code>
	 * if it's an odd number.
	 * 
	 * @param n The number to analyze.
	 * @return <code>true</code> if the input number is even or <code>false</code>
	 *         if the input number is odd.
	 */
	public static boolean isEven(long n) {
		return (n & 1) == 0;
	}

	/**
	 * Calculate the greatest common divisor(GCD) for two positive numbers.
	 * 
	 * @param a First natural number which expected to be not zero.
	 * @param b Second natural number which expected to be not zero.
	 * @return GCD of the two values.
	 * @throws IllegalArgumentException if any of the input argument is not
	 *                                  positive.
	 */
	public static long gcd(long a, long b) {
		if (a < 1 || b < 1) {
			throw new IllegalArgumentException("a=" + a + ",b=" + b);
		}
		// Special cases
		if (a == b) {
			return a;
		}
		if (a == 1 || b == 1) {
			return 1;
		}
		// Core circle
		long m = a < b ? a : b;
		long n = a < b ? b : a;
		long t = 0;
		while ((t = (n % m)) != 0) {
			n = m;
			m = t;
		}
		return m;
	}

	/**
	 * Calculate the least common multiple(LCM) for two positive numbers.
	 * 
	 * @param a First natural number which expected to be not zero.
	 * @param b Second natural number which expected to be not zero.
	 * @return LCM of the two values.
	 * @throws IllegalArgumentException if any of the input argument is not
	 *                                  positive.
	 * @throws ArithmeticException      if the calculate process happens to be any
	 *                                  overflows error.
	 */
	public static long lcm(long a, long b) {
		long gcd = gcd(a, b);
		return Math.multiplyExact(a / gcd, b);
	}

	/**
	 * Returns a square value of the n^2.
	 * 
	 * @see #ipow(long, long)
	 * @param n The base number to calculate square from.
	 * @return The square value.
	 * @throws ArithmeticException if the calculate process overflow.
	 */
	public static long square(long n) {
		// isqrt(Long.MAX_VALUE) = 3037000499L
		if (n > 3037000499L || n < -3037000499L) {
			throw new ArithmeticException("Result overflow");
		}
		return n * n;
	}

	/**
	 * Returns the value of the first argument raised to the power of the second
	 * argument.
	 * 
	 * @see Math#pow(double, double)
	 * @param n the base number in integer way.
	 * @param e the exponent in natural integer way.
	 * @return the pow raised value of the base value.
	 * @throws IllegalArgumentException if the input arguments are illegal.
	 * @throws ArithmeticException      if the calculate process happens to be any
	 *                                  error.
	 */
	public static long ipow(long n, long e) {
		if (e < 0L) {
			throw new IllegalArgumentException("Negative exponent");
		}
		if (n == 0L && e == 0L) {
			throw new ArithmeticException("Arithmetic of 0^0");
		}
		// Core circle
		long r = 1, tn = n, te = e;
		while (true) {
			if ((te & 1) != 0) {
				r = Math.multiplyExact(r, tn);
			}
			te >>= 1;
			if (te == 0) {
				break;
			}
			// isqrt(Long.MAX_VALUE) = 3037000499L
			if (tn > 3037000499L || tn < -3037000499L) {
				throw new ArithmeticException("Result overflow");
			}
			tn *= tn;
		}
		return r;
	}

	/**
	 * Returns the integer part of the square root calculated from an integer in 64
	 * bits but the sign bit should be zero. For example, inputs 9L and gets 3L,
	 * inputs 10L and gets 3L; inputs -1L when the value is 0XffffffffffffffffL and
	 * gets an {@link IllegalArgumentException}.
	 * 
	 * @see Math#sqrt(double)
	 * @param n An integer in 64 bits to calculate square root from.
	 * @return Positive square root calculated in integer part.
	 * @throws IllegalArgumentException if the input number is negative.
	 */
	public static long isqrt(long n) {
		if (n < 0L) {
			throw new IllegalArgumentException("Negative number");
		}
		if ((n & 0XffffffffL) == n) {
			return (int) Math.sqrt((double) n);
		}
		long x = 0;
		long v = n;
		final long tag = 1L << 62;
		int i = 0;
		while (i < 63) {
			if ((tag >> i) + x + Long.MIN_VALUE <= v + Long.MIN_VALUE) {
				v -= (tag >> i) + x;
				x = (x >> 1) | (tag >> i);
			} else {
				x = x >> 1;
			}
			i += 2;
		}
		return x;
	}

	/**
	 * Returns <code>true</code> if a number is a prime or <code>false</code> if
	 * not.
	 * 
	 * @param n Input number to analyze.
	 * @return <code>true</code> if the input value is a prime else not.
	 */
	public static boolean isPrime(long n) {
		// Special cases
		if (n < 2L) {
			return false;
		}
		if (n == 2L || n == 3L) {
			return true;
		}
		if ((n & 1) == 0) {
			return false;
		}
		// Core circle
		long end = isqrt(n);
		for (long i = 3L; i <= end; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Decomposition an integer in 64 bits as the multiply result of prime numbers
	 * and 1, -1 or 0 as sign factor. For example, number 60=1*2*2*3*5, then the
	 * result array of [1, 2, 2, 3, 5] get for input number 60. For each array, the
	 * first value is the sign number of 1, -1 or 0 for positive number, negative
	 * number and zero by each. Specially, when the number is 1, -1 or 0 then the
	 * array is [1], [-1] or [0] by each.
	 * 
	 * @param n The number to decomposition with who is might a composite number or
	 *          a prime number or a negative number or 1 or 0.
	 * @return Array of the prime factorizations and 1, -1 or 0.
	 */
	public static long[] decPrimes(long n) {
		// Special cases
		if (n > -2 && n < 2) {
			return new long[] { n };
		}
		if (n == Long.MIN_VALUE) {
			long[] primes = new long[64];
			Arrays.fill(primes, 2L);
			primes[0] = -1;
			return primes;
		}
		// Array to contain all primes
		long[] primes = new long[8];
		int c = 0;
		long t = n < 0 ? -n : n;
		primes[c++] = n < 0 ? -1L : 1L;
		// Even number has 2 as decomposition
		while ((t & 1) == 0) {
			// Check array capacity
			if (c == primes.length) {
				primes = Arrays.copyOf(primes, primes.length + 8);
			}
			primes[c++] = 2L;
			t >>= 1;
		}
		// These extra values are odd and could be increase by 2.
		long x = 3L, end = isqrt(t);
		while (x <= end) {
			if (t % x == 0) {
				// Check array capacity
				if (c == primes.length) {
					primes = Arrays.copyOf(primes, primes.length + 8);
				}
				primes[c++] = x;
				t /= x;
				end = isqrt(t);
			} else {
				x += 2L;
			}
		}
		// Has more value when only a bigger prime
		if (t > 1) {
			// Check array capacity
			if (c == primes.length) {
				primes = Arrays.copyOf(primes, primes.length + 1);
			}
			primes[c++] = t;
		}
		return c == primes.length ? primes : Arrays.copyOf(primes, c);
	}

	/**
	 * Decomposition an integer in 32 bits as the multiply result of prime numbers
	 * and 1, -1 or 0 as sign factor. For example, number 60=1*2*2*3*5, then the
	 * result array of [1, 2, 2, 3, 5] get for input number 60. For each array, the
	 * first value is the sign number of 1, -1 or 0 for positive number, negative
	 * number and zero by each. Specially, when the number is 1, -1 or 0 then the
	 * array is [1], [-1] or [0] by each.
	 * 
	 * @param n The number to decomposition with who is might a composite number or
	 *          a prime number or a negative number or 1 or 0.
	 * @return Array of the prime factorizations and 1, -1 or 0.
	 */
	public static int[] decPrimes(int n) {
		// Special cases
		if (n > -2 && n < 2) {
			return new int[] { n };
		}
		if (n == Integer.MIN_VALUE) {
			int[] primes = new int[32];
			Arrays.fill(primes, 2);
			primes[0] = -1;
			return primes;
		}
		// Array to contain all primes
		int[] primes = new int[8];
		int c = 0;
		int t = n < 0 ? -n : n;
		primes[c++] = n < 0 ? -1 : 1;
		// Even number has 2 as decomposition
		while ((t & 1) == 0) {
			// Check array capacity
			if (c == primes.length) {
				primes = Arrays.copyOf(primes, primes.length + 8);
			}
			primes[c++] = 2;
			t >>= 1;
		}
		// These extra values are odd and could be increase by 2.
		int x = 3, end = (int) isqrt(t);
		while (x <= end) {
			if (t % x == 0) {
				// Check array capacity
				if (c == primes.length) {
					primes = Arrays.copyOf(primes, primes.length + 8);
				}
				primes[c++] = x;
				t /= x;
				end = (int) isqrt(t);
			} else {
				x += 2L;
			}
		}
		// Has more value when only a bigger prime
		if (t > 1) {
			// Check array capacity
			if (c == primes.length) {
				primes = Arrays.copyOf(primes, primes.length + 1);
			}
			primes[c++] = t;
		}
		return c == primes.length ? primes : Arrays.copyOf(primes, c);
	}

	/**
	 * Returns the factorial result of a number of n!. The result value should be
	 * overflow of {@link Long#MAX_VALUE} or an {@link ArithmeticException} happens.
	 * 
	 * @param n Number to calculate n!.
	 * @return The factorial of n.
	 * @throws IllegalArgumentException if the input number is negative.
	 * @throws ArithmeticException      if the calculate process happens to be
	 *                                  overflow.
	 */
	public static long factorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Negative number");
		}
		if (n > 20) {
			throw new ArithmeticException("Result over flow");
		}
		long r = 1L;
		int tn = n;
		while (tn > 1) {
			r *= tn;
			tn--;
		}
		return r;
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Maths() {
		super();
	}

}
