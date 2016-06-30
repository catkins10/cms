package com.yuanluesoft.portal.wsrp.producer.internal;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ProducerRequest extends HttpServletRequestWrapper {
	private String queryString;
	private Map parameterMap;
	private String requestMethod; //请求方法

	public ProducerRequest(HttpServletRequest request, String queryString, String requestMethod, Map parameterMap, SessionInfo sessionInfo) {
		super(request);
		this.queryString = queryString;
		this.parameterMap = parameterMap;
		this.requestMethod = requestMethod;
		request.getSession().setAttribute("SessionInfo", sessionInfo);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getMethod()
	 */
	public String getMethod() {
		return requestMethod;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getQueryString()
	 */
	public String getQueryString() {
		return queryString;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		return values==null ? null : values[0];
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
	 */
	public Map getParameterMap() {
		return parameterMap;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		return parameterMap==null ? null : Collections.enumeration(parameterMap.keySet());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String name) {
		return parameterMap==null ? null : (String[])parameterMap.get(name);
	}

	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
}