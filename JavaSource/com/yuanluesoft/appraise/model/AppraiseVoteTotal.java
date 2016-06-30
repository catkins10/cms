package com.yuanluesoft.appraise.model;

/**
 * 投票统计
 * @author linchuan
 *
 */
public class AppraiseVoteTotal {
	private int voteTimes; //总投票人次
	private int voteTotal; //总投票数
	private int smsVoteTotal; //短信投票数
	private int internetVoteTotal; //网络投票数
	
	/**
	 * @return the internetVoteTotal
	 */
	public int getInternetVoteTotal() {
		return internetVoteTotal;
	}
	/**
	 * @param internetVoteTotal the internetVoteTotal to set
	 */
	public void setInternetVoteTotal(int internetVoteTotal) {
		this.internetVoteTotal = internetVoteTotal;
	}
	/**
	 * @return the smsVoteTotal
	 */
	public int getSmsVoteTotal() {
		return smsVoteTotal;
	}
	/**
	 * @param smsVoteTotal the smsVoteTotal to set
	 */
	public void setSmsVoteTotal(int smsVoteTotal) {
		this.smsVoteTotal = smsVoteTotal;
	}
	/**
	 * @return the voteTotal
	 */
	public int getVoteTotal() {
		return voteTotal;
	}
	/**
	 * @param voteTotal the voteTotal to set
	 */
	public void setVoteTotal(int voteTotal) {
		this.voteTotal = voteTotal;
	}
	/**
	 * @return the voteTimes
	 */
	public int getVoteTimes() {
		return voteTimes;
	}
	/**
	 * @param voteTimes the voteTimes to set
	 */
	public void setVoteTimes(int voteTimes) {
		this.voteTimes = voteTimes;
	}
}