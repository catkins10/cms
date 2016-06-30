package com.yuanluesoft.jeaf.fingerprint.actions.enroll.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.fingerprint.forms.admin.Enroll;
import com.yuanluesoft.jeaf.fingerprint.service.FingerprintService;
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
public class EnrollAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Enroll enrollForm = (Enroll)form;
		if(enrollForm.isSelfEnroll()) { //采集自己的指纹
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains("application_manager") || acl.contains("manageUnit_enroll")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        Enroll enrollForm = (Enroll)form;
        enrollForm.setFinger(null);
        enrollForm.setPersonName(null);
        enrollForm.setPersonId(0);
        enrollForm.setTemplate(null);
        if(enrollForm.isSelfEnroll()) {
        	enrollForm.setPersonName(sessionInfo.getUserName());
        }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        //保存指纹模板
        Enroll enrollForm = (Enroll)form;
        FingerprintService fingerprintService = (FingerprintService)getService("fingerprintService");
        fingerprintService.saveFingerprintTemplate(enrollForm.isSelfEnroll() ? sessionInfo.getUserId() : enrollForm.getPersonId(), enrollForm.isSelfEnroll() ? sessionInfo.getUserName() : enrollForm.getPersonName(), enrollForm.getFinger(), enrollForm.getTemplate());
        return null;
	}
}