package com.yuanluesoft.bidding.enterprise.services.spring;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author yuanlue
 *
 */
public class EnterpriseViewServiceImpl extends ViewServiceImpl {
	private EnterpriseService enterpriseService; //企业管理服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, int, boolean, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		if("selectFreeJobholder".equals(view.getName())) {
			String jobholderQualifications = request.getParameter("jobholderQualifications");
			if(jobholderQualifications!=null && !jobholderQualifications.isEmpty()) {
				view.addWhere("BiddingJobholder.qualification in ('" + JdbcUtils.resetQuot(jobholderQualifications).replaceAll(",", "','") + "')");
			}
		}
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewActions(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		super.retrieveViewActions(view, request, sessionInfo);
		if("selectFreeJobholder".equals(view.getName())) {
			return;
		}
		List workflowEntries = listWorkflowEntries("bidding/enterprise", sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) { //没有流程入口
			view.setActions(null);
			return;
		}
		//获取流程配置
		if(ListUtils.findObjectByProperty(workflowEntries, "workflowName", "企业注册")==null) {
			//没有企业注册权限,删除企业注册按钮
			ListUtils.removeObjectByProperty(view.getActions(), "title", "企业注册");
		}
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
