package com.yuanluesoft.chd.evaluation.actions.admin.plant.objective;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.actions.admin.plant.PlantAction;
import com.yuanluesoft.chd.evaluation.forms.admin.Objective;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationObjective;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ObjectiveAction extends PlantAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Objective objective = (Objective)form;
		objective.getObjective().setObjectiveYear(DateTimeUtils.getYear(DateTimeUtils.date()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			ChdEvaluationObjective objective = (ChdEvaluationObjective)component;
			objective.setCreated(DateTimeUtils.now());
			objective.setCreatorId(sessionInfo.getUserId());
			objective.setCreator(sessionInfo.getUserName());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}