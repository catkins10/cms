package com.yuanluesoft.portal.container.model;

/**
 * PORTLET实体,添加PORTLET时使用
 * @author linchuan
 *
 */
public class PortletEntity {
	private String wsrpProducerId; //WSRP生产者ID
	private String handle; //句柄
	private String title; //实体名称
	private String description; //描述
	private String portletModes; //支持的模式列表,用逗号分隔
	private String windowStates; //支持的窗口状态列表,用逗号分隔
	
	//扩展属性,添加PORTLET时使用
	private boolean added; //是否已经加入过
	
	public PortletEntity(String wsrpProducerId, String handle, String title, String description, String portletModes, String windowStates) {
		super();
		this.wsrpProducerId = wsrpProducerId;
		this.handle = handle;
		this.title = title;
		this.description = description;
		this.portletModes = portletModes;
		this.windowStates = windowStates;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the handle
	 */
	public String getHandle() {
		return handle;
	}
	/**
	 * @param handle the handle to set
	 */
	public void setHandle(String handle) {
		this.handle = handle;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}
	/**
	 * @param added the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}
	/**
	 * @return the portletModes
	 */
	public String getPortletModes() {
		return portletModes;
	}
	/**
	 * @param portletModes the portletModes to set
	 */
	public void setPortletModes(String portletModes) {
		this.portletModes = portletModes;
	}
	/**
	 * @return the windowStates
	 */
	public String getWindowStates() {
		return windowStates;
	}
	/**
	 * @param windowStates the windowStates to set
	 */
	public void setWindowStates(String windowStates) {
		this.windowStates = windowStates;
	}
}