package com.yuanluesoft.cms.evaluation.actions.evaluationtopic.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Unissue extends EvaluationTopicAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "撤销成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.evaluation.actions.evaluationtopic.admin.EvaluationTopicAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EvaluationTopic evaluationTopic = (EvaluationTopic)record;
		evaluationTopic.setIssue('0');
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}