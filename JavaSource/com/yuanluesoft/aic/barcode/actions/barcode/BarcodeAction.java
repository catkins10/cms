package com.yuanluesoft.aic.barcode.actions.barcode;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.aic.barcode.model.BarcodeCompanySessionInfo;
import com.yuanluesoft.aic.barcode.pojo.Barcode;
import com.yuanluesoft.aic.barcode.service.BarcodeService;
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
 * @author linchuan
 *
 */
public class BarcodeAction extends FormAction {

	public BarcodeAction() {
		super();
		externalAction = true;
		sessionInfoClass = BarcodeCompanySessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode) || (record!=null && ((Barcode)record).getCompanyId()==sessionInfo.getUserId())) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.aic.barcode.forms.Barcode barcodeForm = (com.yuanluesoft.aic.barcode.forms.Barcode)form;
		if(barcodeForm.getBarcode()==null) {
			barcodeForm.setBarcode(((BarcodeCompanySessionInfo)sessionInfo).getCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		com.yuanluesoft.aic.barcode.forms.Barcode barcodeForm = (com.yuanluesoft.aic.barcode.forms.Barcode)formToValidate;
		if(!OPEN_MODE_CREATE.equals(openMode)) { //旧记录
			if(barcodeForm.getBarcode().length()!=13) {
				barcodeForm.setError("条码长度必须是13位");
				throw new ValidateException();
			}
		}
		else { //新记录
			String[] values = barcodeForm.getBarcode().replace('，', ',').split(",");
			for(int i=0; i<values.length; i++) {
				values[i] = values[i].trim();
				if(values[i].length()!=13) {
					barcodeForm.setError("条码“" + values[i] + "”长度不正确");
					throw new ValidateException();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			BarcodeService barcodeService = (BarcodeService)getService("barcodeService");
			com.yuanluesoft.aic.barcode.forms.Barcode barcodeForm = (com.yuanluesoft.aic.barcode.forms.Barcode)form;
			barcodeService.createBarcodes(barcodeForm.getBarcode(), sessionInfo.getUserId());
			return null;
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}