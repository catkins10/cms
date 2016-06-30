package com.yuanluesoft.portal.container.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletContext;

import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.pojo.PortletEntity;


/**
 * 
 * @author linchuan
 *
 */
public class PortletConfigImpl implements javax.portlet.PortletConfig {
	private PortletEntity portletEntity;
	private PortletContext portletContext;
	private PortletDefinition portletDefinition;
	
	public PortletConfigImpl(PortletEntity portletEntity, PortletContext portletContext, PortletDefinition portletDefinition) {
		super();
		this.portletEntity = portletEntity;
		this.portletContext = portletContext;
		this.portletDefinition = portletDefinition;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletConfig#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String name) {
		//从portlet定义中获取
		return portletDefinition.getInitParameterValue(name);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletConfig#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames() {
		List names;
		List initParameters = portletDefinition.getInitParameters();
		if(initParameters!=null) {
			names = ListUtils.generatePropertyList(portletDefinition.getInitParameters(), "name");
		}
		else {
			names = new ArrayList();
		}
		return names.isEmpty() ? null : Collections.enumeration(names);
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletConfig#getPortletContext()
	 */
	public PortletContext getPortletContext() {
		return portletContext;
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletConfig#getPortletName()
	 */
	public String getPortletName() {
		return portletDefinition.getPortletName();
	}

	/* (non-Javadoc)
	 * @see javax.portlet.PortletConfig#getResourceBundle(java.util.Locale)
	 */
	public ResourceBundle getResourceBundle(Locale locale) {
		try {
			return ResourceBundle.getBundle(portletDefinition.getResourceBundle(), locale);
		}
		catch(Exception e) {
			return null;
		}
	}

	/**
	 * @return the portletDefinition
	 */
	public PortletDefinition getPortletDefinition() {
		return portletDefinition;
	}

	/**
	 * @return the portletEntity
	 */
	public PortletEntity getPortletEntity() {
		return portletEntity;
	}
}