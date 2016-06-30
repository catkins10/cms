package com.yuanluesoft.j2oa.bulletin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.bulletin.pojo.Bulletin;
import com.yuanluesoft.j2oa.bulletin.service.BulletinService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author LinChuan
*
 */
public class Issue extends BulletinAction {
	   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, "发布成功！", null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
		Bulletin bulletin = (Bulletin)record;
		BulletinService bulletinService = (BulletinService)getService("bulletinService");
		bulletinService.issueBulletin(bulletin, sessionInfo);
	}
}