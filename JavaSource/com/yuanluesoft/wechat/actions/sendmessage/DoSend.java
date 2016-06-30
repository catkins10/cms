package com.yuanluesoft.wechat.actions.sendmessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.wechat.pojo.WechatMessageSend;
import com.yuanluesoft.wechat.service.WechatService;

/**
 * 
 * @author linchuan
 *
 */
public class DoSend extends SendMessageAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, "发布完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		WechatService wechatService = (WechatService)getService("wechatService");
		WechatMessageSend messageSend = (WechatMessageSend)record;
		wechatService.issue(messageSend, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}