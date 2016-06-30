package com.yuanluesoft.bidding.project.signup.actions.admin.signup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.signup.forms.admin.SignUp;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SignUpAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains("manageUnit_signUpQuery") && !acl.contains("manageUnit_paymentRecord")) {
			throw new PrivilegeException();
		}
		SignUp signUpForm = (SignUp)form;
		if(signUpForm.getSignUpNo()==null && signUpForm.getId()==0) { //输入查询条件页面
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		BiddingSignUp signUp = (BiddingSignUp)record;
		BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
		try {
			if(signUp!=null && signUp.getPaymentTime()!=null && signUp.getDrawPaymentTime()!=null && (signUp.getPledgePaymentTime()!=null || !biddingProjectParameterService.isPledgeInternetPayment(signUp.getProjectId()))) { //已经完成支付
				return RecordControlService.ACCESS_LEVEL_READONLY; //只读
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
		//未支付,检查是否有支付登记权限
		return acl.contains("manageUnit_paymentRecord") ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode) || !acl.contains("manageUnit_paymentRecord")) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		SignUp signUpForm = (SignUp)form;
		if(signUpForm.getId()==0 && signUpForm.getSignUpNo()!=null) {
			BiddingService biddingService = (BiddingService)getService("biddingService");
			return biddingService.loadSignUp(signUpForm.getSignUpNo(), true);
		}
		if(signUpForm.getId()==0) {
			return null;
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BiddingSignUp signUp = (BiddingSignUp)record;
		if(request.getParameterValues("paymentTime")!=null) { //填写了支付时间
			signUp.setRecorder(sessionInfo.getUserName());
			signUp.setRecorderId(sessionInfo.getUserId());
			signUp.setRecordTime(DateTimeUtils.now());
		}
		if(request.getParameterValues("pledgePaymentTime")!=null) { //填写了保证金支付时间
			signUp.setPledgeConfirm('1'); //0/未确认, 1/已确认 2/本项目中排除 3/已被其他项目确认
			signUp.setPledgeRecorder(sessionInfo.getUserName());
			signUp.setPledgeRecorderId(sessionInfo.getUserId());
			signUp.setPledgeRecordTime(DateTimeUtils.now());
		}
		if(request.getParameterValues("drawPaymentTime")!=null) { //填写了图纸支付时间
			signUp.setDrawRecorder(sessionInfo.getUserName());
			signUp.setDrawRecorderId(sessionInfo.getUserId());
			signUp.setDrawRecordTime(DateTimeUtils.now());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		SignUp signUpForm = (SignUp)form;
		BiddingSignUp signUp = (BiddingSignUp)record;
		if(signUp==null || signUp.getId()==0) { //输入查询条件页面
			signUpForm.setSubForm("signUpQuery.jsp");
		}
		else {
			BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			signUpForm.setPaymentPledge(biddingProjectParameterService.isPledgeInternetPayment(signUp.getProjectId())); //是否从平台上支付保证金
		}
	}
}
