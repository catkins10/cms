package com.yuanluesoft.job.talent.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:求职意向职能类别(job_talent_intention_post)
 * @author linchuan
 *
 */
public class JobTalentIntentionPost extends Record {
	private long intentionId; //求职意向ID
	private long postId; //职能类别ID
	private String post; //职能类别
	/**
	 * @return the intentionId
	 */
	public long getIntentionId() {
		return intentionId;
	}
	/**
	 * @param intentionId the intentionId to set
	 */
	public void setIntentionId(long intentionId) {
		this.intentionId = intentionId;
	}
	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * @return the postId
	 */
	public long getPostId() {
		return postId;
	}
	/**
	 * @param postId the postId to set
	 */
	public void setPostId(long postId) {
		this.postId = postId;
	}
}