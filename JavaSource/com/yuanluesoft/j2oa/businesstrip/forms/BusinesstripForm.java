package com.yuanluesoft.j2oa.businesstrip.forms;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class BusinesstripForm extends WorkflowForm {
    private java.lang.String address;
	private java.lang.String reason;
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	private long proposerId;
	private String proposerName;
	private long projectId;
	private java.lang.String vehicle;
	private long departmentId;
	private java.lang.String departmentName;
	private String remark;
	private Set opinions;
	
	private List addresses; //出差地点列表 
	private RecordVisitorList tripPerson = new RecordVisitorList(); //出差人
	
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
    public String getProposerName() {
        return proposerName;
    }
    /**
     * @param proposerName The proposerName to set.
     */
    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }
    /**
     * @return Returns the projectId.
     */
    public long getProjectId() {
        return projectId;
    }
    /**
     * @param projectId The projectId to set.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
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
     * @return Returns the tripPerson.
     */
    public RecordVisitorList getTripPerson() {
        return tripPerson;
    }
    /**
     * @param tripPerson The tripPerson to set.
     */
    public void setTripPerson(RecordVisitorList tripPerson) {
        this.tripPerson = tripPerson;
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
     * @return Returns the addresses.
     */
    public List getAddresses() {
        return addresses;
    }
    /**
     * @param addresses The addresses to set.
     */
    public void setAddresses(List addresses) {
        this.addresses = addresses;
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