/*
 * Created on 2006-5-31
 *
 */
package com.yuanluesoft.j2oa.leave.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 请假条(leave_leave)
 * @author linchuan
 *
 */
public class Leave extends WorkflowData {
	private long personId; //请假人ID
	private String personName; //请假人
	private long departmentId; //部门ID
	private String departmentName; //部门名称
	private String type; //请假类别
	private String reason; //请假事由
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private double dayCount; //请假天数
	private char isTerminated = '0'; //是否已销假
	private Timestamp terminateTime; //销假时间
	private char approvalPass = '0'; //是否审批通过
	private String remark; //备注
	private String workflowInstanceId; //工作流实例ID
	
	/**
	 * 获取实际开始时间
	 * @return
	 */
	public Timestamp getActualBeginTime() {
		return beginTime;
	}
	
	/**
	 * 获取实际结束时间
	 * @return
	 */
	public Timestamp getActualEndTime() {
		return terminateTime==null || terminateTime.after(endTime) ? endTime : terminateTime;
	}
	
    /**
     * @return Returns the reason.
     */
    public java.lang.String getReason() {
        return reason;
    }
    /**
     * @param reason The reason to set.
     */
    public void setReason(java.lang.String reason) {
        this.reason = reason;
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
     * @return Returns the remark.
     */
    public java.lang.String getRemark() {
        return remark;
    }
    /**
     * @param remark The remark to set.
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
    /**
     * @return Returns the workflowInstanceId.
     */
    public java.lang.String getWorkflowInstanceId() {
        return workflowInstanceId;
    }
    /**
     * @param workflowInstanceId The workflowInstanceId to set.
     */
    public void setWorkflowInstanceId(java.lang.String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
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
    public java.lang.String getPersonName() {
        return personName;
    }
    /**
     * @param personName The personName to set.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
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
    public java.lang.String getDepartmentName() {
        return departmentName;
    }
    /**
     * @param departmentName The departmentName to set.
     */
    public void setDepartmentName(java.lang.String departmentName) {
        this.departmentName = departmentName;
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
     * @return Returns the type.
     */
    public java.lang.String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(java.lang.String type) {
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
	/**
	 * @return the approvalPass
	 */
	public char getApprovalPass() {
		return approvalPass;
	}
	/**
	 * @param approvalPass the approvalPass to set
	 */
	public void setApprovalPass(char approvalPass) {
		this.approvalPass = approvalPass;
	}
}
