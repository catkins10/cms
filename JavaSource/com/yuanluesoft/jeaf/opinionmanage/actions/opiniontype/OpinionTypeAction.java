package com.yuanluesoft.jeaf.opinionmanage.actions.opiniontype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.opinionmanage.forms.OpinionType;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionTypeAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			OpinionType opinionTypeForm = (OpinionType)form;
			com.yuanluesoft.jeaf.opinionmanage.pojo.OpinionType opinionType = (com.yuanluesoft.jeaf.opinionmanage.pojo.OpinionType)record;
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = businessDefineService.getBusinessObject(opinionType==null ? opinionTypeForm.getBusinessClassName() : opinionType.getBusinessClassName());
			acl = getAcl(businessObject.getApplicationName(), sessionInfo);
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch(Exception e) {
			
		}
		throw new PrivilegeException();
	}
}