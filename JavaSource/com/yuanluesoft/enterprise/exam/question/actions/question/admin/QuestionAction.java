package com.yuanluesoft.enterprise.exam.question.actions.question.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.exam.question.forms.admin.Question;
import com.yuanluesoft.enterprise.exam.question.pojo.QuestionAnswer;
import com.yuanluesoft.enterprise.exam.question.service.QuestionService;
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
public class QuestionAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || acl.contains("manageUnit_editor")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Question questionForm = (Question)form;
		questionForm.getTabs().addTab(-1, "basic", "基本信息", null, true);
		questionForm.getTabs().addTab(-1, "tabAnalysis", "试题分析", null, false);
		questionForm.getTabs().addTab(-1, "tabHint", "做题提示", null, false);
		questionForm.getTabs().addTab(-1, "tabDetail", "试题详解", null, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Question questionForm = (Question)form;
		questionForm.setCreator(sessionInfo.getUserName());
		questionForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Question questionForm = (Question)form;
		questionForm.setContent(ListUtils.join(questionForm.getQuestionContents(), "questionContent", ",", false)); //试题内容
		questionForm.setHint(ListUtils.join(questionForm.getQuestionHints(), "questionHint", ",", false)); //提示
		questionForm.setAnalysis(ListUtils.join(questionForm.getQuestionAnalysises(), "questionAnalysis", ",", false)); //分析
		questionForm.setDetail(ListUtils.join(questionForm.getQuestionDetails(), "questionDetail", ",", false)); //明细
		questionForm.setPostIds(ListUtils.join(questionForm.getQuestionPosts(), "postId", ",", false)); //适用的岗位ID
		questionForm.setPosts(ListUtils.join(questionForm.getQuestionPosts(), "post", ",", false)); //适用的岗位
		questionForm.setKnowledgeIds(ListUtils.join(questionForm.getQuestionKnowledges(), "knowledgeId", ",", false)); //知识分类ID
		questionForm.setKnowledges(ListUtils.join(questionForm.getQuestionKnowledges(), "knowledge", ",", false)); //知识分类
		questionForm.setItemIds(ListUtils.join(questionForm.getQuestionItems(), "itemId", ",", false)); //项目分类ID
		questionForm.setItems(ListUtils.join(questionForm.getQuestionItems(), "item", ",", false)); //项目分类
		questionForm.setAnswers(ListUtils.join(questionForm.getQuestionAnswers(), "questionAnswer", "\r\n", false)); //答案,多个时用\r\n分隔
		questionForm.setEssayAnswers(ListUtils.join(questionForm.getQuestionAnswers(), "questionAnswer", "\r\n", false)); //问答/论述题答案
		questionForm.setAnswerCaseSensitive(questionForm.getQuestionAnswers()==null || questionForm.getQuestionAnswers().isEmpty() ? false : ((QuestionAnswer)questionForm.getQuestionAnswers().iterator().next()).getCaseSensitive()=='1'); //答案是否区分大小写
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.enterprise.exam.question.pojo.Question question = (com.yuanluesoft.enterprise.exam.question.pojo.Question)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			question.setCreatorId(sessionInfo.getUserId());
			question.setCreator(sessionInfo.getUserName());
			question.setCreated(DateTimeUtils.now());
		}
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Question questionForm = (Question)form;
		QuestionService questionService = (QuestionService)getService("questionService");
		//更新组成部分
		questionService.updateQuestionComponents(question, questionForm.getContent(), questionForm.getHint(), questionForm.getAnalysis(), questionForm.getDetail(), questionForm.getPostIds(), questionForm.getPosts(), questionForm.getKnowledgeIds(), questionForm.getKnowledges(), questionForm.getItemIds(), questionForm.getItems(), questionForm.getAnswers(), questionForm.getEssayAnswers(), questionForm.isAnswerCaseSensitive(), OPEN_MODE_CREATE.equals(openMode));
		return record;
	}
}