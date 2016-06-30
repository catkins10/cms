package com.yuanluesoft.j2oa.meeting.actions.meeting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.meeting.pojo.Meeting;
import com.yuanluesoft.j2oa.meeting.service.MeetingService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.UserBusyException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
*
 */
public class Issue extends MeetingAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    		return executeRunAction(mapping, form, request, response, true, "发布成功！", null);
        }
        catch(UserBusyException e) {
        	//与会人员有其他安排
        	com.yuanluesoft.j2oa.meeting.forms.Meeting formMeeting = (com.yuanluesoft.j2oa.meeting.forms.Meeting)form;
        	formMeeting.setAct(OPEN_MODE_EDIT);
        	formMeeting.setConflict(e.getMessage());
            load(form, request, response);
            //设置对话框
            formMeeting.setInnerDialog("confirmIssue.jsp");
            formMeeting.setFormTitle("会议安排冲突");
            formMeeting.getFormActions().addFormAction(-1, "继续发布", "FormUtils.doAction('issueMeeting', 'forceIssue=true')", true);
            addReloadAction(formMeeting, "取消", request, -1, false);
            return mapping.getInputForward();
        }
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
		Meeting pojoMeeting = (Meeting)record;
		com.yuanluesoft.j2oa.meeting.forms.Meeting formMeeting = (com.yuanluesoft.j2oa.meeting.forms.Meeting)workflowForm;
		MeetingService meetingService = (MeetingService)getService("meetingService");
		meetingService.issueMeeting(pojoMeeting, formMeeting.isForceIssue(), sessionInfo);
	}
}