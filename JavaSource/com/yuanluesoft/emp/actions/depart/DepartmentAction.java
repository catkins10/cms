package com.yuanluesoft.emp.actions.depart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

public class DepartmentAction extends FormAction {
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		// TODO 自动生成方法存根
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode,
				request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "部门信息", "SUBFORM", true);
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
			form.getTabs().addTab(-1, "emps", "下辖员工", "emps.jsp", false);
		}
	}
}
