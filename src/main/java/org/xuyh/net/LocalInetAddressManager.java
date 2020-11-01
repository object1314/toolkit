package org.xuyh.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Predicate;

public class LocalInetAddressManager {

	private volatile ArrayList<LocalInetAddress> addresses;

	/**
	 * 
	 */
	public LocalInetAddressManager() {
		super();
		this.addresses = new ArrayList<>();
	}

	public void load() throws Exception {
		ArrayList<LocalInetAddress> addresses = new ArrayList<>(1);
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			Iterator<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses().iterator();
			while (interfaceAddresses.hasNext()) {
				InterfaceAddress interfaceAddress = interfaceAddresses.next();
				InetAddress inetAddress = interfaceAddress.getAddress();
				if (null == inetAddress || !(inetAddress instanceof Inet4Address)) {
					continue;
				}
			}
		}
		addresses.trimToSize();
		this.addresses = addresses;
	}

	/**
	 * Find all addresses from last load.
	 * 
	 * @return all addresses.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<LocalInetAddress> getAddrs() {
		return (ArrayList<LocalInetAddress>) addresses.clone();
	}

	/**
	 * Find addresses on filter from last load.
	 * 
	 * @param filter the filter on the addresses
	 * @return the addresses match on the filter
	 */
	public ArrayList<LocalInetAddress> getAddrs(Predicate<LocalInetAddress> filter) {
		if (null == filter)
			throw new NullPointerException("filter");
		ArrayList<LocalInetAddress> source = this.addresses;
		ArrayList<LocalInetAddress> target = new ArrayList<>(source.size());
		for (int i = 0, s = source.size(); i < s; i++) {
			LocalInetAddress addr = source.get(i);
			if (filter.test(addr))
				target.add(addr);
		}
		return target;
	}

}
