/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Full Internet URL combined with:<br>
 * <code>[schema]://[username]:[password]@[host]:[port][path][queryString][anchorString]</code><br>
 * Like: <code>http://127.0.0.1:80/dir/file#anchor</code>
 * 
 * @author XuYanhang
 * @since 2020-11-01
 *
 */
public final class InetURL implements Cloneable, java.io.Serializable {

	/**
	 * Parses and gets the URL from a URL string or <code>null</code> if failed.
	 * 
	 * @param url the encoded url who must include scheme, host part and path part
	 * @return an {@link InetURL} or <code>null</code> if parse failed.
	 */
	public static InetURL of(String url) {
		try {
			return new InetURL(url, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Parses and gets the URL from a URL string or <code>null</code> if failed.
	 * 
	 * @param url     the encoded url who must include scheme, host part and path
	 *                part
	 * @param charset the charset for this url to encode or decode
	 * @return an {@link InetURL} or <code>null</code> if parse failed.
	 */
	public static InetURL of(String url, String charset) {
		try {
			return new InetURL(url, charset);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * The charset to encode or decode this URL
	 */
	private String charset;

	/**
	 * The URL scheme of protocol like <code>http</code>
	 */
	private String scheme;

	/**
	 * The username of the URL.
	 */
	private String username;

	/**
	 * The password of the URL.
	 */
	private String password;

	/**
	 * The URL Host like <code>127.0.0.1</code>
	 */
	private String host;

	/**
	 * The URL port like <code>80</code>
	 */
	private int port;

	/**
	 * The URL file path like <code>/path1/path2/index.html</code> then the separate
	 * is <code>[path1, path2, index.html]</code>
	 */
	private ArrayList<String> file;

	/**
	 * The query string after <code>?</code> in URL. The query parameters are like
	 * <code>?a=1&b=2</code> then the separate is <code>{a:1,b:2}</code>
	 */
	private LinkedHashMap<String, String> query;

	/**
	 * The anchor string after <code>#</code> in URL.
	 */
	private String anchor;

	/**
	 * Initial an empty URL in all empty parameters.
	 */
	public InetURL() {
		super();
		this.charset = "UTF-8";
		this.scheme = "http";
		this.username = "";
		this.password = "";
		this.host = "";
		this.port = 0;
		this.file = new ArrayList<>();
		this.query = new LinkedHashMap<>();
		this.anchor = "";
	}

	/**
	 * Initial an URL in specified URL string. Throws Exception on any parse error
	 * happens.
	 * 
	 * @param url the URL to parse
	 */
	public InetURL(String url, String charset) {
		super();
		this.charset = null == charset ? "UTF-8" : Charset.forName(charset).name();
		// Parse
		url = url.replace('\\', '/');
		int from = 0;
		int to = 0;
		// Check schema
		to = url.indexOf("://", from);
		if (to <= 0)
			throw new IllegalArgumentException("scheme");
		this.scheme = decodeURLPart(url.substring(from, to), charset);
		from = to + 3;
		// Check user info and host info
		to = url.indexOf('/', from);
		if (to < 0)
			to = url.indexOf('?');
		if (to < 0)
			to = url.indexOf('#');
		if (to < 0)
			to = url.length();
		parseURLHost(url.substring(from, to));
		// Check path
		parseURLPath(url.substring(to));
	}

	/**
	 * Parse the user information and host information
	 */
	private void parseURLHost(String urlHost) {
		String user_pass;
		String host_port;
		int temp = urlHost.indexOf('@');
		if (temp >= 0) {
			user_pass = urlHost.substring(0, temp);
			host_port = urlHost.substring(temp + 1);
		} else {
			user_pass = null;
			host_port = urlHost;
		}
		if (user_pass != null) {
			temp = user_pass.indexOf(':');
			if (temp >= 0) {
				this.username = decodeURLPart(user_pass.substring(0, temp), charset);
				this.password = decodeURLPart(user_pass.substring(temp + 1), charset);
			} else {
				this.username = user_pass.isEmpty() ? "" : decodeURLPart(user_pass, charset);
				this.password = "";
			}
		} else {
			this.username = this.password = "";
		}
		temp = host_port.indexOf(':');
		if (temp >= 0) {
			this.host = decodeURLPart(host_port.substring(0, temp), charset);
			String portStr = host_port.substring(temp + 1);
			this.port = portStr.isEmpty() ? null : Integer.valueOf(portStr, 10);
			if (this.port < 0 || this.port > 0Xffff)
				throw new IllegalArgumentException("port=" + this.port);
		} else {
			this.host = decodeURLPart(host_port, charset);
			this.port = 0;
		}
	}

	/**
	 * Parse the real path, query path and anchor
	 */
	private void parseURLPath(String urlPath) {
		this.file = new ArrayList<>();
		this.query = new LinkedHashMap<>();
		this.anchor = "";
		this.resetPath(urlPath);
	}

	/**
	 * Reset the charset of this URL of {@link #charset}
	 * 
	 * @param charset the charset to reset
	 */
	public InetURL resetCharset(String charset) {
		this.charset = null == charset ? "UTF-8" : Charset.forName(charset).name();
		return this;
	}

	/**
	 * Reset the scheme of this URL of {@link #scheme}
	 * 
	 * @param username the decoded scheme
	 */
	public InetURL resetScheme(String scheme) {
		if (scheme.isEmpty())
			throw new IllegalArgumentException("Illegal schema:" + scheme);
		this.scheme = scheme;
		return this;
	}

	/**
	 * Reset the user name of {@link #username}
	 * 
	 * @param username the decoded username
	 */
	public InetURL resetUsername(String username) {
		this.username = null == username ? "" : username;
		return this;
	}

	/**
	 * Reset the password of {@link #password}
	 * 
	 * @param host the decoded password
	 */
	public InetURL resetPassword(String password) {
		this.password = null == password ? "" : password;
		return this;
	}

	/**
	 * Reset the Host address of {@link #host}
	 * 
	 * @param host the decoded host
	 */
	public InetURL resetHost(String host) {
		this.host = null == host ? "" : host;
		return this;
	}

	/**
	 * Reset the port of {@link #port}
	 * 
	 * @param port the port to change
	 */
	public InetURL resetPort(int port) {
		if (port < 0 || port > 0Xffff)
			throw new IllegalArgumentException("port=" + port);
		this.port = port;
		return this;
	}

	/**
	 * Reset the file path of this URL. Query parameters and anchor are ignored
	 * here.
	 * 
	 * @param file the encoded file path
	 */
	public InetURL resetFilePath(String file) {
		this.file.clear();
		if (null == file || file.isEmpty())
			return this;
		file = file.replace('\\', '/');
		if (file.charAt(0) == '/')
			file = file.substring(1);
		String temp;
		int cur;
		int i;
		int qi = file.indexOf('?', 0);
		int ai = file.indexOf('#', qi < 0 ? 0 : qi + 1);
		// Fetch path file
		String realFile;
		if (qi >= 0) {
			realFile = file.substring(0, qi);
		} else if (ai >= 0) {
			realFile = file.substring(0, ai);
		} else {
			realFile = file;
		}
		cur = 0;
		while (cur < realFile.length()) {
			i = realFile.indexOf('/');
			if (i < 0)
				i = realFile.length();
			temp = realFile.substring(cur, i);
			if (!temp.isEmpty())
				this.file.add(decodeURLPart(temp, charset));
			cur = i + 1;
		}
		return this;
	}

	/**
	 * Reset the query parameters
	 * 
	 * @param query the encoded query path
	 */
	public InetURL resetQueryPath(String query) {
		this.query.clear();
		if (null == query || query.isEmpty())
			return this;
		if (query.charAt(0) == '?')
			query = query.substring(1);
		int ai = query.indexOf('#');
		if (ai >= 0)
			query = query.substring(0, ai);
		int i;
		String temp;
		int cur = 0;
		while (cur < query.length()) {
			i = query.indexOf('&');
			if (i < 0)
				i = query.length();
			temp = query.substring(cur, i);
			if (temp.isEmpty())
				continue;
			int eqIndex = temp.indexOf('=');
			String key;
			String value;
			if (eqIndex < 0) {
				key = decodeURLPart(temp, charset);
				value = null;
			} else {
				key = decodeURLPart(temp.substring(0, eqIndex), charset);
				value = decodeURLPart(temp.substring(eqIndex + 1), charset);
			}
			this.query.put(key, value);
			cur = i + 1;
		}
		return this;
	}

	/**
	 * Reset the anchor path of this URL
	 * 
	 * @param anchor the encoded anchor
	 */
	public InetURL resetAnchorPath(String anchor) {
		if (isEmpty(anchor))
			this.anchor = "";
		else
			this.anchor = decodeURLPart(anchor.charAt(0) == '#' ? anchor.substring(1) : anchor, charset);
		return this;
	}

	/**
	 * Reset the anchor of this URL
	 * 
	 * @param anchor the decoded anchor
	 */
	public InetURL resetAnchor(String anchor) {
		this.anchor = null == anchor ? "" : anchor;
		return this;
	}

	/**
	 * Reset the full path of this URL. Query parameters and anchor are reset here.
	 * 
	 * @param path the encoded path
	 */
	public InetURL resetPath(String path) {
		path = path.replace('\\', '/');
		if (!path.isEmpty() && path.charAt(0) == '/')
			path = path.substring(1);
		int qi = path.indexOf('?', 0);
		int ai = path.indexOf('#', qi < 0 ? 0 : qi + 1);
		// Fetch path file
		String file;
		if (qi >= 0) {
			file = path.substring(0, qi);
		} else if (ai >= 0) {
			file = path.substring(0, ai);
		} else {
			file = path;
		}
		resetFilePath(file);
		// Fetch query
		String query;
		if (qi >= 0) {
			if (ai >= 0) {
				query = path.substring(qi + 1, ai);
			} else {
				query = path.substring(qi + 1);
			}
		} else {
			query = "";
		}
		resetQueryPath(query);
		// Fetch anchor
		this.anchor = ai >= 0 ? decodeURLPart(path.substring(ai + 1), charset) : "";
		return this;
	}

	/**
	 * Returns the charset
	 * 
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Returns the scheme who is decoded
	 * 
	 * @return the decoded scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * Returns the username who is decoded
	 * 
	 * @return the decoded username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the password who is decoded
	 * 
	 * @return the decoded password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the host who is decoded
	 * 
	 * @return the decoded host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the port where 0 means default
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns the port or a default port if the port in this URL is specified to be
	 * default
	 * 
	 * @return the port or default port if this port should be default
	 */
	public int getPort(int defaultPort) {
		return 0 == port ? defaultPort : port;
	}

	/**
	 * Returns the real file path. The file is encoded.
	 * 
	 * @return the encoded file
	 */
	public String getFilePath() {
		StringBuilder builder = new StringBuilder();
		if (file.isEmpty())
			builder.append('/');
		else
			for (int i = 0; i < file.size(); i++)
				builder.append('/').append(encodeURLPart(file.get(i), charset));
		return builder.toString();
	}

	/**
	 * Returns the query path. The query is encoded.
	 * 
	 * @return the encoded query
	 */
	public String getQueryPath() {
		StringBuilder builder = new StringBuilder();
		if (!query.isEmpty())
			builder.append('?');
		int index = 0;
		for (java.util.Map.Entry<String, String> queryParam : query.entrySet()) {
			builder.append(encodeURLPart(queryParam.getKey(), charset));
			if (null != queryParam.getValue())
				builder.append('=').append(encodeURLPart(queryParam.getValue(), charset));
			index++;
			if (index < query.size())
				builder.append('&');
		}
		return builder.toString();
	}

	/**
	 * Returns the anchor path. The anchor is encoded.
	 * 
	 * @return the encoded anchor
	 */
	public String getAnchorPath() {
		return isEmpty(anchor) ? "" : "#" + encodeURLPart(anchor, charset);
	}

	/**
	 * Returns the anchor. The anchor is decoded.
	 * 
	 * @return the decoded anchor
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * Returns the full path of the URL including file path, query path, anchor
	 * path. It's encoded.
	 */
	public String getPath() {
		return getFilePath() + getQueryPath() + getAnchorPath();
	}

	/**
	 * Returns the file deep count of this URL
	 * 
	 * @return the file deep count of this URL
	 */
	public int getFileDeep() {
		return file.size();
	}

	/**
	 * Returns the decoded file of the URL
	 * 
	 * @return a copy of the file
	 */
	@SuppressWarnings("unchecked")
	public List<String> getFile() {
		return (List<String>) file.clone();
	}

	/**
	 * Push a decoded file at last of this file
	 * 
	 * @param file a decoded file to append
	 */
	public InetURL pushFile(String file) {
		this.file.add(file);
		return this;
	}

	/**
	 * Remove the last file of this file path
	 */
	public InetURL popFile() {
		this.file.remove(this.file.size() - 1);
		return this;
	}

	/**
	 * Returns the query count of this URL.
	 * 
	 * @return the query count of this URL.
	 */
	public int getQuerySize() {
		return query.size();
	}

	/**
	 * Returns the decoded query of the URL
	 * 
	 * @return a copy of the query
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getQuery() {
		return (Map<String, String>) query.clone();
	}

	/**
	 * Returns the decoded query names of the URL
	 * 
	 * @return a copy of the query names
	 */
	public List<String> getQueryNames() {
		return new ArrayList<>(query.keySet());
	}

	/**
	 * Returns if the query parameter contains the specified name.
	 * 
	 * @param name the query parameter name
	 * @return if contains the query parameter name
	 */
	public boolean containsQuery(String name) {
		return null != name && query.containsKey(name);
	}

	/**
	 * Returns a query parameter value of specified name. If not exists, returns
	 * <code>null</code>.
	 * 
	 * @param name the query parameter name
	 * @return the query parameter value
	 */
	public String getQuery(String name) {
		return null == name ? null : query.get(name);
	}

	/**
	 * Puts a query parameter pair into this URL. Origin query will be replaced when
	 * exists. If <code>value</code> is <code>null</code>, then there will not occur
	 * '=' char in query path of this query pair.
	 * 
	 * @param name  the query parameter name
	 * @param value the query parameter value
	 */
	public InetURL putQuery(String name, String value) {
		if (null == name)
			throw new NullPointerException("name");
		if (name.isEmpty() && null == value)
			throw new IllegalArgumentException("Illegal query");
		query.put(name, value);
		return this;
	}

	/**
	 * Removes the specified query parameter from this URL.
	 * 
	 * @param namet he query parameter name
	 */
	public InetURL delQuery(String name) {
		if (null != query)
			query.remove(name);
		return this;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((anchor == null) ? 0 : anchor.hashCode());
		return result;
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
		InetURL other = (InetURL) obj;
		if (charset != other.charset && (charset == null || !charset.equalsIgnoreCase(other.charset)))
			return false;
		if (scheme != other.scheme && (scheme == null || !scheme.equalsIgnoreCase(other.scheme)))
			return false;
		if (username != other.username && (username == null || !username.equals(other.username)))
			return false;
		if (password != other.password && (password == null || !password.equals(other.password)))
			return false;
		if (host != other.host && (host == null || !host.equals(other.host)))
			return false;
		if (port != other.port)
			return false;
		if (file != other.file && (file == null || !file.equals(other.file)))
			return false;
		if (query != other.query && (query == null || !query.equals(other.query)))
			return false;
		if (anchor != other.anchor && (anchor == null || !anchor.equals(other.anchor)))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// Scheme
		builder.append(encodeURLPart(scheme, charset) + "://");
		// FullHost
		if (!isEmpty(username))
			builder.append(encodeURLPart(username, charset));
		if (!isEmpty(password))
			builder.append(':').append(encodeURLPart(password, charset));
		if (!isEmpty(username) || !isEmpty(password) || (!isEmpty(host) && 0 != port))
			builder.append('@');
		if (!isEmpty(host))
			builder.append(encodeURLPart(host, charset));
		if (0 != port)
			builder.append(':').append(port);
		// Full Path
		builder.append(getFilePath());
		builder.append(getQueryPath());
		builder.append(getAnchorPath());
		return builder.toString();
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	public InetURL clone() {
		InetURL clone;
		try {
			clone = (InetURL) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
		clone.file = (ArrayList<String>) file.clone();
		clone.query = (LinkedHashMap<String, String>) query.clone();
		return clone;
	}

	/**
	 * Returns if the String value is empty when it's length is zero or it's value
	 * is <code>null</code>.
	 * 
	 * @param value the String value to check
	 * @return <code>true</code> if the String value is empty
	 */
	private static boolean isEmpty(String value) {
		return null == value || value.isEmpty();
	}

	/*
	 * Encode a String.
	 */
	private static String encodeURLPart(String s, String charset) {
		if (null == s) {
			return "";
		}
		try {
			try {
				return java.net.URLEncoder.encode(s, charset);
			} catch (java.io.UnsupportedEncodingException e1) {
				return java.net.URLEncoder.encode(s, "ISO-8859-1");
			}
		} catch (Exception e) {
		}
		return s;
	}

	/*
	 * Decode a String.
	 */
	private static String decodeURLPart(String s, String charset) {
		if (null == s)
			return "";
		try {
			try {
				return java.net.URLDecoder.decode(s, charset);
			} catch (java.io.UnsupportedEncodingException e1) {
				return java.net.URLDecoder.decode(s, "ISO-8859-1");
			}
		} catch (Exception e) {
		}
		return s;
	}

	private static final long serialVersionUID = 1647658926833704025L;

}
