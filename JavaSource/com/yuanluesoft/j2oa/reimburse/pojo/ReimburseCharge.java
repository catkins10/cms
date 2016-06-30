/*
 * Created on 2006-6-15
 *
 */
package com.yuanluesoft.j2oa.reimburse.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 费用清单(reimburse_charge)
 * @author linchuan
 *
 */
public class ReimburseCharge extends Record {
	private long reimburseId; //报销单ID
	private long projectId; //所属项目ID
	private String projectName; //所属项目名称
	private String chargeCategory; //费用类别
	private String chargeStandard; //费用标准
	private Timestamp time; //费用产生时间
	private double money; //金额
	private String purpose; //用途
	private String remark; //备注
	
    /**
     * @return Returns the chargeCategory.
     */
    public java.lang.String getChargeCategory() {
        return chargeCategory;
    }
    /**
     * @param chargeCategory The chargeCategory to set.
     */
    public void setChargeCategory(java.lang.String chargeCategory) {
        this.chargeCategory = chargeCategory;
    }
    /**
     * @return Returns the time.
     */
    public java.sql.Timestamp getTime() {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(java.sql.Timestamp time) {
        this.time = time;
    }
    /**
     * @return Returns the money.
     */
    public double getMoney() {
        return money;
    }
    /**
     * @param money The money to set.
     */
    public void setMoney(double money) {
        this.money = money;
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
     * @return Returns the projectName.
     */
    public java.lang.String getProjectName() {
        return projectName;
    }
    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }
    /**
     * @return Returns the purpose.
     */
    public java.lang.String getPurpose() {
        return purpose;
    }
    /**
     * @param purpose The purpose to set.
     */
    public void setPurpose(java.lang.String purpose) {
        this.purpose = purpose;
    }
    /**
     * @return Returns the reimburseId.
     */
    public long getReimburseId() {
        return reimburseId;
    }
    /**
     * @param reimburseId The reimburseId to set.
     */
    public void setReimburseId(long reimburseId) {
        this.reimburseId = reimburseId;
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
     * @return Returns the chargeStandard.
     */
    public java.lang.String getChargeStandard() {
        return chargeStandard;
    }
    /**
     * @param chargeStandard The chargeStandard to set.
     */
    public void setChargeStandard(java.lang.String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }
}
