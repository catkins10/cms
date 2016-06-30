package com.yuanluesoft.cms.capture.actions.startuptasks;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.capture.forms.StartupTasks;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class StartupTasksAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		String taskIds = (String)request.getSession().getAttribute(CaptureService.ATTRIBUTE_STARTUP_TASK_IDS);
		if(taskIds!=null && !taskIds.isEmpty()) {
			StartupTasks startupTasksForm = (StartupTasks)form;
			CaptureService captureService = (CaptureService)getService("captureService");
			startupTasksForm.setStartupTasks(captureService.listCaptureTasks(taskIds));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		String taskIds = (String)request.getSession().getAttribute(CaptureService.ATTRIBUTE_STARTUP_TASK_IDS);
		String selectedTaskIds = ListUtils.join(request.getParameterValues("taskId"), ",", false);
		if(taskIds==null || taskIds.isEmpty() || selectedTaskIds==null || selectedTaskIds.isEmpty()) {
			return;
		}
		CaptureService captureService = (CaptureService)getService("captureService");
		selectedTaskIds = "," + selectedTaskIds + ",";
		String[] ids = taskIds.split(",");
		for(int i=0; i<ids.length; i++) {
			if(selectedTaskIds.indexOf("," + ids[i] + ",")!=-1) {
				captureService.startupCapture((CmsCaptureTask)captureService.load(CmsCaptureTask.class, Long.parseLong(ids[i])));
			}
		}
	}
}