package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.listaccepts;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;


/**
 * 
 * @author linchuan
 *
 */
public class Load extends com.yuanluesoft.cms.onlineservice.interactive.actions.listinteractives.Load {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		long itemId = RequestUtils.getParameterLongValue(request, "itemId");
		return new Link(Environment.getWebApplicationSafeUrl() +  "/cms/onlineservice/accept/login.shtml?templateName=onlineservice" + (itemId>0 ? "&itemId=" + itemId : "") + (siteId>0 ? "&siteId=" + siteId : ""), "utf-8");
	}
}