package com.yuanluesoft.cms.infopublic.actions.admin.infopublicstat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.forms.admin.InfoPublicStat;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.request.service.PublicRequestService;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Stat extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, true);
        }
    	InfoPublicStat statForm = (InfoPublicStat)form;
    	PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
    	//检查用户目录的权限
    	if(!publicDirectoryService.checkPopedom(statForm.getDirectoryId(), "manager", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	//统计信息数量
    	PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
    	statForm.setInfoStats(publicInfoService.stat(statForm.getBeginDate(), statForm.getEndDate(), statForm.getDirectoryId()));
    	statForm.setInfoCategoryStats(publicInfoService.statByCategory(statForm.getBeginDate(), statForm.getEndDate(), statForm.getDirectoryId()));
    	//统计信息公开申请
    	PublicMainDirectory publicMainDirectory = publicDirectoryService.getMainDirectory(statForm.getDirectoryId());
    	PublicRequestService publicRequestService = (PublicRequestService)getService("publicRequestService");
    	statForm.setRequestStats(publicRequestService.stat(statForm.getBeginDate(), statForm.getEndDate(), publicMainDirectory.getSiteId()));
    	statForm.setFormTitle("信息公开统计");
    	return mapping.getInputForward();
    }
}