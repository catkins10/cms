package com.yuanluesoft.cms.siteresource.actions.admin.article;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction;
import com.yuanluesoft.cms.siteresource.forms.Article;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ArticleAction extends ResourceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runArticleApproval";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SiteResource siteResource = (SiteResource)record;
		siteResource.setType(SiteResourceService.RESOURCE_TYPE_ARTICLE);
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.ResourceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Article articleForm = (Article)form;
		SiteResource siteResource = (SiteResource)record;
		boolean editabled = articleForm.getSubForm().indexOf("Edit")!=-1;
		int tabIndex = 1;
		if(editabled || (siteResource!=null && siteResource.getResourcePhotos()!=null && !siteResource.getResourcePhotos().isEmpty())) {
			articleForm.getTabs().addTab(tabIndex++, "photos", "图集", "photos.jsp", false);
		}
		if(editabled || (siteResource!=null && siteResource.getResourceVideos()!=null && !siteResource.getResourceVideos().isEmpty())) {
			articleForm.getTabs().addTab(tabIndex++, "videos", "视频集", "videos.jsp", false);
		}
		if(editabled || (siteResource!=null && siteResource.getRelationLinks()!=null && !siteResource.getRelationLinks().isEmpty())) {
			articleForm.getTabs().addTab(tabIndex++, "relationLinks", "相关链接", "relationLinks.jsp", false);
		}
		//设置所在站点的子栏目ID列表,用来配置相关链接
		SiteService siteService = (SiteService)getService("siteService");
		WebSite site = siteService.getParentSite(articleForm.getColumnId()>0 ? articleForm.getColumnId() : ((SiteResourceSubjection)siteResource.getSubjections().iterator().next()).getSiteId());
		articleForm.setRelationResourceColumnIds(siteService.getChildDirectoryIds("" + site.getId(), "column"));
	}
}