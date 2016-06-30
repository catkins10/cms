package com.yuanluesoft.enterprise.exam.actions.exam.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.forms.admin.Exam;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
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
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ExamAction extends FormAction {

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
		Exam examForm = (Exam)form;
		com.yuanluesoft.enterprise.exam.pojo.Exam exam = (com.yuanluesoft.enterprise.exam.pojo.Exam)record;
		QuestionService questionService = (QuestionService)getService("questionService");
		examForm.setQuestionTypes(questionService.listQuestionTypes()); //题型列表
		examForm.setDifficultyLevels(ListUtils.generatePropertyList(questionService.listDifficultyLevels(), "level")); //难度等级列表
		if(exam!=null && exam.getSelfTest()==1) { //自助方式
			examForm.getFormActions().removeFormAction("组卷");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Exam examForm = (Exam)form;
		examForm.setCreator(sessionInfo.getUserName()); //创建人
		examForm.setCreated(DateTimeUtils.now()); //创建时间
		examForm.setScore(100); //分数
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Exam examForm = (Exam)form;
		examForm.setExamPostIds(ListUtils.join(examForm.getExamPosts(), "postId", ",", false)); //适用的岗位ID
		examForm.setExamPostNames(ListUtils.join(examForm.getExamPosts(), "post", ",", false)); //适用的岗位名称ID
		examForm.setExamCorrectorIds(ListUtils.join(examForm.getExamCorrectors(), "correctorId", ",", false)); //批改人ID
		examForm.setExamCorrectorNames(ListUtils.join(examForm.getExamCorrectors(), "corrector", ",", false)); //批改人姓名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.enterprise.exam.pojo.Exam exam = (com.yuanluesoft.enterprise.exam.pojo.Exam)record;
		Exam examForm = (Exam)form;
		ExamService examService = (ExamService)getService("examService");
		if(OPEN_MODE_CREATE.equals(openMode)) {
			exam.setCreator(sessionInfo.getUserName());
			exam.setCreatorId(sessionInfo.getUserId());
			exam.setCreated(DateTimeUtils.now());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		examService.updateExamComponents(exam, OPEN_MODE_CREATE.equals(openMode), examForm.getExamPostIds(), examForm.getExamPostNames(), examForm.getExamCorrectorIds(), examForm.getExamCorrectorNames(), request);
		return exam;
	}
}