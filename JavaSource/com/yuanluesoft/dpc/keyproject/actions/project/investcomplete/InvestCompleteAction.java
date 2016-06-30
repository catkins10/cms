package com.yuanluesoft.dpc.keyproject.actions.project.investcomplete;

import java.sql.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.actions.project.ProjectAction;
import com.yuanluesoft.dpc.keyproject.forms.InvestComplete;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestComplete;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InvestCompleteAction extends ProjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		InvestComplete investCompleteForm = (InvestComplete)form;
		if(investCompleteForm.getInvestCompletes()==null || investCompleteForm.getInvestCompletes().isEmpty()) {
			Date date = DateTimeUtils.date();
			investCompleteForm.getInvestComplete().setCompleteYear(DateTimeUtils.getYear(date));
			investCompleteForm.getInvestComplete().setCompleteMonth(DateTimeUtils.getMonth(date) + 1);
			investCompleteForm.getInvestComplete().setCompleteTenDay((char)('1' + Math.min(2, DateTimeUtils.getMonth(date)%10)));
		}
		else {
			KeyProjectInvestComplete lastInvestComplete = null;
			for(Iterator iterator = investCompleteForm.getInvestCompletes().iterator(); iterator.hasNext();) {
				lastInvestComplete = (KeyProjectInvestComplete)iterator.next();
			}
			investCompleteForm.getInvestComplete().setCompleteTenDay((char)(lastInvestComplete.getCompleteTenDay()=='3' ? '1' : lastInvestComplete.getCompleteTenDay() + 1));
			investCompleteForm.getInvestComplete().setCompleteMonth(lastInvestComplete.getCompleteTenDay()<'3' && lastInvestComplete.getCompleteTenDay()>'0' ? lastInvestComplete.getCompleteMonth() : (lastInvestComplete.getCompleteMonth()==12 ? 1 : lastInvestComplete.getCompleteMonth() + 1));
			investCompleteForm.getInvestComplete().setCompleteYear(investCompleteForm.getInvestComplete().getCompleteMonth()==lastInvestComplete.getCompleteMonth() || lastInvestComplete.getCompleteMonth()<12 ? lastInvestComplete.getCompleteYear() : lastInvestComplete.getCompleteYear() + 1);
		}
	}
}