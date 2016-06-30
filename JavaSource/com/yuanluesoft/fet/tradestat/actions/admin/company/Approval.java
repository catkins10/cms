package com.yuanluesoft.fet.tradestat.actions.admin.company;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.tradestat.forms.admin.Company;
import com.yuanluesoft.fet.tradestat.pojo.FetCompany;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;


/**
 * 
 * @author linchuan
 *
 */
public class Approval extends CompanyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "完成审核", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fet.tradestat.actions.admin.company.CompanyAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Company companyForm = (Company)form;
		FetCompany company = (FetCompany)record;
		company.setApprovalPass(companyForm.getApprovalPass());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}