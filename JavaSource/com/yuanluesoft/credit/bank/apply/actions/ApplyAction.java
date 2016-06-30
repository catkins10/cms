package com.yuanluesoft.credit.bank.apply.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.bank.apply.forms.Apply;
import com.yuanluesoft.credit.enterprise.pojo.Enterprise;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseIn;
import com.yuanluesoft.credit.enterprise.pojo.EnterpriseOut;
import com.yuanluesoft.credit.regist.pojo.admin.CreditUser;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
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
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.initForm(form, acl, sessionInfo, request, response);
		Apply applyForm = (Apply)form;
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		Enterprise enterprise = (Enterprise)databaseService.findRecordById("com.yuanluesoft.credit.enterprise.pojo.Enterprise", sessionInfo.getUserId());
		if(enterprise != null){
			applyForm.setApplyPerson(enterprise.getName());//申请人
			applyForm.setNature("个体企业");//企业性质
			applyForm.setIndustry(enterprise.getIndustry());//行业类别
			applyForm.setCompanyAddr(enterprise.getAddr());//公司地址
			applyForm.setLegalPerson(enterprise.getPerson());//法定代表人
			applyForm.setTotalMoney(enterprise.getWorth());//总资产（万元）
			applyForm.setPhone(enterprise.getTel());//联系电话
			return;
		}
		EnterpriseIn enterpriseIn = (EnterpriseIn)databaseService.findRecordById("com.yuanluesoft.credit.enterprise.pojo.EnterpriseIn", sessionInfo.getUserId());
		if(enterprise != null){
			applyForm.setApplyPerson(enterpriseIn.getName());//申请人
			applyForm.setNature("内资企业");//企业性质
			applyForm.setIndustry(enterpriseIn.getIndustry());//行业类别
			applyForm.setCompanyAddr(enterpriseIn.getAddr());//公司地址
			applyForm.setLegalPerson(enterpriseIn.getPerson());//法定代表人
			applyForm.setTotalMoney(enterpriseIn.getWorth());//总资产（万元）
			applyForm.setPhone(enterpriseIn.getTel());//联系电话
			return;
		}
		
		EnterpriseOut enterpriseOut = (EnterpriseOut)databaseService.findRecordById("com.yuanluesoft.credit.enterprise.pojo.EnterpriseOut", sessionInfo.getUserId());
		if(enterprise != null){
			applyForm.setApplyPerson(enterpriseOut.getName());//申请人
			applyForm.setNature("外资企业");//企业性质
			applyForm.setIndustry(enterpriseOut.getIndustry());//行业类别
			applyForm.setCompanyAddr(enterpriseOut.getAddr());//公司地址
			applyForm.setLegalPerson(enterpriseOut.getPerson());//法定代表人
			applyForm.setTotalMoney(enterpriseOut.getWorth());//总资产（万元）
			applyForm.setPhone(enterpriseOut.getTel());//联系电话
			return;
		}
		CreditUser creditUser = (CreditUser)databaseService.findRecordById("com.yuanluesoft.credit.regist.pojo.admin.CreditUser", sessionInfo.getUserId());
		if(enterprise != null){
			applyForm.setApplyPerson(creditUser.getName());//申请人
			applyForm.setCompanyAddr(creditUser.getAddr());//公司地址
			applyForm.setLegalPerson(creditUser.getName());//法定代表人
			applyForm.setPhone(creditUser.getTel());//联系电话
		}
		
	}
	
	

	
}