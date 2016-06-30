package com.yuanluesoft.cms.siteresource.actions.admin.link;

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
public class Issue extends LinkAction {

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
		//增加根作为工作流实例的访问者
		//getWorkflowExploitService().addVisitor(resource.getWorkflowInstanceId(), workflowForm.getWorkItemId(), "0", getOrgService().getOrg(0).getDirectoryName(), RecordControlService.VISITOR_TYPE_DEPARTMENT, null, resource, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#run(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void run(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		super.run(mapping, form, request, response, actionResult);
		Resource resourceForm = (Resource)form;
		if(resourceForm.getBatchIds()==null || resourceForm.getBatchIds().isEmpty()) { //不是批量处理
			resourceForm.getFormActions().addFormAction(0, "添加下一个", "window.top.location='link.shtml?act=create&columnId=" + resourceForm.getColumnId() + "&seq=" + UUIDLongGenerator.generateId() + "';", false);
		}
    }
}