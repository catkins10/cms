package com.yuanluesoft.bidding.enterprise.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEmployee;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteLink;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 企业首页
 * @author linchuan
 *
 */
public class BiddingEnterpriseIndexPageService extends PageService {
	private DatabaseService databaseService;
	private EnterpriseService enterpriseService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetPageLink(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetPageLink(link, linkName, template, applicationName, pageName, sitePage, siteId, request);
		if("企业变更".equals(linkName)) {
			SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
			if(!(sessionInfo instanceof BiddingSessionInfo)) {
				link.getParentNode().removeChild(link);
				return;
			}
			if(!enterpriseService.isEnterpriseValid(sessionInfo.getDepartmentId())) { //企业未生效
				link.getParentNode().removeChild(link);
				return;
			}
			//检查是否有未办结的企业变更记录
			String hql = "select BiddingEnterprise.id" +
						 " from BiddingEnterprise BiddingEnterprise, BiddingEnterprisePrivilege BiddingEnterprisePrivilege, WorkItem WorkItem" +
						 " where BiddingEnterprise.alterEnterpriseId=" + sessionInfo.getDepartmentId() +
						 " and BiddingEnterprise.isAlter='1'" +
						 " and WorkItem.recordId=BiddingEnterprise.id" +
						 " and BiddingEnterprisePrivilege.recordId=BiddingEnterprise.id" +
						 " and BiddingEnterprisePrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
			Number alterId = (Number)databaseService.findRecordByHql(hql);
			if(alterId!=null) {
				SiteLink pageLink = (SiteLink)ListUtils.findObjectByProperty(sitePage.getLinks(), "title", linkName);
				pageLink.setUrl("/bidding/enterprise/enterprise.shtml?id=" + alterId);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取企业用户
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		BiddingEmployee employee = (BiddingEmployee)databaseService.findRecordById(BiddingEmployee.class.getName(), sessionInfo.getUserId());
		if(employee==null) { 
			employee = new BiddingEmployee();
		}
		sitePage.setAttribute("record", employee); //设置record属性
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the enterpriseService
	 */
	public EnterpriseService getEnterpriseService() {
		return enterpriseService;
	}

	/**
	 * @param enterpriseService the enterpriseService to set
	 */
	public void setEnterpriseService(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}
}