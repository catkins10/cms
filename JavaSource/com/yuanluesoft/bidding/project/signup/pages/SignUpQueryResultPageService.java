package com.yuanluesoft.bidding.project.signup.pages;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoomSchedule;
import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 报名号查询结果页面服务
 * @author linchuan
 *
 */
public class SignUpQueryResultPageService extends PageService {
	private BiddingProjectService biddingProjectService; //工程服务
	private BiddingService biddingService; //投标服务
	private AttachmentService attachmentService; //附件服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取报名记录
		BiddingSignUp signUp = biddingService.loadSignUp(request.getParameter("signUpNo"), true);
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
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetRecordList(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetRecordList(HTMLAnchorElement recordListElement, String recordListName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetRecordList(recordListElement, recordListName, template, applicationName, pageName, sitePage, siteId, request);
		BiddingSignUp signUp = (BiddingSignUp)sitePage.getAttribute("record");
		if("project.biddingDocuments".equals(recordListName) || "project.otherBiddingDocuments".equals(recordListName)) { //招标文件/其他招标文件
			if(signUp.getPaymentTime()==null) { //未缴费
				recordListElement.getParentNode().removeChild(recordListElement);
			}
		}
		else if("project.biddingDrawing".equals(recordListName)) { //图纸
			if(signUp.getDrawPaymentTime()==null) { //未缴费
				recordListElement.getParentNode().removeChild(recordListElement);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetPageLink(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetPageLink(link, linkName, template, applicationName, pageName, sitePage, siteId, request);
		BiddingSignUp signUp = (BiddingSignUp)sitePage.getAttribute("record");
		//获取项目信息
		BiddingProject project = (BiddingProject)sitePage.getAttribute("currentProject");
		if(project==null) {
			project = biddingProjectService.getProject(signUp.getProjectId());
			sitePage.setAttribute("currentProject", project);
		}
		//判断是否在报名时间内,超出则删除购买链接
		boolean isSignUpTimeout =  biddingService.isSignUpTimeout(project);
		if("网上购买标书和图纸".equals(linkName)) {
			if(isSignUpTimeout || signUp.getPaymentTime()!=null || signUp.getDrawPaymentTime()!=null) { //已经缴费过
				link.getParentNode().removeChild(link);
			}
		}
		else if("网上购买标书".equals(linkName)) {
			if(isSignUpTimeout || signUp.getPaymentTime()!=null) { //已经缴费过
				link.getParentNode().removeChild(link);
			}
		}
		else if("网上购买图纸".equals(linkName)) {
			if(isSignUpTimeout || signUp.getPaymentTime()==null || signUp.getDrawPaymentTime()!=null) { //标书未买、或者图纸已经缴费过
				link.getParentNode().removeChild(link);
			}
		}
		else if("网上缴纳保证金".equals(linkName)) {
			if(signUp.getPaymentTime()==null || signUp.getPledgePaymentTime()!=null || //未缴纳报名费或者已经缴过保证金
			   (biddingService.isPledgePaymentTimeout(project)) || //超过保证金缴费时间
				biddingService.getPledgePaymentMerchantIds(project.getId(), project.getPledgeAccount())==null) { //没有找到保证金支付商户
				link.getParentNode().removeChild(link);
			}
		}
		else if("下载标书".equals(linkName)) {
			if(signUp.getPaymentTime()==null) { //还没有缴费
				link.getParentNode().removeChild(link);
			}
		}
		else if("上传投标书".equals(linkName)) { //没有缴费,备注：南平需要缴纳保证金 signUp.getUploadTime()!=null || 已经上传过
			BiddingRoomSchedule schedule = project.getBidopeningRoomSchedule();
			if(signUp.getPaymentTime()==null ||
			   schedule==null || schedule.getBeginTime()==null || DateTimeUtils.now().after(DateTimeUtils.add(schedule.getBeginTime(), Calendar.MINUTE, -biddingService.getBidUploadPaddingMinutes()))) {
				link.getParentNode().removeChild(link);
			}
		}
		else if("答疑纪要".equals(linkName)) {
			if(!project.isComponentPublic("answer")) { //没有答疑纪要
				link.getParentNode().removeChild(link);
			}
		}
		else if("补充通知".equals(linkName)) {
			if(!project.isComponentPublic("supplement")) { //没有补充通知
				link.getParentNode().removeChild(link);
			}
		}
		else if("预审公示".equals(linkName)) {
			if(!project.isComponentPublic("preapproval")) { //预审公示未发布
				link.getParentNode().removeChild(link);
			}
		}
		else if("开标公示".equals(linkName)) {
			if(!project.isComponentPublic("bidopening")) { //开标公示未发布
				link.getParentNode().removeChild(link);
			}
		}
		else if("中标公示".equals(linkName)) {
			if(!project.isComponentPublic("pitchon")) { //中标公示未发布
				link.getParentNode().removeChild(link);
			}
		}
		else if("中标通知书".equals(linkName)) {
			if(!project.isComponentPublic("notice")) { //中标通知书未发布
				link.getParentNode().removeChild(link);
			}
		}
		else if("领取纸质标书和图纸".equals(linkName)) {
			if(signUp.getReceivePaperDocumentsTime()!=null) { //纸质标书和图纸已领取
				link.getParentNode().removeChild(link);
			}
			else if(signUp.getPaymentTime()==null && signUp.getDrawPaymentTime()!=null) { //标书、图纸未支付
				link.getParentNode().removeChild(link);
			}
			else {
				//检查当前用户是否是当前项目的代理
				SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
				if(!(sessionInfo instanceof BiddingSessionInfo) || project.getAgentId()!=sessionInfo.getUnitId()) {
					link.getParentNode().removeChild(link);
				}
			}
		}
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
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
}