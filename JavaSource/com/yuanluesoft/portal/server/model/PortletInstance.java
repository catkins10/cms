package com.yuanluesoft.portal.server.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class PortletInstance implements Serializable {
	private long id; //ID
	private String portletTitle; //标题
	private String wsrpProducerId; //WSRP生产者ID
	private String portletHandle; //PORTLET句柄
	private String portletStyle; //PORTLET风格
	private int columnIndex; //列号
	private String state; //状态,minimized|最小化,normal|默认
	
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the portletHandle
	 */
	public String getPortletHandle() {
		return portletHandle;
	}
	/**
	 * @param portletHandle the portletHandle to set
	 */
	public void setPortletHandle(String portletHandle) {
		this.portletHandle = portletHandle;
	}
	/**
	 * @return the portletTitle
	 */
	public String getPortletTitle() {
		return portletTitle;
	}
	/**
	 * @param portletTitle the portletTitle to set
	 */
	public void setPortletTitle(String portletTitle) {
		this.portletTitle = portletTitle;
	}
	/**
	 * @return the wsrpProducerId
	 */
	public String getWsrpProducerId() {
		return wsrpProducerId;
	}
	/**
	 * @param wsrpProducerId the wsrpProducerId to set
	 */
	public void setWsrpProducerId(String wsrpProducerId) {
		this.wsrpProducerId = wsrpProducerId;
	}
}