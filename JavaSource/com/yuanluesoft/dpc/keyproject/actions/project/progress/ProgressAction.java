package com.yuanluesoft.dpc.keyproject.actions.project.progress;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.dpc.keyproject.actions.project.ProjectAction;
import com.yuanluesoft.dpc.keyproject.forms.Progress;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProgress;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ProgressAction extends ProjectAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Progress progressForm = (Progress)form;
		if(progressForm.getProgresses()==null || progressForm.getProgresses().isEmpty()) {
			Date date = DateTimeUtils.date();
			progressForm.getProgress().setProgressYear(DateTimeUtils.getYear(date));
			progressForm.getProgress().setProgressMonth(DateTimeUtils.getMonth(date) + 1);
			progressForm.getProgress().setProgressTenDay((char)('1' + Math.min(2, DateTimeUtils.getMonth(date)%10)));
		}
		else {
			KeyProjectProgress lastProgress = (KeyProjectProgress)progressForm.getProgresses().iterator().next();
			progressForm.getProgress().setProgressTenDay((char)(lastProgress.getProgressTenDay()=='3' ? '1' : lastProgress.getProgressTenDay() + 1));
			progressForm.getProgress().setProgressMonth(lastProgress.getProgressTenDay()<'3' && lastProgress.getProgressTenDay()>'0' ? lastProgress.getProgressMonth() : (lastProgress.getProgressMonth()==12 ? 1 : lastProgress.getProgressMonth() + 1));
			progressForm.getProgress().setProgressYear(progressForm.getProgress().getProgressMonth()==lastProgress.getProgressMonth() || lastProgress.getProgressMonth()<12 ? lastProgress.getProgressYear() : lastProgress.getProgressYear() + 1);
		}
	}
}