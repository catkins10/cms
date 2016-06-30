package com.yuanluesoft.cms.onlineservice.faq.actions.faq.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.faq.forms.admin.Faq;
import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Issue extends FaqAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, "发布成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		OnlineServiceFaq faq = (OnlineServiceFaq)record;
		onlineServiceFaqService.issue(faq, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#run(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void run(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		super.run(mapping, form, request, response, actionResult);
		Faq faqForm = (Faq)form;
		if(faqForm.getBatchIds()==null || faqForm.getBatchIds().isEmpty()) { //不是批量处理
			String js = "location=" +
						"'faq.shtml" +
						"?act=create" +
						"&directoryId=" + faqForm.getDirectoryId() +
						"&seq=" + UUIDLongGenerator.generateId() + "';";
			faqForm.getFormActions().addFormAction(0, "添加下一个", js, false);
		}
    }
}