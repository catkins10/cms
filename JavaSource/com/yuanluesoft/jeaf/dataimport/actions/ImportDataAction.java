package com.yuanluesoft.jeaf.dataimport.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.dataimport.services.DataImportService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class ImportDataAction extends BaseAction {
	
	/**
	 * 检查用户权限
	 * @param request
	 * @param response
	 * @throws SessionException
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	protected void checkPrivilege(HttpServletRequest request, HttpServletResponse response) throws SessionException, PrivilegeException, SystemUnregistException {
		OrgService orgService = (OrgService)getService("orgService");
		if(!orgService.checkPopedom(0, "manager", getSessionInfo(request, response))) {
			throw new PrivilegeException();
		}
	}
	
	/**
	 * 获取数据导入服务
	 * @param parameter
	 * @return
	 */
	protected DataImportService getDataImportService(String dateImportServiceClass) {
		try {
			return (DataImportService)Class.forName(dateImportServiceClass).newInstance();
		}
		catch (Exception e) {
			return null;
		}
	}
}