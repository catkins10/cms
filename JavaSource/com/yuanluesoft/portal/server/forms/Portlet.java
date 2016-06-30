package com.yuanluesoft.portal.server.forms;

import com.yuanluesoft.jeaf.form.ActionForm;




/**
 * 
 * @author linchuan
 *
 */
public class Portlet extends ActionForm {
	private String wsrpProducerId; //生产者ID
	private String portletInstanceKey; //PORTLET实例ID
	private String portletHandle; //PORTLET句柄
	private String urlType; //URL类型, Constants.URL_TYPE_BLOCKINGACTION/Constants.URL_TYPE_RENDER/Constants.URL_TYPE_RESOURCE
	private String portletMode; //PORTLET模式
	private String windowState; //PORTLET窗口状态
	private String navigationalState; //base64编码后的render参数
	private String interactionState; //base64编码后的action参数
	private String secureURL; //安全的资源URL
	private String resourceUrl; //资源URL
	private String rewriteResource; //重新资源
	private String userContextKey; //用户上下文KEY
    private String sessionId; //会话ID
    private long portalUserId = -1; //用户ID
    private long portalSiteId = -1; //站点ID
	
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
	 * @return the portletMode
	 */
	public String getPortletMode() {
		return portletMode;
	}
	/**
	 * @param portletMode the portletMode to set
	 */
	public void setPortletMode(String portletMode) {
		this.portletMode = portletMode;
	}
	/**
	 * @return the windowState
	 */
	public String getWindowState() {
		return windowState;
	}
	/**
	 * @param windowState the windowState to set
	 */
	public void setWindowState(String windowState) {
		this.windowState = windowState;
	}
	/**
	 * @return the interactionState
	 */
	public String getInteractionState() {
		return interactionState;
	}
	/**
	 * @param interactionState the interactionState to set
	 */
	public void setInteractionState(String interactionState) {
		this.interactionState = interactionState;
	}
	/**
	 * @return the navigationalState
	 */
	public String getNavigationalState() {
		return navigationalState;
	}
	/**
	 * @param navigationalState the navigationalState to set
	 */
	public void setNavigationalState(String navigationalState) {
		this.navigationalState = navigationalState;
	}
	/**
	 * @return the portletInstanceKey
	 */
	public String getPortletInstanceKey() {
		return portletInstanceKey;
	}
	/**
	 * @param portletInstanceKey the portletInstanceKey to set
	 */
	public void setPortletInstanceKey(String portletInstanceKey) {
		this.portletInstanceKey = portletInstanceKey;
	}
	/**
	 * @return the rewriteResource
	 */
	public String getRewriteResource() {
		return rewriteResource;
	}
	/**
	 * @param rewriteResource the rewriteResource to set
	 */
	public void setRewriteResource(String rewriteResource) {
		this.rewriteResource = rewriteResource;
	}
	/**
	 * @return the secureURL
	 */
	public String getSecureURL() {
		return secureURL;
	}
	/**
	 * @param secureURL the secureURL to set
	 */
	public void setSecureURL(String secureURL) {
		this.secureURL = secureURL;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the urlType
	 */
	public String getUrlType() {
		return urlType;
	}
	/**
	 * @param urlType the urlType to set
	 */
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	/**
	 * @return the userContextKey
	 */
	public String getUserContextKey() {
		return userContextKey;
	}
	/**
	 * @param userContextKey the userContextKey to set
	 */
	public void setUserContextKey(String userContextKey) {
		this.userContextKey = userContextKey;
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
	 * @return the resourceUrl
	 */
	public String getResourceUrl() {
		return resourceUrl;
	}
	/**
	 * @param resourceUrl the resourceUrl to set
	 */
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	/**
	 * @return the portalSiteId
	 */
	public long getPortalSiteId() {
		return portalSiteId;
	}
	/**
	 * @param portalSiteId the portalSiteId to set
	 */
	public void setPortalSiteId(long portalSiteId) {
		this.portalSiteId = portalSiteId;
	}
	/**
	 * @return the portalUserId
	 */
	public long getPortalUserId() {
		return portalUserId;
	}
	/**
	 * @param portalUserId the portalUserId to set
	 */
	public void setPortalUserId(long portalUserId) {
		this.portalUserId = portalUserId;
	}	
}