package com.yuanluesoft.j2oa.supervise.actions.supervise.timelimit;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.supervise.actions.supervise.SuperviseAction;
import com.yuanluesoft.j2oa.supervise.pojo.SuperviseTimeLimit;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SuperviseTimeLimitAction extends SuperviseAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		SuperviseTimeLimit superviseTimeLimit = (SuperviseTimeLimit)component;
		superviseTimeLimit.setCreated(DateTimeUtils.now());
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}	
}