package com.yuanluesoft.cms.siteresource.actions.admin.link;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction;
import com.yuanluesoft.cms.siteresource.forms.Resource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class LinkAction extends ResourceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runLinkApproval";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SiteResource siteResource = (SiteResource)record;
		siteResource.setType(SiteResourceService.RESOURCE_TYPE_LINK);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Resource resourceForm = (Resource)form;
		resourceForm.getOpinionPackage().setWriteOpinionActionName("writeLinkOpinion"); //设置写意见操作名称
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction#getWaterMark(com.yuanluesoft.jeaf.attachmentmanage.model.AttachmentConfig, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		return null; //禁止给链接图片增加水印
	}
}