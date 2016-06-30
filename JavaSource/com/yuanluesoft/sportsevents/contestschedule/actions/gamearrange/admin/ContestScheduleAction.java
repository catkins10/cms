package com.yuanluesoft.sportsevents.contestschedule.actions.gamearrange.admin;


import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.sportsevents.contestschedule.forms.ContestSchedule;
/**
 * 体育赛程安排
 * @author kangshiwei
 *
 */
public class ContestScheduleAction extends FormAction {

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

	/* （非 Javadoc）
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.String, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
       if(OPEN_MODE_CREATE.equals(openMode)){
    	   ContestSchedule contestSchedule=(ContestSchedule) form;
    	   Timestamp now = DateTimeUtils.now();
    	   if(now.compareTo(contestSchedule.getBeginTime())>=0){
    		   contestSchedule.setError("比赛开始时间应晚于当前时间");
    		   throw new ValidateException();
    	   }
    	   if(contestSchedule.getEndTime().compareTo(contestSchedule.getBeginTime())<=0){
    		   contestSchedule.setError("比赛开始时间应早于结束时间");
    		   throw new ValidateException();
    	   }
       }
	}
}