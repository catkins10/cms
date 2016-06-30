package com.yuanluesoft.im.webim.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.im.model.message.SystemMessageDetail;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class WebimSystemMessagePageService extends PageService {
	private IMService imService; //IM服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		long systemMessageId = RequestUtils.getParameterLongValue(request, "systemMessageId");
		SystemMessageDetail systemMessageDetail;
		if(systemMessageId==0) {
			systemMessageDetail = new SystemMessageDetail();
			systemMessageDetail.setSystemMessageId(-1);
		}
		else {
			systemMessageDetail = imService.retrieveSystemMessage(RequestUtils.getSessionInfo(request).getUserId(), systemMessageId);
			if(systemMessageDetail==null) {
				systemMessageDetail = new SystemMessageDetail();
			}
		}
		sitePage.setAttribute("record", systemMessageDetail);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		SystemMessageDetail systemMessageDetail = (SystemMessageDetail)sitePage.getAttribute("record");
		if(systemMessageDetail.getSystemMessageId()>=0) {
			String onload = "parent.frames['frameWebim'].webim.getAction('系统消息').setRecordCount(parent.frames['frameWebim'].webim.getAction('系统消息').getRecordCount()-1);";
			if(systemMessageDetail.getSystemMessageId()>0) {
				template.getBody().setAttribute("onmousemove", "parent.frames['frameWebim'].webim.feedbackSystemMessage();");
			}
			else {
				onload += "parent.frames['frameWebim'].webim.retrieveNextSystemMessage();";
			}
			template.getBody().setAttribute("onload", onload);
		}
	}

	/**
	 * @return the imService
	 */
	public IMService getImService() {
		return imService;
	}

	/**
	 * @param imService the imService to set
	 */
	public void setImService(IMService imService) {
		this.imService = imService;
	}
}