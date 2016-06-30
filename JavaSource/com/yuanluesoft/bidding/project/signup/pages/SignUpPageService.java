package com.yuanluesoft.bidding.project.signup.pages;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 投标报名页面
 * @author linchuan
 *
 */
public class SignUpPageService extends PageService {
	private BiddingProjectService biddingProjectService;
	private BiddingService biddingService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取工程
		BiddingProject project = (BiddingProject)biddingProjectService.getProject(RequestUtils.getParameterLongValue(request, "projectId"));
		BiddingProjectTender tender = project.getTender();
		if(tender==null) {
			throw new PageNotFoundException();
		}
		//检查报名时间是否超过
		Timestamp now = DateTimeUtils.now();
		if(tender.getBuyDocumentEnd()!=null && now.after(tender.getBuyDocumentEnd())) {
			throw new ServiceException("不在报名时间段内,不允许报名");
		}
		BiddingSignUp signUp = new BiddingSignUp();
		try {
			PropertyUtils.copyProperties(signUp, tender);
		}
		catch (Exception e) {
			
		}
		signUp.setId(UUIDLongGenerator.generateId());
		//设置企业名称和用户名
		signUp.setPaymentMoney(biddingService.getSignUpPrice(tender.getProjectId())); //报名费
		signUp.setDrawPaymentMoney(tender.getDrawingPrice()); //图纸价格
		signUp.setPledgePaymentMoney(tender.getPledgeMoney()); //保证金
		signUp.setProject(project); //项目
		signUp.setBiddingAgent(project.getBiddingAgent()); //代理
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
		template.setTitle((title==null || title.equals("") ? "投标报名" : title) + " - " + signUp.getProjectName());
		String error = request.getParameter("error");
		if(error!=null) {
			//增加错误提醒
			template.getBody().setAttribute("onload", "alert('" + error + "')");
		}
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