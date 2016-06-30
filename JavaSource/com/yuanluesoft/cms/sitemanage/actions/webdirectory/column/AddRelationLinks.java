package com.yuanluesoft.cms.sitemanage.actions.webdirectory.column;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.forms.Column;
import com.yuanluesoft.cms.sitemanage.pojo.WebColumn;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class AddRelationLinks extends ColumnAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "relationLinks", null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.siteresource.actions.admin.article.ArticleAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		WebColumn webColumn = (WebColumn)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Column columnForm = (Column)form;
		SiteService siteService = (SiteService)getService("siteService");
		siteService.addRelationColumns(webColumn, columnForm.getRelationColumnIds());
		columnForm.setRelationColumnIds(null);
		columnForm.setRelationColumnNames(null);
		return webColumn;
	}
}