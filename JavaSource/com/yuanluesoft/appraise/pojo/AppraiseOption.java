package com.yuanluesoft.appraise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评议管理:评议选项(appraise_option)
 * @author linchuan
 *
 */
public class AppraiseOption extends Record {
	private long taskId; //评议任务ID
	private int type; //选项类型,0/短信,1/网络
	private String option; //选项名称
	private double score; //分值
	private int abstain; //是否弃权,1/弃权
	private String smsOption; //短信选项
	
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
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}