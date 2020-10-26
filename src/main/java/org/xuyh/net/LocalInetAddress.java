/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;

/**
 * Beans on local address fetcher. Supports both on IPv4 and IPv6.
 * 
 * @author XuYanhang
 * @since 2020-10-26
 *
 */
public class LocalInetAddress implements java.io.Serializable, Comparable<LocalInetAddress> {

	/**
	 * Create a {@link LocalInetAddress} from given {@link InetAddress}
	 * 
	 * @param address the INET address known
	 * @return the address or <code>null</code> if failed
	 */
	public static LocalInetAddress createAddress(InetAddress address) {
		if (null == address)
			return null;
		NetworkInterface networkInterface;
		try {
			networkInterface = NetworkInterface.getByInetAddress(address);
		} catch (Exception e) {
			return null;
		}
		for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
			if (address.equals(interfaceAddress.getAddress()))
				return createAddress(networkInterface, interfaceAddress);
		return null;
	}

	/**
	 * Create a {@link LocalInetAddress} from given interface and address
	 * 
	 * @param networkInterface network interface
	 * @param interfaceAddress interface address
	 * @return the address or <code>null</code> if failed
	 */
	public static LocalInetAddress createAddress(NetworkInterface networkInterface, InterfaceAddress interfaceAddress) {
		if (null == networkInterface || null == interfaceAddress)
			return null;
		// Core address
		InetAddress inetAddress = interfaceAddress.getAddress();
		if (null == inetAddress)
			return null;

		if (inetAddress.isLinkLocalAddress())
			return null;

		String name = networkInterface.getName();
		int index = networkInterface.getIndex();
		if (null == name || index < 0)
			return null;
		byte[] mac = null;
		int mtu = -1;
		if (!inetAddress.isLoopbackAddress()) {
			try {
				mac = networkInterface.getHardwareAddress();
				mtu = networkInterface.getMTU();
			} catch (SocketException e) {
				return null;
			}
			// Local
			if (null == mac || mac.length != 6 || mtu < 0)
				return null;
		}

		byte[] address = inetAddress.getAddress();
		if (address.length != 4 && address.length != 16)
			return null;
		byte[] broadcast = null;
		if (null != interfaceAddress.getBroadcast()) {
			broadcast = interfaceAddress.getBroadcast().getAddress();
			if (broadcast.length != 4)
				return null;
		}
		short prefixLength = interfaceAddress.getNetworkPrefixLength();
		if (prefixLength < 0 || prefixLength > address.length << 3)
			return null;
		LocalInetAddress target = new LocalInetAddress();
		target.nicName = name;
		target.nicIndex = index;
		target.nicMac = mac;
		target.nicMtu = mtu;
		target.address = inetAddress;
		target.prefixLength = prefixLength;
		return target;
	}

	/**
	 * Returns all local addresses including IPv4 and IPv6 and some invalid
	 * addresses.
	 * 
	 * @return All local addresses
	 * @throws SocketException if an I/O error occurs in list network interfaces
	 */
	public static List<LocalInetAddress> listAddresses() throws SocketException {
		return listAddresses((Predicate<LocalInetAddress>) null);
	}

	/**
	 * Returns all local addresses on filter passed.
	 * 
	 * @param filter filter to test addresses where only accepted address returns
	 * @return All local addresses on the filter passes
	 * @throws SocketException if an I/O error occurs in list network interfaces
	 */
	public static List<LocalInetAddress> listAddresses(Predicate<LocalInetAddress> filter) throws SocketException {
		List<LocalInetAddress> inetAddrs = new ArrayList<>();
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				LocalInetAddress address = createAddress(networkInterface, interfaceAddress);
				if (null != address && (null == filter || filter.test(address)))
					inetAddrs.add(address);
			}
		}
		return inetAddrs;
	}

	/*
	 * Network Interface Card
	 */
	/** Network Interface Card Name */
	private String nicName;
	/** Network Interface Card Index */
	private int nicIndex;
	/** Media Access Control Address */
	private byte[] nicMac;
	/** Maximum Transmission Unit */
	private int nicMtu;

	/*
	 * Interface address informations
	 */
	/** IP address */
	private InetAddress address;
	/** Network Prefix Length */
	private short prefixLength;

	/**
	 * Create a {@link LocalInetAddress} in empty parameters
	 */
	private LocalInetAddress() {
		super();
	}

	/**
	 * Returns the source address
	 * 
	 * @return the {@link #address}
	 */
	public InetAddress source() {
		return address;
	}

	/**
	 * Returns if the address is a IPv4 address
	 * 
	 * @return if the address is a IPv4 address
	 */
	public boolean isIPv4() {
		return (address instanceof Inet4Address);
	}

	/**
	 * Returns if the address is a IPv6 address
	 * 
	 * @return if the address is a IPv6 address
	 */
	public boolean isIPv6() {
		return (address instanceof Inet6Address);
	}

	/**
	 * Returns the IP version of <code>IPv4</code> or <code>IPv6</code>
	 * 
	 * @return the IP version of <code>IPv4</code> or <code>IPv6</code>
	 */
	public String getVersion() {
		if (address instanceof Inet4Address)
			return "IPv4";
		if (address instanceof Inet6Address)
			return "IPv6";
		return null;
	}

	/**
	 * Returns if the address is a loopback address.
	 * 
	 * @return if the address is a loopback address
	 */
	public boolean isLoopback() {
		return address.isLoopbackAddress();
	}

	/**
	 * Returns the IP address in bytes.
	 * 
	 * @return the IP address
	 */
	public byte[] getIpAddress() {
		return address.getAddress();
	}

	/**
	 * Returns the IP address in string. If it's an IPv4 address, it will be like a
	 * 4 digit number splits in '.'. If it's an IPv6 address, it will be like an 8
	 * hex number splits in ':'.
	 * 
	 * @return the IP address
	 */
	public String getIp() {
		byte[] addr = address.getAddress();
		if (isIPv4())
			return (addr[0] & 0Xff) + "." + (addr[1] & 0Xff) + "." + (addr[2] & 0Xff) + "." + (addr[3] & 0Xff);
		StringBuilder sb = new StringBuilder(39);
		for (int i = 0; i < 8; i++) {
			sb.append(Integer.toHexString(((addr[i << 1] & 0Xff) << 8) | (addr[(i << 1) + 1] & 0Xff)));
			if (i < 7)
				sb.append(':');
		}
		return sb.toString();
	}

	/**
	 * Returns the Mask IP address.
	 * 
	 * @return the Mask IP address
	 */
	public String getMaskIp() {
		if (isIPv4()) {
			int imask = 0Xffffffff << (32 - prefixLength);
			return ((imask >> 24) & 0Xff) + "." + ((imask >> 16) & 0Xff) + "." + ((imask >> 24) & 0Xff) + "."
					+ (imask & 0Xff);
		}
		return null;
	}

	/**
	 * Returns the network prefix length
	 * 
	 * @return the prefixLength
	 */
	public int getPrefixLength() {
		return prefixLength;
	}

	/**
	 * Returns the Network Interface Card Name
	 * 
	 * @return the nicName
	 */
	public String getNicName() {
		return nicName;
	}

	/**
	 * Returns the Network Interface Card Index
	 * 
	 * @return the nicIndex
	 */
	public int getNicIndex() {
		return nicIndex;
	}

	/**
	 * Returns the Media Access Control Address. eturns "00:00:00:00:00:00" for
	 * loopback address.
	 * 
	 * @return the nicMac
	 */
	public byte[] getNicMacAddress() {
		byte[] addr = nicMac;
		if (null == addr)
			return new byte[6];
		return addr.clone();
	}

	/**
	 * Returns the Media Access Control Address. Returns "00:00:00:00:00:00" for
	 * loopback address.
	 * 
	 * @return the nicMac
	 */
	public String getNicMac() {
		byte[] addr = nicMac;
		if (null == addr)
			return "00:00:00:00:00:00";
		final char[] mac = new char[addr.length * 3 - 1];
		int cur = 0;
		for (int i = 0; i < addr.length; i++) {
			if (i != 0)
				mac[cur++] = ':';
			mac[cur++] = (Character.forDigit((addr[i] >> 4) & 0Xf, 16));
			mac[cur++] = (Character.forDigit(addr[i] & 0Xf, 16));
		}
		return new String(mac);
	}

	/**
	 * Returns the Maximum Transmission Unit. Returns -1 for loopback address.
	 * 
	 * @return the nicMtu
	 */
	public int getNicMtu() {
		return nicMtu;
	}

	/**
	 * Compare this address with another one, depends on the address value.
	 */
	@Override
	public int compareTo(LocalInetAddress o) {
		if (this == o)
			return 0;
		byte[] taddr = this.address.getAddress();
		byte[] oaddr = o.address.getAddress();
		if (taddr.length != oaddr.length)
			return taddr.length - oaddr.length;
		for (int i = 0, l = taddr.length; i < l; i++)
			if (taddr[i] != oaddr[i])
				return (taddr[i] & 0Xff) - (oaddr[i] & 0Xff);
		return 0;
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
		LocalInetAddress other = (LocalInetAddress) obj;
		if (null == address ? null != other.address : !address.equals(other.address))
			return false;
		if (!Arrays.equals(nicMac, other.nicMac))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (null == address ? 0 : address.hashCode());
		result = prime * result + Arrays.hashCode(nicMac);
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = getIp() + "/" + prefixLength + "%" + nicIndex + "(" + nicName + ")";
		if (!isLoopback())
			str += " MAC=" + getNicMac() + " MTU=" + nicMtu;
		return str;
	}

	/**
	 * Serialize this address
	 */
	private static final long serialVersionUID = 4344796823796955442L;

}
