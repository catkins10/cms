package com.yuanluesoft.aic.barcode.model;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 厂商会话信息
 * @author yuanlue
 *
 */
public class BarcodeCompanySessionInfo extends SessionInfo {
	private String code; //厂商识别码

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