package com.yuanluesoft.jeaf.opinionmanage.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Opinion extends ActionForm {
	private java.lang.String opinion;
	private Timestamp created;
	private long mainRecordId;
	private String opinionType;
	private long personId;
	private String activityId;
	private String activityName;
	private String workItemId;
	private String personName;
	private String agentName;
	private long agentId;
	
	private String mainRecordClassName; //主记录类名称
	
    /**
     * @return Returns the activityId.
     */
    public String getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
        return activityName;
    }
    /**
     * @param activityName The activityName to set.
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    /**
     * @return Returns the created.
     */
    public Timestamp getCreated() {
        return created;
    }
    /**
     * @param created The created to set.
     */
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    /**
     * @return Returns the mainRecordId.
     */
    public long getMainRecordId() {
        return mainRecordId;
    }
    /**
     * @param mainRecordId The mainRecordId to set.
     */
    public void setMainRecordId(long mainRecordId) {
        this.mainRecordId = mainRecordId;
    }
    /**
     * @return Returns the opinion.
     */
    public java.lang.String getOpinion() {
        return opinion;
    }
    /**
     * @param opinion The opinion to set.
     */
    public void setOpinion(java.lang.String opinion) {
        this.opinion = opinion;
    }
    /**
     * @return Returns the opinionType.
     */
    public String getOpinionType() {
        return opinionType;
    }
    /**
     * @param opinionType The opinionType to set.
     */
    public void setOpinionType(String opinionType) {
        this.opinionType = opinionType;
    }
    /**
     * @return Returns the personId.
     */
    public long getPersonId() {
        return personId;
    }
    /**
     * @param personId The personId to set.
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }
    /**
     * @return Returns the workItemId.
     */
    public String getWorkItemId() {
        return workItemId;
    }
    /**
     * @param workItemId The workItemId to set.
     */
    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }
    /**
     * @return Returns the agentId.
     */
    public long getAgentId() {
        return agentId;
    }
    /**
     * @param agentId The agentId to set.
     */
    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }
    /**
     * @return Returns the agentName.
     */
    public String getAgentName() {
        return agentName;
    }
    /**
     * @param agentName The agentName to set.
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    /**
     * @return Returns the personName.
     */
    public String getPersonName() {
        return personName;
    }
    /**
     * @param personName The personName to set.
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
	/**
	 * @return the mainRecordClassName
	 */
	public String getMainRecordClassName() {
		return mainRecordClassName;
	}
	/**
	 * @param mainRecordClassName the mainRecordClassName to set
	 */
	public void setMainRecordClassName(String mainRecordClassName) {
		this.mainRecordClassName = mainRecordClassName;
	}
}