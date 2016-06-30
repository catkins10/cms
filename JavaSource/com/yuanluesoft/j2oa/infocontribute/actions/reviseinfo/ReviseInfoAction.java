package com.yuanluesoft.j2oa.infocontribute.actions.reviseinfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.infocontribute.forms.ReviseInfo;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeRevise;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ReviseInfoAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(record==null) {
			throw new PrivilegeException();
		}
		InfoContributeRevise revise = (InfoContributeRevise)record;
		//检查用户的投稿权限
		if(!getOrgService().checkPopedom(revise.getInfo().getFromUnitId(), "infoContribute", sessionInfo)) {
			throw new PrivilegeException();
		}
		return revise.getEditTime()==null ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ReviseInfo reviseForm = (ReviseInfo)form;
		if(reviseForm.getNewBody()==null || reviseForm.getNewBody().isEmpty()) {
			reviseForm.setNewBody(reviseForm.getInfo().getBody());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		InfoContributeRevise revise = (InfoContributeRevise)record;
		revise.setEditor(sessionInfo.getUserName());
		revise.setEditorId(sessionInfo.getUserId());
		revise.setEditTime(DateTimeUtils.now());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}