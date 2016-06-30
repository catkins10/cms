package com.yuanluesoft.credit.bank.bank.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.bank.bank.forms.Bank;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 
 * @author zyh
 *
 */
public class BankAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) { //管理员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(openMode.equals("edit")){
			com.yuanluesoft.credit.bank.bank.pojo.Bank bank = (com.yuanluesoft.credit.bank.bank.pojo.Bank)record;
			if(bank.getId()==sessionInfo.getUserId()){
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Bank bankForm = (Bank)form;
		if(bankForm.getPassword()!=null &&
				   !"".equals(bankForm.getPassword()) &&
				   (!bankForm.getPassword().startsWith("{") ||
				    !bankForm.getPassword().endsWith("}"))) {
			bankForm.setPassword("{" + bankForm.getPassword() + "}");
				}
	}
	
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		com.yuanluesoft.credit.bank.bank.pojo.Bank bank = (com.yuanluesoft.credit.bank.bank.pojo.Bank)record;
		Bank bankForm = (Bank)form;
		if(bank.getLoginName()!=null && memberServiceList.isLoginNameInUse(bank.getLoginName(), bank.getId())) {
			bankForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
	}
	
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode,
				request, sessionInfo);
		if(form.getSubForm().contains("Read")){
			form.getTabs().addTab(-1, "basic", "基本信息", "bankRead.jsp", false);
		}else{
			form.getTabs().addTab(-1, "basic", "基本信息", "bankEdit.jsp", false);
		}
		form.getTabs().addTab(-1, "products", "金融产品", "products.jsp", false);
	}
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		AttachmentService attachmentService=(AttachmentService) getService("attachmentService");
		try{
			com.yuanluesoft.credit.bank.bank.pojo.Bank bank = (com.yuanluesoft.credit.bank.bank.pojo.Bank)record;
			List attachments=attachmentService.list("credit/bank", "logoImages", form.getId(),false, 1, request);
			if(attachments!=null&&!attachments.isEmpty()){
				Attachment attachment=(Attachment) attachments.get(0);
				bank.setLogoImage(attachment.getName());
			}
		}catch(Exception e){
			if(e instanceof ValidateException){
				throw e;
			}
			Logger.exception(e);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		// TODO 自动生成方法存根
		com.yuanluesoft.credit.bank.bank.pojo.Bank bank = (com.yuanluesoft.credit.bank.bank.pojo.Bank)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(bank!=null){
			DatabaseService databaseService = (DatabaseService)getService("databaseService");
			Person person = (Person)databaseService.findRecordById(Person.class.getName(), bank.getId());
			bank.setPassword(person.getPassword());
		}
		return bank;
	}

	
}