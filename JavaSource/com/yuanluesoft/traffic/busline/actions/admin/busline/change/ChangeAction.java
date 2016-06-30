package com.yuanluesoft.traffic.busline.actions.admin.busline.change;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.traffic.busline.actions.admin.busline.BusLineAction;
import com.yuanluesoft.traffic.busline.forms.admin.LineChange;
import com.yuanluesoft.traffic.busline.pojo.BusLineChange;

/**
 * 
 * @author lmiky
 *
 */
public class ChangeAction extends BusLineAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		LineChange lineChangeForm = (LineChange)form;
		lineChangeForm.getChange().setCreated(DateTimeUtils.now()); //创建时间
		lineChangeForm.getChange().setCreator(sessionInfo.getUserName()); //创建人
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			BusLineChange BusLineChange = (BusLineChange)component;
			BusLineChange.setCreated(DateTimeUtils.now()); //创建时间
			BusLineChange.setCreator(sessionInfo.getUserName()); //创建人
			BusLineChange.setCreatorId(sessionInfo.getUserId()); //创建人ID
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}