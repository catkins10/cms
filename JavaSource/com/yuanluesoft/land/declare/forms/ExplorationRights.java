package com.yuanluesoft.land.declare.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author kangshiwei
 *
 */
public class ExplorationRights extends ActionForm {
	private String licenseNum; //许可证号
	private String projectName; //项目名称
	private Date issueDate; //发证日期
	private String applicant; //申请人
	private String investigationUnit; //勘察单位
	private String minerals;//勘察矿种
	private String location; //地理位置
	private Date validFrom; //有效期起
	private Date validEnd; //有效期止
	private double area; //总面积
	private String projectType; //项目类型
	private String coordinate; //坐标
	private Date approvalTime; //审批时间
	private Timestamp created; //创建时间
	
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
	 * @return area
	 */
	public double getArea() {
		return area;
	}
	/**
	 * @param area 要设置的 area
	 */
	public void setArea(double area) {
		this.area = area;
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
	 * @return issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}
	/**
	 * @param issueDate 要设置的 issueDate
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
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
	 * @return location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location 要设置的 location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName 要设置的 projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	 * @return minerals
	 */
	public String getMinerals() {
		return minerals;
	}
	/**
	 * @param minerals 要设置的 minerals
	 */
	public void setMinerals(String minerals) {
		this.minerals = minerals;
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
