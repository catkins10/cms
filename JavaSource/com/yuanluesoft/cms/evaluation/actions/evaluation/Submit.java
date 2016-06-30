package com.yuanluesoft.cms.evaluation.actions.evaluation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.evaluation.forms.Evaluation;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends EvaluationAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward actionForward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(actionForward!=null && "result".equals(actionForward.getName())) {
    		Evaluation evaluationForm = (Evaluation)form;
    		response.sendRedirect("evaluationTopic.shtml?id=" + evaluationForm.getTopicId() + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
    		return null;
    	}
    	return actionForward;
    }
}