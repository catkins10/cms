package com.yuanluesoft.jeaf.dataimport.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class DataImportParameter implements Serializable {
	private String jdbcDriver; //sun.jdbc.odbc.JdbcOdbcDriver
	private String jdbcURL; //jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=C:/Documents and Settings/linchuan.KDSOFT-HP/桌面/#goback2006056.mdb
	private String jdbcUserName;
	private String jdbcPassword;
	private String sourceSiteURL; //源站点URL
	private String templatePath; //模板路径,新的对象序列号：FE DE 84 69 C0 16 BC 28, 原来的是：E0 0A 0A 0A 8E A0 74 53
	private String templateUrl = "/cms/templates/"; //模板在旧网站中的URL,默认/cms/templates/
	private int maxImport = 100000000; //最大导入的记录数
	
	/**
	 * @return the jdbcDriver
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	/**
	 * @return the jdbcPassword
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	/**
	 * @param jdbcPassword the jdbcPassword to set
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	/**
	 * @return the jdbcURL
	 */
	public String getJdbcURL() {
		return jdbcURL;
	}
	/**
	 * @param jdbcURL the jdbcURL to set
	 */
	public void setJdbcURL(String jdbcURL) {
		this.jdbcURL = jdbcURL;
	}
	/**
	 * @return the jdbcUserName
	 */
	public String getJdbcUserName() {
		return jdbcUserName;
	}
	/**
	 * @param jdbcUserName the jdbcUserName to set
	 */
	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
	}
	/**
	 * @return the maxImport
	 */
	public int getMaxImport() {
		return maxImport;
	}
	/**
	 * @param maxImport the maxImport to set
	 */
	public void setMaxImport(int maxImport) {
		this.maxImport = maxImport;
	}
	/**
	 * @return the sourceSiteURL
	 */
	public String getSourceSiteURL() {
		return sourceSiteURL;
	}
	/**
	 * @param sourceSiteURL the sourceSiteURL to set
	 */
	public void setSourceSiteURL(String sourceSiteURL) {
		this.sourceSiteURL = sourceSiteURL + (sourceSiteURL.endsWith("/") ? "" : "/");
	}
	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}
	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	/**
	 * @return the templateUrl
	 */
	public String getTemplateUrl() {
		return templateUrl;
	}
	/**
	 * @param templateUrl the templateUrl to set
	 */
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
}