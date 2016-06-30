/*
 * Created on 2006-7-3
 *
 */
package com.yuanluesoft.onlinesignup.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.onlinesignup.forms.SignUpForm;
import com.yuanluesoft.onlinesignup.pojo.admin.SignUp;

/**
 *
 * @author zyh
 *
 */
public class SignUpAction extends PublicServiceAction {
	
	public SignUpAction() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		}
		catch(Exception e) {
			
		}
	}
    
	
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		SignUpForm signUpForm = (SignUpForm)form;
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		SignUp signUp= (SignUp)databaseService.findRecordByHql("from com.yuanluesoft.onlinesignup.pojo.admin.SignUp SignUp where (SignUp.status=1 or SignUp.status=4) and SignUp.idCard = '"+signUpForm.getIdCard()+"'");
		if(signUp!=null){
			signUpForm.setError("您已提交过了！");
			throw new ValidateException();
		}
		signUp= (SignUp)record;
		signUp.setStatus(4);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode,
				request, sessionInfo);
		SignUpForm signUpForm = (SignUpForm)form;
		signUpForm.setSubForm("signUp"); //提交页面
	}
	
	
	
    
}