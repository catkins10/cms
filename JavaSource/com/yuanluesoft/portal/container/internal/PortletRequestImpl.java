package com.yuanluesoft.portal.container.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import oasis.names.tc.wsrp.v1.types.MarkupType;
import oasis.names.tc.wsrp.v1.types.PortletDescription;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.portal.container.service.PortletContainer;

/**
 * 
 * @author linchuan
 *
 */
public class PortletRequestImpl extends HttpServletRequestWrapper implements PortletRequest {
	private PortletContainer portletContainer;
	private PortalContext portalContext;
	private PortletContext portletContext;
	private PortletDescription portletDescription;
	private PortletWindow portletWindow;
	
	//私有属性
	private PortletSession portletSession;
	private PortletPreferences portletPreferences;
	private HashMap parameters = new HashMap();
	private Map portletURLParameters; //PORTAL URL参数
	private String originalURL; //原始URL

	public PortletRequestImpl(HttpServletRequest request, PortletContainer portletContainer, PortalContext portalContext, PortletContext portletContext, PortletDescription portletDescription, PortletWindow portletWindow) {
		super(request);
		this.portletContainer = portletContainer;
		this.portalContext = portalContext;
		this.portletContext = portletContext;
		this.portletDescription = portletDescription;
		this.portletWindow = portletWindow;
		//解析PORTAL URL参数
		String parameters = request.getParameter("navigationalState");
		if(parameters==null) {
			parameters = request.getParameter("interactionState");
		}
		if(parameters!=null) {
			try {
				portletURLParameters = (Map)ObjectSerializer.deserialize(new Base64Decoder().decode(parameters)); //解析url参数
				String[] values = (String[])portletURLParameters.get("originalURL");
				if(values!=null) {
					originalURL = values[0];
				}
			}
			catch (Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 设置参数
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, String value) {
		parameters.put(name, new String[]{value});
	}

	/**
	 * 设置参数
	 * @param name
	 * @param values
	 */
	public void setParameter(String name, String[] values) {
		parameters.put(name, values);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getQueryString()
	 */
	public String getQueryString() {
		String queryString = super.getQueryString();
		if(queryString==null) {
			queryString = "";
		}
		else {
			//删除ticket、seq以及portlet url参数
			queryString = StringUtils.removeQueryParameters(queryString, "ticket,seq,portletInstanceKey,wsrpProducerId,urlType,portletHandle,portletMode,windowState,interactionState,navigationalState,secureURL,resourceUrl,rewriteResource,userContextKey,sessionId,portalUserId,portalSiteId");
		}
		//添加原始URL参数
		Map originalParameters = StringUtils.getQueryParameters(originalURL);
		for(Iterator iterator = originalParameters==null ? null : originalParameters.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String name = (String)iterator.next();
			if(queryString.indexOf(name + "=")!=-1) { //已经出现过
				continue;
			}
			String[] values = (String[])originalParameters.get(name);
			for(int i=0; i<(values==null ? 0 : values.length); i++) {
				try {
					queryString += (queryString.isEmpty() ? "" : "&") + name + "=" + URLEncoder.encode(values[i], "utf-8");
				} 
				catch (UnsupportedEncodingException e) {
					Logger.exception(e);
				}
			}
		}
		return queryString.isEmpty() ? null : queryString;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		return values==null ? null : values[0];
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String name) {
		String[] values = (String[])parameters.get(name);
		if(values!=null) {
			return values;
		}
		values = (String[])super.getParameterValues(name);
		if(values!=null) {
			return values;
		}
		//从portal url参数中获取
		values = portletURLParameters==null ? null : (String[])portletURLParameters.get(name);
		if(values!=null) {
			return values;
		}
		//从原始URL中获取
		String value = StringUtils.getQueryParameter(originalURL, name);
		return value==null ? null : new String[]{value};
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
	 */
	public Map getParameterMap() {
		Map parameters = (Map)this.parameters.clone();
		addParameters(parameters, super.getParameterMap());
		addParameters(parameters, portletURLParameters); //portlet url参数
		addParameters(parameters, StringUtils.getQueryParameters(originalURL)); //原始url参数
		return parameters;
	}
	
	/**
	 * 添加参数
	 * @param parameters
	 * @param toAddParameters
	 */
	private void addParameters(Map parameters, Map toAddParameters) {
		for(Iterator iterator = toAddParameters==null ? null : toAddParameters.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String name = (String)iterator.next();
			if(parameters.get(name)==null) {
				parameters.put(name, toAddParameters.get(name));
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		Map map = getParameterMap();
		return map==null ? null : Collections.enumeration(map.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
        if(PortletRequest.USER_INFO.equals(name)) {
            return RequestUtils.getSessionInfo(getHttpServletRequest());
        }
        Object value = super.getAttribute(name);
        if(value==null || !"javax.servlet.forward.request_uri".equals(name)) { //不是forward前的地址
        	return value;
        }
    	String textValue = (String)value;
    	if(textValue.indexOf("portlet.shtml")!=-1 || textValue.indexOf("/services/")!=-1) { 
    		return null;
    	}
        return value;
	}
	
	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPortalContext()
	 */
	public PortalContext getPortalContext() {
		return portalContext;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPortletMode()
	 */
	public PortletMode getPortletMode() {
		return portletWindow.getPortletMode();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPortletSession()
	 */
	public PortletSession getPortletSession() {
		return getPortletSession(true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getRemoteUser()
	 */
	public String getRemoteUser() {
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(getHttpServletRequest());
		return sessionInfo.getLoginName();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPortletSession(boolean)
	 */
	public PortletSession getPortletSession(boolean create) {
		if(portletContext == null) {
            throw new IllegalStateException();
        }
        HttpSession httpSession = getHttpServletRequest().getSession(create);
        if(httpSession != null) {
            int maxInactiveInterval = httpSession.getMaxInactiveInterval();
            if (maxInactiveInterval >= 0) { // < 0 => Never expires.
                long maxInactiveTime = httpSession.getMaxInactiveInterval() * 1000L;
                long currentInactiveTime = System.currentTimeMillis() - httpSession.getLastAccessedTime();
                if (currentInactiveTime > maxInactiveTime) {
                    httpSession.invalidate();
                    httpSession = getHttpServletRequest().getSession(create);
                }
            }
        }
        if(httpSession == null) {
            return null;
        }
        if (portletSession == null) {
            portletSession = new PortletSessionImpl(httpSession, portletContext);
        }
        return portletSession;
	}
	
	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPreferences()
	 */
	public PortletPreferences getPreferences() {
		if (portletPreferences == null) {
            portletPreferences = new PortletPreferencesImpl(getPortletContainer(), getPortletWindow(), this);
        }
        return portletPreferences;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getProperties(java.lang.String)
	 */
	public Enumeration getProperties(String name) {
		return getHttpServletRequest().getHeaders(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		return getHttpServletRequest().getHeader(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getPropertyNames()
	 */
	public Enumeration getPropertyNames() {
		return getHttpServletRequest().getHeaderNames();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getResponseContentType()
	 */
	public String getResponseContentType() {
		return (String)getResponseContentTypes().nextElement();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getResponseContentTypes()
	 */
	public Enumeration getResponseContentTypes() {
        List types = ListUtils.generatePropertyList(portletDescription.getMarkupTypes(), "mimeType");
        if(types==null) {
        	types = ListUtils.generateList("text/html");
        }
        return Collections.enumeration(types);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#getWindowState()
	 */
	public WindowState getWindowState() {
		return portletWindow.getWindowState();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#isPortletModeAllowed(javax.portlet.PortletMode)
	 */
	public boolean isPortletModeAllowed(PortletMode mode) {
		if(portletDescription.getMarkupTypes()==null || portletDescription.getMarkupTypes().length==0) {
			return PortletMode.VIEW.equals(mode) || PortletMode.EDIT.equals(mode) || PortletMode.HELP.equals(mode);
		}
		for(int i=0; i<portletDescription.getMarkupTypes().length; i++) {
			MarkupType markupType = portletDescription.getMarkupTypes()[i];
			for(int j=0; j<(markupType.getModes()==null ? 0 : markupType.getModes().length); j++) {
				if(mode.toString().equals(markupType.getModes()[j])) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletRequest#isWindowStateAllowed(javax.portlet.WindowState)
	 */
	public boolean isWindowStateAllowed(WindowState state) {
		for(Enumeration en = portalContext.getSupportedWindowStates(); en.hasMoreElements();) {
			if(en.nextElement().toString().equals(state.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取HTTP请求
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest)super.getRequest();
    }

	/**
	 * @return the portletContainer
	 */
	public PortletContainer getPortletContainer() {
		return portletContainer;
	}

	/**
	 * @return the portletContext
	 */
	public PortletContext getPortletContext() {
		return portletContext;
	}

	/**
	 * @return the portletWindow
	 */
	public PortletWindow getPortletWindow() {
		return portletWindow;
	}

	/**
	 * @return the portletDescription
	 */
	public PortletDescription getPortletDescription() {
		return portletDescription;
	}
}