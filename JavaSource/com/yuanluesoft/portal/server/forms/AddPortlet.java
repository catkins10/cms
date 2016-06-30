package com.yuanluesoft.portal.server.forms;

import java.util.List;


/**
 * 
 * @author linchuan
 *
 */
public class AddPortlet extends PortalForm {
	private long pageId; //页面ID
	private String portletStyle; //PORTLET风格
	private int columnIndex = 1; //列号
	private String[] selectedWsrpProducerIds; //选中的WSRP生产者ID
	private String[] selectedPortletHandles; //选中PORTLET的句柄
	private String[] selectedPortletTitles; //选中PORTLET的标题
	private List portletGroups; //PORTLET分组列表
	
	/**
	 * @return the columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	/**
	 * @param columnIndex the columnIndex to set
	 */
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	/**
	 * @return the portletStyle
	 */
	public String getPortletStyle() {
		return portletStyle;
	}
	/**
	 * @param portletStyle the portletStyle to set
	 */
	public void setPortletStyle(String portletStyle) {
		this.portletStyle = portletStyle;
	}
	/**
	 * @return the pageId
	 */
	public long getPageId() {
		return pageId;
	}
	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	/**
	 * @return the selectedPortletHandles
	 */
	public String[] getSelectedPortletHandles() {
		return selectedPortletHandles;
	}
	/**
	 * @param selectedPortletHandles the selectedPortletHandles to set
	 */
	public void setSelectedPortletHandles(String[] selectedPortletHandles) {
		this.selectedPortletHandles = selectedPortletHandles;
	}
	/**
	 * @return the selectedPortletTitles
	 */
	public String[] getSelectedPortletTitles() {
		return selectedPortletTitles;
	}
	/**
	 * @param selectedPortletTitles the selectedPortletTitles to set
	 */
	public void setSelectedPortletTitles(String[] selectedPortletTitles) {
		this.selectedPortletTitles = selectedPortletTitles;
	}
	/**
	 * @return the selectedWsrpProducerIds
	 */
	public String[] getSelectedWsrpProducerIds() {
		return selectedWsrpProducerIds;
	}
	/**
	 * @param selectedWsrpProducerIds the selectedWsrpProducerIds to set
	 */
	public void setSelectedWsrpProducerIds(String[] selectedWsrpProducerIds) {
		this.selectedWsrpProducerIds = selectedWsrpProducerIds;
	}
	/**
	 * @return the portletGroups
	 */
	public List getPortletGroups() {
		return portletGroups;
	}
	/**
	 * @param portletGroups the portletGroups to set
	 */
	public void setPortletGroups(List portletGroups) {
		this.portletGroups = portletGroups;
	}
}