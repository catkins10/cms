/*
 * Created on 2006-5-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.view.model.ViewPackage;

/**
 *
 * @author linchuan
 *
 */
public class MailFilter extends ActionForm {
	private java.lang.String action;
	private java.lang.String conditions;
	private char enable = '0';
	private long userId;
	
	private List receiveMailboxes; //收件箱列表
	private String actionType; //执行的操作类别,delete/直接删除,move/发送到指定邮箱
	private long selectedMailboxId; //用户选择的邮箱ID
	
	private ViewPackage viewPackage = new ViewPackage(); //视图包
    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }
    /**
     * @return Returns the conditions.
     */
    public String getConditions() {
        return conditions;
    }
    /**
     * @param conditions The conditions to set.
     */
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    /**
     * @return Returns the enable.
     */
    public char getEnable() {
        return enable;
    }
    /**
     * @param enable The enable to set.
     */
    public void setEnable(char enable) {
        this.enable = enable;
    }
    /**
     * @return Returns the userId.
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return Returns the viewPackage.
     */
    public ViewPackage getViewPackage() {
        return viewPackage;
    }
    /**
     * @param viewPackage The viewPackage to set.
     */
    public void setViewPackage(ViewPackage viewPackage) {
        this.viewPackage = viewPackage;
    }
    /**
     * @return Returns the actionType.
     */
    public String getActionType() {
        return actionType;
    }
    /**
     * @param actionType The actionType to set.
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    /**
     * @return Returns the receiveMailboxes.
     */
    public List getReceiveMailboxes() {
        return receiveMailboxes;
    }
    /**
     * @param receiveMailboxes The receiveMailboxes to set.
     */
    public void setReceiveMailboxes(List receiveMailboxes) {
        this.receiveMailboxes = receiveMailboxes;
    }
    /**
     * @return Returns the selectedMailboxId.
     */
    public long getSelectedMailboxId() {
        return selectedMailboxId;
    }
    /**
     * @param selectedMailboxId The selectedMailboxId to set.
     */
    public void setSelectedMailboxId(long selectedMailboxId) {
        this.selectedMailboxId = selectedMailboxId;
    }
}
