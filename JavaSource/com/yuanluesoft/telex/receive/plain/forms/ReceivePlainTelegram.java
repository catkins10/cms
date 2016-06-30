package com.yuanluesoft.telex.receive.plain.forms;

import java.sql.Timestamp;

import com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram;

/**
 * 
 * @author linchuan
 *
 */
public class ReceivePlainTelegram extends ReceiveTelegram {
	private long signId; //签收记录ID
	private long signPersonId; //签收用户ID
	private String signPersonName; //签收用户名
	private boolean isAgentSign; //是否代签收
	private Timestamp signTime; //签收时间
	
	/**
	 * @return the isAgentSign
	 */
	public boolean isAgentSign() {
		return isAgentSign;
	}
	/**
	 * @param isAgentSign the isAgentSign to set
	 */
	public void setAgentSign(boolean isAgentSign) {
		this.isAgentSign = isAgentSign;
	}
	/**
	 * @return the signId
	 */
	public long getSignId() {
		return signId;
	}
	/**
	 * @param signId the signId to set
	 */
	public void setSignId(long signId) {
		this.signId = signId;
	}
	/**
	 * @return the signPersonId
	 */
	public long getSignPersonId() {
		return signPersonId;
	}
	/**
	 * @param signPersonId the signPersonId to set
	 */
	public void setSignPersonId(long signPersonId) {
		this.signPersonId = signPersonId;
	}
	/**
	 * @return the signPersonName
	 */
	public String getSignPersonName() {
		return signPersonName;
	}
	/**
	 * @param signPersonName the signPersonName to set
	 */
	public void setSignPersonName(String signPersonName) {
		this.signPersonName = signPersonName;
	}
	/**
	 * @return the signTime
	 */
	public Timestamp getSignTime() {
		return signTime;
	}
	/**
	 * @param signTime the signTime to set
	 */
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
}