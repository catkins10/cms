package com.yuanluesoft.cms.capture.actions.task.copytask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.capture.actions.task.TaskAction;
import com.yuanluesoft.cms.capture.forms.Task;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Copy extends TaskAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	loadPage = false;
    	Task taskForm = (Task)form;
    	taskForm.setAct(OPEN_MODE_OPEN);
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.actions.task.TaskAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(com.yuanluesoft.jeaf.form.ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CmsCaptureTask task = (CmsCaptureTask)record;
		CaptureService captureService = (CaptureService)getService("captureService");
		Task taskForm = (Task)form;
		record = captureService.copyTask(task, taskForm.getDescription(), taskForm.getCaptureURL());
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.actions.task.TaskAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Task taskForm = (Task)form;
		taskForm.getFormActions().addFormAction(-1, "打开新的抓取任务", "loadNewTask()", true);
		taskForm.getFormActions().addFormAction(-1, "取消", "DialogUtils.closeDialog()", false);
	}
}