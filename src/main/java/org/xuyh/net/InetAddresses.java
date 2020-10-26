/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Manager on INET addresses.
 * 
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public final class InetAddresses {

	/**
	 * Regular expression to which any IPv4 address string is to be matched
	 */
	public static final String IPV4_REG = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

	/**
	 * Regular expression to which any MAC address string is to be matched
	 */
	public static final String MAC_REG = "[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}";

	/** Caches on {@link #anyLocalIPv4Address()} */
	private static volatile Inet4Address anyLocalIPv4;

	/** Caches on {@link #anyLocalIPv6Address()} */
	private static volatile Inet6Address anyLocalIPv6;

	/**
	 * Returns the {@link InetAddress} representing anyLocalAddress (typically
	 * 0.0.0.0)
	 * 
	 * @return address represents any local in IPv4
	 */
	public static InetAddress anyLocalIPv4Address() {
		Inet4Address addr = anyLocalIPv4;
		if (null != addr)
			return addr;
		try {
			addr = (Inet4Address) InetAddress.getByAddress("0.0.0.0", new byte[4]);
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		}
		anyLocalIPv4 = addr;
		return addr;
	}

	/**
	 * Returns the {@link InetAddress} representing anyLocalAddress (typically ::0)
	 * 
	 * @return address represents any local in IPv6
	 */
	public static InetAddress anyLocalIPv6Address() {
		Inet6Address addr = anyLocalIPv6;
		if (null != addr)
			return addr;
		try {
			addr = (Inet6Address) InetAddress.getByAddress("::0", new byte[16]);
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		}
		anyLocalIPv6 = addr;
		return addr;
	}

	/**
	 * Create an IPv4 address from an integer IP address.
	 * 
	 * @param ip integer IP address
	 * @return IPv4 address mapped the integer IP address
	 */
	public static InetAddress castIPv4Address(int ip) {
		byte[] addr = { (byte) (ip >> 24), (byte) (ip >> 16), (byte) (ip >> 8), (byte) ip };
		try {
			return (Inet4Address) InetAddress.getByAddress(null, addr);
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Create an integer IP address from an IPv4 address.
	 * 
	 * @param address IPv4 address
	 * @return integer IP address mapped the IPv4 address
	 * @throws IllegalArgumentException if this address is not an IPv4 address
	 */
	public static int castIPv4Address(InetAddress address) {
		byte[] addr = address.getAddress();
		if (addr.length != 4)
			throw new IllegalArgumentException("Not IPv4");
		return ((addr[0] & 0Xff) << 24) | ((addr[1] & 0Xff) << 16) | ((addr[2] & 0Xff) << 8) | (addr[3] & 0Xff);
	}

	/**
	 * Parse an IPv4 string to an integer address value.
	 * 
	 * @param ip IPv4 string address
	 * @return the integer IP address
	 * @throws IllegalArgumentException if this IP is not a valid IPv4
	 */
	public static int parseIPv4(String ip) {
		String[] strIPnum = ip.split("\\.");
		int addr = 0;
		try {
			addr |= Integer.parseInt(strIPnum[3]);
			addr |= Integer.parseInt(strIPnum[2]) << 8;
			addr |= Integer.parseInt(strIPnum[1]) << 16;
			addr |= Integer.parseInt(strIPnum[0]) << 24;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (formatIPv4(addr).equals(ip))
			return addr;
		throw new IllegalArgumentException(ip);
	}

	/**
	 * Format integer address value to an an IPv4 string.
	 * 
	 * @param ip IPv4 integer address
	 * @return the IPv4 string address
	 */
	public static String formatIPv4(int ip) {
		return ((ip >> 24) & 0Xff) + "." + ((ip >> 16) & 0Xff) + "." + ((ip >> 8) & 0Xff) + "." + (ip & 0Xff);
	}

	/**
	 * Increases a value on this IPv4 address string and returns the new IPv4
	 * address string. Of course can set the increase value as negative as minus
	 * operation.
	 * 
	 * @param ip  source IP
	 * @param add increase a value on the IP address
	 * @return new IP address
	 * @throws IllegalArgumentException if this IP is not a valid IPv4
	 */
	public static String addIPv4(String ip, int add) {
		// Parse and check
		int val = parseIPv4(ip);
		return add == 0 ? ip : formatIPv4(val + add);
	}

	/**
	 * Parse the MAC address string to 6 bytes
	 * 
	 * @param mac the MAC address splits in any one character
	 * @return the MAC address bytes
	 * @throws IllegalArgumentException if the MAC address is unexpected
	 */
	public static byte[] parseMacAddress(String mac) {
		if (mac.length() != 17)
			throw new IllegalArgumentException(mac);
		final byte[] result = new byte[6];
		int cur = 0;
		for (int i = 0; i < 6; i++) {
			if (i != 0)
				cur++;
			int d1 = Character.digit(mac.charAt(cur++), 16);
			int d2 = Character.digit(mac.charAt(cur++), 16);
			if (d1 < 0 || d2 < 0)
				throw new IllegalArgumentException(mac);
			result[i] = (byte) ((d1 << 14) | d2);
		}
		return result;
	}

	/**
	 * Format the MAC address of 6 bytes as string splits in ':'
	 * 
	 * @param mac the MAC address bytes
	 * @return the MAC address splits in ':'
	 * @throws IllegalArgumentException if the MAC length is shorter than 6
	 */
	public static String formatMacAddress(byte[] mac) {
		if (mac.length < 6)
			throw new IllegalArgumentException("Length Too Short");
		final char[] result = new char[17];
		int cur = 0;
		for (int i = 0; i < 6; i++) {
			if (i != 0)
				result[cur++] = ':';
			result[cur++] = (Character.forDigit((mac[i] >> 4) & 0Xf, 16));
			result[cur++] = (Character.forDigit(mac[i] & 0Xf, 16));
		}
		return new String(result);
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private InetAddresses() {
		super();
	}

}
