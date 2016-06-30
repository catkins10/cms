package com.yuanluesoft.portal.container.model;

import java.io.Serializable;
import java.util.List;

/**
 * PORTLET实体分组,添加PORTLET时使用
 * @author linchuan
 *
 */
public class PortletGroup implements Serializable {
	private String group; //分组
	private List portletEntities; //PORTLET实体列表
	
	public PortletGroup(String group, List portletEntities) {
		super();
		this.group = group;
		this.portletEntities = portletEntities;
	}
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the portletEntities
	 */
	public List getPortletEntities() {
		return portletEntities;
	}
	/**
	 * @param portletEntities the portletEntities to set
	 */
	public void setPortletEntities(List portletEntities) {
		this.portletEntities = portletEntities;
	}
}