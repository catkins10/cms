package com.yuanluesoft.enterprise.evaluation.mutual.model;

/**
 * 互评统计
 * @author linchuan
 *
 */
public class MutualEvaluationTotal {
	private int voteNumber; //互评投票数
	private int highNumber; //互评靠前数
	private int lowNumber; //互评靠后数
	
	/**
	 * @return the highNumber
	 */
	public int getHighNumber() {
		return highNumber;
	}
	/**
	 * @param highNumber the highNumber to set
	 */
	public void setHighNumber(int highNumber) {
		this.highNumber = highNumber;
	}
	/**
	 * @return the lowNumber
	 */
	public int getLowNumber() {
		return lowNumber;
	}
	/**
	 * @param lowNumber the lowNumber to set
	 */
	public void setLowNumber(int lowNumber) {
		this.lowNumber = lowNumber;
	}
	/**
	 * @return the voteNumber
	 */
	public int getVoteNumber() {
		return voteNumber;
	}
	/**
	 * @param voteNumber the voteNumber to set
	 */
	public void setVoteNumber(int voteNumber) {
		this.voteNumber = voteNumber;
	}
}