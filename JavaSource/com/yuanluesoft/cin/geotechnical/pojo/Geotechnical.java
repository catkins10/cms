package com.yuanluesoft.cin.geotechnical.pojo;

import java.sql.Date;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 岩土工程勘察项目(cin_geotechnical)
 * @author linchuan
 *
 */
public class Geotechnical extends PublicService {
	private String projectName; //项目名称
	private String constructionUnit; //建设单位
	private String surveyUnit; //勘察单位
	private String constructionPlace; //建设地点
	private Date approachTime; //拟进场时间
	
	/**
	 * @return the approachTime
	 */
	public Date getApproachTime() {
		return approachTime;
	}
	/**
	 * @param approachTime the approachTime to set
	 */
	public void setApproachTime(Date approachTime) {
		this.approachTime = approachTime;
	}
	/**
	 * @return the constructionPlace
	 */
	public String getConstructionPlace() {
		return constructionPlace;
	}
	/**
	 * @param constructionPlace the constructionPlace to set
	 */
	public void setConstructionPlace(String constructionPlace) {
		this.constructionPlace = constructionPlace;
	}
	/**
	 * @return the constructionUnit
	 */
	public String getConstructionUnit() {
		return constructionUnit;
	}
	/**
	 * @param constructionUnit the constructionUnit to set
	 */
	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the surveyUnit
	 */
	public String getSurveyUnit() {
		return surveyUnit;
	}
	/**
	 * @param surveyUnit the surveyUnit to set
	 */
	public void setSurveyUnit(String surveyUnit) {
		this.surveyUnit = surveyUnit;
	}
}
