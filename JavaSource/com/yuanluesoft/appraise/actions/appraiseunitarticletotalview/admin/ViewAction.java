package com.yuanluesoft.appraise.actions.appraiseunitarticletotalview.admin;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.appraise.forms.admin.AppraiseUnitArticleTotalView;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ViewAction extends ApplicationViewAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "appraise";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/appraiseUnitArticleTotal";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		AppraiseUnitArticleTotalView totalView = (AppraiseUnitArticleTotalView)viewForm;
		if(totalView.getBeginDate()==null) {
			Date date = DateTimeUtils.date();
			date = DateTimeUtils.set(date, Calendar.MONTH, 0);
			date = DateTimeUtils.set(date, Calendar.DAY_OF_MONTH, 1);
			totalView.setBeginDate(date);
		}
	}
}