package com.yuanluesoft.portal.container.internal;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import oasis.names.tc.wsrp.v1.types.MarkupType;
import oasis.names.tc.wsrp.v1.types.PortletDescription;

/**
 * 
 * @author linchuan
 *
 */
public class PortletURLImpl implements PortletURL {
    private PortalContext portalContext; //PORTAL上下文
    private PortletWindow portletWindow;
	private PortletDescription portletDescription;
    private WindowState windowState; //窗口状态
    private PortletMode portletMode; //模式
    private boolean isAction;

    //私有属性
    private Map parameters = new HashMap();
    protected boolean secure; //暂不使用
    
	public PortletURLImpl(PortalContext portalContext, PortletWindow portletWindow, PortletDescription portletDescription, WindowState windowState, PortletMode portletMode, boolean isAction) {
		super();
		this.portalContext = portalContext;
		this.portletWindow = portletWindow;
		this.portletDescription = portletDescription;
		this.windowState = WindowState.MINIMIZED.equals(windowState) ? WindowState.NORMAL : windowState;
		this.portletMode = portletMode;
		this.isAction = isAction;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return portletWindow.getPortletURLGenerator().generatePortletURL(portletWindow, windowState, portletMode, parameters, isAction);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setParameter(java.lang.String, java.lang.String)
	 */
	public void setParameter(String name, String value) {
		parameters.put(name, new String[]{value});
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setParameter(java.lang.String, java.lang.String[])
	 */
	public void setParameter(String name, String[] values) {
		parameters.put(name, values);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setParameters(java.util.Map)
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setPortletMode(javax.portlet.PortletMode)
	 */
	public void setPortletMode(PortletMode portletMode) throws PortletModeException {
		if(!isPortletModeAllowed(portletMode)) { //Test and throw exception if not allowed.
			throw new PortletModeException("unsupported portlet mode used: " + portletMode, portletMode);
        }
		this.portletMode = portletMode;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setSecure(boolean)
	 */
	public void setSecure(boolean secure) throws PortletSecurityException {
		this.secure = secure;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletURL#setWindowState(javax.portlet.WindowState)
	 */
	public void setWindowState(WindowState windowState) throws WindowStateException {
		if(windowState==null || !isWindowStateAllowed(windowState)) {
	        throw new WindowStateException("unsupported Window State used: " + windowState, windowState);
        }
        this.windowState = windowState;
	}
	
	/**
	 * 判断窗口状态是否被允许
	 * @param state
	 * @return
	 */
    private boolean isWindowStateAllowed(WindowState state) {
        Enumeration supportedStates = portalContext.getSupportedWindowStates();
        while(supportedStates.hasMoreElements()) {
            if(supportedStates.nextElement().toString().toUpperCase().equals(
                (state.toString().toUpperCase()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断模式是否被支持
     * @param mode
     * @return
     * @throws PortletModeException
     */
    private boolean isPortletModeAllowed(PortletMode mode) throws PortletModeException {
        return isPortletModeAllowedByPortlet(mode) && isPortletModeAllowedByPortal(mode);
    }
    
    /**
     * 判断模式是否被PORTLET支持
     * @param mode
     * @return
     * @throws PortletModeException
     */
    private boolean isPortletModeAllowedByPortlet(PortletMode mode) throws PortletModeException {
        if(mode.equals(PortletMode.VIEW)) { //VIEW是必须支持的
            return true;
        }
        for(int i=0; i<(portletDescription.getMarkupTypes()==null ? 0 : portletDescription.getMarkupTypes().length); i++) {
        	MarkupType markupType = portletDescription.getMarkupTypes()[i];
        	for(int j=0; j<(markupType.getModes()==null ? 0 : markupType.getModes().length); j++) {
	            if(mode.toString().equals(markupType.getModes()[j])) {
	            	return true;
	            }
        	}
        }
        return false;
    }

    /**
     * 判断模式是否被PORTAL支持
     * @param mode
     * @return
     * @throws PortletModeException
     */
    private boolean isPortletModeAllowedByPortal(PortletMode mode)
        throws PortletModeException {
        Enumeration supportedModes = portalContext.getSupportedPortletModes();
        while(supportedModes.hasMoreElements()) {
            if (supportedModes.nextElement().toString().equalsIgnoreCase(mode.toString())) {
                return true;
            }
        }
        return false;
    }
}