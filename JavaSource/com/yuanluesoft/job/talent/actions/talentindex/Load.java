package com.yuanluesoft.job.talent.actions.talentindex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	externalAction = true; //对外操作
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	if(sessionInfo.getUserType()!=JobTalentService.PERSON_TYPE_JOB_TALENT) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
    	JobTalent talent = (JobTalent)jobTalentService.load(JobTalent.class, sessionInfo.getUserId());
    	if(talent.getStatus()==2 || talent.getStatus()==4 || talent.getIntentions()==null || talent.getIntentions().isEmpty() || talent.getSchoolings()==null || talent.getSchoolings().isEmpty()) { //审核不通过,或者没有求职意向,或者没有教育经历
    		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
    		response.sendRedirect(request.getContextPath() + "/job/talent/talent.shtml?id=" + sessionInfo.getUserId() + "&act=edit" + (siteId>0 ? "&siteId=" + siteId : ""));
    		return null;
		}
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("job/talent", "talentIndex", request, response, false);
    	return null;
    }
}