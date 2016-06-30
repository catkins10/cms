package com.yuanluesoft.jeaf.timetable.actions.timetable.offday;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.timetable.actions.timetable.TimetableAction;
import com.yuanluesoft.jeaf.timetable.forms.OffDay;

/**
 * 
 * @author linchuan
 *
 */
public class OffDayAction extends TimetableAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo,	request);
		OffDay offDayForm = (OffDay)form;
		offDayForm.getOffDay().setBeginTime("00:00");
		offDayForm.getOffDay().setEndTime("23:59");
	}
}