/*
 * Created on 2006-5-31
 *
 */
package com.yuanluesoft.j2oa.businesstrip.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 出差申请单(businesstrip_businesstrip)
 * @author linchuan
 *
 */
public class Businesstrip extends WorkflowData {
	private long id; //ID
	private long projectId; //项目ID
	private long proposerId; //申请人ID
	private String proposerName; //申请人
	private long departmentId; //部门ID
	private String departmentName; //部门名称
	private String address; //出差地点
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private String reason; //出差目的
	private String vehicle; //使用的交通工具
	private char approvalPass = '0'; //是否审批通过
	private String remark; //备注
	private String workflowInstanceId; //工作流实例ID
	
    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }
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
     * @return Returns the vehicle.
     */
    public java.lang.String getVehicle() {
        return vehicle;
    }
    /**
     * @param vehicle The vehicle to set.
     */
    public void setVehicle(java.lang.String vehicle) {
        this.vehicle = vehicle;
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
     * @return Returns the proposerId.
     */
    public long getProposerId() {
        return proposerId;
    }
    /**
     * @param proposerId The proposerId to set.
     */
    public void setProposerId(long proposerId) {
        this.proposerId = proposerId;
    }
    /**
     * @return Returns the proposerName.
     */
    public java.lang.String getProposerName() {
        return proposerName;
    }
    /**
     * @param proposerName The proposerName to set.
     */
    public void setProposerName(java.lang.String proposerName) {
        this.proposerName = proposerName;
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
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
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
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
