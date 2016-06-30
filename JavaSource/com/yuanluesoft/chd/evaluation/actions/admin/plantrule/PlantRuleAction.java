package com.yuanluesoft.chd.evaluation.actions.admin.plantrule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.chd.evaluation.actions.admin.DirectoryAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class PlantRuleAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		/*EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)record;
		while((evaluationDirectory=(ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(evaluationDirectory.getSourceDirectoryId()))!=null) {
			ChdEvaluationCompany company = (ChdEvaluationCompany)evaluationDirectoryService.getParentDirectory(evaluationDirectory.getParentDirectoryId(), "company");
			Tab tab = form.getTabs().addTab(1, "detail_" + company.getId(), company.getDirectoryName() + "考评要求", "plantRuleDetail.jsp", false);
			tab.setAttribute("rule", evaluationDirectory); //设置"rule"属性
			//创建子TAB列表
			TabList ruleDetailTabs = new TabList();
			ruleDetailTabs.addTab(-1, "provision_" + evaluationDirectory.getId(), "评分规定", null, true);
			ruleDetailTabs.addTab(-1, "scores_" + evaluationDirectory.getId(), "考评分数", null, false);
			tab.setAttribute("ruleDetailTabs", ruleDetailTabs);
			if(evaluationDirectory.getSourceDirectoryId()==0) {
				break;
			}
		}*/
	}
}