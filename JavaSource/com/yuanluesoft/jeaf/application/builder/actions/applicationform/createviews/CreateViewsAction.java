package com.yuanluesoft.jeaf.application.builder.actions.applicationform.createviews;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.builder.actions.applicationform.ApplicationFormAction;
import com.yuanluesoft.jeaf.application.builder.forms.CreateViews;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationDefineService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class CreateViewsAction extends ApplicationFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		CreateViews createViewsForm = (CreateViews)form;
		ApplicationForm applicationForm = (ApplicationForm)mainRecord;
		ApplicationDefineService applicationDefineService = (ApplicationDefineService)getService("applicationDefineService");
		applicationDefineService.createViews(applicationForm, createViewsForm.getView().getViewFieldIds(), createViewsForm.getView().getViewFieldNames(), createViewsForm.getView().getSortFieldIds(), createViewsForm.getView().getSortFieldNames(), createViewsForm.getView().getSortFieldDirections());
	}
}