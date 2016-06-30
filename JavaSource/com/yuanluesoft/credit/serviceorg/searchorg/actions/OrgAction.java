package com.yuanluesoft.credit.serviceorg.searchorg.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
public class OrgAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode,
				request, sessionInfo);
		if(form.getSubForm().contains("Read")){
			form.getTabs().addTab(-1, "basic", "基本信息", "serviceorgRead.jsp", false);
		}else{
			form.getTabs().addTab(-1, "basic", "基本信息", "serviceorgEdit.jsp", false);
		}
		form.getTabs().addTab(-1, "serviceitems", "办事事项", "serviceitems.jsp", false);
	}
	
}