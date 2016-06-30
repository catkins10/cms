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

/**
 * 
 * @author linchuan
 *
 */
public class RedoIssue extends MeetingAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    	  	return executeSaveAction(mapping, form, request, response, false, null, "发布成功！", null);
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
            formMeeting.getFormActions().addFormAction(-1, "继续发布", "FormUtils.doAction('redoIssueMeeting', 'forceIssue=true')", true);
            addReloadAction(formMeeting, "取消", request, -1, false);
            return mapping.getInputForward();
        }
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Meeting meeting = (Meeting)record;
		com.yuanluesoft.j2oa.meeting.forms.Meeting formMeeting = (com.yuanluesoft.j2oa.meeting.forms.Meeting)form;
		MeetingService meetingService = (MeetingService)getService("meetingService"); //会议服务
		meetingService.issueMeeting(meeting, formMeeting.isForceIssue(), sessionInfo);
		return record;
	}
}