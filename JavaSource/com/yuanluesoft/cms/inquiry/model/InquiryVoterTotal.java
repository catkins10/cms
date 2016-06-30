package com.yuanluesoft.cms.inquiry.model;

/**
 * 投票参与情况统计
 * @author linchuan
 *
 */
public class InquiryVoterTotal {
	private int votePersonNumber; //投票参与人员数量
	private int personNumber; //总用户数量
	private double votePercent; //投票参与率
	private String votePersonNames; //参与投票人员,以","分隔
	private String notVotePersonNames; //未参与投票人员,以","分隔
	
	/**
	 * @return the notVotePersonNames
	 */
	public String getNotVotePersonNames() {
		return notVotePersonNames;
	}
	/**
	 * @param notVotePersonNames the notVotePersonNames to set
	 */
	public void setNotVotePersonNames(String notVotePersonNames) {
		this.notVotePersonNames = notVotePersonNames;
	}
	/**
	 * @return the personNumber
	 */
	public int getPersonNumber() {
		return personNumber;
	}
	/**
	 * @param personNumber the personNumber to set
	 */
	public void setPersonNumber(int personNumber) {
		this.personNumber = personNumber;
	}
	/**
	 * @return the votePercent
	 */
	public double getVotePercent() {
		return votePercent;
	}
	/**
	 * @param votePercent the votePercent to set
	 */
	public void setVotePercent(double votePercent) {
		this.votePercent = votePercent;
	}
	/**
	 * @return the votePersonNames
	 */
	public String getVotePersonNames() {
		return votePersonNames;
	}
	/**
	 * @param votePersonNames the votePersonNames to set
	 */
	public void setVotePersonNames(String votePersonNames) {
		this.votePersonNames = votePersonNames;
	}
	/**
	 * @return the votePersonNumber
	 */
	public int getVotePersonNumber() {
		return votePersonNumber;
	}
	/**
	 * @param votePersonNumber the votePersonNumber to set
	 */
	public void setVotePersonNumber(int votePersonNumber) {
		this.votePersonNumber = votePersonNumber;
	}
}