package com.yuanluesoft.job.apply.actions.favorite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.apply.service.JobApplyService;

/**
 * 
 * @author linchuan
 *
 */
public class Add extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        externalAction = true;
        SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        JobApplyService jobApplyService = (JobApplyService)getService("jobApplyService");
        jobApplyService.addFavorite(sessionInfo.getUserId(), RequestUtils.getParameterLongValue(request, "jobId"));
        PageService pageService = (PageService)getService("pageService");
        pageService.writePage("job/apply", "addFavoriteSuccess", request, response, false);
        return null;
    }
}