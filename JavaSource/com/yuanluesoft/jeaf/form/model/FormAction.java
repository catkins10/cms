/*
 * Created on 2004-12-19
 *
 */
package com.yuanluesoft.jeaf.form.model;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class FormAction extends CloneableObject {
	private String title; //标题
	private String type; //类型
	private String image; //图标
	private String hideCondition; //隐藏条件
	private String execute; //执行的操作
	private boolean isDefault; //是否默认按钮
	
	//流程相关属性
	private boolean isDeleteAction; //是否删除操作
	private boolean isSendAction; //是否流程发送操作
	private boolean isReverseAction; //操作中是否包含流程回退操作
	private String actionPrompt; //未执行时的提示
	private boolean isNecessaryAction; //是否必须执行
	private boolean promptOnCreate; //创建时是否需要在未执行时的提示
	
	public FormAction() {
		
	}
	
	public FormAction(String title, String execute) {
		super();
		this.title = title;
		this.execute = execute;
	}

	public FormAction(String title, String type, String image, String hideCondition, String execute, boolean isDefault) {
		super();
		this.title = title;
		this.type = type;
		this.image = image;
		this.hideCondition = hideCondition;
		this.execute = execute;
		this.isDefault = isDefault;
	}
	
	public FormAction(String title, String type, String image, String hideCondition, String execute, boolean isDeleteAction, boolean isSendAction, boolean isReverseAction, String actionPrompt, boolean isNecessaryAction, boolean promptOnCreate) {
		super();
		this.title = title;
		this.type = type;
		this.image = image;
		this.hideCondition = hideCondition;
		this.execute = execute;
		this.isDeleteAction = isDeleteAction;
		this.isSendAction = isSendAction;
		this.isReverseAction = isReverseAction;
		this.actionPrompt = actionPrompt;
		this.isNecessaryAction = isNecessaryAction;
		this.promptOnCreate = promptOnCreate;
	}

	/**
	 * @return Returns the execute.
	 */
	public String getExecute() {
		return execute;
	}
	/**
	 * @param execute The execute to set.
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}
	/**
	 * @return Returns the hideCondition.
	 */
	public String getHideCondition() {
		return hideCondition;
	}
	/**
	 * @param hideCondition The hideCondition to set.
	 */
	public void setHideCondition(String hideCondition) {
		this.hideCondition = hideCondition;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the actionPrompt
	 */
	public String getActionPrompt() {
		return actionPrompt;
	}

	/**
	 * @param actionPrompt the actionPrompt to set
	 */
	public void setActionPrompt(String actionPrompt) {
		this.actionPrompt = actionPrompt;
	}

	/**
	 * @return the isDeleteAction
	 */
	public boolean isDeleteAction() {
		return isDeleteAction;
	}

	/**
	 * @param isDeleteAction the isDeleteAction to set
	 */
	public void setDeleteAction(boolean isDeleteAction) {
		this.isDeleteAction = isDeleteAction;
	}

	/**
	 * @return the isNecessaryAction
	 */
	public boolean isNecessaryAction() {
		return isNecessaryAction;
	}

	/**
	 * @param isNecessaryAction the isNecessaryAction to set
	 */
	public void setNecessaryAction(boolean isNecessaryAction) {
		this.isNecessaryAction = isNecessaryAction;
	}

	/**
	 * @return the isReverseAction
	 */
	public boolean isReverseAction() {
		return isReverseAction;
	}

	/**
	 * @param isReverseAction the isReverseAction to set
	 */
	public void setReverseAction(boolean isReverseAction) {
		this.isReverseAction = isReverseAction;
	}

	/**
	 * @return the isSendAction
	 */
	public boolean isSendAction() {
		return isSendAction;
	}

	/**
	 * @param isSendAction the isSendAction to set
	 */
	public void setSendAction(boolean isSendAction) {
		this.isSendAction = isSendAction;
	}

	/**
	 * @return the promptOnCreate
	 */
	public boolean isPromptOnCreate() {
		return promptOnCreate;
	}

	/**
	 * @param promptOnCreate the promptOnCreate to set
	 */
	public void setPromptOnCreate(boolean promptOnCreate) {
		this.promptOnCreate = promptOnCreate;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
}
