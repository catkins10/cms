package com.yuanluesoft.appraise.actions.internetappraise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.appraise.forms.InternetAppraise;
import com.yuanluesoft.appraise.pojo.Appraise;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class InternetAppraiseAction extends FormAction {

	public InternetAppraiseAction() {
		super();
		anonymousAlways = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
		InternetAppraise internetAppraiseForm = (InternetAppraise)form;
		Appraise appraise = appraiseService.loadAppraiseByCode(internetAppraiseForm.getAppraiserNumber(), internetAppraiseForm.getAppraiseCode(), false);
		if(appraise!=null) {
			request.setAttribute("record", appraise);
		}
		if(internetAppraiseForm.getAppraiseCode()!=null && !internetAppraiseForm.getAppraiseCode().isEmpty()) {
			Logger.info("InternetAppraise: validate appraise code " + internetAppraiseForm.getAppraiseCode() + " " + (appraise!=null ? "pass" : "failed") + ", remote ip is " + request.getRemoteAddr());
		}
		return appraise;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.setSubForm(record!=null ? "internetAppraise" : "internetAppraiseCodeFailed");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
		InternetAppraise internetAppraiseForm = (InternetAppraise)form;
		Logger.info("InternetAppraise: submit appraise, code is " + internetAppraiseForm.getAppraiseCode() + ", remote ip is " + request.getRemoteAddr());
		appraiseService.submitInternetAppraise(internetAppraiseForm.getAppraiserNumber(), internetAppraiseForm.getAppraiseCode(), false, request);
		return null;
	}
}