/*
	Android Asynchronous HttpURLConnection
	Copyright 2011 Chris Roemmich <chris@cr-wd.com>
	https://cr-wd.com

	Licensed under the Apache License, Version 2.0 (the "License");
 	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.example.httpurlconnection.network;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Holds the data from an http response in a serializable form for passing with
 * messages.
 */
public class AsyncHttpResponse implements Serializable {

	public static final long serialVersionUID = 5847719786957029870L;

	/**
	 * The id of the request
	 */
	public int requestId;

	/**
	 * @see java.net.HttpURLConnection#getContentEncoding()
	 */
	public String contentEncoding;

	/**
	 * @see java.net.HttpURLConnection#getContentLength()
	 */
	public int contentLength;

	/**
	 * @see java.net.HttpURLConnection#getContentType()
	 */
	public String contentType;

	/**
	 * @see java.net.HttpURLConnection#getDate()
	 */
	public long date;

	/**
	 * @see java.net.HttpURLConnection#getExpiration()
	 */
	public long expiration;

	/**
	 * @see java.net.HttpURLConnection#getHeaderFields()
	 */
	public Map<String, List<String>> headerFields;

	/**
	 * @see java.net.HttpURLConnection#getIfModifiedSince()
	 */
	public long ifModifiedSince;

	/**
	 * @see java.net.HttpURLConnection#getLastModified()
	 */
	public long lastModified;

	/**
	 * @see java.net.HttpURLConnection#getResponseMessage()
	 */
	public String responseMessage;

	/**
	 * @see java.net.HttpURLConnection#getRequestMethod()
	 */
	public String requestMethod;

	/**
	 * @see java.net.HttpURLConnection#getRequestProperties()
	 */
	public Map<String, List<String>> requestProperties;

	/**
	 * The body of the response from either
	 * {@link java.net.HttpURLConnection#getInputStream()} or
	 * {@link java.net.HttpURLConnection#getErrorStream()}
	 */
	public String responseBody;

	/**
	 * @see java.net.HttpURLConnection#getResponseCode()
	 */
	public int responseCode;

	/**
	 * An exception that was thrown during the request or a generated one when
	 * {@link responseCode} > 400.
	 */
	public Throwable throwable;

	/**
	 * The url as a String. Derived from
	 * {@link java.net.HttpURLConnection#getURL()}.
	 */
	public String url;

	/**
	 * Creates a new empty HttpResponse object
	 */
	public AsyncHttpResponse() {}

	/**
	 * Creates a new HttpResponse object
	 * 
	 * @param method
	 * @param url
	 */
	public AsyncHttpResponse(final int requestId, final AsyncHttpURLConnection.Method method, final String url) {
		this.requestId = requestId;
		this.requestMethod = method.toString();
		this.url = url;
	}

}