/*
 * Created on 2006-5-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邮件过滤(webmail_filter)
 * @author linchuan
 *
 */
public class MailFilter extends Record {
	private long userId; //用户ID
	private String conditions; //过虑条件
	private String action; //执行的操作
	private char enable = '0'; //是否生效
	private int priority; //优先级
	
	/**
	 * 获取条件描述
	 * @return
	 */
	public String getConditionsDescribe() {
	    if(conditions==null || conditions.equals("")) {
	        return null;
	    }
	    else {
	        return conditions.substring(0, conditions.indexOf("\r\n\r\n"));
	    }
    }
	/**
	 * 获取操作描述
	 * @return
	 */
	public String getActionDescribe() {
	    if(action==null || action.equals("")) {
	        return null;
	    }
	    else {
	        return action.substring(0, action.indexOf('|'));
	    }
    }
    /**
     * @return Returns the action.
     */
    public java.lang.String getAction() {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }
    /**
     * @return Returns the conditions.
     */
    public java.lang.String getConditions() {
        return conditions;
    }
    /**
     * @param conditions The conditions to set.
     */
    public void setConditions(java.lang.String conditions) {
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
     * @return Returns the priority.
     */
    public int getPriority() {
        return priority;
    }
    /**
     * @param priority The priority to set.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
