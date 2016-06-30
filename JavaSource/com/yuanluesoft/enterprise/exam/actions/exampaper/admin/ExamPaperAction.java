package com.yuanluesoft.enterprise.exam.actions.exampaper.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.forms.admin.ExamPaper;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ExamPaperAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_create")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.enterprise.exam.pojo.ExamPaper examPaper = (com.yuanluesoft.enterprise.exam.pojo.ExamPaper)record;
		if(examPaper!=null) {
			if(examPaper.getStatus()==1) { //试卷已经发布
				form.getFormActions().removeFormAction("发布");
			}
			if(examPaper.getOnComputer()!=1) { //不是计算机作答
				form.getFormActions().removeFormAction("查看答卷");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ExamPaper examPaperForm = (ExamPaper)form;
		ExamService examService = (ExamService)getService("examService");
		examPaperForm.setCreator(sessionInfo.getUserName()); //创建人
		examPaperForm.setCreated(DateTimeUtils.now()); //创建时间
		examPaperForm.setOnComputer(1);
		examPaperForm.setExamPaperName(((com.yuanluesoft.enterprise.exam.pojo.Exam)examService.load(com.yuanluesoft.enterprise.exam.pojo.Exam.class, examPaperForm.getExamId())).getName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ExamPaper examPaperForm = (ExamPaper)form;
		//设置参加考试人员
		examPaperForm.setTestUsers(getRecordControlService().getVisitors(examPaperForm.getId(), com.yuanluesoft.enterprise.exam.pojo.ExamPaper.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ExamPaper examPaperForm = (ExamPaper)form;
		com.yuanluesoft.enterprise.exam.pojo.ExamPaper examPaper = (com.yuanluesoft.enterprise.exam.pojo.ExamPaper)record;
		ExamService examService = (ExamService)getService("examService");
		if(OPEN_MODE_CREATE.equals(openMode)) {
			examPaper = examService.generateExamPaper(examPaper.getExamId(), examPaper.getExamPaperName(), false, examPaper.getBeginTime(), examPaper.getEndTime(), examPaper.getOnComputer()==1, sessionInfo);
		}
		else {
			super.saveRecord(form, record, openMode, request, response, sessionInfo);
		}
		//保存参加考试人员
		if(examPaperForm.getTestUsers().getVisitorIds()!=null) {
			getRecordControlService().updateVisitors(examPaper.getId(), com.yuanluesoft.enterprise.exam.pojo.ExamPaper.class.getName(), examPaperForm.getTestUsers(), RecordControlService.ACCESS_LEVEL_READONLY);
		}
		return examPaper;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}
}