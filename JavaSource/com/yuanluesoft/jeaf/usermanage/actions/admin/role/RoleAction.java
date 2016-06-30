package com.yuanluesoft.jeaf.usermanage.actions.admin.role;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.forms.admin.Role;
import com.yuanluesoft.jeaf.usermanage.pojo.RoleMember;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RoleAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户是否有组织机构的完全控制权限 
		OrgService orgService = (OrgService)getService("orgService");
		Role roleForm = (Role)form;
		if(orgService.checkPopedom(OPEN_MODE_CREATE.equals(openMode) ? roleForm.getOrgId() : ((com.yuanluesoft.jeaf.usermanage.pojo.Role)record).getOrgId(), "manager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		else if(!OPEN_MODE_CREATE.equals(openMode)) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Role roleForm = (Role)form;
		//设置成员列表
		if(roleForm.getMembers()!=null) {
			String ids = null;
			String names = null;
			for(Iterator iterator = roleForm.getMembers().iterator(); iterator.hasNext();) {
				RoleMember roleMember = (RoleMember)iterator.next();
				ids = (ids==null ? "" : ids + ",") + roleMember.getMemberId();
				names = (names==null ? "" : names + ",") + roleMember.getMemberName(); 
			}
			if(ids!=null) {
				roleForm.setMemberIds(ids);
				roleForm.setMemberNames(names);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Role roleForm = (Role)form;
		com.yuanluesoft.jeaf.usermanage.pojo.Role role = (com.yuanluesoft.jeaf.usermanage.pojo.Role)record;
		if(roleForm.getMemberIds()!=null) {
			Set members = new HashSet();
			String[] ids = roleForm.getMemberIds().split(",");
			String[] names = roleForm.getMemberNames().split(",");
			if(!"".equals(ids[0])) {
				for(int i=0; i<ids.length; i++) {
					RoleMember roleMember = new RoleMember();
					roleMember.setId(UUIDLongGenerator.generateId()); //ID
					roleMember.setMemberId(Long.parseLong(ids[i])); //成员ID
					roleMember.setMemberName(names[i]); //成员姓名
					roleMember.setRoleId(role.getId()); //角色ID
					members.add(roleMember);
				}
			}
			role.setMembers(members);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		((Role)newForm).setOrgId(((Role)currentForm).getOrgId());
	}	
}