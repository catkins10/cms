package com.yuanluesoft.cms.templatemanage.actions.batchcopytemplate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.templatemanage.forms.BatchCopyTemplate;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class Copy extends DialogFormAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		BatchCopyTemplate batchCopyTemplate = (BatchCopyTemplate)form;
		TemplateService templateService = (TemplateService)getService("templateService");
    	templateService.batchCopyTemplate(batchCopyTemplate.getSourceTemplateId(), batchCopyTemplate.getTargetPageNames(), sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
    	BatchCopyTemplate batchCopyTemplate = (BatchCopyTemplate)form;
    	//获取模板
    	TemplateService templateService = (TemplateService)getService("templateService");
    	Template template;
		try {
			template = (Template)templateService.getTemplate(batchCopyTemplate.getSourceTemplateId());
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
    	//检查用户对站点的管理权限
    	SiteService siteService = (SiteService)getService("siteService");
    	if(!siteService.checkPopedom(template.getSiteId(), "manager", sessionInfo)) {
    		throw new PrivilegeException();
    	}
	}
}