/*
 * Created on 2005-3-22
 *
 */
package com.yuanluesoft.jeaf.workflow.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工作项(workflow_workitem)
 * @author linchuan
 *
 */
public class WorkItem extends Record {
	private String workItemId; //工作项ID
	private String workflowName; //流程名称
	private String activityName; //环节名称
	private long participantId; //办理人ID
	private String participantName; //办理人姓名
	private char participantType = '0'; //办理人类型
	private String previousParticipantName; //上一办理人姓名
	private Timestamp created; //创建时间
	private long recordId; //主记录ID
	private double deadline; //办理期限(天)
	private char isReverse = '0'; //是否被回退
	private char isUndo = '0'; //是否是取回
	
	/**
	 * @return Returns the participantId.
	 */
	public long getParticipantId() {
		return participantId;
	}
	/**
	 * @param participantId The participantId to set.
	 */
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	/**
	 * @return Returns the participantType.
	 */
	public char getParticipantType() {
		return participantType;
	}
	/**
	 * @param participantType The participantType to set.
	 */
	public void setParticipantType(char participantType) {
		this.participantType = participantType;
	}
	/**
	 * @return Returns the deadline.
	 */
	public double getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline The deadline to set.
	 */
	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return Returns the workflowName.
	 */
	public java.lang.String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName The workflowName to set.
	 */
	public void setWorkflowName(java.lang.String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return Returns the previousParticipantName.
	 */
	public java.lang.String getPreviousParticipantName() {
		return previousParticipantName;
	}
	/**
	 * @param previousParticipantName The previousParticipantName to set.
	 */
	public void setPreviousParticipantName(
			java.lang.String previousParticipantName) {
		this.previousParticipantName = previousParticipantName;
	}
	/**
	 * @return Returns the participantName.
	 */
	public java.lang.String getParticipantName() {
		return participantName;
	}
	/**
	 * @param participantName The participantName to set.
	 */
	public void setParticipantName(java.lang.String participantName) {
		this.participantName = participantName;
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
	 * @return Returns the activityName.
	 */
	public java.lang.String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName The activityName to set.
	 */
	public void setActivityName(java.lang.String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return Returns the workItemId.
	 */
	public java.lang.String getWorkItemId() {
		return workItemId;
	}
	/**
	 * @param workItemId The workItemId to set.
	 */
	public void setWorkItemId(java.lang.String workItemId) {
		this.workItemId = workItemId;
	}
    /**
     * @return Returns the recordId.
     */
    public long getRecordId() {
        return recordId;
    }
    /**
     * @param recordId The recordId to set.
     */
    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
	/**
	 * @return the isReverse
	 */
	public char getIsReverse() {
		return isReverse;
	}
	/**
	 * @param isReverse the isReverse to set
	 */
	public void setIsReverse(char isReverse) {
		this.isReverse = isReverse;
	}
	/**
	 * @return the isUndo
	 */
	public char getIsUndo() {
		return isUndo;
	}
	/**
	 * @param isUndo the isUndo to set
	 */
	public void setIsUndo(char isUndo) {
		this.isUndo = isUndo;
	}
}
