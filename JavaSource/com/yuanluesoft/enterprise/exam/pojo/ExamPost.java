package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:适用的岗位(exam_exam_post)
 * @author linchuan
 *
 */
public class ExamPost extends Record {
	private long examId; //考试ID
	private long postId; //岗位ID
	private String post; //岗位名称
	
	/**
	 * @return the examId
	 */
	public long getExamId() {
		return examId;
	}
	/**
	 * @param examId the examId to set
	 */
	public void setExamId(long examId) {
		this.examId = examId;
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