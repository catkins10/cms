package com.yuanluesoft.jeaf.fingerprint.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 指纹验证:指纹比对(fingerprint_match)
 * @author linchuan
 *
 */
public class FingerprintMatch extends Record {
	private String ip; //IP
	private long personId; //用户ID
	private int matchTimes; //比对成功次数
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the matchTimes
	 */
	public int getMatchTimes() {
		return matchTimes;
	}
	/**
	 * @param matchTimes the matchTimes to set
	 */
	public void setMatchTimes(int matchTimes) {
		this.matchTimes = matchTimes;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
}
