package com.yuanluesoft.emp.actions.employee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.emp.actions.depart.DepartmentAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

public class EmployeeaAction extends DepartmentAction {
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadComponentResource(form, mapping, record, component, componentName,
				acl, accessLevel, deleteEnable, request, sessionInfo);
	}
}
