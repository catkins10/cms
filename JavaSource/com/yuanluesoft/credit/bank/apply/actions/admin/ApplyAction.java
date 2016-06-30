package com.yuanluesoft.credit.bank.apply.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.bank.apply.pojo.Apply;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author zyh
 *
 */
public class ApplyAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//TODO 自动生成方法存根
		if(record!=null){
			Apply apply = (Apply)record;
			if(!acl.contains("application_manager")&&apply.getBankId()!=sessionInfo.getUserId()) { //管理员或创建者
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		Apply apply = (Apply)record;
		com.yuanluesoft.credit.bank.apply.forms.Apply applyForm = (com.yuanluesoft.credit.bank.apply.forms.Apply)form;
		if(apply.getStatus()>0&&(apply.getOpinions()==null||apply.getOpinions().equals(""))){
			applyForm.setError("请填写办理意见");
			throw new ValidateException();
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	

	
}