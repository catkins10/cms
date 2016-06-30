package com.yuanluesoft.educ.student.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.educ.student.service.StudentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author yuanlue
 *
 */
public class CompleteRegist extends StudentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }
    /*
     * (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
     */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Stude stude = (Stude)record;
		StudentService studentService = (StudentService)getService("studentService");
		studentService.completeRegist(stude, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}