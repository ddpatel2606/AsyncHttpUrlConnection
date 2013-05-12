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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class AsyncHttpURLConnection {

	public static final String TAG = AsyncHttpURLConnection.class.getSimpleName();

	public static final int DEFAULT_READ_TIMEOUT = 10000;
	public static final int DEFAULT_CONNECT_TIMEOUT = 15000;
	public static final String DEFAULT_ENCODING = "UTF-8";

	public final RequestOptions requestOptions = new RequestOptions();

	public class RequestOptions {
		public int readTimeout = DEFAULT_READ_TIMEOUT;
		public int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
		public String encoding = DEFAULT_ENCODING;
	}


	boolean canceled = false;
	
	public AsyncHttpResponse response;
	public int requestId = 0;

	/**
	 * Supported HTTP request methods
	 */
	public static enum Method {
		GET, POST
	}

	/**
	 * Create a new HttpClient
	 */
	public AsyncHttpURLConnection() {}


	
	/**
	 * get a new HttpResponse
	 */
	public AsyncHttpResponse getHttpResponse()
	{
		return response;
	}
	
	
	/**
	 * Perform a HTTP GET request, without any parameters.
	 * 
	 * @param url
	 *            the URL
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse get(final String url) {
		return doRequest(Method.GET, url, null, null);
	}

	/**
	 * Perform a HTTP GET request with additional headers
	 * 
	 * @param url
	 *            the URL
	 * @param headers
	 *            additional headers for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse get(final String url, final AsyncHttpHeaders headers) {
		return doRequest(Method.GET, url, headers, null);
	}

	/**
	 * Perform a HTTP GET request with parameters
	 * 
	 * @param url
	 *            the URL
	 * @param params
	 *            additional parameters for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse get(final String url, final AsyncHttpParams params) {
		return doRequest(Method.GET, url, null, params);
	}

	/**
	 * Perform a HTTP GET request with additional headers and parameters
	 * 
	 * @param url
	 *            the URL
	 * @param headers
	 *            additional headers for the request
	 * @param params
	 *            additional parameters for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse get(final String url, final AsyncHttpHeaders headers, final AsyncHttpParams params) {
		return doRequest(Method.GET, url, headers, params);
	}

	/**
	 * Perform a HTTP GET request, without any parameters.
	 * 
	 * @param url
	 *            the URL
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse post(final String url) {
		return doRequest(Method.POST, url, null, null);
	}

	/**
	 * Perform a HTTP POST request with additional headers
	 * 
	 * @param url
	 *            the URL
	 * @param headers
	 *            additional headers for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse post(final String url, final AsyncHttpHeaders headers) {
		return doRequest(Method.POST, url, headers, null);
	}

	/**
	 * Perform a HTTP POST request with parameters
	 * 
	 * @param url
	 *            the URL
	 * @param params
	 *            additional parameters for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse post(final String url, final AsyncHttpParams params) {
		return doRequest(Method.POST, url, null, params);
	}

	/**
	 * Perform a HTTP POST request with additional headers and parameters
	 * 
	 * @param url
	 *            the URL
	 * @param headers
	 *            additional headers for the request
	 * @param params
	 *            additional parameters for the request
	 * @param responseHandler
	 *            the response handler
	 */
	public AsyncHttpResponse post(final String url, final AsyncHttpHeaders headers, final AsyncHttpParams params) {
		return doRequest(Method.POST, url, headers, params);
	}

	/**
	 * Performs a HTTP GET/POST Request
	 * 
	 * @return id of the request
	 */
	public AsyncHttpResponse doRequest(final Method method, String url, AsyncHttpHeaders headers, AsyncHttpParams params) {
		
			
		if (headers == null) {
			headers = new AsyncHttpHeaders();
		}
		
		if (params == null) {
			params = new AsyncHttpParams();
		}

		final int requestId = incrementRequestId();


				response = new AsyncHttpResponse(requestId, method, url);

				HttpURLConnection conn = null;
				try {
					/* append query string for GET requests */
					if (method == Method.GET) {
						if (!params.urlParams.isEmpty()) {
							url += ('?' + params.getParamString());
						}
					}

					/* setup headers for POST requests */
					if (method == Method.POST) {
						headers.addHeader("Accept-Charset", requestOptions.encoding);
						if (params.hasMultipartParams()) {
							final AsyncSimpleMultipart multipart = params.getMultipart();
							headers.addHeader("Content-Type", multipart.getContentType());
						} else {
							headers.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + requestOptions.encoding);
						}
					}

					if (canceled) {						
						return response;
					}

					/* open and configure the connection */
					conn = (HttpURLConnection) new URL(url).openConnection();

					if (method == Method.GET) {
						conn = (HttpURLConnection) new URL(url).openConnection();
						conn.setRequestMethod("GET");
					} else if (method == Method.POST) {
						conn.setRequestMethod("POST");
						conn.setDoOutput(true);
						conn.setDoInput(true);
						conn.setUseCaches(false);
					}
					conn.setAllowUserInteraction(false);
					conn.setReadTimeout(requestOptions.readTimeout);
					conn.setConnectTimeout(requestOptions.connectTimeout);

					/* add headers to the connection */
					for (final Map.Entry<String, List<String>> entry : headers.getHeaders().entrySet()) {
						for (final String value : entry.getValue()) {
							conn.addRequestProperty(entry.getKey(), value);
						}
					}

					if (canceled) {
						try {
							conn.disconnect();
						} catch (final Exception e) {}
					
						return response;
					}

					response.requestProperties = conn.getRequestProperties();

					/* do post */
					if (method == Method.POST) {
						InputStream is;

						if (params.hasMultipartParams()) {
							is = params.getMultipart().getContent();
						} else {
							is = new ByteArrayInputStream(params.getParamString().getBytes());
						}

						final OutputStream os = conn.getOutputStream();

						writeStream(os, is);
					} else {
						conn.connect();
					}

					if (canceled) {
						try {
							conn.disconnect();
						} catch (final Exception e) {}
					
						return response;
					}

					response.contentEncoding = conn.getContentEncoding();
					response.contentLength = conn.getContentLength();
					response.contentType = conn.getContentType();
					response.date = conn.getDate();
					response.expiration = conn.getExpiration();
					response.headerFields = conn.getHeaderFields();
					response.ifModifiedSince = conn.getIfModifiedSince();
					response.lastModified = conn.getLastModified();
					response.responseCode = conn.getResponseCode();
					response.responseMessage = conn.getResponseMessage();

					/* do get */
					if (conn.getResponseCode() < 400) {
						response.responseBody = readStream(conn.getInputStream());
						return response;
					} else {
						response.responseBody = readStream(conn.getErrorStream());
						response.throwable = new Exception(response.responseMessage);
						return response;
					}

				} catch (Exception e) {
					
					Log.i(TAG, ""+e.getStackTrace());
					e.printStackTrace();
					
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}

		return response;
	}


	/**
	 * Gets the HttpURLConnection readTimeout
	 * 
	 * @return readTimeout in milliseconds
	 */
	public int getReadTimeout() {
		return requestOptions.readTimeout;
	}

	/**
	 * Set the HttpURLConnection readTimeout
	 * 
	 * @param readTimeout
	 *            in milliseconds
	 */
	public void setReadTimeout(final int readTimeout) {
		requestOptions.readTimeout = readTimeout;
	}

	/**
	 * Gets the HttpURLConnection connectTimeout
	 * 
	 * @return connectTimeout in milliseconds
	 */
	public int getConnectTimeout() {
		return requestOptions.connectTimeout;
	}

	/**
	 * Set the HttpURLConnection connectTimeout
	 * 
	 * @param connectTimeout
	 *            in milliseconds
	 */
	public void setConnectTimeout(final int connectTimeout) {
		requestOptions.connectTimeout = connectTimeout;
	}

	public synchronized int incrementRequestId() {
		return requestId++;
	}

	

	public String readStream(final InputStream is) throws IOException {
		final BufferedInputStream bis = new BufferedInputStream(is);
		final ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int read = 0;
		final byte[] buffer = new byte[8192];
		while (true) {
			if (canceled) {
				break;
			}

			read = bis.read(buffer);
			if (read == -1) {
				break;
			}
			baf.append(buffer, 0, read);
		}

		try {
			bis.close();
		} catch (final IOException e) {}

		try {
			is.close();
		} catch (final IOException e) {}

		return new String(baf.toByteArray());
	}

	public void writeStream(final OutputStream os, final InputStream is) throws IOException {
		final BufferedInputStream bis = new BufferedInputStream(is);
		int read = 0;
		final byte[] buffer = new byte[8192];
		while (true) {
			if (canceled) {
				break;
			}

			read = bis.read(buffer);
			if (read == -1) {
				break;
			}
			os.write(buffer, 0, read);
		}

		if (!canceled) {
			os.flush();
		}

		try {
			os.close();
		} catch (final IOException e) {}

		try {
			bis.close();
		} catch (final IOException e) {}

		try {
			is.close();
		} catch (final IOException e) {}
	}
}