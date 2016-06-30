package com.yuanluesoft.enterprise.project.actions.project;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.Project;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Schedule extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Project projectForm = (Project)form;
		if(projectForm.getTeams()!=null) {
			projectForm.setProjectTeam(new EnterpriseProjectTeam());
			for(Iterator iterator=projectForm.getTeams().iterator(); iterator.hasNext();) {
				EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
				if(team.getCompletionDate()==null) {
					projectForm.setProjectTeam(team);
					break;
				}
			}
		}
		if(projectForm.getProjectTeam().getId()==0) { //新建项目组
			projectForm.getProjectTeam().setId(UUIDLongGenerator.generateId());
		}
		projectForm.setInnerDialog("transact/schedule.jsp");
		projectForm.setFormTitle("项目安排");
		projectForm.getFormActions().addFormAction(-1, "完成安排", "completeSchedule(false)", true);
		projectForm.getFormActions().addFormAction(-1, "项目已完成", "completeSchedule(true)", false);
		addReloadAction(form, "取消", request, -1, false);
	}
}