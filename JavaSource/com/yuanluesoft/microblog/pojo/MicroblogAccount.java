package com.yuanluesoft.microblog.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 微博:帐号配置(microblog_account)
 * @author linchuan
 *
 */
public class MicroblogAccount extends Record {
	private long unitId; //单位ID
	private String platform; //微博平台,新浪微博,腾讯微博,搜狐微博
	private String siteUrl; //网址
	private String name; //微博名称
	private String userName; //微博帐号
	private String password; //微博密码
	private Set parameters; //参数
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public String getParameterValue(String parameterName) {
		MicroblogAccountParameter parameter = (MicroblogAccountParameter)ListUtils.findObjectByProperty(parameters, "parameterName", parameterName);
		return parameter==null ? null : parameter.getParameterValue();
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
	 * @return the parameters
	 */
	public Set getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Set parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	/**
	 * @param siteUrl the siteUrl to set
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
}