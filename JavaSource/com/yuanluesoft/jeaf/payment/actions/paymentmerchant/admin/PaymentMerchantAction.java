package com.yuanluesoft.jeaf.payment.actions.paymentmerchant.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.payment.service.PaymentService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PaymentMerchantAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PaymentService paymentService = (PaymentService)getService("paymentService");
		com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant paymentMerchantForm = (com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant)form;
		paymentMerchantForm.setParameters(paymentService.listPaymentMerchantParameters(paymentMerchantForm.getPaymentMethod(), null));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		PaymentService paymentService = (PaymentService)getService("paymentService");
		com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant paymentMerchantForm = (com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant)form;
		PaymentMerchant paymentMerchant = (PaymentMerchant)record;
		paymentMerchantForm.setParameters(paymentService.listPaymentMerchantParameters(paymentMerchantForm.getPaymentMethod(), paymentMerchant));
		if(paymentMerchantForm.getAccountTypes()!=null && !paymentMerchantForm.getAccountTypes().isEmpty()) {
			paymentMerchantForm.setAccountTypeArray(paymentMerchantForm.getAccountTypes().split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(record!=null) {
			request.setAttribute("paymentMethod", ((PaymentMerchant)record).getPaymentMethod());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant paymentMerchantForm = (com.yuanluesoft.jeaf.payment.forms.admin.PaymentMerchant)form;
		PaymentMerchant paymentMerchant = (PaymentMerchant)record;
		if(paymentMerchantForm.getAccountTypeArray().length>0 && !paymentMerchantForm.getAccountTypeArray()[0].isEmpty()) {
			paymentMerchant.setAccountTypes(ListUtils.join(paymentMerchantForm.getAccountTypeArray(), ",", false));
		}
		else {
			paymentMerchant.setAccountTypes(null);
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		PaymentService paymentService = (PaymentService)getService("paymentService");
		paymentService.savePaymentMerchantParameters(paymentMerchant, request.getParameterValues("parameterValue"));
		return paymentMerchant;
	}
}