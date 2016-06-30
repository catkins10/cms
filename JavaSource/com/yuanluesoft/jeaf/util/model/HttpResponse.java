package com.yuanluesoft.jeaf.util.model;

import org.apache.commons.httpclient.Header;

/**
 * 
 * @author linchuan
 *
 */
public class HttpResponse {
	private int statusCode; //状态码
	private String responseBody; //内容
	private Header[] responseHeaders; //头部
	private String cookie; //COOKIE
	private String url;
	
	/**
	 * @return the responseBody
	 */
	public String getResponseBody() {
		return responseBody;
	}
	/**
	 * @param responseBody the responseBody to set
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	/**
	 * @return the responseHeaders
	 */
	public Header[] getResponseHeaders() {
		return responseHeaders;
	}
	/**
	 * @param responseHeaders the responseHeaders to set
	 */
	public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the cookie
	 */
	public String getCookie() {
		return cookie;
	}
	/**
	 * @param cookie the cookie to set
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}