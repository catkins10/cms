package com.yuanluesoft.chd.evaluation.actions.plantindex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlant;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	externalAction = true;
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	long plantId = RequestUtils.getParameterLongValue(request, "plantId");
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		ChdEvaluationDirectory plant = null;
    	if(plantId==0) {
    		plant = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectoryByName(0, sessionInfo.getUnitName(), false);
    		if(plant==null || !(plant instanceof ChdEvaluationPlant)) {
    			response.sendRedirect(request.getContextPath());
    			return null;
    		}
    		plantId = plant.getId();
    	}
    	if(request.getParameter("siteId")==null) {
    		SiteService siteService = (SiteService)getService("siteService");
    		WebDirectory site = (WebDirectory)siteService.getDirectoryByName(0, sessionInfo.getUnitName(), false);
    		response.sendRedirect(request.getContextPath() + "/chd/evaluation/plantIndex.shtml?plantId=" + plantId + "&siteId=" + (site==null ? 0 : site.getId()));
    		return null;
    	}
    	if(plant==null) {
    		plant = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(plantId);
    	}
    	if(!evaluationDirectoryService.checkPopedom(plant.getId(), "all", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	request.setAttribute("record", plant);
    	PageService pageService = (PageService)getService("plantPageService");
    	pageService.writePage("chd/evaluation", plant.getDirectoryName().equals(sessionInfo.getUnitName()) ? "myPlant" : "plant", request, response, false);
        return null;
    }
}