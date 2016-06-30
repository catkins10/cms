package com.yuanluesoft.jeaf.workflow.callback;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.callback.HideConditionCheckCallback;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 工作流表单按钮隐藏条件判断回调
 * @author linchuan
 *
 */
public class WorkflowHideConditionCheckCallback extends HideConditionCheckCallback {
	private List workflowActions; //环节操作列表
	private String actionName;

	public WorkflowHideConditionCheckCallback(char accessLevel, boolean deleteEnable, String openMode, String subForm, List workflowActions, String actionName) {
		super(accessLevel, deleteEnable, openMode, subForm);
		this.workflowActions = workflowActions;
		this.actionName = actionName;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction.ActionHideConditionCheckCallback#check(java.lang.String, java.lang.String[])
	 */
	public boolean check(String formulaName, String[] parameters) throws ServiceException {
		if(formulaName.equals("workflow")) { //判断是否流程操作
			String actionName = parameters==null || parameters.length==0 || parameters[0].equals("") ? this.actionName : parameters[0];
			return workflowActions==null || ListUtils.findObjectByProperty(workflowActions, "name", actionName)==null;
		}
		else {
			return super.check(formulaName, parameters);
		}
	}
}