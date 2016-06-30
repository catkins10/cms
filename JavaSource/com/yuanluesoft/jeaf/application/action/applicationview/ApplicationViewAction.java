package com.yuanluesoft.jeaf.application.action.applicationview;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationViewAction extends ViewFormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actiOons.ViewFormAction#getViewApplication()
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		ApplicationView formApplicationView = (ApplicationView)viewForm;
		return formApplicationView.getApplicationName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName()
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		ApplicationView formApplicationView = (ApplicationView)viewForm;
		return formApplicationView.getViewName();
	}
}