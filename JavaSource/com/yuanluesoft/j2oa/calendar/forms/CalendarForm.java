/*
 * Created on 2006-5-31
 *
 */
package com.yuanluesoft.j2oa.calendar.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 *
 * @author linchuan
 *
 */
public class CalendarForm extends ActionForm {
    private java.lang.String address;
	private java.sql.Timestamp beginTime;
	private java.sql.Timestamp endTime;
	private java.sql.Timestamp created;
	private java.lang.String department;
	private java.lang.String description;
	private java.lang.String important;
	private char publish = '0';
	private java.lang.String subject;
	private long creatorId;
	
	private String creatorName; //经办人姓名
	
	//参加领导
	private RecordVisitorList leaders = new RecordVisitorList();
	private String conflict; //领导忙碌原因
	private boolean forcePublish; //是否在冲突时还要发布
	
    /**
     * @return Returns the address.
     */
    public java.lang.String getAddress() {
        return address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }
    /**
     * @return Returns the beginTime.
     */
    public java.sql.Timestamp getBeginTime() {
        return beginTime;
    }
    /**
     * @param beginTime The beginTime to set.
     */
    public void setBeginTime(java.sql.Timestamp beginTime) {
        this.beginTime = beginTime;
    }
    /**
     * @return Returns the created.
     */
    public java.sql.Timestamp getCreated() {
        return created;
    }
    /**
     * @param created The created to set.
     */
    public void setCreated(java.sql.Timestamp created) {
        this.created = created;
    }
    /**
     * @return Returns the creatorId.
     */
    public long getCreatorId() {
        return creatorId;
    }
    /**
     * @param creatorId The creatorId to set.
     */
    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
    /**
     * @return Returns the department.
     */
    public java.lang.String getDepartment() {
        return department;
    }
    /**
     * @param department The department to set.
     */
    public void setDepartment(java.lang.String department) {
        this.department = department;
    }
    /**
     * @return Returns the description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    /**
     * @return Returns the endTime.
     */
    public java.sql.Timestamp getEndTime() {
        return endTime;
    }
    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(java.sql.Timestamp endTime) {
        this.endTime = endTime;
    }
    /**
     * @return Returns the important.
     */
    public java.lang.String getImportant() {
        return important;
    }
    /**
     * @param important The important to set.
     */
    public void setImportant(java.lang.String important) {
        this.important = important;
    }
    /**
     * @return Returns the publish.
     */
    public char getPublish() {
        return publish;
    }
    /**
     * @param publish The publish to set.
     */
    public void setPublish(char publish) {
        this.publish = publish;
    }
    /**
     * @return Returns the subject.
     */
    public java.lang.String getSubject() {
        return subject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }
    /**
     * @return Returns the leaders.
     */
    public RecordVisitorList getLeaders() {
        return leaders;
    }
    /**
     * @param leaders The leaders to set.
     */
    public void setLeaders(RecordVisitorList leaders) {
        this.leaders = leaders;
    }
    /**
     * @return Returns the creatorName.
     */
    public String getCreatorName() {
        return creatorName;
    }
    /**
     * @param creatorName The creatorName to set.
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    /**
     * @return Returns the leaderBusyReason.
     */
    public String getConflict() {
        return conflict;
    }
    /**
     * @param leaderBusyReason The leaderBusyReason to set.
     */
    public void setConflict(String leaderBusyReason) {
        this.conflict = leaderBusyReason;
    }
    /**
     * @return Returns the forcePublish.
     */
    public boolean isForcePublish() {
        return forcePublish;
    }
    /**
     * @param forcePublish The forcePublish to set.
     */
    public void setForcePublish(boolean forcePublish) {
        this.forcePublish = forcePublish;
    }
}
