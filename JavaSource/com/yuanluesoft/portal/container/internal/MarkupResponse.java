package com.yuanluesoft.portal.container.internal;

/**
 * 
 * @author linchuan
 *
 */
public class MarkupResponse {
	private String contentType; //类型
	private String markup; //标记
	private String portletViewURL; //portlet浏览URL
	private String portletEditURL; //portlet编辑URL
	
	public MarkupResponse() {
		super();
	}
	
	public MarkupResponse(String markup) {
		super();
		this.markup = markup;
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the markup
	 */
	public String getMarkup() {
		return markup;
	}
	/**
	 * @param markup the markup to set
	 */
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	/**
	 * @return the portletEditURL
	 */
	public String getPortletEditURL() {
		return portletEditURL;
	}
	/**
	 * @param portletEditURL the portletEditURL to set
	 */
	public void setPortletEditURL(String portletEditURL) {
		this.portletEditURL = portletEditURL;
	}
	/**
	 * @return the portletViewURL
	 */
	public String getPortletViewURL() {
		return portletViewURL;
	}
	/**
	 * @param portletViewURL the portletViewURL to set
	 */
	public void setPortletViewURL(String portletViewURL) {
		this.portletViewURL = portletViewURL;
	}
}