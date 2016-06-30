package com.yuanluesoft.dpc.keyproject.actions.project.annualobjective;

import java.sql.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.actions.project.ProjectAction;
import com.yuanluesoft.dpc.keyproject.forms.AnnualObjective;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectAnnualObjective;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AnnualObjectiveAction extends ProjectAction {
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		AnnualObjective objectiveForm = (AnnualObjective)form;
		if(objectiveForm.getAnnualObjectives()==null || objectiveForm.getAnnualObjectives().isEmpty()) {
			Date date = DateTimeUtils.date();
			objectiveForm.getAnnualObjective().setObjectiveYear(DateTimeUtils.getYear(date));
		}
		else {
			KeyProjectAnnualObjective lastObjective = null;
			for(Iterator iterator = objectiveForm.getAnnualObjectives().iterator(); iterator.hasNext();) {
				lastObjective = (KeyProjectAnnualObjective)iterator.next();
			}
			objectiveForm.getAnnualObjective().setObjectiveYear(lastObjective.getObjectiveYear() + 1);
		}
	}
}