package com.yuanluesoft.chd.evaluation.actions.admin.company.level;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.actions.admin.company.CompanyAction;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationLevel;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class LevelAction extends CompanyAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			((ChdEvaluationLevel)component).setCreated(DateTimeUtils.now());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}