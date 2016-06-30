package com.yuanluesoft.cms.advice.actions.advicefeedback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.advice.service.AdviceService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	AdviceService adviceService = (AdviceService)getService("adviceService");
    	request.setAttribute("record", adviceService.getAdviceFeedback(RequestUtils.getParameterLongValue(request, "topicId")));
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("cms/advice", "adviceFeedback", request, response, false);
    	return null;
    }
}