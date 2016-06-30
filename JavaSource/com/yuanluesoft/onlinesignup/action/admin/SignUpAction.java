/*
 * Created on 2006-7-3
 *
 */
package com.yuanluesoft.onlinesignup.action.admin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.onlinesignup.forms.admin.SignUpForm;
import com.yuanluesoft.onlinesignup.pojo.admin.SignUp;

/**
 *
 * @author zyh
 *
 */
public class SignUpAction extends PublicServiceAdminAction {
	

		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
		 */
		public String getWorkflowActionName(WorkflowForm workflowForm) {
			return "runSignUp";
		}
		
		 /* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterApproval(java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
		 */
		protected void afterApproval(String approvalResult, WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
			super.afterApproval(approvalResult, workflowForm, request, record, openMode, sessionInfo);
			SignUp signUp = (SignUp)record;
			boolean pass= "同意".equals(approvalResult);
			signUp.setStatus(pass ? 1 : 2);
		}
		
		public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
			// TODO 自动生成方法存根
			super.validateBusiness(validateService, form, openMode, record, sessionInfo,
					request);
			SignUpForm signUpForm = (SignUpForm)form;
			SignUp signUp = (SignUp)record;
			Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
			//通过Pattern获得Matcher
			Matcher idNumMatcher = idNumPattern.matcher(signUp.getIdCard());
			//判断用户输入是否为身份证号
			if(!idNumMatcher.matches()){
				signUpForm.setError("请输入正确的身份证号！");
				throw new ValidateException();
			}
			
		}
	
    
}