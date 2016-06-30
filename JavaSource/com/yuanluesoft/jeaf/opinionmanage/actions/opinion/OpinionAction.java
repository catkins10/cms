package com.yuanluesoft.jeaf.opinionmanage.actions.opinion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.opinionmanage.forms.Opinion;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			Opinion opinionForm = (Opinion)form;
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = businessDefineService.getBusinessObject(opinionForm.getMainRecordClassName());
			acl = getAcl(businessObject.getApplicationName(), sessionInfo);
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch(Exception e) {
			
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Opinion opinionForm = (Opinion)form;
		OpinionService opinionService = (OpinionService)getService("opinionService");
		return opinionService.loadOpinion(opinionForm.getMainRecordClassName(), opinionForm.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		OpinionService opinionService = (OpinionService)getService("opinionService");
		Opinion opinionForm = (Opinion)form;
		com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion opinion = (com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion)record;
		return opinionService.saveOpinion(opinionForm.getMainRecordClassName(), opinion.getId(), opinion.getMainRecordId(), opinion.getOpinion(), opinion.getOpinionType(), opinion.getPersonId(), opinion.getPersonName(), opinion.getAgentId(), opinion.getAgentName(), opinion.getActivityId(), opinion.getActivityName(), opinion.getWorkItemId(), opinion.getCreated());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		OpinionService opinionService = (OpinionService)getService("opinionService");
		Opinion opinionForm = (Opinion)form;
		opinionService.deleteOpinion(opinionForm.getMainRecordClassName(), opinionForm.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateReloadURL(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String generateReloadURL(ActionForm form, HttpServletRequest request) {
		Opinion opinionForm = (Opinion)form;
		String url = super.generateReloadURL(form, request);
		return url==null ? null : url + "&mainRecordClassName=" + opinionForm.getMainRecordClassName();
	}
}