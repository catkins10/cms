package com.yuanluesoft.cin.geotechnical.actions.geotechnical;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cin.geotechnical.pojo.Geotechnical;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class GeotechnicalAction extends PublicServiceAction {
	
	public GeotechnicalAction() {
		super();
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Geotechnical geotechnical = (Geotechnical)record;
		com.yuanluesoft.cin.geotechnical.forms.Geotechnical geotechnicalForm = (com.yuanluesoft.cin.geotechnical.forms.Geotechnical)form;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			geotechnicalForm.setSubForm("geotechnical"); //提交页面
		}
		else if(geotechnical.getPublicPass()=='1') { //已公开
			geotechnicalForm.setSubForm("fullyGeotechnical");
		}
	}
}