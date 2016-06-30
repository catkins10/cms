package com.yuanluesoft.cms.interview.actions.admin.interviewsubject.interviewspeak;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.interview.actions.admin.interviewsubject.InterviewSubjectAction;
import com.yuanluesoft.cms.interview.forms.admin.InterviewSpeak;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewSpeakAction extends InterviewSubjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		InterviewSpeak speakForm = (InterviewSpeak)form;
		Timestamp now = DateTimeUtils.now();
		speakForm.getInterviewSpeak().setSpeakTime(now);
		speakForm.getInterviewSpeak().setPublishTime(now);
		speakForm.getInterviewSpeak().setIsLeave('0');
		speakForm.getInterviewSpeak().setSpeakerType(InterviewService.SPEAKER_TYPE_COMPERE);
		speakForm.getInterviewSpeak().setSpeaker("主持人");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		com.yuanluesoft.cms.interview.pojo.InterviewSpeak interviewSpeak = (com.yuanluesoft.cms.interview.pojo.InterviewSpeak)component;
		String content = interviewSpeak.getContent();
		if(content.equals("")) {
			return;
		}
		content = content.replaceAll("(?i)<p([^>]*)>(.*?)</p>", "$2<br>");
		if(content.endsWith("<br>")) {
			content = content.substring(0, content.length() - "<br>".length());
		}
		if(content.equals("")) {
			return;
		}
		interviewSpeak.setContent(content);
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}