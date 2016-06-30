package com.yuanluesoft.cms.interview.actions.uploadimage;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.interview.actions.admin.interviewsubject.interviewimage.InterviewImageAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class UploadImageAction extends InterviewImageAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#isLockByPerson(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return true;
	}
}