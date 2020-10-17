/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.util;

import java.util.Arrays;

/**
 * The toolkit to resolve with data on encoder as well as decoder in some cases.
 * Here provides the directly implements on these encoder methods.
 * 
 * @author XuYanhang
 * @since 2020-10-06
 *
 */
public final class Codecs {

	/**
	 * Returns a base64 string from the input data in bytes.
	 *
	 * @see #base64(byte[], int, int)
	 * @param data the source data to encode
	 * @return encoded string in base64 of the input data
	 * @throws NullPointerException if the input data is <code>null</code>
	 */
	public static String base64(byte[] data) {
		return Base64.encode(data, 0, data.length);
	}

	/**
	 * Returns a base64 string from the input data in bytes.
	 * 
	 * @param data   the source data to encode
	 * @param offset the start position of the data to encode
	 * @param len    the length of the data to encode
	 * @return encoded string in base64 of the input data
	 * @throws NullPointerException      if the input data is <code>null</code>
	 * @throws IndexOutOfBoundsException if the data range is illegal
	 */
	public static String base64(byte[] data, int offset, int len) {
		return Base64.encode(data, offset, len);
	}

	/**
	 * Returns the original data from the base64 string.
	 * 
	 * @param base64 the base64 string
	 * @return decoded data from the input base64 string
	 * @throws NullPointerException if the input base64 string is <code>null</code>
	 */
	public static byte[] ofBase64(String base64) {
		return Base64.decode(base64);
	}

	/**
	 * Returns a hex string with only hex chars range in 0-f from the input data in
	 * bytes.
	 * 
	 * @param data the source data to encode
	 * @return encoded string in hex of the input data
	 * @throws NullPointerException if the input data is <code>null</code>
	 * @see #hex(byte[], int, int)
	 */
	public static String hex(byte[] data) {
		return hex(data, 0, data.length);
	}

	/**
	 * Returns a hex string with only hex chars range in 0-f from the input data in
	 * bytes.
	 * 
	 * @param data   the source data to encode
	 * @param offset the start position of the data to encode
	 * @param len    the length of the data to encode
	 * @return encoded string in hex of the input data
	 * @throws NullPointerException      if the input data is <code>null</code>
	 * @throws IndexOutOfBoundsException if the data range is illegal
	 */
	public static String hex(byte[] data, int offset, int len) {

		if (null == data)
			throw new NullPointerException();

		final int to = offset + len;
		if (offset < 0 || to > data.length || offset > to)
			throw new IndexOutOfBoundsException("offset=" + offset + ",len=" + len);
		if (len > (Integer.MAX_VALUE >> 1))
			throw new OutOfMemoryError();

		final char[] cs = new char[len << 1];
		int cur = 0;

		for (int i = offset; i < to; i++) {
			cs[cur++] = Character.forDigit((data[i] >> 4) & 0Xf, 16);
			cs[cur++] = Character.forDigit(data[i] & 0Xf, 16);
		}

		return new String(cs);
	}

	/**
	 * Returns the original data from the hex string.
	 * 
	 * @param hex the hex string
	 * @return decoded data from the input hex string
	 * @throws NullPointerException if the input hex is <code>null</code>
	 */
	public static byte[] ofHex(CharSequence hex) {

		if (null == hex)
			return null;

		final int len = hex.length();
		final byte[] data = new byte[len >> 1];
		int cur = 0;

		int a = -1;
		int i = 0;
		while (i < len) {
			char c = hex.charAt(i++);
			int digit = Character.digit(c, 16);
			// Jump not a digit.
			if (digit < 0)
				continue;
			// Find first digit
			if (a < 0) {
				a = digit;
				continue;
			}
			// Get the both digits to build byte value
			data[cur++] = (byte) ((a << 4) | digit);
			a = -1;
		}
		// Result
		return cur == data.length ? data : Arrays.copyOf(data, cur);
	}

	/**
	 * Returns MD5(Message-Digest Algorithm) bytes from the input data in bytes.
	 * 
	 * @param data the source data to encode
	 * @return encoded MD5 in bytes of the input data
	 * @throws NullPointerException if the input data is <code>null</code>
	 * @see #md5(byte[], int, int)
	 */
	public static byte[] md5(byte[] data) {
		return MD5.calHashes(data, 0, data.length);
	}

	/**
	 * Returns MD5(Message-Digest Algorithm) bytes from the input data in bytes.
	 * 
	 * @param data   the source data to encode
	 * @param offset the start position of the data to encode
	 * @param len    the length of the data to encode
	 * @return encoded MD5 in bytes of the input data
	 * @throws NullPointerException      if the input data is <code>null</code>
	 * @throws IndexOutOfBoundsException if the data range is illegal
	 */
	public static byte[] md5(byte[] data, int offset, int len) {
		return MD5.calHashes(data, offset, len);
	}

	/**
	 * Returns SHA256 hash bytes from the input data in bytes.
	 * 
	 * @param data the source data to encode
	 * @return encoded SHA256 hash bytes of the input data
	 * @throws NullPointerException if the input data is <code>null</code>
	 * @see #sha256(byte[], int, int)
	 */
	public static byte[] sha256(byte[] data) {
		return SHA256.toHashes(data, 0, data.length);
	}

	/**
	 * Returns SHA256 hash bytes from the input data in bytes.
	 * 
	 * @param data   the source data to encode
	 * @param offset the start position of the data to encode
	 * @param len    the length of the data to encode
	 * @return encoded SHA256 hash bytes of the input data
	 * @throws NullPointerException      if the input data is <code>null</code>
	 * @throws IndexOutOfBoundsException if the data range is illegal
	 */
	public static byte[] sha256(byte[] data, int offset, int len) {
		return SHA256.toHashes(data, offset, len);
	}

	/**
	 * The tool to encode and decode on base64.
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class Base64 {

		/**
		 * The encode value based on 64 chars.
		 */
		static final char[] EN_CS;

		/**
		 * The decoder value based on 128 bytes.
		 */
		static final byte[] DE_BS;

		/**
		 * Use static block initial-method. <br>
		 */
		static {
			EN_CS = new char[] { // Begin initialize
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 00-07(8)
					'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 10-17(8)
					'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 20-27(8)
					'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 30-37(8)
					'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 40-47(8)
					'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 50-57(8)
					'w', 'x', 'y', 'z', '0', '1', '2', '3', // 60-67(8)
					'4', '5', '6', '7', '8', '9', '+', '/' // 70-77(8)
			};
			DE_BS = new byte[] { // Begin initialize
					-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 00-0f
					-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 10-1f
					-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, // 20-2f
					52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, // 30-3f
					-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // 40-4f
					15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, // 50-5f
					-1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, // 60-6f
					41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 // 70-7f
			};
		}

		/**
		 * Encode method.
		 */
		static String encode(byte[] data, int offset, int len) {

			if (null == data)
				throw new NullPointerException();

			final int to = offset + len;
			if (offset < 0 || to > data.length || offset > to)
				throw new IndexOutOfBoundsException("offset=" + offset + ",len=" + len);
			if (len > ((Integer.MAX_VALUE >> 2) * 3 - 2))
				throw new OutOfMemoryError();

			final char[] cs = new char[((len + 2) / 3) << 2];
			int cur = 0;

			int i = offset;
			while (i < to) {
				// 3n-2(n=1,2,3...)
				int b1 = data[i++] & 0Xff;
				if (i == to) {
					cs[cur++] = EN_CS[b1 >> 2];
					cs[cur++] = EN_CS[(b1 & 0X3) << 4];
					cs[cur++] = '=';
					cs[cur++] = '=';
					break;
				}
				// 3n-1(n=1,2,3...)
				int b2 = data[i++] & 0xff;
				if (i == to) {
					cs[cur++] = EN_CS[b1 >> 2];
					cs[cur++] = EN_CS[((b1 & 0X3) << 4) | ((b2 & 0Xf0) >> 4)];
					cs[cur++] = EN_CS[(b2 & 0X0f) << 2];
					cs[cur++] = '=';
					break;
				}
				// 3n(n=1,2,3...)
				int b3 = data[i++] & 0xff;
				cs[cur++] = EN_CS[b1 >> 2];
				cs[cur++] = EN_CS[((b1 & 0X03) << 4) | ((b2 & 0Xf0) >> 4)];
				cs[cur++] = EN_CS[((b2 & 0X0f) << 2) | ((b3 & 0Xc0) >> 6)];
				cs[cur++] = EN_CS[b3 & 0X3f];
			}

			return new String(cs);
		}

		/**
		 * Decode method.
		 */
		static byte[] decode(CharSequence base64) {

			if (null == base64)
				throw new NullPointerException();

			final int len = base64.length();

			final byte[] data = new byte[(len >> 2) * 3 + (((len & 0X3) * 3 + 3) >> 2)];
			int cur = 0;

			int b1 = -1, b2 = -1, b3 = -1, b4 = -1;
			int i = 0;
			while (i < len) {
				// Analyze char 1
				b1 = -1;
				while (i < len && b1 == -1) {
					b1 = base64.charAt(i++);
					// Shouldn't in normal base64
					if (b1 == '=') {
						b1 = -1;
						break;
					}
					b1 = b1 < 0X80 ? DE_BS[b1] : -1;
				}
				if (b1 == -1)
					break;

				// Analyze char 2
				b2 = -1;
				while (i < len && b2 == -1) {
					b2 = base64.charAt(i++);
					// Shouldn't in normal base64
					if (b2 == '=') {
						b2 = -1;
						break;
					}
					b2 = b2 < 0X80 ? DE_BS[b2] : -1;
				}
				if (b2 == -1)
					break;
				data[cur++] = (byte) ((b1 << 2) | ((b2 & 0X30) >> 4));

				// Analyze char 3
				b3 = -1;
				while (i < len && b3 == -1) {
					b3 = base64.charAt(i++);
					if (b3 == '=') {
						b3 = -1;
						break;
					}
					b3 = b3 < 0X80 ? DE_BS[b3] : -1;
				}
				if (b3 == -1)
					break;
				data[cur++] = (byte) (((b2 & 0X0f) << 4) | ((b3 & 0X3c) >> 2));

				// Analyze char 4
				b4 = -1;
				while (i < len && b4 == -1) {
					b4 = base64.charAt(i++);
					if (b4 == '=') {
						b4 = -1;
						break;
					}
					b4 = b4 < 0X80 ? DE_BS[b4] : -1;
				}
				if (b4 == -1)
					break;
				data[cur++] = (byte) (((b3 & 0X03) << 6) | b4);
			}

			// Result
			return cur == data.length ? data : Arrays.copyOf(data, cur);
		}

	}

	/**
	 * The calculator on MD5(Message-Digest Algorithm).
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class MD5 {

		/**
		 * Calculate the MD5 hashes from a input data.
		 */
		static byte[] calHashes(byte[] data, int offset, int len) {
			if (null == data)
				throw new NullPointerException();
			if (offset < 0 || offset + len > data.length || offset > offset + len)
				throw new IndexOutOfBoundsException("offset=" + offset + ",len=" + len);
			MD5 md5 = new MD5();
			md5.calGroupsHashes(data, offset, len);
			md5.calExtraHashes(data, offset, len);
			return md5.produceResult();
		}

		/**
		 * Initialize seed of initial hashes.
		 */
		static final int[] SEED_HASHES;

		/**
		 * Initialize table of round constants as T.
		 */
		static final int[] T;

		/**
		 * Initialize table of round constants as S.
		 */
		static final int[] S;

		/**
		 * Initialize table of round constants as M.
		 */
		static final int[] M;

		/**
		 * Use static block initial-method. <br>
		 */
		static {
			SEED_HASHES = new int[] { 0X67452301, 0Xefcdab89, 0X98badcfe, 0X10325476 };
			T = new int[] { // Begin initialize
					0Xd76aa478, 0Xe8c7b756, 0X242070db, 0Xc1bdceee, 0Xf57c0faf, 0X4787c62a, 0Xa8304613, 0Xfd469501, // R0
					0X698098d8, 0X8b44f7af, 0Xffff5bb1, 0X895cd7be, 0X6b901122, 0Xfd987193, 0Xa679438e, 0X49b40821, // R1
					0Xf61e2562, 0Xc040b340, 0X265e5a51, 0Xe9b6c7aa, 0Xd62f105d, 0X02441453, 0Xd8a1e681, 0Xe7d3fbc8, // R2
					0X21e1cde6, 0Xc33707d6, 0Xf4d50d87, 0X455a14ed, 0Xa9e3e905, 0Xfcefa3f8, 0X676f02d9, 0X8d2a4c8a, // R3
					0Xfffa3942, 0X8771f681, 0X6d9d6122, 0Xfde5380c, 0Xa4beea44, 0X4bdecfa9, 0Xf6bb4b60, 0Xbebfbc70, // R4
					0X289b7ec6, 0Xeaa127fa, 0Xd4ef3085, 0X04881d05, 0Xd9d4d039, 0Xe6db99e5, 0X1fa27cf8, 0Xc4ac5665, // R5
					0Xf4292244, 0X432aff97, 0Xab9423a7, 0Xfc93a039, 0X655b59c3, 0X8f0ccc92, 0Xffeff47d, 0X85845dd1, // R6
					0X6fa87e4f, 0Xfe2ce6e0, 0Xa3014314, 0X4e0811a1, 0Xf7537e82, 0Xbd3af235, 0X2ad7d2bb, 0Xeb86d391 // R7
			};
			S = new int[] { // Begin initialize
					7, 12, 17, 22, 7, 12, 17, 22, // R0
					7, 12, 17, 22, 7, 12, 17, 22, // R1
					5,  9, 14, 20, 5,  9, 14, 20, // R2
					5,  9, 14, 20, 5,  9, 14, 20, // R3
					4, 11, 16, 23, 4, 11, 16, 23, // R4
					4, 11, 16, 23, 4, 11, 16, 23, // R5
					6, 10, 15, 21, 6, 10, 15, 21, // R6
					6, 10, 15, 21, 6, 10, 15, 21// R7
			};
			M = new int[] { // Begin initialize
					 0,  1,  2,  3,  4,  5,  6,  7, // R0
					 8,  9, 10, 11, 12, 13, 14, 15, // R1
					 1,  6, 11,  0,  5, 10, 15,  4, // R2
					 9, 14,  3,  8, 13,  2,  7, 12, // R3
					 5,  8, 11, 14,  1,  4,  7, 10, // R4
					13,  0,  3,  6,  9, 12, 15,  2, // R5
					 0,  7, 14,  5, 12,  3, 10,  1, // R6
					 8, 15,  6, 13,  4, 11,  2,  9 // R7
			};
		}

		/** The hashes */
		final int[] hashes = SEED_HASHES.clone();

		/**
		 * Create a MD5 calculate engine.
		 */
		MD5() {
			super();
		}

		/**
		 * STEP ONE: Resolve these whole groups: <br>
		 * In this step, the all whole groups in 512 bits from the original data would
		 * be resolved one by one.
		 * 
		 */
		void calGroupsHashes(byte[] data, int offset, int len) {
			// Divide group on 512 bits
			int groupCount = len >> 6;
			// Resolve each group
			for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
				hashGroup(data, offset, groupIndex);
			}
		}

		/**
		 * STEP ONE: Resolve the extra data as a special group: <br>
		 * In this step, these data not in groups would be resolve as a special group
		 * data.
		 */
		void calExtraHashes(byte[] data, int offset, int len) {
			int extraLen = len & 0X3f;
			byte[] tempBytes = new byte[64];
			System.arraycopy(data, len - extraLen + offset, tempBytes, 0, extraLen);
			if (extraLen != 56)
				tempBytes[extraLen] = (byte) (1 << 7);
			if (extraLen > 56) {
				hashGroup(tempBytes, 0, 0);
				for (int i = 0; i < 56; i++)
					tempBytes[i] = 0;
			}
			long l = ((long) len) << 3;
			for (int i = 56; i < 64; i++) {
				tempBytes[i] = (byte) (l & 0Xff);
				l >>= 8;
			}
			hashGroup(tempBytes, 0, 0);
		}

		/**
		 * STEP THREE: Build the result: <br>
		 * Produce the final hash results.
		 */
		byte[] produceResult() {
			byte[] result = new byte[16];
			for (int i = 0, j = 0; j < 4; j++) {
				result[i++] = (byte) hashes[j];
				result[i++] = (byte) (hashes[j] >> 8);
				result[i++] = (byte) (hashes[j] >> 16);
				result[i++] = (byte) (hashes[j] >> 24);
			}
			// Clear trace
			System.arraycopy(SEED_HASHES, 0, hashes, 0, 4);
			return result;
		}

		/**
		 * Calculate hashes in a group.
		 */
		private void hashGroup(byte[] data, int offset, int groupIndex) {
			/*
			 * Cast data in this group
			 */
			int ti = (groupIndex << 6) + offset;
			int[] group = new int[16];
			for (int i = 0; i < 16; i++) {
				group[i] |= data[ti++] & 0Xff;
				group[i] |= (data[ti++] & 0Xff) << 8;
				group[i] |= (data[ti++] & 0Xff) << 16;
				group[i] |= (data[ti++] & 0Xff) << 24;
			}
			/*
			 * Fetch current hash values as seed
			 */
			int a = hashes[0], b = hashes[1], c = hashes[2], d = hashes[3];
			/*
			 * Cursor index for calculate rounds
			 */
			int ci = 0;
			/*
			 * Round 1 FF
			 */
			while (ci < 16) {
				a = FF(a, b, c, d, group[M[ci]], S[ci], T[ci]);
				ci++;
				d = FF(d, a, b, c, group[M[ci]], S[ci], T[ci]);
				ci++;
				c = FF(c, d, a, b, group[M[ci]], S[ci], T[ci]);
				ci++;
				b = FF(b, c, d, a, group[M[ci]], S[ci], T[ci]);
				ci++;
			}
			/*
			 * Round 2 GG
			 */
			while (ci < 32) {
				a = GG(a, b, c, d, group[M[ci]], S[ci], T[ci]);
				ci++;
				d = GG(d, a, b, c, group[M[ci]], S[ci], T[ci]);
				ci++;
				c = GG(c, d, a, b, group[M[ci]], S[ci], T[ci]);
				ci++;
				b = GG(b, c, d, a, group[M[ci]], S[ci], T[ci]);
				ci++;
			}
			/*
			 * Round 3 HH
			 */
			while (ci < 48) {
				a = HH(a, b, c, d, group[M[ci]], S[ci], T[ci]);
				ci++;
				d = HH(d, a, b, c, group[M[ci]], S[ci], T[ci]);
				ci++;
				c = HH(c, d, a, b, group[M[ci]], S[ci], T[ci]);
				ci++;
				b = HH(b, c, d, a, group[M[ci]], S[ci], T[ci]);
				ci++;
			}
			/*
			 * Round 4 II
			 */
			while (ci < 64) {
				a = II(a, b, c, d, group[M[ci]], S[ci], T[ci]);
				ci++;
				d = II(d, a, b, c, group[M[ci]], S[ci], T[ci]);
				ci++;
				c = II(c, d, a, b, group[M[ci]], S[ci], T[ci]);
				ci++;
				b = II(b, c, d, a, group[M[ci]], S[ci], T[ci]);
				ci++;
			}
			/*
			 * Clear trace
			 */
			Arrays.fill(group, 0);
			/*
			 * Add results to hash
			 */
			hashes[0] += a;
			hashes[1] += b;
			hashes[2] += c;
			hashes[3] += d;
		}

		/**
		 * For round 1
		 */
		private static int FF(int a, int b, int c, int d, int m, int s, int t) {
			a = a + ((b & c) | ((~b) & d)) + m + t;
			a = (a << s) | (a >>> (32 - s));
			return a + b;
		}

		/**
		 * For round 2
		 */
		private static int GG(int a, int b, int c, int d, int m, int s, int t) {
			a = a + ((b & d) | (c & (~d))) + m + t;
			a = (a << s) | (a >>> (32 - s));
			return a + b;
		}

		/**
		 * For round 3
		 */
		private static int HH(int a, int b, int c, int d, int m, int s, int t) {
			a = a + (b ^ c ^ d) + m + t;
			a = (a << s) | (a >>> (32 - s));
			return a + b;
		}

		/**
		 * For round 4
		 */
		private static int II(int a, int b, int c, int d, int m, int s, int t) {
			a = a + (c ^ (b | (~d))) + m + t;
			a = (a << s) | (a >>> (32 - s));
			return a + b;
		}

	}

	/**
	 * The calculator on SHA-256. SHA-256 is a sub-case on SHA-2(Secure Hash
	 * Algorithm 2). In this calculator, The calculate trace can be considered as be
	 * secure.
	 * <p>
	 * Note: All variables are unsigned 32 bits and wrap modulo 232 when calculating
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class SHA256 {

		/**
		 * Calculate the MD5 hashes from a input data.
		 */
		static byte[] toHashes(byte[] data, int offset, int len) {
			if (null == data)
				throw new NullPointerException();
			if (offset < 0 || offset + len > data.length || offset > offset + len)
				throw new IndexOutOfBoundsException("offset=" + offset + ",len=" + len);
			SHA256 sha256 = new SHA256();
			sha256.actionInit(data, offset, len);
			sha256.calHashes();
			return sha256.produceResult();
		}

		/**
		 * Initialize seed of initial hashes <br>
		 * (first 32 bits of the fractional parts of the square roots of the first 8
		 * primes 2,3,5,7,11,13,17,19).
		 * 
		 */
		static final int[] SEED_HASHES;

		/**
		 * Initialize table of round constants <br>
		 * (first 32 bits of the fractional parts of the cube roots of the first 64
		 * primes 2,3,5,7,11..311).
		 */
		static final int[] R;

		/**
		 * Use static block initial-method. <br>
		 */
		static {
			SEED_HASHES = new int[] { // Begin initialize
					0X6a09e667, 0Xbb67ae85, 0X3c6ef372, 0Xa54ff53a, 0X510e527f, 0X9b05688c, 0X1f83d9ab, 0X5be0cd19 // R0
			};
			R = new int[] { // Begin initialize
					0X428a2f98, 0X71374491, 0Xb5c0fbcf, 0Xe9b5dba5, 0X3956c25b, 0X59f111f1, 0X923f82a4, 0Xab1c5ed5, // R0
					0Xd807aa98, 0X12835b01, 0X243185be, 0X550c7dc3, 0X72be5d74, 0X80deb1fe, 0X9bdc06a7, 0Xc19bf174, // R1
					0Xe49b69c1, 0Xefbe4786, 0X0fc19dc6, 0X240ca1cc, 0X2de92c6f, 0X4a7484aa, 0X5cb0a9dc, 0X76f988da, // R2
					0X983e5152, 0Xa831c66d, 0Xb00327c8, 0Xbf597fc7, 0Xc6e00bf3, 0Xd5a79147, 0X06ca6351, 0X14292967, // R3
					0X27b70a85, 0X2e1b2138, 0X4d2c6dfc, 0X53380d13, 0X650a7354, 0X766a0abb, 0X81c2c92e, 0X92722c85, // R4
					0Xa2bfe8a1, 0Xa81a664b, 0Xc24b8b70, 0Xc76c51a3, 0Xd192e819, 0Xd6990624, 0Xf40e3585, 0X106aa070, // R5
					0X19a4c116, 0X1e376c08, 0X2748774c, 0X34b0bcb5, 0X391c0cb3, 0X4ed8aa4a, 0X5b9cca4f, 0X682e6ff3, // R6
					0X748f82ee, 0X78a5636f, 0X84c87814, 0X8cc70208, 0X90befffa, 0Xa4506ceb, 0Xbef9a3f7, 0Xc67178f2 // R7
			};
		}

		/** The message after pre processing */
		int[] message;
		/** Calculate temporary words */
		int[] w;
		/** The hashes */
		int[] hashes;

		/**
		 * Create a SHA256 calculate engine.
		 */
		SHA256() {
			super();
		}

		/**
		 * STEP ONE: Initialize the fields: <br>
		 * In pre-processing: <br>
		 * append the bit '1' to the message <br>
		 * append k bits '0', where k is the minimum number >= 0 such that the resulting
		 * message length (in bits) is congruent to 448(mod 512) <br>
		 * append length of message (before pre-processing), in bits, as 64-bit
		 * big-endian integer <br>
		 */
		void actionInit(byte[] data, int offset, int len) {
			final int ilen = (((len + 8) >>> 6) << 4) + 16;
			final int to = offset + len;
			this.message = new int[ilen];
			int i = 0, j = 0;
			while (true) {
				if (i == to) {
					this.message[j] |= 0X80 << 24;
					break;
				}
				this.message[j] |= (data[i++] & 0Xff) << 24;
				if (i == to) {
					this.message[j] |= 0X80 << 16;
					break;
				}
				this.message[j] |= (data[i++] & 0Xff) << 16;
				if (i == to) {
					this.message[j] |= 0X80 << 8;
					break;
				}
				this.message[j] |= (data[i++] & 0Xff) << 8;
				if (i == to) {
					this.message[j] |= 0X80;
					break;
				}
				this.message[j] |= data[i++] & 0Xff;
				j++;
			}
			this.message[ilen - 2] = len >>> 29;
			this.message[ilen - 1] = len << 3;
			this.w = new int[64];
			this.hashes = SEED_HASHES.clone();
		}

		/**
		 * STEP TWO: Action calculate: <br>
		 * Process the message in successive 512-bit chunks. <br>
		 */
		void calHashes() {
			int len = message.length;
			/*
			 * for each chunk:
			 */
			for (int chunk = 0; chunk < len; chunk += 16) {
				/*
				 * break chunk into sixteen 32-bit big-endian words w[0..15]:
				 */
				for (int i = 0; i < 16; i++) {
					w[i] = message[chunk + i];
				}
				/*
				 * Extend the sixteen 32-bit words into sixty-four 32-bit words:
				 */
				for (int i = 16; i < 64; i++) {
					int s0 = ror(w[i - 15], 7) ^ ror(w[i - 15], 18) ^ (w[i - 15] >>> 3);
					int s1 = ror(w[i - 2], 17) ^ ror(w[i - 2], 19) ^ (w[i - 2] >>> 10);
					w[i] = s0 + w[i - 16] + s1 + w[i - 7];
				}
				/*
				 * Initialize hash value for this chunk:
				 */
				int a = hashes[0];
				int b = hashes[1];
				int c = hashes[2];
				int d = hashes[3];
				int e = hashes[4];
				int f = hashes[5];
				int g = hashes[6];
				int h = hashes[7];
				/*
				 * Main loop:
				 */
				for (int i = 0; i < 64; i++) {
					int t1 = h + (ror(e, 6) ^ ror(e, 11) ^ ror(e, 25)) + ((e & f) ^ ((~e) & g)) + R[i] + w[i];
					int t2 = (ror(a, 2) ^ ror(a, 13) ^ ror(a, 22)) + ((a & b) ^ (a & c) ^ (b & c));
					h = g;
					g = f;
					f = e;
					e = d + t1;
					d = c;
					c = b;
					b = a;
					a = t1 + t2;
				}
				/*
				 * Add this chunk's hash to result so far:
				 */
				hashes[0] += a;
				hashes[1] += b;
				hashes[2] += c;
				hashes[3] += d;
				hashes[4] += e;
				hashes[5] += f;
				hashes[6] += g;
				hashes[7] += h;
			}
			/*
			 * Clear trace
			 */
			Arrays.fill(w, 0);
			Arrays.fill(message, 0);
			w = null;
			message = null;
		}

		/**
		 * STEP THREE: Build the result: <br>
		 * Produce the final hash value in big-endian. <br>
		 * digest = hash = h0 append h1 append h2 append h3 append h4 append h5 append
		 * h6 append h7. <br>
		 */
		byte[] produceResult() {
			byte[] result = new byte[32];
			for (int i = 0, j = 0; j < 8; j++) {
				result[i++] = (byte) (hashes[j] >> 24);
				result[i++] = (byte) (hashes[j] >> 16);
				result[i++] = (byte) (hashes[j] >> 8);
				result[i++] = (byte) hashes[j];
			}
			// Clear trace
			Arrays.fill(hashes, 0);
			hashes = null;
			return result;
		}

		/**
		 * Right rotate: <br>
		 * Bits shifted out of the right hand, or low-order, side reenter on the left,
		 * or high-order for a integer. <br>
		 */
		private static int ror(int v, int count) {
			return (v >>> count) | (v << -count);
		}

	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Codecs() {
		super();
	}

}
