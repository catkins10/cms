package com.yuanluesoft.cms.evaluation.forms;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationTopic extends ActionForm {
	private String name; //主题名称
	private String description; //描述
	private char anonymous = '0'; //是否匿名测评
	private Timestamp endTime; //截止日期
	private Timestamp issueTime; //发布时间
	private char issue = '0'; //是否发布
	private Timestamp created; //创建时间
	private long creatorId; //创建者ID
	private String creator; //创建者
	private Set options; //选择项列表
	private Set items; //测评项目列表
	private Set visitors; //访问者列表
	private Set marks; //测评记录列表
	
	private List toEvaluateTargetPersons; //待测评用户列表
	private List evaluatedTargetPersons; //已测评用户列表
	
	
	/**
	 * @return the anonymous
	 */
	public char getAnonymous() {
		return anonymous;
	}
	/**
	 * @param anonymous the anonymous to set
	 */
	public void setAnonymous(char anonymous) {
		this.anonymous = anonymous;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the issue
	 */
	public char getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(char issue) {
		this.issue = issue;
	}
	/**
	 * @return the items
	 */
	public Set getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(Set items) {
		this.items = items;
	}
	/**
	 * @return the marks
	 */
	public Set getMarks() {
		return marks;
	}
	/**
	 * @param marks the marks to set
	 */
	public void setMarks(Set marks) {
		this.marks = marks;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the options
	 */
	public Set getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(Set options) {
		this.options = options;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the evaluatedTargetPersons
	 */
	public List getEvaluatedTargetPersons() {
		return evaluatedTargetPersons;
	}
	/**
	 * @param evaluatedTargetPersons the evaluatedTargetPersons to set
	 */
	public void setEvaluatedTargetPersons(List evaluatedTargetPersons) {
		this.evaluatedTargetPersons = evaluatedTargetPersons;
	}
	/**
	 * @return the toEvaluateTargetPersons
	 */
	public List getToEvaluateTargetPersons() {
		return toEvaluateTargetPersons;
	}
	/**
	 * @param toEvaluateTargetPersons the toEvaluateTargetPersons to set
	 */
	public void setToEvaluateTargetPersons(List toEvaluateTargetPersons) {
		this.toEvaluateTargetPersons = toEvaluateTargetPersons;
	}
}