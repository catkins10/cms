package com.yuanluesoft.railway.evaluation.actions.evaluationview.admin;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.railway.evaluation.forms.admin.EvaluationView;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationViewAction extends ApplicationViewAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "railway/evaluation";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/railwayEvaluation";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		EvaluationView evaluationView = (EvaluationView)viewForm;
		if(evaluationView.getYear()==0) {
			Date date = DateTimeUtils.add(DateTimeUtils.date(), Calendar.MONTH, -1);
			evaluationView.setYear(DateTimeUtils.getYear(date));
			evaluationView.setMonth(DateTimeUtils.getMonth(date) + 1);
		}
	}
}