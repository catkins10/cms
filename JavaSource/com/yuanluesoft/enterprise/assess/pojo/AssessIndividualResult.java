package com.yuanluesoft.enterprise.assess.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 绩效考核:各单项成绩(assess_individual_result)
 * @author linchuan
 *
 */
public class AssessIndividualResult extends Record {
	private long resultId; //个人考核ID
	private long contentId; //考核内容ID
	private long activityId; //考核步骤ID
	private float result; //考核成绩
	
	/**
	 * @return the activityId
	 */
	public long getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return the contentId
	 */
	public long getContentId() {
		return contentId;
	}
	/**
	 * @param contentId the contentId to set
	 */
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	/**
	 * @return the result
	 */
	public float getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(float result) {
		this.result = result;
	}
	/**
	 * @return the resultId
	 */
	public long getResultId() {
		return resultId;
	}
	/**
	 * @param resultId the resultId to set
	 */
	public void setResultId(long resultId) {
		this.resultId = resultId;
	}
}