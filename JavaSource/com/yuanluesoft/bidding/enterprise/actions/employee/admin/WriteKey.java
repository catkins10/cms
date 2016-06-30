package com.yuanluesoft.bidding.enterprise.actions.employee.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.enterprise.forms.admin.Employee;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanlue
 *
 */
public class WriteKey extends EmployeeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveComponentAction(mapping, form, "employee", "employees", "enterpriseId", "refreshEnterprise", true, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.actions.employee.admin.EmployeeAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.saveComponentRecord(form, mainRecord, component, componentName,	foreignKeyProperty, sessionInfo, request);
		Employee employeeForm = (Employee)form;
		employeeForm.setPrompt("KEY写入完成。");
	}
}