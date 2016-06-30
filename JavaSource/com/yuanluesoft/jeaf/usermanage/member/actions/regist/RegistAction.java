package com.yuanluesoft.jeaf.usermanage.member.actions.regist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RegistAction extends FormAction {

	public RegistAction() {
		super();
		anonymousEnable = true;
		anonymousAlways = true;
		forceValidateCode = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.jeaf.usermanage.member.forms.Member memberForm = (com.yuanluesoft.jeaf.usermanage.member.forms.Member)form;
		if(memberForm.getSex()==0) {
			memberForm.setSex('M'); //默认“男”
		}
		if(memberForm.getHideDetail()==0) {
			memberForm.setHideDetail('1'); //默认隐藏个人资料
		}
		if(memberForm.getIdentityCardName()==null) {
			memberForm.setIdentityCardName("身份证"); //证件名称
		}
	}
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		Member member = (Member)record;
		member.setHalt('1');
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Member member = (Member)record;
		if(memberServiceList.isLoginNameInUse(member.getLoginName(), member.getId())) {
			com.yuanluesoft.jeaf.usermanage.member.forms.Member memberForm = (com.yuanluesoft.jeaf.usermanage.member.forms.Member)form;
			memberForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
	}
}