package com.yuanluesoft.chd.evaluation.actions.plantprerequisitesdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
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
		if(!evaluationDirectoryService.checkPopedom(plantId, "all", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
		request.setAttribute("record", evaluationDirectoryService.getDirectory(plantId));
    	PageService pageService = (PageService)getService("plantPageService");
    	pageService.writePage("chd/evaluation", "plantPrerequisitesData", request, response, false);
        return null;
    }
}