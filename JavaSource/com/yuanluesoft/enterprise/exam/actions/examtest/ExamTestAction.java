package com.yuanluesoft.enterprise.exam.actions.examtest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.pojo.ExamTest;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ExamTestAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ExamTest examTest = (ExamTest)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(examTest==null) {
			com.yuanluesoft.enterprise.exam.forms.ExamTest examTestForm = (com.yuanluesoft.enterprise.exam.forms.ExamTest)form;
			ExamService examService = (ExamService)getService("examService");
			examTest = examService.createExamTest(examTestForm.getPaperId(), sessionInfo);
		}
		return examTest;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ExamTest examTest = (ExamTest)record;
		com.yuanluesoft.enterprise.exam.forms.ExamTest examTestForm = (com.yuanluesoft.enterprise.exam.forms.ExamTest)form;
		ExamService examService = (ExamService)getService("examService");
		if(examTest==null) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		try {
			if(examTestForm.isCorrect()) {
				if(!examService.isCorrector(examTest.getExam().getId(), sessionInfo)) {
					throw new PrivilegeException();
				}
			}
			else if(examTestForm.isCheck()) {
				if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_create")) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			else if(examTest.getPersonId()!=sessionInfo.getUserId() && !examService.isCorrector(examTest.getExam().getId(), sessionInfo)) {
				throw new PrivilegeException();
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.enterprise.exam.forms.ExamTest examTestForm = (com.yuanluesoft.enterprise.exam.forms.ExamTest)form;
		ExamTest examTest = (ExamTest)record;
		if(examTest==null) {
			form.setSubForm("examTestNotExist");
		}
		else if(examTestForm.isCorrect() && examTest.getStatus()==ExamService.EXAM_TEST_STATUS_TOCONRRECT) { //批改
			form.setSubForm("examTestCorrect");
		}
		else if(examTestForm.isCheck()) { //复核
			form.setSubForm("examTestCheck");
		}
		else if(examTest.getStatus()==ExamService.EXAM_TEST_STATUS_TESTING) {
			form.setSubForm("examTest");
		}
		else {
			form.setSubForm("examTestRead");
		}
		if(examTest!=null) {
			request.setAttribute("EXAM_TEST_STATUS", new Integer(examTest.getStatus()));
			request.setAttribute("record", examTest);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ExamTest examTest = (ExamTest)record;
		if(examTest==null) {
			throw new ValidateException("试卷已经回答过，不允许重复作答。");
		}
		com.yuanluesoft.enterprise.exam.forms.ExamTest examTestForm = (com.yuanluesoft.enterprise.exam.forms.ExamTest)form;
		ExamService examService = (ExamService)getService("examService");
		if(examTest.getStatus()==ExamService.EXAM_TEST_STATUS_TESTING) {
			examService.submitExamTest(examTest, request);
		}
		else if(examTestForm.isCorrect() && examTest.getStatus()==ExamService.EXAM_TEST_STATUS_TOCONRRECT) {
			examService.correctExamTest(examTest, request, sessionInfo);
		}
		else if(examTestForm.isCheck() && examTest.getStatus()==ExamService.EXAM_TEST_STATUS_COMPLETE) { //复核
			examService.correctExamTest(examTest, request, sessionInfo);
		}
		return record;
	}
}