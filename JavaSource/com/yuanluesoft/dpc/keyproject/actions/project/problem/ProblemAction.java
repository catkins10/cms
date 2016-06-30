package com.yuanluesoft.dpc.keyproject.actions.project.problem;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.actions.project.ProjectAction;
import com.yuanluesoft.dpc.keyproject.forms.Problem;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ProblemAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Problem problemForm = (Problem)form;
		problemForm.getProblem().setProblemYear(DateTimeUtils.getYear(DateTimeUtils.now()));
		problemForm.getProblem().setProblemMonth(DateTimeUtils.getMonth(DateTimeUtils.now()) + 1);
	}
}