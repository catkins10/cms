package com.yuanluesoft.sportsevents.contestschedule.actions.score.admin;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
/**
 * 体育赛程：分数
 * @author kangshiwei
 *
 */
public class ScoreAction extends FormAction {

	/* （非 Javadoc）
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl!=null&&acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)){
        	return RecordControlService.ACCESS_LEVEL_EDITABLE;      	
        }else{
        	return RecordControlService.ACCESS_LEVEL_READONLY;      	
        }
	}

	
}
