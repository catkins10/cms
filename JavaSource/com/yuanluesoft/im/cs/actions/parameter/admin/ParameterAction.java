package com.yuanluesoft.im.cs.actions.parameter.admin;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.im.cs.forms.admin.Parameter;
import com.yuanluesoft.im.cs.pojo.CSParameter;
import com.yuanluesoft.im.cs.service.CSService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ParameterAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE.equals(getOpenMode(form, request))) {
			CSService csService = (CSService)getService("csService");
			Parameter parameterForm = (Parameter)form;
			CSParameter csParameter = csService.loadParameter(parameterForm.getSiteId());
			return csParameter==null || csParameter.getSiteId()!=parameterForm.getSiteId() ? null : csParameter;
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}
}