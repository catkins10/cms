/*
 * Created on 2006-6-4
 *
 */
package com.yuanluesoft.j2oa.calendar.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.calendar.forms.CalendarForm;
import com.yuanluesoft.j2oa.calendar.pojo.Calendar;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 *
 * @author linchuan
 *
 */
public class CalendarAction extends FormAction {
	
	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, boolean)
     */
    public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        CalendarForm calendarForm = (CalendarForm)form;
        Calendar calendar = (Calendar)record;
        //设置参加人
		RecordVisitorList visitors = getRecordControlService().getVisitors(calendar.getId(), Calendar.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
		    calendarForm.setLeaders(visitors);
		}
    }
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        CalendarForm calendarForm = (CalendarForm)form;
        //设置经办人姓名
        calendarForm.setCreatorName(sessionInfo.getUserName());
        //设置经办部门
        calendarForm.setDepartment(sessionInfo.getDepartmentName());
        //创建时间
        calendarForm.setCreated(DateTimeUtils.now());
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
	    CalendarForm calendarForm = (CalendarForm)form;
        Calendar calendar = (Calendar)record;
        if(calendar!=null && calendar.getPublish()=='1') {
            calendarForm.setSubForm(SUBFORM_READ);
		}
        super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
    }
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        Calendar calendar = (Calendar)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
		    calendar.setCreatorId(sessionInfo.getUserId());
		    calendar.setCreated(DateTimeUtils.now());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存参加人
		CalendarForm calendarForm = (CalendarForm)form;
		if(calendarForm.getLeaders().getVisitorIds()!=null) { //提交的内容中必须包含参加人
			getRecordControlService().updateVisitors(calendar.getId(), Calendar.class.getName(), calendarForm.getLeaders(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        if(OPEN_MODE_CREATE.equals(openMode)) {
			if(acl==null || !acl.contains("manageUnit_create")) {
				throw new PrivilegeException();
			}
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		Calendar calendar = (Calendar)record;
		if(calendar.getCreatorId()==sessionInfo.getUserId()) { //作者
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		try {
			char level = getRecordControlService().getAccessLevel(calendar.getId(), record.getClass().getName(), sessionInfo);
			if(level==RecordControlService.ACCESS_LEVEL_NONE) {
			    throw new PrivilegeException();
			}
			return level;
		} catch (ServiceException e) {
			throw new PrivilegeException();
		}
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Calendar calendar = (Calendar)record;
		if(calendar.getCreatorId()!=sessionInfo.getUserId()) { //作者
			throw new PrivilegeException();
		}
	}
}