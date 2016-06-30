/*
 * Created on 2007-1-4
 *
 */
package com.yuanluesoft.j2oa.receival.actions.filingconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.databank.service.DatabankDirectoryService;
import com.yuanluesoft.j2oa.receival.forms.FilingConfig;
import com.yuanluesoft.j2oa.receival.pojo.ReceivalFilingConfig;
import com.yuanluesoft.j2oa.receival.service.ReceivalService;
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
public class FilingConfigAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		FilingConfig filingConfigForm = (FilingConfig)form;
		ReceivalService receivalService = (ReceivalService)getService("receivalService");
		ReceivalFilingConfig filingConfig = receivalService.getFilingConfig();
		if(filingConfig!=null) {
			filingConfigForm.setAct("edit");
			filingConfigForm.setId(filingConfig.getId());
		}
		return super.load(form, request, response);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		FilingConfig filingConfigForm = (FilingConfig)form;
		filingConfigForm.setDirectoryName(((DatabankDirectoryService)getService("databankDirectoryService")).getDirectoryFullName(filingConfigForm.getDirectoryId(), "/", null));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		FilingConfig filingConfigForm = (FilingConfig)form;
		//filingConfigForm.setDirectoryName(getDirectoryService().getDirectoryFullName(filingConfigForm.getDirectoryId()));
		if(filingConfigForm.getToArchives()==0) {
			filingConfigForm.setToArchives('1');
		}
		if(filingConfigForm.getToDatabank()==0) {
			filingConfigForm.setToDatabank('0');
		}
		filingConfigForm.setDirectoryId(0);
		if(filingConfigForm.getCreateDirectoryByYear()==0) {
			filingConfigForm.setCreateDirectoryByYear('0');
		}
	}
}