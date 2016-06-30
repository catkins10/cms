package com.yuanluesoft.chd.evaluation.actions.admin.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.chd.evaluation.actions.admin.DirectoryAction;
import com.yuanluesoft.chd.evaluation.forms.admin.Rule;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class RuleAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		form.getTabs().addTab(-1, "scores", "考评分数", "ruleScores.jsp", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Rule ruleForm = (Rule)form;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		ruleForm.setScores(evaluationDirectoryService.listRuleScores(ruleForm.getParentDirectoryId(), null));
		ruleForm.setParentDirectory((ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(ruleForm.getParentDirectoryId()));
		if(ruleForm.getParentDirectory() instanceof ChdEvaluationRule) {
			ruleForm.setIsIndicator(((ChdEvaluationRule)ruleForm.getParentDirectory()).getIsIndicator());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Rule ruleForm = (Rule)form;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		ruleForm.setScores(evaluationDirectoryService.listRuleScores(ruleForm.getId(), (ChdEvaluationRule)record));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Rule ruleForm = (Rule)form;
		ChdEvaluationRule rule = (ChdEvaluationRule)record;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			//获取父目录
			ChdEvaluationDirectory parentDirectory = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(ruleForm.getParentDirectoryId());
			if(parentDirectory instanceof ChdEvaluationRule) {
				rule.setIsIndicator(((ChdEvaluationRule)parentDirectory).getIsIndicator()); //继承父项目的类型
			}
		}
		rule = (ChdEvaluationRule)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存各级别对应的考核分数
		evaluationDirectoryService.saveRuleScores(rule.getId(), request.getParameterValues("levelId"), request.getParameterValues("minScore"), request.getParameterValues("maxScore"));
		return rule;
	}
}