package com.yuanluesoft.j2oa.info.actions.magazinedefine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.info.forms.MagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
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
public class MagazineDefineAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		MagazineDefine magazineDefineForm = (MagazineDefine)form;
		RecordControlService recordControlService = getRecordControlService();
		magazineDefineForm.setIssueRanges(recordControlService.getVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD));
		magazineDefineForm.setEditors(recordControlService.getVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		magazineDefineForm.setChiefEditors(recordControlService.getVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_EDITABLE));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		MagazineDefine magazineDefineForm = (MagazineDefine)form;
		RecordControlService recordControlService = getRecordControlService();
		recordControlService.updateVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), magazineDefineForm.getIssueRanges(), RecordControlService.ACCESS_LEVEL_PREREAD);
		recordControlService.updateVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), magazineDefineForm.getEditors(), RecordControlService.ACCESS_LEVEL_READONLY);
		recordControlService.updateVisitors(magazineDefineForm.getId(), InfoMagazineDefine.class.getName(), magazineDefineForm.getChiefEditors(), RecordControlService.ACCESS_LEVEL_EDITABLE);
		return record;
	}
}