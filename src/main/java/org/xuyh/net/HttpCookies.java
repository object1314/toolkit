/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The cookies for HTTP client. Managed on cookies CURD operations.
 * 
 * @author XuYanhang
 * @since 2020-11-01
 * @see HttpClient
 *
 */
public final class HttpCookies implements java.io.Serializable {

	/** The Cookies Resource */
	private LinkedList<Cookie> cookies;

	/**
	 * Initialize the cookies.
	 */
	public HttpCookies() {
		super();
		this.cookies = new LinkedList<>();
	}

	/**
	 * Clear the cookies.
	 */
	public void clearCookies() {
		synchronized (cookies) {
			cookies.clear();
		}
	}

	/**
	 * Clear the cookies those has been expired.
	 */
	public void clearExpiredCookies() {
		synchronized (cookies) {
			Iterator<Cookie> it = cookies.iterator();
			while (it.hasNext()) {
				if (it.next().isExpired()) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Create the Cookie header values for HTTP request. If cookie doesn't exist
	 * then an empty List gets.
	 * 
	 * @return the cookie values for Cookie header of HTTP request
	 */
	public List<String> createCookieHeaderValues() {
		clearExpiredCookies();
		LinkedHashMap<String, StringBuilder> builders = new LinkedHashMap<>();
		synchronized (cookies) {
			for (Cookie cookie : cookies) {
				StringBuilder builder = builders.get(cookie.version);
				if (null == builder) {
					builder = new StringBuilder();
					builders.put(cookie.version, builder);
					if (null != cookie.version) {
						builder.append("$Version=").append(cookie.version).append("; ");
					}
				} else {
					builder.append("; ");
				}
				builder.append(cookie.name).append('=').append(cookie.value);
				if (null != cookie.path) {
					builder.append("; $Path=").append(cookie.path);
				}
				if (null != cookie.domain) {
					builder.append("; $Domain=").append(cookie.domain);
				}
			}
		}
		ArrayList<String> cookieValues = new ArrayList<>(builders.size());
		for (StringBuilder builder : builders.values())
			cookieValues.add(builder.toString());
		return cookieValues;
	}

	/**
	 * Refresh the cookies from Set-Cookie header values.
	 * 
	 * @param values the cookies to set from Set-Cookie header of Http response
	 */
	public void refreshFromSetCookieHeaderValues(List<String> values) {
		clearExpiredCookies();
		if (null == values || values.isEmpty()) {
			return;
		}
		for (String value : values) {
			refreshFromSetCookieHeaderValue(value);
		}
	}

	/**
	 * Refresh the cookies from a Set-Cookie header value.
	 */
	private void refreshFromSetCookieHeaderValue(String value) {
		if (null == value || value.isEmpty()) {
			return;
		}
		String[] s = value.split(";");
		if (s.length < 1) {
			return;
		}
		Cookie cookie = null;
		for (int i = 0; i < s.length; i++) {
			String t = s[i].trim();
			String n;
			String v;
			int eq = t.indexOf('=');
			if (eq >= 0) {
				n = t.substring(0, eq).trim();
				v = t.substring(eq + 1).trim();
			} else {
				n = t;
				v = null;
			}
			// Parse name-value pair
			if (i == 0) {
				// Cookie Name And Value
				if (n.isEmpty() || n.charAt(0) == '$' || null == v)
					return;
				cookie = new Cookie(n, v);
			} else {
				cookie.setCookieAV(n, v);
			}
		}
		putCookie(cookie);
	}

	/**
	 * Add a cookie into the cookies.
	 * 
	 * @param cookie the cookie to add
	 */
	private void putCookie(Cookie cookie) {
		synchronized (cookies) {
			Iterator<Cookie> it = cookies.iterator();
			while (it.hasNext()) {
				Cookie old = it.next();
				if (!cookie.name.equals(old.name)) {
					continue;
				}
				if (!Objects.equals(cookie.version, old.version)) {
					continue;
				}
				if (!Objects.equals(cookie.domain, old.domain)) {
					continue;
				}
				if (!Objects.equals(cookie.path, old.path)) {
					continue;
				}
				it.remove();
			}
			cookies.add(cookie);
		}
	}

	/**
	 * Returns a copy of all cookies
	 * 
	 * @return the cookies copy
	 */
	@SuppressWarnings("unchecked")
	public List<Cookie> getCookies() {
		synchronized (cookies) {
			return (List<Cookie>) cookies.clone();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		synchronized (cookies) {
			for (Cookie cookie : cookies) {
				sb.append(cookie.toString()).append('\n');
			}
		}
		return sb.toString();
	}

	/**
	 * According <a href="https://tools.ietf.org/html/rfc2109">RFC 2109</a> and
	 * <a href="https://tools.ietf.org/html/rfc6265">RFC 6265</a>. Creates a cookie,
	 * a small amount of information sent by a servlet to a Web browser, saved by
	 * the browser, and later sent back to the server. A cookie's value can uniquely
	 * identify a client, so cookies are commonly used for session management. <br>
	 * This bean defines the HTTP Cookie and Set-Cookie header fields. These header
	 * fields can be used by HTTP servers to store state (called cookies) at HTTP
	 * user agents, letting the servers maintain a stateful session over the mostly
	 * stateless HTTP protocol. Although cookies have many historical infelicities
	 * that degrade their security and privacy, the Cookie and Set-Cookie header
	 * fields are widely used on the Internet.
	 * 
	 * @author XuYanhang
	 */
	public static final class Cookie implements java.io.Serializable {

		/**
		 * <code>NAME=VALUE</code> ... name of the cookie. <br>
		 * Required. The name of the state information ("cookie") is NAME, and its value
		 * is VALUE. NAMEs that begin with $ are reserved for other uses and must not be
		 * used by applications.
		 */
		private final String name;

		/**
		 * <code>NAME=VALUE</code> ... value of the cookie. <br>
		 * The VALUE is opaque to the user agent and may be anything the origin server
		 * chooses to send, possibly in a server-selected printable ASCII encoding.
		 * "Opaque" implies that the content is of interest and relevance only to the
		 * origin server. The content may, in fact, be readable by anyone that examines
		 * the Set-Cookie header.
		 */
		private String value;

		/**
		 * <code>;Version=version</code> ... means
		 * <a href="https://tools.ietf.org/html/rfc2109">RFC 2109</code> style. <br>
		 * The Version attribute, a decimal integer, identifies to which version of the
		 * state management specification the cookie conforms. For this specification,
		 * Version=1 applies.
		 */
		private String version;

		/**
		 * <code>;Comment=comment</code> ... describes cookie's use. <br>
		 * Because cookies can contain private information about a user, the Cookie
		 * attribute allows an origin server to document its intended use of a cookie.
		 * The user can inspect the information to decide whether to initiate or
		 * continue a session with this cookie.
		 */
		private String comment;

		/**
		 * <code>;Domain=domain</code> ... domain that sees cookie
		 */
		private String domain;

		/**
		 * <code>;Max-Age=delta-seconds</code> ... cookies auto-expire
		 */
		private long maxAge = -1;

		/**
		 * <code>;Path=path</code> ... URLs that see the cookie
		 */
		private String path;

		/**
		 * <code>;Secure</code> ... e.g. use SSL
		 */
		private boolean secure;

		/**
		 * <code>;HttpOnly</code> ... Not in cookie specs, but supported by browsers.
		 */
		private boolean httpOnly;

		/**
		 * System time when the cookie created. It's the difference, measured in
		 * milliseconds, between the current time and midnight, January 1, 1970 UTC.
		 */
		private final long timestamp;

		/**
		 * Create a cookie.
		 * 
		 * @param name  the cookie name
		 * @param value the cookie value
		 */
		private Cookie(String name, String value) {
			super();
			this.name = name;
			this.value = value;
			this.timestamp = System.currentTimeMillis();
		}

		/**
		 * Set from cookie-av part.
		 * 
		 * @param name  the name property
		 * @param value the value property
		 */
		private void setCookieAV(String name, String value) {
			if (name.equalsIgnoreCase("Version") && null != value) {
				this.version = value;
			} else if (name.equalsIgnoreCase("Comment") && null != value) {
				this.comment = value;
			} else if (name.equalsIgnoreCase("Domain") && null != value) {
				this.domain = value;
			} else if (name.equalsIgnoreCase("Max-Age") && null != value) {
				String maxAgeStr;
				// Remove double quota
				if (value.charAt(0) == '\"')
					maxAgeStr = value.substring(1);
				else
					maxAgeStr = value;
				try {
					this.maxAge = Integer.parseInt(maxAgeStr);
				} catch (NumberFormatException e) {
				}
			} else if (name.equalsIgnoreCase("Path") && null != value) {
				this.path = value;
			} else if (name.equalsIgnoreCase("Secure")) {
				this.secure = true;
			} else if (name.equalsIgnoreCase("HttpOnly")) {
				this.httpOnly = true;
			}
		}

		/**
		 * @return the {@link #name}
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the {@link #value}
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the {@link #version}
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @return the {@link #comment}
		 */
		public String getComment() {
			return comment;
		}

		/**
		 * @return the {@link #domain}
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return the {@link #maxAge} in seconds
		 */
		public long getMaxAge() {
			return maxAge;
		}

		/**
		 * @return if the cookie is expired.
		 */
		public boolean isExpired() {
			if (maxAge <= 0L)
				return false;
			long passedSeconds = Math.abs(System.currentTimeMillis() / 1000 - timestamp / 1000);
			return passedSeconds > maxAge;
		}

		/**
		 * @return the {@link #path}
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return the {@link #secure}
		 */
		public boolean isSecure() {
			return secure;
		}

		/**
		 * @return the {@link #httpOnly}
		 */
		public boolean isHttpOnly() {
			return httpOnly;
		}

		/**
		 * @return the {@link #timestamp} in milliseconds
		 */
		public long getTimestamp() {
			return timestamp;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(name).append('=').append(value);
			if (null != version) {
				sb.append("; Version=").append(version);
			}
			if (null != comment) {
				sb.append("; Comment=").append(comment);
			}
			if (null != domain) {
				sb.append("; Domain=").append(domain);
			}
			if (maxAge >= 0L) {
				sb.append("; Max-Age=").append(maxAge);
			}
			if (null != path) {
				sb.append("; Path=").append(path);
			}
			if (secure) {
				sb.append("; Secure");
			}
			if (httpOnly) {
				sb.append("; HttpOnly");
			}
			return sb.toString();
		}

		private static final long serialVersionUID = 3094195183254456803L;

	}

	private static final long serialVersionUID = -157146414048960947L;

}
