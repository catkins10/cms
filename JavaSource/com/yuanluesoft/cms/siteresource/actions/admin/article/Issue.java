package com.yuanluesoft.cms.siteresource.actions.admin.article;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.forms.Resource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Issue extends ArticleAction {

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
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		SiteResource resource = (SiteResource)record;
		siteResourceService.issue(resource, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#run(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void run(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		super.run(mapping, form, request, response, actionResult);
		Resource resourceForm = (Resource)form;
		if(resourceForm.getBatchIds()==null || resourceForm.getBatchIds().isEmpty()) { //不是批量处理
			String js = "window.top.location=" +
						"'article.shtml" +
						"?act=create" +
						"&columnId=" + resourceForm.getColumnId() +
						(resourceForm.getSource()==null || resourceForm.getSource().isEmpty() ? "" : "&source=" + URLEncoder.encode(resourceForm.getSource(), "utf-8")) +
						(resourceForm.getAuthor()==null || resourceForm.getAuthor().isEmpty() ? "" : "&author=" + URLEncoder.encode(resourceForm.getAuthor(), "utf-8")) +
						"&seq=" + UUIDLongGenerator.generateId() + "';";
			resourceForm.getFormActions().addFormAction(0, "添加下一篇", js, false);
		}
    }
}