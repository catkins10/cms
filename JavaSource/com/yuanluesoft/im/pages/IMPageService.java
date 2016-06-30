package com.yuanluesoft.im.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.ClientPageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.im.model.IM;
import com.yuanluesoft.im.model.IMClientLogin;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * IM主页面服务
 * @author linchuan
 *
 */
public class IMPageService extends ClientPageService {
	private IMService imService; //IM服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		IM im = new IM();
		im.setUserId(sessionInfo.getUserId()); //用户ID
		im.setUserName(sessionInfo.getUserName()); //用户名
		im.setStatus("在线"); //用户状态
		sitePage.setAttribute("record", im);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.ClientPageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//添加脚本
		getHtmlParser().appendScriptFile(template, Environment.getContextPath() + "/im/js/im.js");
		IM im = (IM)sitePage.getAttribute("record");
		//输出客户端登录信息
		IMClientLogin clientLogin = imService.clientLogin(im.getUserId());
		getHtmlParser().appendHiddenField("ticket", clientLogin.getTicket(), template.getBody());
		getHtmlParser().appendHiddenField("personName", im.getUserName(), template.getBody());
		getHtmlParser().appendHiddenField("assignUdpChannelIP", clientLogin.getAssignUdpChannelIP()==null || clientLogin.getAssignUdpChannelIP().isEmpty() ? request.getRemoteHost() : clientLogin.getAssignUdpChannelIP(), template.getBody());
		getHtmlParser().appendHiddenField("assignUdpChannelPort", "" + (int)clientLogin.getAssignUdpChannelPort(), template.getBody());
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