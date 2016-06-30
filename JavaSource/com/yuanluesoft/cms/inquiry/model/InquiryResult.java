package com.yuanluesoft.cms.inquiry.model;

/**
 * 投票结果
 * @author linchuan
 *
 */
public class InquiryResult {
	private long optionId; //选择ID
	private String option; //选项
	private int needSupplement = 0; //是否需要补充说明,0/不需要,1/需要
	private int voteNumber; //投票数
	private double votePercent; //所占百分比
	
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
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
	 * @return the needSupplement
	 */
	public int getNeedSupplement() {
		return needSupplement;
	}
	/**
	 * @param needSupplement the needSupplement to set
	 */
	public void setNeedSupplement(int needSupplement) {
		this.needSupplement = needSupplement;
	}
}