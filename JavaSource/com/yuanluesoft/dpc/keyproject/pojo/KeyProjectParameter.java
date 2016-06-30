package com.yuanluesoft.dpc.keyproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 重点项目:参数配置(keyproject_parameter)
 * @author linchuan
 *
 */
public class KeyProjectParameter extends Record {
	private String projectStatus; //项目状态,前期、在建、竣工、其他
	private String projectLevel; //项目级别,省级重点、市级重点、县级重点
	private String projectClassify; //项目类别,储备、在建和续建
	private String area; //所属区域
	private String projectCategory; //项目分类,考核类、跟踪服务类
	private String investmentSubject; //投资主体,国有、国有控股与外资合股、国有控股与民营合股、民营、民营控股与国有合资、外资独资、外资控股与国有合资、外资控股与民营合资、其他
	private String managementUnit; //项目管理部门
	private String constructionType; //建设性质,新建、扩建
	private String developmentArea; //开发区
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the constructionType
	 */
	public String getConstructionType() {
		return constructionType;
	}
	/**
	 * @param constructionType the constructionType to set
	 */
	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}
	/**
	 * @return the investmentSubject
	 */
	public String getInvestmentSubject() {
		return investmentSubject;
	}
	/**
	 * @param investmentSubject the investmentSubject to set
	 */
	public void setInvestmentSubject(String investmentSubject) {
		this.investmentSubject = investmentSubject;
	}
	/**
	 * @return the managementUnit
	 */
	public String getManagementUnit() {
		return managementUnit;
	}
	/**
	 * @param managementUnit the managementUnit to set
	 */
	public void setManagementUnit(String managementUnit) {
		this.managementUnit = managementUnit;
	}
	/**
	 * @return the projectCategory
	 */
	public String getProjectCategory() {
		return projectCategory;
	}
	/**
	 * @param projectCategory the projectCategory to set
	 */
	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	/**
	 * @return the projectClassify
	 */
	public String getProjectClassify() {
		return projectClassify;
	}
	/**
	 * @param projectClassify the projectClassify to set
	 */
	public void setProjectClassify(String projectClassify) {
		this.projectClassify = projectClassify;
	}
	/**
	 * @return the projectLevel
	 */
	public String getProjectLevel() {
		return projectLevel;
	}
	/**
	 * @param projectLevel the projectLevel to set
	 */
	public void setProjectLevel(String projectLevel) {
		this.projectLevel = projectLevel;
	}
	/**
	 * @return the projectStatus
	 */
	public String getProjectStatus() {
		return projectStatus;
	}
	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	/**
	 * @return the developmentArea
	 */
	public String getDevelopmentArea() {
		return developmentArea;
	}
	/**
	 * @param developmentArea the developmentArea to set
	 */
	public void setDevelopmentArea(String developmentArea) {
		this.developmentArea = developmentArea;
	}
}
