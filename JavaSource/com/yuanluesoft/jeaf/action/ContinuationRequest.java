package com.yuanluesoft.jeaf.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 
 * @author linchuan
 *
 */
public class ContinuationRequest extends HttpServletRequestWrapper implements HttpServletRequest {
	private Map parameters;
	
	public ContinuationRequest(HttpServletRequest request, Map parameters) {
		super(request);
		this.parameters = parameters;
		//追加request的参数
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames!=null && parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			this.parameters.put(parameterName, request.getParameterValues(parameterName));
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
		String[] values = getParameterValues(arg0);
		return values==null || values.length==0 ? null : values[0];
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		Set names = parameters.keySet();
		Hashtable hashtable = new Hashtable();
		if(names!=null && !names.isEmpty()) {
			for(Iterator iterator = names.iterator(); iterator.hasNext();) {
				String name = (String)iterator.next();
				hashtable.put(name, name);
			}
		}
		return hashtable.elements();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {
		return (String[])parameters.get(arg0);
	}
}