package com.yuanluesoft.job.company.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.apply.service.JobApplyService;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class JobPageService extends PageService {
	private JobApplyService jobApplyService; //应聘服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetPageLink(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetPageLink(link, linkName, template, applicationName, pageName, sitePage, siteId, request);
		if(linkName.equals("收藏") || linkName.equals("取消收藏")) {
			//检查用户是否已经收藏过
			Boolean isFavarite = (Boolean)sitePage.getAttribute("isFavarite");
			if(isFavarite==null) {
				isFavarite = Boolean.FALSE;
				SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
				if(sessionInfo!=null && sessionInfo.getUserType()==JobTalentService.PERSON_TYPE_JOB_TALENT) {
					isFavarite = new Boolean(jobApplyService.isFavorite(sessionInfo.getUserId(), RequestUtils.getParameterLongValue(request, "id")));
				}
				sitePage.setAttribute("isFavarite", isFavarite);
			}
			if((isFavarite.booleanValue() && linkName.equals("收藏")) || (!isFavarite.booleanValue() && linkName.equals("取消收藏"))) {
				link.getParentNode().removeChild(link);
			}
		}
	}

	/**
	 * @return the jobApplyService
	 */
	public JobApplyService getJobApplyService() {
		return jobApplyService;
	}

	/**
	 * @param jobApplyService the jobApplyService to set
	 */
	public void setJobApplyService(JobApplyService jobApplyService) {
		this.jobApplyService = jobApplyService;
	}
}