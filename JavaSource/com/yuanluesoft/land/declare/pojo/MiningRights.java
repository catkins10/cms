package com.yuanluesoft.land.declare.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 采矿权
 * @author kangshiwei
 *
 */
public class MiningRights extends Record {
	private String applicant; //申请人
	private String subject; //标题
	private String mineName; //矿山名称
	private String address; //地址
	private String licenseNum; //许可证号
	private String mainMinerals; //开采主矿种
	private Date validFrom; //有效期起
	private Date validEnd; //有效期止
	private double miningArea; //矿区面积
	private double caps; //采深上限
	private double lower; //采深下限
	private String projectType; //项目类型
	private String coordinate; //坐标
	private Date approvalTime; //审批时间
	private Timestamp created; //创建时间
	
	/**
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address 要设置的 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return applicant
	 */
	public String getApplicant() {
		return applicant;
	}
	/**
	 * @param applicant 要设置的 applicant
	 */
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	/**
	 * @return caps
	 */
	public double getCaps() {
		return caps;
	}
	/**
	 * @param caps 要设置的 caps
	 */
	public void setCaps(double caps) {
		this.caps = caps;
	}
	/**
	 * @return coordinate
	 */
	public String getCoordinate() {
		return coordinate;
	}
	/**
	 * @param coordinate 要设置的 coordinate
	 */
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	/**
	 * @return licenseNum
	 */
	public String getLicenseNum() {
		return licenseNum;
	}
	/**
	 * @param licenseNum 要设置的 licenseNum
	 */
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	/**
	 * @return lower
	 */
	public double getLower() {
		return lower;
	}
	/**
	 * @param lower 要设置的 lower
	 */
	public void setLower(double lower) {
		this.lower = lower;
	}
	/**
	 * @return mainMinerals
	 */
	public String getMainMinerals() {
		return mainMinerals;
	}
	/**
	 * @param mainMinerals 要设置的 mainMinerals
	 */
	public void setMainMinerals(String mainMinerals) {
		this.mainMinerals = mainMinerals;
	}
	/**
	 * @return mineName
	 */
	public String getMineName() {
		return mineName;
	}
	/**
	 * @param mineName 要设置的 mineName
	 */
	public void setMineName(String mineName) {
		this.mineName = mineName;
	}
	/**
	 * @return miningArea
	 */
	public double getMiningArea() {
		return miningArea;
	}
	/**
	 * @param miningArea 要设置的 miningArea
	 */
	public void setMiningArea(double miningArea) {
		this.miningArea = miningArea;
	}
	/**
	 * @return projectType
	 */
	public String getProjectType() {
		return projectType;
	}
	/**
	 * @param projectType 要设置的 projectType
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
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
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject 要设置的 subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return validEnd
	 */
	public Date getValidEnd() {
		return validEnd;
	}
	/**
	 * @param validEnd 要设置的 validEnd
	 */
	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}
	/**
	 * @return validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}
	/**
	 * @param validFrom 要设置的 validFrom
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
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
