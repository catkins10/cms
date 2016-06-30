package com.yuanluesoft.jeaf.timetable.actions.timetable.commutetime;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.timetable.actions.timetable.TimetableAction;
import com.yuanluesoft.jeaf.timetable.forms.CommuteTime;

/**
 * 
 * @author linchuan
 *
 */
public class CommuteTimeAction extends TimetableAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		CommuteTime commuteTimeForm = (CommuteTime)form;
		commuteTimeForm.getCommuteTime().setIsOvertime('0');
		commuteTimeForm.getCommuteTime().setAbsentDay(0.5f);
	}
}