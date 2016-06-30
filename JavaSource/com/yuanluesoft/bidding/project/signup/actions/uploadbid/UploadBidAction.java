package com.yuanluesoft.bidding.project.signup.actions.uploadbid;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.biddingroom.pojo.BiddingRoomSchedule;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ChargeException;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
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
public class UploadBidAction extends FormAction {

	public UploadBidAction() {
		super();
		anonymousAlways = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		com.yuanluesoft.bidding.project.signup.forms.BiddingSignUp uploadForm = (com.yuanluesoft.bidding.project.signup.forms.BiddingSignUp)form;
		BiddingService biddingService = (BiddingService)getService("biddingService");
		//按报名号获取报名记录
		BiddingSignUp signUp = biddingService.loadSignUp(uploadForm.getSignUpNo(), false);
		//获取项目
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
    	signUp.setProject(biddingProjectService.getProject(signUp.getProjectId()));
    	return signUp;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkCharge(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkCharge(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws ChargeException, SystemUnregistException {
		BiddingSignUp signUp = (BiddingSignUp)record;
		BiddingService biddingService = (BiddingService)getService("biddingService");
		//检查是否缴费
    	if(signUp.getPaymentTime()==null) { // || (signUp.getPledgePaymentTime()==null && biddingService.isPaymentPledge())) { //未缴费
    		throw new ChargeException();
    	}
    	BiddingRoomSchedule schedule = (BiddingRoomSchedule)signUp.getProject().getBidopeningRoomSchedules().iterator().next();
    	if(schedule==null || schedule.getBeginTime()==null || DateTimeUtils.now().after(DateTimeUtils.add(schedule.getBeginTime(), Calendar.MINUTE, -biddingService.getBidUploadPaddingMinutes()))) {
    		throw new ChargeException();
    	}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#transactException(java.lang.Exception, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public ActionForward transactException(Exception e, ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean resumeAction) throws Exception {
		if(e instanceof ChargeException) { //计费异常
			com.yuanluesoft.bidding.project.signup.forms.BiddingSignUp uploadForm = (com.yuanluesoft.bidding.project.signup.forms.BiddingSignUp)form;
    		//重定向到缴费页面
			response.sendRedirect("signUpPayment.shtml?signUpNo=" + uploadForm.getSignUpNo());
			return null;
		}
		return super.transactException(e, mapping, form, request, response,	resumeAction);
	}
}