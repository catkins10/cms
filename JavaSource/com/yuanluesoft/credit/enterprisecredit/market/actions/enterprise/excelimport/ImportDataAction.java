package com.yuanluesoft.credit.enterprisecredit.market.actions.enterprise.excelimport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.enterprisecredit.market.forms.ImportEnterpriseDateExcel;
import com.yuanluesoft.credit.enterprisecredit.market.service.spring.MarketEnterpriseImportServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

public class ImportDataAction extends FormAction {

	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
public static void main(String[] args) {
	
}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode,HttpServletRequest request, HttpServletResponse response,SessionInfo sessionInfo) throws Exception {


		MarketEnterpriseImportServiceImpl dataImportService = (MarketEnterpriseImportServiceImpl) getService("marketEnterpriseImportServiceImpl");
		ImportEnterpriseDateExcel importDataForm = (ImportEnterpriseDateExcel) form;
		try {
			dataImportService.importData(importDataForm,sessionInfo);
		} catch (Exception e) {
			if (e.getMessage() != null) {
				importDataForm.setError(e.getMessage());
				throw new ValidateException();
			}
		}
		
		
		return null;
	}
}
