package com.yuanluesoft.cms.evaluation.forms.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Evaluation extends ActionForm {
	private long topicId; //主题ID
	private long targetPersonId; //被测评人ID
	private String targetPersonName; //被测评人名称
	private long evaluatePersonId; //测评人ID
	private String evaluatePersonName; //测评人名称
	private String evaluatePersonIP; //测评人IP
	private Timestamp evaluateTime; //测评时间
	private Set scores; //测评分数列表
	
	//表单属性
	private EvaluationTopic topic = new EvaluationTopic(); //测评主题
	private String targetPersonOrg; //被评测用户所在部门
	private List itemCategories; //测评项目分类
	
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
		return evaluatePersonName;
	}
	/**
	 * @param evaluatePersonName the evaluatePersonName to set
	 */
	public void setEvaluatePersonName(String evaluatePersonName) {
		this.evaluatePersonName = evaluatePersonName;
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
	/**
	 * @return the itemCategories
	 */
	public List getItemCategories() {
		return itemCategories;
	}
	/**
	 * @param itemCategories the itemCategories to set
	 */
	public void setItemCategories(List itemCategories) {
		this.itemCategories = itemCategories;
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
	 * @return the targetPersonOrg
	 */
	public String getTargetPersonOrg() {
		return targetPersonOrg;
	}
	/**
	 * @param targetPersonOrg the targetPersonOrg to set
	 */
	public void setTargetPersonOrg(String targetPersonOrg) {
		this.targetPersonOrg = targetPersonOrg;
	}
	/**
	 * @return the topic
	 */
	public EvaluationTopic getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(EvaluationTopic topic) {
		this.topic = topic;
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
}