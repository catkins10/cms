package com.yuanluesoft.jeaf.usermanage.actions.admin.userpagetemplate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.forms.admin.UserPageTemplate;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateAction extends com.yuanluesoft.cms.templatemanage.actions.template.TemplateAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.actions.template.TemplateAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		UserPageTemplate templateForm = (UserPageTemplate)form;
		com.yuanluesoft.jeaf.usermanage.pojo.UserPageTemplate template = (com.yuanluesoft.jeaf.usermanage.pojo.UserPageTemplate)record;
		long userId = (template==null ? templateForm.getUserId() : template.getUserId());
		if(userId==sessionInfo.getUserId()) { //用户配置自己的主页模板
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//检查是否组织机构
		OrgService orgService = (OrgService)getService("orgService");
		Org org = null;
		try {
			org = orgService.getOrg(userId);
		}
		catch (ServiceException e) {
			
		}
		long orgId = org==null ? 0 : org.getId();
		if(org==null) { //不是组织机构
			//获取用户
			PersonService personService = (PersonService)getService("personService");
			Person person = null;
			try {
				person = personService.getPerson(userId);
			}
			catch (ServiceException e) {
				
			}
			if(person==null) {
				throw new PrivilegeException();
			}
			orgId = ((PersonSubjection)person.getSubjections().iterator().next()).getOrgId();
		}
		//检查用户对组织机构的权限
		if(orgService.checkPopedom(orgId, "manager", sessionInfo)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();	
	}
}