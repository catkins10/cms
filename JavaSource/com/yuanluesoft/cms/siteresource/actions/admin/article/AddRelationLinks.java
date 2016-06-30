package com.yuanluesoft.cms.siteresource.actions.admin.article;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.forms.Article;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AddRelationLinks extends ArticleAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "relationLinks", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.article.ArticleAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SiteResource siteResource = (SiteResource)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Article articleForm = (Article)form;
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		siteResourceService.addRelationLinks(siteResource, articleForm.getRelationResourceIds());
		articleForm.setRelationResourceIds(null);
		articleForm.setRelationResourceSubjects(null);
		return siteResource;
	}
}