package com.yuanluesoft.jeaf.system.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 系统注册(system_sn)
 * @author linchuan
 *
 */
public class SystemSN extends Record {
	private String agnomen; //密钥
	private String code; //注册码
	private String sn; //序列号
	
	/**
	 * @return the agnomen
	 */
	public String getAgnomen() {
		return agnomen;
	}
	/**
	 * @param agnomen the agnomen to set
	 */
	public void setAgnomen(String agnomen) {
		this.agnomen = agnomen;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
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
}
