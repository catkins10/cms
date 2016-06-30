package com.yuanluesoft.cms.monitor.actions.monitorrecord;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.monitor.pojo.MonitorRecord;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorRecordAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(record==null) {
			throw new PrivilegeException();
		}
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员编辑
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		MonitorRecord monitorRecord = (MonitorRecord)record;
		try {
			if(getOrgService().checkPopedom(monitorRecord.getUnitId(), "manager,monitor", sessionInfo)) { //检查用户的管理权限和监察权限
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		catch (ServiceException e) {
		
		}
		throw new PrivilegeException();
	}
}