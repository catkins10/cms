package com.yuanluesoft.cms.advice.actions.advicetopic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.advice.forms.Advice;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Advice adviceForm = (Advice)form;
    	if(adviceForm.getId()==0) {
    		adviceForm.setId(UUIDLongGenerator.generateId());
    	}
    	adviceForm.setAct("create");
    	PageService pageService = (PageService)getService("adviceTopicPageService");
    	pageService.writePage("cms/advice", "adviceTopic", request, response, false);
    	return null;
    }
}