package com.yuanluesoft.fet.tradestat.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 区县信息(fet_county)
 * @author linchuan
 *
 */
public class FetCounty extends Record {
	private String code; //编码
	private String name; //名称
	private String loginName; //帐号
	private String password; //密码
	
	private Set tradeQueryPermits;
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	 * @return the tradeQueryPermits
	 */
	public Set getTradeQueryPermits() {
		return tradeQueryPermits;
	}
	/**
	 * @param tradeQueryPermits the tradeQueryPermits to set
	 */
	public void setTradeQueryPermits(Set tradeQueryPermits) {
		this.tradeQueryPermits = tradeQueryPermits;
	}

}
