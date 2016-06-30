package com.yuanluesoft.j2oa.info.actions.issuestatistic;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class IssueStatisticViewAction extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction#getViewApplicationName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "j2oa/info";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "issueStatistic";
	}
}