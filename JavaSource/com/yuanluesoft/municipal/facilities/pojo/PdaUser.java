package com.yuanluesoft.municipal.facilities.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * PDA使用者(pda_user)
 * @author linchuan
 *
 */
public class PdaUser extends Record {
	private String name; //使用者姓名
	private String code; //工号
	private String pdaNumber; //PDA号码
	private long orgId; //所在组织机构ID
	private String orgName; //所在组织机构名称
	
	//拓展的属性
	private boolean login; //是否已经登录,调用PDAService获取
	private boolean gpsLogin; //是否已经登录GPS,调用PDAService获取
	
	/**
	 * 是否已经登录
	 * @return
	 */
	public String getLoginTitle() {
		return login ? "是" : "否";
	}
	
	/**
	 * 是否已经登录图标
	 * @return
	 */
	public String getLoginImage() {
		return "icon/" + (login ? "login.gif" : "logout.gif");
	}
	
	/**
	 * 是否已经登录
	 * @return
	 */
	public String getGPSLoginTitle() {
		return gpsLogin ? "是" : "否";
	}
	
	/**
	 * 是否已经登录图标
	 * @return
	 */
	public String getGPSLoginImage() {
		return "icon/" + (gpsLogin ? "gpsLogin.gif" : "gpsLogout.gif");
	}

	/**
	 * 获取操作列
	 * @return
	 */
	public String getActions() {
		return "<a href=\"javascript:locatePdaUser('" + getCode() + "')\">定位</a> <a href=\"javascript:trackPdaUser('" + getCode() + "')\">跟踪</a> <a href=\"javascript:showPdaUserHistory('" + getCode() + "')\">历史轨迹</a>";
	}

	/**
	 * @return the gpsLogin
	 */
	public boolean isGpsLogin() {
		return gpsLogin;
	}

	/**
	 * @param gpsLogin the gpsLogin to set
	 */
	public void setGpsLogin(boolean gpsLogin) {
		this.gpsLogin = gpsLogin;
	}

	/**
	 * @return the login
	 */
	public boolean isLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(boolean login) {
		this.login = login;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the pdaNumber
	 */
	public String getPdaNumber() {
		return pdaNumber;
	}
	/**
	 * @param pdaNumber the pdaNumber to set
	 */
	public void setPdaNumber(String pdaNumber) {
		this.pdaNumber = pdaNumber;
	}
}
