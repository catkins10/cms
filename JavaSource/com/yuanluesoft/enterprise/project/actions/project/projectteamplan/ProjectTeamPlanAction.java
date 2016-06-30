package com.yuanluesoft.enterprise.project.actions.project.projectteamplan;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.actions.project.ProjectAction;
import com.yuanluesoft.enterprise.project.forms.ProjectTeamPlan;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeamMember;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectTeamPlanAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		//获取项目组
		EnterpriseProject project = (EnterpriseProject)record;
		ProjectTeamPlan planForm = (ProjectTeamPlan)form;
		EnterpriseProjectTeam team = (EnterpriseProjectTeam)ListUtils.findObjectByProperty(project.getTeams(), "id", new Long(planForm.getPlan().getTeamId()));
		//检查用户是否项目组负责人
		return isTeamManager(team, sessionInfo) ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, java.lang.Object, java.lang.Object, java.util.List, char, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		//设置项目组成员列表,供选择
		EnterpriseProject project = (EnterpriseProject)record;
		ProjectTeamPlan planForm = (ProjectTeamPlan)form;
		EnterpriseProjectTeam team = (EnterpriseProjectTeam)ListUtils.findObjectByProperty(project.getTeams(), "id", new Long(planForm.getPlan().getTeamId()));
		String members = null;
		for(Iterator iterator = team.getMembers().iterator(); iterator.hasNext();) {
			EnterpriseProjectTeamMember member = (EnterpriseProjectTeamMember)iterator.next();
			members = (members==null ? "" : members + ",") + member.getMemberName() + "|" +  member.getMemberId();
		}
		planForm.setTeamMembers(members);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		//获取项目组
		EnterpriseProject project = (EnterpriseProject)mainRecord;
		ProjectTeamPlan planForm = (ProjectTeamPlan)form;
		EnterpriseProjectTeam team = (EnterpriseProjectTeam)ListUtils.findObjectByProperty(project.getTeams(), "id", new Long(planForm.getPlan().getTeamId()));
		//获取项目安排
		return (Record)ListUtils.findObjectByProperty(team.getPlans(), "id", PropertyUtils.getProperty(form, "plan.id"));
	}
}