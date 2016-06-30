package com.yuanluesoft.enterprise.project.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 工程视图定义服务
 * @author yuanlue
 *
 */
public class EnterpriseProjectViewServiceImpl extends ViewServiceImpl {
	private EnterpriseProjectService enterpriseProjectService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewActions(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		super.retrieveViewActions(view, request, sessionInfo);
		List enteredProjectTypes = enterpriseProjectService.listEnteredProjectTypeList(sessionInfo);
		if(enteredProjectTypes==null || enteredProjectTypes.isEmpty()) { //没有流程入口
			view.setActions(null);
			return;
		}
	}

	/**
	 * @return the enterpriseProjectService
	 */
	public EnterpriseProjectService getEnterpriseProjectService() {
		return enterpriseProjectService;
	}

	/**
	 * @param enterpriseProjectService the enterpriseProjectService to set
	 */
	public void setEnterpriseProjectService(EnterpriseProjectService enterpriseProjectService) {
		this.enterpriseProjectService = enterpriseProjectService;
	}
}