package com.yuanluesoft.bidding.project.signup.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 投标报名打印页面
 * @author linchuan
 *
 */
public class SignUpPrintPageService extends PageService {
	private BiddingService biddingService; //投标服务
	private BiddingProjectService biddingProjectService; //项目服务 
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取报名记录
		BiddingSignUp signUp = biddingService.loadSignUp(request.getParameter("signUpNo"), false);
		if(signUp==null) {
			throw new PageNotFoundException();
		}
		//获取项目信息
		BiddingProject project = biddingProjectService.getProject(signUp.getProjectId());
		signUp.setProject(project);
		//设置record属性
		sitePage.setAttribute("record", signUp);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		BiddingSignUp signUp = (BiddingSignUp)sitePage.getAttribute("record");
		//设置标题
		String title = template.getTitle();
		template.setTitle((title==null || title.equals("") ? "报名号打印" : title) + " - " + signUp.getProjectName());
		//追加启动打印的脚本
		template.getBody().setAttribute("onload", "window.print()");
	}

	/**
	 * @return the biddingService
	 */
	public BiddingService getBiddingService() {
		return biddingService;
	}

	/**
	 * @param biddingService the biddingService to set
	 */
	public void setBiddingService(BiddingService biddingService) {
		this.biddingService = biddingService;
	}

	/**
	 * @return the biddingProjectService
	 */
	public BiddingProjectService getBiddingProjectService() {
		return biddingProjectService;
	}

	/**
	 * @param biddingProjectService the biddingProjectService to set
	 */
	public void setBiddingProjectService(BiddingProjectService biddingProjectService) {
		this.biddingProjectService = biddingProjectService;
	}
}