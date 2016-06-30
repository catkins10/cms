package com.yuanluesoft.cms.inquiry.model;

/**
 * 投票匹配情况
 * @author linchuan
 *
 */
public class InquiryMatch {
	private String personName; //用户名
	private int matchNumber; //匹配数量
	private double matchRate; //匹配率
	
	/**
	 * @return the matchRate
	 */
	public double getMatchRate() {
		return matchRate;
	}
	/**
	 * @param matchRate the matchRate to set
	 */
	public void setMatchRate(double matchRate) {
		this.matchRate = matchRate;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	/**
	 * @return the matchNumber
	 */
	public int getMatchNumber() {
		return matchNumber;
	}
	/**
	 * @param matchNumber the matchNumber to set
	 */
	public void setMatchNumber(int matchNumber) {
		this.matchNumber = matchNumber;
	}
}