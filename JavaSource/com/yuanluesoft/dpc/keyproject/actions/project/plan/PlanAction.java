package com.yuanluesoft.dpc.keyproject.actions.project.plan;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.actions.project.ProjectAction;
import com.yuanluesoft.dpc.keyproject.forms.Plan;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectPlan;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PlanAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Plan planForm = (Plan)form;
		if(planForm.getPlans()==null || planForm.getPlans().isEmpty()) {
			planForm.getProjectPlan().setPlanYear(DateTimeUtils.getYear(DateTimeUtils.now()));
			planForm.getProjectPlan().setPlanMonth(1);
		}
		else {
			KeyProjectPlan lastPlan = null;
			for(Iterator iterator = planForm.getPlans().iterator(); iterator.hasNext();) {
				lastPlan = (KeyProjectPlan)iterator.next();
			}
			planForm.getProjectPlan().setPlanYear(lastPlan.getPlanYear() + (lastPlan.getPlanMonth()>=12 ? 1 : 0));
			planForm.getProjectPlan().setPlanMonth(lastPlan.getPlanMonth()>=12 ? 1 : lastPlan.getPlanMonth() + 1);
		}
	}
}