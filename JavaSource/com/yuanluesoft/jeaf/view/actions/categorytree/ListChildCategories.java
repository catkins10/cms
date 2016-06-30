package com.yuanluesoft.jeaf.view.actions.categorytree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.CategoryTree;

/**
 * 
 * @author linchuan
 *
 */
public class ListChildCategories extends CategoryTreeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	CategoryTree categoryTreeForm = (CategoryTree)form;
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			categoryTreeForm.setActionResult("NOSESSIONINFO");
			return mapping.findForward("load");
		}
    	//获取子节点列表
		categoryTreeForm.setChildNodes(listChildTreeNodes(getView(categoryTreeForm.getApplicationName(), categoryTreeForm.getViewName(), sessionInfo), categoryTreeForm.getParentNodeId(), request, sessionInfo));
		categoryTreeForm.setActionResult("SUCCESS");
    	return mapping.findForward("load");
    }
}