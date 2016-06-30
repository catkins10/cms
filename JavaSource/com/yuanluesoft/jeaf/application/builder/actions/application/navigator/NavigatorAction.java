package com.yuanluesoft.jeaf.application.builder.actions.application.navigator;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.builder.actions.application.ApplicationAction;
import com.yuanluesoft.jeaf.application.builder.forms.ApplicationNavigator;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class NavigatorAction extends ApplicationAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
		ApplicationNavigator navigatorForm = (ApplicationNavigator)form;
		com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator navigator = (com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator)component;
		navigatorForm.setType(navigator.getViewId()>0 ? "viewLink" : "link");
	}
}