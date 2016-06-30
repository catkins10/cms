package com.yuanluesoft.jeaf.usermanage.actions.registteacher;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.actions.registperson.RegistPersonAction;
import com.yuanluesoft.jeaf.usermanage.forms.RegistTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;

/**
 * 
 * @author linchuan
 *
 */
public class RegistTeacherAction extends RegistPersonAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		RegistTeacher registTeacherForm = (RegistTeacher)form;
		RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
		//检查校验码
		SchoolRegistCode registCode = registPersonService.retireveSchoolRegistCode(getOrgService().getParentUnitOrSchool(registTeacherForm.getOrgId()).getId());
		if(registCode==null || !registTeacherForm.getValidateCode().equals(registCode.getCode())) {
			registTeacherForm.setValidateCode(null);
			registTeacherForm.setError("校验码输入错误");
			throw new ValidateException();
		}
	}

}