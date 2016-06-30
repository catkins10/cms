package com.yuanluesoft.portal.container.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class PortalApplication implements Serializable, Cloneable {
	private List portletDefinitions; //PORTLET定义列表

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		PortalApplication portalApplication = new PortalApplication();
		portalApplication.setPortletDefinitions(new ArrayList());
	    for(Iterator iterator = portletDefinitions==null ? null : portletDefinitions.iterator(); iterator!=null && iterator.hasNext();) {
	    	PortletDefinition portletDefinition = (PortletDefinition)iterator.next();
	    	portalApplication.getPortletDefinitions().add(portletDefinition.clone());
	    }
    	return portalApplication;
	}

	/**
	 * @return the portletDefinitions
	 */
	public List getPortletDefinitions() {
		return portletDefinitions;
	}

	/**
	 * @param portletDefinitions the portletDefinitions to set
	 */
	public void setPortletDefinitions(List portletDefinitions) {
		this.portletDefinitions = portletDefinitions;
	}
}