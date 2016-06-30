package com.yuanluesoft.chd.evaluation.pages;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantDetail;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PlantPageService extends PageService {
	private EvaluationDirectoryService evaluationDirectoryService;
	private SiteService siteService; //站点服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		Object record = request.getAttribute("record");
		if((template.getTitle()==null || template.getTitle().isEmpty()) && (record instanceof ChdEvaluationDirectory)) {
			template.setTitle(((ChdEvaluationDirectory)record).getDirectoryName());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetPageLink(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetPageLink(HTMLAnchorElement link, String linkName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetPageLink(link, linkName, template, applicationName, pageName, sitePage, siteId, request);
		SessionInfo sessionInfo =  RequestUtils.getSessionInfo(request);
		if("修改企业信息".equals(linkName)) {
			if(!evaluationDirectoryService.checkPopedom(RequestUtils.getParameterLongValue(request, "plantId"), "manager,leader", sessionInfo)) {
				link.getParentNode().removeChild(link);
			}
		}
		else if("提交必备条件完成情况".equals(linkName)) {
			if(!evaluationDirectoryService.checkPopedom(RequestUtils.getParameterLongValue(request, "plantId"), "manager,leader,transactor", sessionInfo)) {
				link.getParentNode().removeChild(link);
			}
		}
		else if("提交指标完成情况".equals(linkName)) {
			long directoryId = RequestUtils.getParameterLongValue(request, "directoryId");
			if(!evaluationDirectoryService.checkPopedom(directoryId, "manager,leader,transactor", sessionInfo)) { //没有当前目录的提交权限
				List popedoms = evaluationDirectoryService.listChildDirectoryPopedoms(directoryId, sessionInfo); //获取用户对子目录的权限
				if(popedoms==null || (popedoms.indexOf("manager")==-1 && popedoms.indexOf("leader")==-1 && popedoms.indexOf("transactor")==-1)) {
					link.getParentNode().removeChild(link);
				}
			}
		}
		else if("资料提交".equals(linkName)) {
			if(!evaluationDirectoryService.checkPopedom(RequestUtils.getParameterLongValue(request, "directoryId"), "transactor", sessionInfo)) {
				link.getParentNode().removeChild(link);
			}
		}
		else if("自查提交".equals(linkName)) {
			ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)request.getAttribute("record");;
			if(!(evaluationDirectory instanceof ChdEvaluationPlantDetail) || !evaluationDirectoryService.checkPopedom(RequestUtils.getParameterLongValue(request, "directoryId"), "transactor", sessionInfo)) {
				link.getParentNode().removeChild(link);
			}
		}
	}

	/**
	 * @return the evaluationDirectoryService
	 */
	public EvaluationDirectoryService getEvaluationDirectoryService() {
		return evaluationDirectoryService;
	}

	/**
	 * @param evaluationDirectoryService the evaluationDirectoryService to set
	 */
	public void setEvaluationDirectoryService(
			EvaluationDirectoryService evaluationDirectoryService) {
		this.evaluationDirectoryService = evaluationDirectoryService;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}