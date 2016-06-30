package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Date;

/**
 * 参建单位(keyproject_unit)
 * @author linchuan
 *
 */
public class KeyProjectUnit extends KeyProjectComponent {
	private String type; //单位类型,施工单位、监理单位、设计单位
	private String name; //参建单位名称
	private String qualificationLevel; //资质等级
	private String partName; //承担合同段名称
	private double contractAmount; //合同金额（万元）
	private Date contractBegin; //合同开始时间
	private Date contractEnd; //合同结束时间
	private String projectManager; //项目经理（总监/设总）
	private String tel; //联系电话
	private String unitTel; //单位总部电话
	private String unitPostcode; //参建单位总部邮编
	private String unitAddress; //参建单位总部地址
	private String workingPostcode; //参建单位现场邮编
	private String workingAddress; //参建单位现场地址
	private String reporter; //填报人
	private Date reportDate; //填报时间
	
	/**
	 * @return the contractAmount
	 */
	public double getContractAmount() {
		return contractAmount;
	}
	/**
	 * @param contractAmount the contractAmount to set
	 */
	public void setContractAmount(double contractAmount) {
		this.contractAmount = contractAmount;
	}
	/**
	 * @return the contractBegin
	 */
	public Date getContractBegin() {
		return contractBegin;
	}
	/**
	 * @param contractBegin the contractBegin to set
	 */
	public void setContractBegin(Date contractBegin) {
		this.contractBegin = contractBegin;
	}
	/**
	 * @return the contractEnd
	 */
	public Date getContractEnd() {
		return contractEnd;
	}
	/**
	 * @param contractEnd the contractEnd to set
	 */
	public void setContractEnd(Date contractEnd) {
		this.contractEnd = contractEnd;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}
	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}
	/**
	 * @return the projectManager
	 */
	public String getProjectManager() {
		return projectManager;
	}
	/**
	 * @param projectManager the projectManager to set
	 */
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	/**
	 * @return the qualificationLevel
	 */
	public String getQualificationLevel() {
		return qualificationLevel;
	}
	/**
	 * @param qualificationLevel the qualificationLevel to set
	 */
	public void setQualificationLevel(String qualificationLevel) {
		this.qualificationLevel = qualificationLevel;
	}
	/**
	 * @return the reportDate
	 */
	public Date getReportDate() {
		return reportDate;
	}
	/**
	 * @param reportDate the reportDate to set
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * @return the reporter
	 */
	public String getReporter() {
		return reporter;
	}
	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the unitAddress
	 */
	public String getUnitAddress() {
		return unitAddress;
	}
	/**
	 * @param unitAddress the unitAddress to set
	 */
	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}
	/**
	 * @return the unitPostcode
	 */
	public String getUnitPostcode() {
		return unitPostcode;
	}
	/**
	 * @param unitPostcode the unitPostcode to set
	 */
	public void setUnitPostcode(String unitPostcode) {
		this.unitPostcode = unitPostcode;
	}
	/**
	 * @return the unitTel
	 */
	public String getUnitTel() {
		return unitTel;
	}
	/**
	 * @param unitTel the unitTel to set
	 */
	public void setUnitTel(String unitTel) {
		this.unitTel = unitTel;
	}
	/**
	 * @return the workingAddress
	 */
	public String getWorkingAddress() {
		return workingAddress;
	}
	/**
	 * @param workingAddress the workingAddress to set
	 */
	public void setWorkingAddress(String workingAddress) {
		this.workingAddress = workingAddress;
	}
	/**
	 * @return the workingPostcode
	 */
	public String getWorkingPostcode() {
		return workingPostcode;
	}
	/**
	 * @param workingPostcode the workingPostcode to set
	 */
	public void setWorkingPostcode(String workingPostcode) {
		this.workingPostcode = workingPostcode;
	}
}