package com.yuanluesoft.appraise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:评议选项统计(appraise_option_vote)
 * @author linchuan
 *
 */
public class AppraiseOptionVote extends Record {
	private long unitAppraiseId; //单位评议ID
	private long optionId; //选项ID
	private String option; //选项名称
	private int optionType; //选项类型
	private String smsOption; //短信选项
	private double optionScore; //选项分值
	private int abstain; //是否弃权,1/弃权
	private int voteTotal; //投票数
	private int smsVoteTotal; //短信投票数
	private int internetVoteTotal; //网络投票数
	private double score; //累计分数
	private UnitAppraise unitAppraise; //单位评议
	
	/**
	 * 生成投票框
	 * @return
	 */
	public String getAppraiseBox() {
		return "<input class=\"radio\"" +
			   " id=\"" + getId() + "\"" +
			   " name=\"appraise_" + unitAppraiseId + "\"" +
			   " type=\"radio\"" +
			   " title=\"" + unitAppraise.getUnitName() + "_" + option + "\"" +
			   " value=\"" + getId() + "\"" +
			   ">";
	}
	
	/**
	 * 获取选项(含标签)
	 * @return
	 */
	public String getOptionLabel() {
		return "<label for=\"" + getId() + "\">" + option + "</label>";
	}
	
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
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
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
	 * @return the unitAppraiseId
	 */
	public long getUnitAppraiseId() {
		return unitAppraiseId;
	}
	/**
	 * @param unitAppraiseId the unitAppraiseId to set
	 */
	public void setUnitAppraiseId(long unitAppraiseId) {
		this.unitAppraiseId = unitAppraiseId;
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
	 * @return the optionScore
	 */
	public double getOptionScore() {
		return optionScore;
	}
	/**
	 * @param optionScore the optionScore to set
	 */
	public void setOptionScore(double optionScore) {
		this.optionScore = optionScore;
	}
	/**
	 * @return the smsOption
	 */
	public String getSmsOption() {
		return smsOption;
	}
	/**
	 * @param smsOption the smsOption to set
	 */
	public void setSmsOption(String smsOption) {
		this.smsOption = smsOption;
	}
	/**
	 * @return the abstain
	 */
	public int getAbstain() {
		return abstain;
	}
	/**
	 * @param abstain the abstain to set
	 */
	public void setAbstain(int abstain) {
		this.abstain = abstain;
	}

	/**
	 * @return the unitAppraise
	 */
	public UnitAppraise getUnitAppraise() {
		return unitAppraise;
	}

	/**
	 * @param unitAppraise the unitAppraise to set
	 */
	public void setUnitAppraise(UnitAppraise unitAppraise) {
		this.unitAppraise = unitAppraise;
	}

	/**
	 * @return the optionType
	 */
	public int getOptionType() {
		return optionType;
	}

	/**
	 * @param optionType the optionType to set
	 */
	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}
}