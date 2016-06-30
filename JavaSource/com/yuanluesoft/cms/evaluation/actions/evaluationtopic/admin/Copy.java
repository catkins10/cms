package com.yuanluesoft.cms.evaluation.actions.evaluationtopic.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.cms.evaluation.service.EvaluationService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Copy extends EvaluationTopicAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward actionForward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(actionForward!=null && "result".equals(actionForward.getName())) {
    		com.yuanluesoft.cms.evaluation.forms.admin.EvaluationTopic topicForm = (com.yuanluesoft.cms.evaluation.forms.admin.EvaluationTopic)form;
    		EvaluationService evaluationService = (EvaluationService)getService("evaluationService");
    		EvaluationTopic evaluationTopic = evaluationService.copyEvaluationTopic(topicForm.getId(), RequestUtils.getSessionInfo(request));
    		response.sendRedirect("evaluationTopic.shtml?act=edit&id=" + evaluationTopic.getId());
    		return null;
    	}
    	return actionForward;
    }
}