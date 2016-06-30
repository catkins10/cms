package com.yuanluesoft.land.declare.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author kangshiwei
 *
 */
public class DisastersUnit extends Record {
	private String investigationUnit; //勘察单位
	private String qualificationType; //资质类型
	private String qualificationLevel; //资质等级
	private Date approvalTime; //审批时间
	private Timestamp created; //创建时间
	
	/**
	 * @return approvalTime
	 */
	public Date getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime 要设置的 approvalTime
	 */
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return investigationUnit
	 */
	public String getInvestigationUnit() {
		return investigationUnit;
	}
	/**
	 * @param investigationUnit 要设置的 investigationUnit
	 */
	public void setInvestigationUnit(String investigationUnit) {
		this.investigationUnit = investigationUnit;
	}
	/**
	 * @return qualificationLevel
	 */
	public String getQualificationLevel() {
		return qualificationLevel;
	}
	/**
	 * @param qualificationLevel 要设置的 qualificationLevel
	 */
	public void setQualificationLevel(String qualificationLevel) {
		this.qualificationLevel = qualificationLevel;
	}
	/**
	 * @return qualificationType
	 */
	public String getQualificationType() {
		return qualificationType;
	}
	/**
	 * @param qualificationType 要设置的 qualificationType
	 */
	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
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

}
