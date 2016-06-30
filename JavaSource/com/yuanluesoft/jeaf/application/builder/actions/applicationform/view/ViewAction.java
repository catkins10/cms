package com.yuanluesoft.jeaf.application.builder.actions.applicationform.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.application.builder.actions.applicationform.ApplicationFormAction;
import com.yuanluesoft.jeaf.application.builder.forms.View;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ViewAction extends ApplicationFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.util.List, char, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		View viewForm = (View)form;
		ApplicationForm applicationForm = (ApplicationForm)record;
		ApplicationBuilder applicationBuilder = (ApplicationBuilder)getService("applicationBuilder");
		viewForm.setWorkflowSupport(applicationBuilder.getFormTemplate(applicationForm.getTemplateName()).isWorkflowSupport());
	}
}