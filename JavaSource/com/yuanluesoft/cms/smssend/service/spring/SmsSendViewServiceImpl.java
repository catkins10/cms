package com.yuanluesoft.cms.smssend.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.smssend.service.SmsSendService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 短信发送视图服务
 * @author linchuan
 *
 */
public class SmsSendViewServiceImpl extends ViewServiceImpl {
	private OrgService orgService; //组织机构服务
	private SmsSendService smsSendService; //短信发送服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		if(view.getName().equals("admin/workflowConfig")) { //流程配置
			//获取用户有管理权限的机构目录ID
			List directoryIds = orgService.listDirectoryIds("root,category,area,unit", "manager", true, sessionInfo, 0, 0);
			if(directoryIds==null || directoryIds.isEmpty()) {
				throw new PrivilegeException();
			}
			if(directoryIds.indexOf(new Long(0))==-1) { //不含根目录
				view.setJoin(",OrgSubjection OrgSubjection");
				view.addWhere("SmsSendWorkflow.orgId=OrgSubjection.directoryId and OrgSubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(ListUtils.join(directoryIds, ",", false)) + ")");
			}
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#listWorkflowEntries(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listWorkflowEntries(String applicationPath, SessionInfo sessionInfo) throws ServiceException {
		WorkflowEntry workflowEntry  = smsSendService.getSmsSendWorkflowEntry(sessionInfo);
		return workflowEntry==null ? null : ListUtils.generateList(workflowEntry);
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the smsSendService
	 */
	public SmsSendService getSmsSendService() {
		return smsSendService;
	}

	/**
	 * @param smsSendService the smsSendService to set
	 */
	public void setSmsSendService(SmsSendService smsSendService) {
		this.smsSendService = smsSendService;
	}
}