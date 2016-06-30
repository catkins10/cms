package com.yuanluesoft.cms.evaluation.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 民主测评：测评记录(evaluation_mark)
 * @author linchuan
 *
 */
public class EvaluationMark extends Record {
	private long topicId; //主题ID
	private long targetPersonId; //被测评人ID
	private String targetPersonName; //被测评人姓名
	private long evaluatePersonId; //测评人ID
	private String evaluatePersonName; //测评人名称
	private String evaluatePersonIP; //测评人IP
	private Timestamp evaluateTime; //测评时间
	private Set scores; //测评分数列表
	
	/**
	 * 各选择项统计
	 * @return
	 */
	public String getOptionCount() {
		if(scores==null) {
			return null;
		}
		List scores = new ArrayList(this.scores);
		Collections.sort(scores, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return (int)(((EvaluationScore)arg0).getOptionId() - ((EvaluationScore)arg1).getOptionId());
			}
		});
		String result = null;
		long currentOptionId = 0;
		int count = 0;
		EvaluationScore score = null;
		for(int i=0; i<scores.size(); i++) {
			score = (EvaluationScore)scores.get(i);
			if(score.getOptionId()<=0) {
				continue;
			}
			if(score.getOptionId()!=currentOptionId) {
				if(currentOptionId>0) {
					result = (result==null ? "" : result + "，") + ((EvaluationScore)scores.get(i-1)).getOption() + "：" + count;
				}
				count = 0;
				currentOptionId = score.getOptionId();
				currentOptionId = score.getOptionId();
			}
			count++;
		}
		return (result==null ? "" : result + "，") + score.getOption() + "：" + count;
	}
	
	/**
	 * @return the evaluatePersonId
	 */
	public long getEvaluatePersonId() {
		return evaluatePersonId;
	}
	/**
	 * @param evaluatePersonId the evaluatePersonId to set
	 */
	public void setEvaluatePersonId(long evaluatePersonId) {
		this.evaluatePersonId = evaluatePersonId;
	}
	/**
	 * @return the evaluatePersonIP
	 */
	public String getEvaluatePersonIP() {
		return evaluatePersonIP;
	}
	/**
	 * @param evaluatePersonIP the evaluatePersonIP to set
	 */
	public void setEvaluatePersonIP(String evaluatePersonIP) {
		this.evaluatePersonIP = evaluatePersonIP;
	}
	/**
	 * @return the evaluatePersonName
	 */
	public String getEvaluatePersonName() {
		return evaluatePersonName==null ? "匿名" : evaluatePersonName;
	}
	/**
	 * @param evaluatePersonName the evaluatePersonName to set
	 */
	public void setEvaluatePersonName(String evaluatePersonName) {
		this.evaluatePersonName = evaluatePersonName;
	}
	/**
	 * @return the targetPersonId
	 */
	public long getTargetPersonId() {
		return targetPersonId;
	}
	/**
	 * @param targetPersonId the targetPersonId to set
	 */
	public void setTargetPersonId(long targetPersonId) {
		this.targetPersonId = targetPersonId;
	}
	/**
	 * @return the targetPersonName
	 */
	public String getTargetPersonName() {
		return targetPersonName;
	}
	/**
	 * @param targetPersonName the targetPersonName to set
	 */
	public void setTargetPersonName(String targetPersonName) {
		this.targetPersonName = targetPersonName;
	}
	/**
	 * @return the topicId
	 */
	public long getTopicId() {
		return topicId;
	}
	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}
	/**
	 * @return the scores
	 */
	public Set getScores() {
		return scores;
	}
	/**
	 * @param scores the scores to set
	 */
	public void setScores(Set scores) {
		this.scores = scores;
	}
	/**
	 * @return the evaluateTime
	 */
	public Timestamp getEvaluateTime() {
		return evaluateTime;
	}
	/**
	 * @param evaluateTime the evaluateTime to set
	 */
	public void setEvaluateTime(Timestamp evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
}