package com.yuanluesoft.portal.server.forms;



/**
 * 
 * @author linchuan
 *
 */
public class PortletJs extends PortalForm {
	private long pageId; //页面ID
	private long portletInstanceId; //PORTLET实体ID
	private String portletAction; //PORTLET操作,move/minimize/restore/remove
	private int portletColumnIndex; //列序号,portletAction为move时有效
	private int portletIndex; //序号,portletAction为move时有效
	
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
	 * @return the portletAction
	 */
	public String getPortletAction() {
		return portletAction;
	}
	/**
	 * @param portletAction the portletAction to set
	 */
	public void setPortletAction(String portletAction) {
		this.portletAction = portletAction;
	}
	/**
	 * @return the portletColumnIndex
	 */
	public int getPortletColumnIndex() {
		return portletColumnIndex;
	}
	/**
	 * @param portletColumnIndex the portletColumnIndex to set
	 */
	public void setPortletColumnIndex(int portletColumnIndex) {
		this.portletColumnIndex = portletColumnIndex;
	}
	/**
	 * @return the portletIndex
	 */
	public int getPortletIndex() {
		return portletIndex;
	}
	/**
	 * @param portletIndex the portletIndex to set
	 */
	public void setPortletIndex(int portletIndex) {
		this.portletIndex = portletIndex;
	}
	/**
	 * @return the portletInstanceId
	 */
	public long getPortletInstanceId() {
		return portletInstanceId;
	}
	/**
	 * @param portletInstanceId the portletInstanceId to set
	 */
	public void setPortletInstanceId(long portletInstanceId) {
		this.portletInstanceId = portletInstanceId;
	}
}