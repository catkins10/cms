package com.yuanluesoft.enterprise.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.Project;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class SwitchPage extends ProjectAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public Record save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult) throws Exception {
		Project projectForm = (Project)form;
		Record record = super.save(mapping, form, request, response, reload, tabSelected, actionResult);
		setTransactDialog(projectForm, request);
		return record;
	}
	
	/**
	 * 输出办理对话框
	 * @param projectForm
	 */
	private void setTransactDialog(Project projectForm, HttpServletRequest request) {
		projectForm.setInnerDialog("transact/" + projectForm.getPageName() + ".jsp");
		if("approvalDesign".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("完成审核");
			projectForm.getFormActions().addFormAction(-1, "完成审核", "completeDesign()", true);
		}
		else if("completeArchive".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("合同归档");
			projectForm.getFormActions().addFormAction(-1, "完成归档", "completeArchive()", true);
		}
		else if("completeSign".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("合同签订");
			projectForm.getFormActions().addFormAction(-1, "完成签订", "completeSign()", true);
		}
		else if("completeSeal".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("合同盖章");
			projectForm.getFormActions().addFormAction(-1, "完成盖章", "completeSeal()", true);
		}
		else if("completeDesign".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("完成设计");
			projectForm.getFormActions().addFormAction(-1, "完成设计", "completeDesign()", true);
		}
		else if("schedule".equals(projectForm.getPageName())) {
			projectForm.setFormTitle("项目安排");
			projectForm.getFormActions().addFormAction(-1, "完成安排", "completeSchedule(false)", true);
			projectForm.getFormActions().addFormAction(-1, "项目已完成", "completeSchedule(true)", false);
		}
		addReloadAction(projectForm, "取消", request, -1, false);
	}
}