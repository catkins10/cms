package com.yuanluesoft.fet.tradestat.model;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class FetSessionInfo extends SessionInfo {
	private String code; //企业、区县或者开发区编码

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
