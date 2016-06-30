package com.yuanluesoft.cms.monitor.actions.unitconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.monitor.forms.UnitConfig;
import com.yuanluesoft.cms.monitor.pojo.MonitorUnitConfig;
import com.yuanluesoft.cms.monitor.service.MonitorService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class UnitConfigAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		MonitorUnitConfig unitConfig = (MonitorUnitConfig)record;
		try {
			if(unitConfig!=null) {
				if(getOrgService().isOrgManager(unitConfig.getUnitId(), sessionInfo)) { //检查用户是否单位管理员
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			else {
				List directoryIds = getOrgService().listDirectoryIds("root,category,area,unit", "manager", true, sessionInfo, 0, 1); //获取用户有管理权限的目录ID
				if(directoryIds!=null && !directoryIds.isEmpty()) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
		}
		catch (ServiceException e) {
		
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		UnitConfig unitConfigForm = (UnitConfig)form;
		MonitorService monitorService = (MonitorService)getService("monitorService");
		unitConfigForm.setCaptureTime(monitorService.getCaptureTime()); //设置采集时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		form.getTabs().addTab(-1, "sqls", "SQL语句", null, false);
	}
}