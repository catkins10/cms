package com.yuanluesoft.jeaf.workflow.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工作项:超时工作项(workflow_workitem_timeout)
 * @author linchuan
 *
 */
public class WorkItemTimeout extends Record {
	private String recordListClassName; //主记录类名称
	private String workItemId; //工作项ID
	private String workflowName; //流程名称
	private String activityName; //环节名称
	private long participantId; //办理人ID
	private String participantName; //办理人姓名
	private char participantType = '0'; //办理人类型
	private String previousParticipantName; //上一办理人姓名
	private Timestamp created; //创建时间
	private long recordId; //主记录ID
	private double deadline; //办理期限
	private long unitId; //单位ID
	private Timestamp completed; //完成时间
	private double workDays; //办理用时
	private int timeout; //是否超时
	
	//扩展属性
	private WorkflowData record; //主记录,超时统计时使用
	
	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return the completed
	 */
	public Timestamp getCompleted() {
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Timestamp completed) {
		this.completed = completed;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the deadline
	 */
	public double getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return the participantId
	 */
	public long getParticipantId() {
		return participantId;
	}
	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	/**
	 * @return the participantName
	 */
	public String getParticipantName() {
		return participantName;
	}
	/**
	 * @param participantName the participantName to set
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return the participantType
	 */
	public char getParticipantType() {
		return participantType;
	}
	/**
	 * @param participantType the participantType to set
	 */
	public void setParticipantType(char participantType) {
		this.participantType = participantType;
	}
	/**
	 * @return the previousParticipantName
	 */
	public String getPreviousParticipantName() {
		return previousParticipantName;
	}
	/**
	 * @param previousParticipantName the previousParticipantName to set
	 */
	public void setPreviousParticipantName(String previousParticipantName) {
		this.previousParticipantName = previousParticipantName;
	}
	/**
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return the workItemId
	 */
	public String getWorkItemId() {
		return workItemId;
	}
	/**
	 * @param workItemId the workItemId to set
	 */
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	/**
	 * @return the workDays
	 */
	public double getWorkDays() {
		return workDays;
	}
	/**
	 * @param workDays the workDays to set
	 */
	public void setWorkDays(double workDays) {
		this.workDays = workDays;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the record
	 */
	public WorkflowData getRecord() {
		return record;
	}
	/**
	 * @param record the record to set
	 */
	public void setRecord(WorkflowData record) {
		this.record = record;
	}
	/**
	 * @return the recordListClassName
	 */
	public String getRecordListClassName() {
		return recordListClassName;
	}
	/**
	 * @param recordListClassName the recordListClassName to set
	 */
	public void setRecordListClassName(String recordListClassName) {
		this.recordListClassName = recordListClassName;
	}
}