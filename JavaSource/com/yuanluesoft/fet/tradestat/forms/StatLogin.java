package com.yuanluesoft.fet.tradestat.forms;

import com.yuanluesoft.jeaf.sso.forms.LoginForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class StatLogin extends LoginForm {
	private String userType; //用户类型,企业|company/区县|county/开发区|developmentArea
	private String companyCode;
	private String county;
	private String developmentArea;
	
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
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
	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}
	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
}