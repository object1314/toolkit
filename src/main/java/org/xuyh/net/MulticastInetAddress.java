/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

/**
 * Beans for multiCast addresses regions in #{@link #MIN_IP} and
 * {@link #MAX_IP}.
 * 
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public class MulticastInetAddress implements java.io.Serializable, Comparable<MulticastInetAddress> {

	/**
	 * The minimum multiCast address value.
	 */
	public static final int MIN_IP = 0Xe0000100;

	/**
	 * The maximum multiCast address value.
	 */
	public static final int MAX_IP = 0Xe07fffff;

	/**
	 * Get a multiCast address on IP. Returns <code>null</code> If it's not a
	 * MulticastAddress.
	 */
	public static MulticastInetAddress createFromIp(String ip) {
		if (null == ip)
			return null;
		int intIp = 0;
		try {
			String[] addr = ip.split("\\.");
			intIp |= ((Integer.parseInt(addr[0]) & 0Xff) << 24);
			intIp |= ((Integer.parseInt(addr[1]) & 0Xff) << 16);
			intIp |= ((Integer.parseInt(addr[2]) & 0Xff) << 8);
			intIp |= ((Integer.parseInt(addr[3]) & 0Xff) << 0);
		} catch (Exception e) {
			return null;
		}
		return createFromIp(intIp);
	}

	/**
	 * Get a multiCast address on IP. Returns <code>null</code> If it's not a
	 * MulticastAddress.
	 */
	public static MulticastInetAddress createFromIp(int ip) {
		if (!isMultiIp(ip))
			return null;
		return new MulticastInetAddress(ip);
	}

	/**
	 * Check if an IP address is multiCast address.
	 */
	public static boolean isMultiIp(int intIp) {
		if (Integer.compareUnsigned(intIp, MIN_IP) < 0 || Integer.compareUnsigned(intIp, MAX_IP) > 0)
			return false;
		return true;
	}

	/**
	 * The IP address in Integer way
	 */
	private int ip;

	/**
	 * Create a multiCast address.
	 */
	private MulticastInetAddress(int intIp) {
		super();
		this.ip = intIp;
	}

	/**
	 * Get the IP address.
	 */
	public int getIpValue() {
		return ip;
	}

	/**
	 * Get the IP address in view way.
	 */
	public String getIp() {

		StringBuilder sb = new StringBuilder(15);

		sb.append((ip >> 24) & 0Xff).append('.');
		sb.append((ip >> 16) & 0Xff).append('.');
		sb.append((ip >> 8) & 0Xff).append('.');
		sb.append(ip & 0Xff);

		return sb.toString();
	}

	/**
	 * Get the IP address on bytes.
	 */
	public byte[] getIpAddress() {

		byte[] bytes = new byte[4];
		bytes[0] = (byte) (ip >> 24);
		bytes[1] = (byte) (ip >> 16);
		bytes[2] = (byte) (ip >> 8);
		bytes[3] = (byte) ip;
		return bytes;
	}

	/**
	 * Get the MAC address in long type.
	 */
	public long getMacValue() {
		return (0X01005eL << 24) | (ip & 0XffffffL);
	}

	/**
	 * Get the MAC address in a view way.
	 */
	public String getMac() {
		long longMac = getMacValue();
		final char[] mac = new char[17];
		int cur = 0;
		for (int i = 44; i > -1; i -= 4) {
			if (i != 44)
				mac[cur++] = ':';
			mac[cur++] = (Character.forDigit((int) (longMac >> i) & 0Xf, 16));
		}
		return new String(mac);
	}

	/**
	 * Get the MAC address on bytes. <br />
	 */
	public byte[] getMacAddress() {

		long longMac = getMacValue();
		byte[] bytes = new byte[6];
		bytes[0] = (byte) (longMac >> 40);
		bytes[1] = (byte) (longMac >> 32);
		bytes[2] = (byte) (longMac >> 24);
		bytes[3] = (byte) (longMac >> 16);
		bytes[4] = (byte) (longMac >> 8);
		bytes[5] = (byte) longMac;

		return bytes;
	}

	/**
	 * Compare this address with another one.
	 */
	@Override
	public int compareTo(MulticastInetAddress o) {
		return Integer.compareUnsigned(ip, o.ip);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MulticastInetAddress other = (MulticastInetAddress) obj;
		return ip == other.ip;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ip;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{\"ip\":\"" + getIp() + "\", \"mac\":\"" + getMac() + "\"}";
	}

	/**
	 * This address can be serialized.
	 */
	private static final long serialVersionUID = 3597360712146990013L;

}
