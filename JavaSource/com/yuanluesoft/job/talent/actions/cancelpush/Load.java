package com.yuanluesoft.job.talent.actions.cancelpush;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author chuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
    	jobTalentService.cancelPush(request.getParameter("talentId"));
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("job/talent", "cancelPush", request, response, false);
    	return null;
    }
}