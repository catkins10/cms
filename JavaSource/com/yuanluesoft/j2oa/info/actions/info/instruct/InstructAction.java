package com.yuanluesoft.j2oa.info.actions.info.instruct;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.actions.info.InfoAction;
import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class InstructAction extends InfoAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户是否刊物的编辑或者主编
		InfoFilter info = (InfoFilter)record;
		try {
			if(getRecordControlService().getAccessLevel(info.getMagazineDefineId(), InfoMagazineDefine.class.getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch (ServiceException e) {
		
		}
		return super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}
}