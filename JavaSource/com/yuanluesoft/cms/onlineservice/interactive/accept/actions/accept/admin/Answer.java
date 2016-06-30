package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Answer extends AcceptAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Accept acceptForm = (Accept)form;
    	String back = RequestUtils.getParameterStringValue(request, "back");//是否回退
    	if(back!=null && back.equals("1")){//不同意执行回退
    		acceptForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE);
    		executeSaveAction(mapping, form, request, response, false, null, null, null);
    	}
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		OnlineServiceAccept accept = (OnlineServiceAccept)record;
		if(accept.getCaseAccept()=='1') { //同意受理
			//删除缺件通知
			OnlineServiceAcceptService onlineServiceAcceptService = (OnlineServiceAcceptService)getService("onlineServiceAcceptService");
			onlineServiceAcceptService.clearMissingNotifies(accept.getId());
			//设置受理时间
			accept.setCaseAcceptTime(DateTimeUtils.now());
			accept.setCaseDeclinedReason("");
		}
	}
}