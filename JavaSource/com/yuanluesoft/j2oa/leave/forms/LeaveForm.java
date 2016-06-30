package com.yuanluesoft.j2oa.leave.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class LeaveForm extends WorkflowForm {
    private String reason;
	private java.sql.Timestamp beginTime;
	private java.sql.Timestamp endTime;
	private String remark;
	private String workflowInstanceId;
	private long personId;
	private double dayCount; //请假天数
	private String personName;
	private long departmentId;
	private String departmentName;
	private char isTerminated = '0';
	private java.sql.Timestamp terminateTime;
	private Set opinions;
	private String type;

	private RecordVisitorList agents = new RecordVisitorList();

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
     * @return Returns the departmentId.
     */
    public long getDepartmentId() {
        return departmentId;
    }
    /**
     * @param departmentId The departmentId to set.
     */
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
    /**
     * @return Returns the departmentName.
     */
    public String getDepartmentName() {
        return departmentName;
    }
    /**
     * @param departmentName The departmentName to set.
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
     * @return Returns the isTerminated.
     */
    public char getIsTerminated() {
        return isTerminated;
    }
    /**
     * @param isTerminated The isTerminated to set.
     */
    public void setIsTerminated(char isTerminated) {
        this.isTerminated = isTerminated;
    }
    /**
     * @return Returns the opinions.
     */
    public Set getOpinions() {
        return opinions;
    }
    /**
     * @param opinions The opinions to set.
     */
    public void setOpinions(Set opinions) {
        this.opinions = opinions;
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
     * @return Returns the reason.
     */
    public String getReason() {
        return reason;
    }
    /**
     * @param reason The reason to set.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    /**
     * @return Returns the remark.
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark The remark to set.
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return Returns the terminateTime.
     */
    public java.sql.Timestamp getTerminateTime() {
        return terminateTime;
    }
    /**
     * @param terminateTime The terminateTime to set.
     */
    public void setTerminateTime(java.sql.Timestamp terminateTime) {
        this.terminateTime = terminateTime;
    }
    /**
     * @return Returns the workflowInstanceId.
     */
    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }
    /**
     * @param workflowInstanceId The workflowInstanceId to set.
     */
    public void setWorkflowInstanceId(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }
    /**
     * @return Returns the agents.
     */
    public RecordVisitorList getAgents() {
        return agents;
    }
    /**
     * @param agents The agents to set.
     */
    public void setAgents(RecordVisitorList agents) {
        this.agents = agents;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return Returns the dayCount.
     */
    public double getDayCount() {
        return dayCount;
    }
    /**
     * @param dayCount The dayCount to set.
     */
    public void setDayCount(double dayCount) {
        this.dayCount = dayCount;
    }
}