package com.yuanluesoft.exchange.client.packet.data;

import com.yuanluesoft.exchange.client.packet.ExchangePacket;

/**
 * 文件校验结果
 * @author linchuan
 *
 */
public class FileValidateResult extends ExchangePacket {
	private boolean validatePass; //是否通过

	public FileValidateResult(boolean validatePass) {
		super();
		this.validatePass = validatePass;
	}

	/**
	 * @return the validatePass
	 */
	public boolean isValidatePass() {
		return validatePass;
	}

	/**
	 * @param validatePass the validatePass to set
	 */
	public void setValidatePass(boolean validatePass) {
		this.validatePass = validatePass;
	}
}