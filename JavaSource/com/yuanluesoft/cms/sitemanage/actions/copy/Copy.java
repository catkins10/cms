package com.yuanluesoft.cms.sitemanage.actions.copy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.forms.CopyWebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateService;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Copy extends CopyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSaveAction(mapping, form, request, response, false, null, "完成拷贝", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.CopyDirectoryAction#copyDirectory(long, long, java.lang.String, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected Directory copyDirectory(long fromDirectoryId, long toDirectoryId, String newDirectoryName, ActionForm form, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Directory directory = super.copyDirectory(fromDirectoryId, toDirectoryId, newDirectoryName, form, response, sessionInfo);
		CopyWebDirectory copyWebDirectory = (CopyWebDirectory)form;
		if(copyWebDirectory.getCopyTemplate()=='1') {
			//复制模板
			SiteTemplateService siteTemplateService = (SiteTemplateService)getService("siteTemplateService");
			siteTemplateService.copySiteTemplate(directory.getId(), copyWebDirectory.getFromDirectoryId(), sessionInfo);
		}
		return directory;
	}
}