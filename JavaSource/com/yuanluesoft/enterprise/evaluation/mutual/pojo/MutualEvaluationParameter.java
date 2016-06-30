package com.yuanluesoft.enterprise.evaluation.mutual.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 互评:参数配置(evaluation_mutual_parameter)
 * @author linchuan
 *
 */
public class MutualEvaluationParameter extends Record {
	private double ratio; //比例
	private int minPersonNumber; //最低人数要求,人数太少的部门不做互评
	private int leaderEnabled; //部门领导是否参与
	private String rejectPostIds; //不参与的岗位ID
	private String rejectPosts; //不参与的岗位名称
	
	/**
	 * @return the leaderEnabled
	 */
	public int getLeaderEnabled() {
		return leaderEnabled;
	}
	/**
	 * @param leaderEnabled the leaderEnabled to set
	 */
	public void setLeaderEnabled(int leaderEnabled) {
		this.leaderEnabled = leaderEnabled;
	}
	/**
	 * @return the minPersonNumber
	 */
	public int getMinPersonNumber() {
		return minPersonNumber;
	}
	/**
	 * @param minPersonNumber the minPersonNumber to set
	 */
	public void setMinPersonNumber(int minPersonNumber) {
		this.minPersonNumber = minPersonNumber;
	}
	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}
	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	/**
	 * @return the rejectPostIds
	 */
	public String getRejectPostIds() {
		return rejectPostIds;
	}
	/**
	 * @param rejectPostIds the rejectPostIds to set
	 */
	public void setRejectPostIds(String rejectPostIds) {
		this.rejectPostIds = rejectPostIds;
	}
	/**
	 * @return the rejectPosts
	 */
	public String getRejectPosts() {
		return rejectPosts;
	}
	/**
	 * @param rejectPosts the rejectPosts to set
	 */
	public void setRejectPosts(String rejectPosts) {
		this.rejectPosts = rejectPosts;
	}
}