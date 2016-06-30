package com.yuanluesoft.chd.evaluation.pojo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 评价体系目录:发电企业(chd_evaluation_plant)
 * @author linchuan
 *
 */
public class ChdEvaluationPlant extends ChdEvaluationDirectory {
	private long orgId; //组织机构ID
	private String address; //地址
	private String postalCode; //邮政编码
	private String installedCapacity; //全厂总装机容量构成,MW
	private Date constructionDate; //建厂时间
	private String type; //企业类型,水电、火电、风电
	private String maxLevel; //获得最高星级档次
	private Date maxLevelDate; //获得最高星级时间
	private double saleProceeds; //企业年销售收入,万元
	private int employeeNumber; //年末在册职工人数
	private String contactDepartment; //联系部门
	private String contactPerson; //联系人
	private String tel; //电话
	private String mobile; //手机
	private Set honors; //企业荣誉列表
	private Set objectives; //年度目标
	
	/**
	 * 本年度目标
	 * @return
	 */
	public ChdEvaluationObjective getCurrentYearObjective() {
		return (ChdEvaluationObjective)ListUtils.findObjectByProperty(objectives, "objectiveYear", new Integer(DateTimeUtils.getYear(DateTimeUtils.date())));
	}
	
	/**
	 * 获取创星历程
	 * @return
	 */
	public List getObjectiveResults() {
		if(objectives==null) {
			return null;
		}
		List objectiveResults = new ArrayList();
		for(Iterator iterator = objectives.iterator(); iterator.hasNext();) {
			ChdEvaluationObjective objective = (ChdEvaluationObjective)iterator.next();
			if(objective.getResult()!=null && !objective.getResult().isEmpty()) {
				objectiveResults.add(objective);
			}
		}
		return objectiveResults;
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the constructionDate
	 */
	public Date getConstructionDate() {
		return constructionDate;
	}
	/**
	 * @param constructionDate the constructionDate to set
	 */
	public void setConstructionDate(Date constructionDate) {
		this.constructionDate = constructionDate;
	}
	/**
	 * @return the contactDepartment
	 */
	public String getContactDepartment() {
		return contactDepartment;
	}
	/**
	 * @param contactDepartment the contactDepartment to set
	 */
	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}
	/**
	 * @return the contactPerson
	 */
	public String getContactPerson() {
		return contactPerson;
	}
	/**
	 * @param contactPerson the contactPerson to set
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	/**
	 * @return the employeeNumber
	 */
	public int getEmployeeNumber() {
		return employeeNumber;
	}
	/**
	 * @param employeeNumber the employeeNumber to set
	 */
	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	/**
	 * @return the installedCapacity
	 */
	public String getInstalledCapacity() {
		return installedCapacity;
	}
	/**
	 * @param installedCapacity the installedCapacity to set
	 */
	public void setInstalledCapacity(String installedCapacity) {
		this.installedCapacity = installedCapacity;
	}
	/**
	 * @return the maxLevel
	 */
	public String getMaxLevel() {
		return maxLevel;
	}
	/**
	 * @param maxLevel the maxLevel to set
	 */
	public void setMaxLevel(String maxLevel) {
		this.maxLevel = maxLevel;
	}
	/**
	 * @return the maxLevelDate
	 */
	public Date getMaxLevelDate() {
		return maxLevelDate;
	}
	/**
	 * @param maxLevelDate the maxLevelDate to set
	 */
	public void setMaxLevelDate(Date maxLevelDate) {
		this.maxLevelDate = maxLevelDate;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the saleProceeds
	 */
	public double getSaleProceeds() {
		return saleProceeds;
	}
	/**
	 * @param saleProceeds the saleProceeds to set
	 */
	public void setSaleProceeds(double saleProceeds) {
		this.saleProceeds = saleProceeds;
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
	 * @return the honors
	 */
	public Set getHonors() {
		return honors;
	}
	/**
	 * @param honors the honors to set
	 */
	public void setHonors(Set honors) {
		this.honors = honors;
	}
	/**
	 * @return the objectives
	 */
	public Set getObjectives() {
		return objectives;
	}
	/**
	 * @param objectives the objectives to set
	 */
	public void setObjectives(Set objectives) {
		this.objectives = objectives;
	}
}