package com.yuanluesoft.jeaf.form.model;

import java.util.ArrayList;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 表单操作列表
 * @author linchuan
 *
 */
public class FormActionList extends ArrayList {

	/**
	 * 添加表单操作
	 * @param actionIndex
	 * @param title
	 * @param execute
	 * @param firstAction 是否第一个操作
	 * @return
	 */
	public FormAction addFormAction(int actionIndex, String title, String execute, boolean firstAction) {
		if(firstAction) {
			clear();
		}
		FormAction action = new FormAction(title, execute);
		if(actionIndex==-1) {
			add(action);
		}
		else {
			add(actionIndex, action);
		}
		return action;
	}
	
	/**
	 * 删除按钮
	 * @param actionTitle
	 * @return
	 */
	public FormAction removeFormAction(String actionTitle) {
		return (FormAction)ListUtils.removeObjectByProperty(this, "title", actionTitle);
	}
}