/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.net;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP client to communicate with other HTTP server. It's a proxy from
 * {@link org.apache.http.client.HttpClient HttpClient}. Better don't forget to
 * close it after used to release the connection resource.
 *
 * @author XuYanhang
 * @since 2020-11-01
 * @see org.apache.http.client.HttpClient
 *
 */
public final class HttpClient implements java.io.Closeable {

	/**
	 * Create a minimal {@link HttpClient} but only in HTTP.
	 */
	public static HttpClient createMinimal() {
		return new HttpClient(org.apache.http.impl.client.HttpClients.createMinimal());
	}

	/**
	 * Create a minimal {@link HttpClient} allowed both in HTTP and HTTPS but weak
	 * SSL.
	 */
	public static HttpClient createSSLMinimal() {
		// setup a Trust Strategy that allows all certificates.
		javax.net.ssl.SSLContext sslContext;
		try {
			sslContext = new org.apache.http.ssl.SSLContextBuilder()
					.loadTrustMaterial(null, new org.apache.http.ssl.TrustStrategy() {
						@Override
						public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) {
							return true;
						}
					}).build();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		// don't check HostName, either.
		// Also SSLConnectionSocketFactory.getDefaultHostnameVerifier()
		javax.net.ssl.HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.NoopHostnameVerifier.INSTANCE;
		// Create an SSL Socket Factory, to use our weakened "trust strategy"
		org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory = new org.apache.http.conn.ssl.SSLConnectionSocketFactory(
				sslContext, hostnameVerifier);
		// Create a Registry, to register it.
		org.apache.http.config.Registry<org.apache.http.conn.socket.ConnectionSocketFactory> socketFactoryRegistry = // Registry
				org.apache.http.config.RegistryBuilder.<org.apache.http.conn.socket.ConnectionSocketFactory>create() // Initialize
						.register("http", org.apache.http.conn.socket.PlainConnectionSocketFactory.getSocketFactory()) // HTTP
						.register("https", sslSocketFactory) // HTTPS
						.build();
		// create connection-manager using our Registry allows multi-threaded
		org.apache.http.impl.conn.PoolingHttpClientConnectionManager connManager = new org.apache.http.impl.conn.PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		connManager.setMaxTotal(200);
		connManager.setDefaultMaxPerRoute(100);
		// Build a client
		org.apache.http.impl.client.CloseableHttpClient client = org.apache.http.impl.client.HttpClientBuilder.create()
				.setSSLContext(sslContext) // sslContext
				.setConnectionManager(connManager) // connManager
				.build();
		// Build
		return new HttpClient(client);
	}

	/** The proxy client bean */
	private final org.apache.http.impl.client.CloseableHttpClient client;
	/** The cookies for the client */
	private final HttpCookies cookies;

	/**
	 * Create a minimal client but only allows HTTP.
	 */
	public HttpClient() {
		super();
		cookies = new HttpCookies();
		client = org.apache.http.impl.client.HttpClients.createMinimal();
	}

	/**
	 * Create a client in a custom given client parameter.
	 */
	public HttpClient(org.apache.http.impl.client.CloseableHttpClient client) {
		super();
		if (null == client)
			throw new NullPointerException();
		this.cookies = new HttpCookies();
		this.client = client;
	}

	/**
	 * Clear current all cookies
	 */
	public void clearCookies() {
		cookies.clearCookies();
	}

	/**
	 * Returns the current cookies in this client
	 * 
	 * @return {@link #cookies}
	 */
	public HttpCookies getCookies() {
		return cookies;
	}

	/**
	 * Create a request with given method like 'GET', 'POST'. If not found,
	 * exception happens.
	 */
	public Request newRequest(String method, String url) {
		method = method.toUpperCase();
		org.apache.http.client.methods.HttpRequestBase requestBase;
		if (method.equals(org.apache.http.client.methods.HttpGet.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpGet();
		else if (method.equals(org.apache.http.client.methods.HttpDelete.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpDelete();
		else if (method.equals(org.apache.http.client.methods.HttpPost.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpPost();
		else if (method.equals(org.apache.http.client.methods.HttpPut.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpPut();
		else if (method.equals(org.apache.http.client.methods.HttpPatch.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpPatch();
		else if (method.equals(org.apache.http.client.methods.HttpHead.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpHead();
		else if (method.equals(org.apache.http.client.methods.HttpOptions.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpOptions();
		else if (method.equals(org.apache.http.client.methods.HttpTrace.METHOD_NAME))
			requestBase = new org.apache.http.client.methods.HttpTrace();
		else
			throw new UnsupportedOperationException(method);
		return new Request(new InetURL(url, "UTF-8"), requestBase);
	}

	/**
	 * Create a request with GET method.
	 */
	public Request newGET(String url) {
		return newRequest(org.apache.http.client.methods.HttpGet.METHOD_NAME, url);
	}

	/**
	 * Create a request with DELETE method.
	 */
	public Request newDELETE(String url) {
		return newRequest(org.apache.http.client.methods.HttpDelete.METHOD_NAME, url);
	}

	/**
	 * Create a request with POST method.
	 */
	public Request newPOST(String url) {
		return newRequest(org.apache.http.client.methods.HttpPost.METHOD_NAME, url);
	}

	/**
	 * Create a request with PUT method.
	 */
	public Request newPUT(String url) {
		return newRequest(org.apache.http.client.methods.HttpPut.METHOD_NAME, url);
	}

	/**
	 * Create a request with PATCH method.
	 */
	public Request newPATCH(String url) {
		return newRequest(org.apache.http.client.methods.HttpPatch.METHOD_NAME, url);
	}

	/**
	 * Create a request with HEAD method.
	 */
	public Request newHEAD(String url) {
		return newRequest(org.apache.http.client.methods.HttpHead.METHOD_NAME, url);
	}

	/**
	 * Create a request with OPTIONS method.
	 */
	public Request newOPTIONS(String url) {
		return newRequest(org.apache.http.client.methods.HttpOptions.METHOD_NAME, url);
	}

	/**
	 * Create a request with TRACE method.
	 */
	public Request newTRACE(String url) {
		return newRequest(org.apache.http.client.methods.HttpTrace.METHOD_NAME, url);
	}

	/**
	 * Close the client
	 */
	public void close() {
		try {
			client.close();
		} catch (Exception e) {
		}
	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			client.close();
		} catch (Exception e) {
		}
		super.finalize();
	}

	/**
	 * The request for the client.
	 * 
	 * @author XuYanhang
	 *
	 */
	public final class Request {

		/**
		 * URL of this request
		 */
		private final InetURL url;

		/**
		 * Proxy request base value
		 */
		private final org.apache.http.client.methods.HttpRequestBase requestBase;

		/**
		 * Request config builder
		 */
		private final org.apache.http.client.config.RequestConfig.Builder requestConfigBuilder;

		/**
		 * Initialize the request
		 */
		private Request(InetURL url, org.apache.http.client.methods.HttpRequestBase requestBase) {
			super();
			this.url = url;
			this.requestBase = requestBase;
			for (String cookie : HttpClient.this.cookies.createCookieHeaderValues())
				this.requestBase.addHeader("Cookie", cookie);
			this.requestConfigBuilder = org.apache.http.client.config.RequestConfig.custom() // Create_builder
					.setSocketTimeout(5000) // Read_time_out
					.setConnectTimeout(2000); // Connect_timeout
		}

		/**
		 * Set the connect timeout to another server.
		 */
		public Request setConnectTimeout(int connectTimeout) {
			requestConfigBuilder.setConnectTimeout(connectTimeout);
			return this;
		}

		/**
		 * The response timeout from another server.
		 */
		public Request setResponseTimeout(int responseTimeout) {
			requestConfigBuilder.setSocketTimeout(responseTimeout);
			return this;
		}

		/**
		 * Add a new header or reset the old header on the request.
		 */
		public Request setHeader(String name, String value) {
			requestBase.setHeader(name, value);
			return this;
		}

		/**
		 * Add a new header or update the old header on the request.
		 */
		public Request putHeader(String name, String value) {
			requestBase.addHeader(name, value);
			return this;
		}

		/**
		 * Delete a header with a name.
		 */
		public Request delHeader(String name) {
			requestBase.removeHeaders(name);
			return this;
		}

		/**
		 * Put a query parameter of this request who is based on UTF-8
		 */
		public Request putQuery(String name, String value) {
			url.putQuery(name, value);
			return this;
		}

		/**
		 * Delete a query parameter of this request
		 */
		public Request delQuery(String name) {
			url.delQuery(name);
			return this;
		}

		/**
		 * When the method can set HTTP Entity then we can set entity to the request. In
		 * this way we'll set the entity with content type of "application/json". If the
		 * method not support entity setting then do nothing.
		 */
		public Request setJsonEntity(String json) {

			setStringBody(json, "application/json");

			return this;
		}

		/**
		 * When the method can set HTTP Entity then we can set entity to the request. In
		 * this way we'll set the entity with given content type. If the method not
		 * support entity setting then do nothing.
		 */
		public Request setStringBody(String body, String contentType) {
			if (null == body || null == contentType)
				throw new NullPointerException();

			org.apache.http.entity.StringEntity entity = new org.apache.http.entity.StringEntity(body, "utf-8");

			entity.setContentType(contentType);
			entity.setContentEncoding("utf-8");

			_setEntity(entity);

			return this;
		}

		/**
		 * When the method can set HTTP Entity then we can set entity to the request. In
		 * this way we'll set the entity with given content type. If the method not
		 * support entity setting then do nothing.
		 */
		public Request setBody(byte[] body, String contentType) {

			if (null == body || null == contentType)
				throw new NullPointerException();

			org.apache.http.entity.ByteArrayEntity entity = new org.apache.http.entity.ByteArrayEntity(body);
			entity.setContentType(contentType);

			_setEntity(entity);

			return this;
		}

		/*
		 * Update the entity.
		 */
		private void _setEntity(org.apache.http.HttpEntity entity) {
			if (!(requestBase instanceof org.apache.http.client.methods.HttpEntityEnclosingRequestBase)) {
				return;
			}
			((org.apache.http.client.methods.HttpEntityEnclosingRequestBase) requestBase).setEntity(entity);
		}

		/**
		 * Returns the method of this request like 'GET', 'POST'
		 * 
		 * @return the method of this request
		 */
		public String getMethod() {
			return requestBase.getMethod().toUpperCase();
		}

		/**
		 * Returns the URL string of this request
		 * 
		 * @return the URL string of this request
		 */
		public String getUrl() {
			return url.toString();
		}

		/**
		 * Returns the header value of the first header with a specified name of this
		 * request. If there is more than one matching header in the request the first
		 * element of {@link #getHeaders(String)} is returned. If there is no matching
		 * header in the message {@code null} is returned.
		 * 
		 * @param name the name of the header to search
		 * @return the first header value in specified header name or <code>null</code>
		 *         if no such a header
		 */
		public String getHeader(String name) {
			org.apache.http.Header header = requestBase.getFirstHeader(name);
			return null == header ? null : header.getValue();
		}

		/**
		 * Returns all the headers values in a specified name of this request. Header
		 * values are ignored. Headers are ordered in the sequence they will be sent
		 * over a connection.
		 * 
		 * @param name the name of the headers to search
		 * @return the header values in specified header name
		 */
		public List<String> getHeaders(String name) {
			org.apache.http.Header[] headers = requestBase.getHeaders(name);
			List<String> results = new ArrayList<>(headers.length);
			for (int i = 0; i < headers.length; i++)
				results.add(headers[i].getValue());
			return results;
		}

		/**
		 * Returns the header names in this request
		 * 
		 * @return the header names in this request
		 */
		public List<String> getHeaderNames() {

			org.apache.http.Header[] headers = requestBase.getAllHeaders();

			List<String> headerNames = new ArrayList<>();

			for (int index = 0; index < headers.length; index++) {
				String name = headers[index].getName().toLowerCase();
				if (!headerNames.contains(name))
					headerNames.add(name);
			}

			return headerNames;
		}

		/**
		 * Returns a query parameter value of specified name. If not exists, returns
		 * <code>null</code>.
		 * 
		 * @param name the query parameter name
		 * @return the query parameter value
		 */
		public String getQuery(String name) {
			return url.getQuery(name);
		}

		/**
		 * Returns the query parameter names in this request
		 * 
		 * @return the query parameter names in this request
		 */
		public List<String> getQueryNames() {
			return url.getQueryNames();
		}

		/**
		 * Returns if the request supports set content body entity
		 * 
		 * @return <code>if</code> if the request support set content body entity
		 */
		public boolean supportBody() {
			return (requestBase instanceof org.apache.http.client.methods.HttpEntityEnclosingRequestBase);
		}

		/**
		 * Invoke the request and get the response context as byte array.
		 * 
		 * @throws IOException if any IO error happens
		 */
		public byte[] invokeData() throws java.io.IOException {

			return invoke(false).getContentData();
		}

		/**
		 * Invoke the request and get the response context as text.
		 * 
		 * @throws java.io.IOException if any IO error happens
		 */
		public String invokeText() throws java.io.IOException {

			return invoke(false).getContentText();
		}

		/**
		 * Invoke the request and get the response body. The response is failed when the
		 * {@link Response#isOK()} is <code>false</code>
		 * 
		 * @throws java.io.IOException if any IO error happens
		 */
		public Response invoke() throws java.io.IOException {
			return invoke(true);
		}

		/**
		 * Invoke the request and get the full response of {@link Response}.
		 * 
		 * @param allowError If the allowError is <code>false</code> (not allow error)
		 *                   then the response must be a right one of
		 *                   {@link Response#isOK()} is <code>true</code>
		 * @throws java.io.IOException if any IO error happens
		 */
		public Response invoke(boolean allowError) throws java.io.IOException {

			requestBase.setURI(java.net.URI.create(url.toString()));
			requestBase.setConfig(requestConfigBuilder.build());

			Response _response;
			try (org.apache.http.client.methods.CloseableHttpResponse response = HttpClient.this.client
					.execute(requestBase)) {
				_response = new Response(response);
			}

			if (!allowError && !_response.isOK()) {
				String contentText = _response.getContentText();
				if (null == contentText) {
					throw new java.io.IOException("Response error: " + _response.getStatusCode());
				} else {
					throw new java.io.IOException("Response error: " + _response.getStatusCode() + "-" + contentText);
				}
			}

			HttpClient.this.cookies.refreshFromSetCookieHeaderValues(_response.getHeaders("Set-Cookie"));

			return _response;
		}

	}

	/**
	 * The response for the client after execute the {@link Request}. It contains
	 * the whole response message of the first line for status, all headers, and the
	 * content.
	 * 
	 * @author XuYanhang
	 *
	 */
	public static final class Response {

		/**
		 * The response status line who's the first line of a Response message is the
		 * Status-Line.
		 */
		private org.apache.http.StatusLine statusLine;

		/**
		 * Represents an HTTP header field from a response.
		 */
		private org.apache.http.Header[] headers;

		/**
		 * The response execute data.
		 */
		private byte[] contentData;

		/**
		 * Initial the response
		 */
		private Response(org.apache.http.client.methods.CloseableHttpResponse response) throws java.io.IOException {
			super();
			this.statusLine = response.getStatusLine();
			if (null == this.statusLine) {
				throw new java.io.IOException("No status line");
			}
			this.headers = response.getAllHeaders();
			org.apache.http.HttpEntity entity = response.getEntity();
			if (null != entity) {
				this.contentData = org.apache.http.util.EntityUtils.toByteArray(entity);
			}
		}

		/**
		 * Read the status code for the response.
		 * 
		 * @see #statusLine
		 */
		public int getStatusCode() {
			return statusLine.getStatusCode();
		}

		/**
		 * If the response is an error response. For correct response the
		 * {@link #getStatusCode()} expected to be in range of <code>[100, 400)</code>.
		 */
		public boolean isError() {
			int statusCode = statusLine.getStatusCode();
			return statusCode < 100 || statusCode >= 400;
		}

		/**
		 * If the response is a OK response. For correct response the
		 * {@link #getStatusCode()} expected to be in range of <code>[200, 300)</code>.
		 */
		public boolean isOK() {
			int statusCode = statusLine.getStatusCode();
			return statusCode >= 200 && statusCode < 300;
		}

		/**
		 * Returns the value for the specified header, or <code>null</code> if this
		 * header has not been set. If more than one value was added for this name, only
		 * the first is returned.
		 */
		public String getHeader(String name) {

			for (int index = 0; index < headers.length; index++)
				if (headers[index].getName().equalsIgnoreCase(name))
					return headers[index].getValue();

			return null;
		}

		/**
		 * Returns a List of all the header values associated with the specified header
		 * name. If not found then an empty List returned.
		 */
		public List<String> getHeaders(String name) {

			List<String> headerValues = new ArrayList<>();

			for (int index = 0; index < headers.length; index++)
				if (headers[index].getName().equalsIgnoreCase(name))
					headerValues.add(headers[index].getValue());

			return headerValues;
		}

		/**
		 * Returns the header names set for this HTTP response.
		 */
		public List<String> getHeaderNames() {

			List<String> headerNames = new ArrayList<>();

			for (int index = 0; index < headers.length; index++) {
				String name = headers[index].getName().toLowerCase();
				if (!headerNames.contains(name))
					headerNames.add(name);
			}

			return headerNames;
		}

		/**
		 * Reads the content data for the response in bytes or <code>null</code> if no
		 * response content.
		 */
		public byte[] getContentData() {
			return contentData;
		}

		/**
		 * Reads the content text for the response in string or <code>null</code> if no
		 * response content.
		 */
		public String getContentText() {

			if (null == contentData) {
				return null;
			}

			try {
				return new String(contentData, "utf-8");
			} catch (java.io.UnsupportedEncodingException e) {
				return new String(contentData);
			}

		}

		/**
		 * The response to String. Doesn't resolve the context.
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(statusLine.toString()).append('\n');
			for (int index = 0; index < headers.length; index++) {
				builder.append(headers[index].toString()).append('\n');
			}
			builder.append('\n');
			builder.append("ContentData: length" + contentData.length);
			return builder.toString();
		}

	}

}
