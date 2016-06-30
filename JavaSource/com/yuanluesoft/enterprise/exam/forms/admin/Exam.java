package com.yuanluesoft.enterprise.exam.forms.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 考试:考试(exam_exam)
 * @author linchuan
 *
 */
public class Exam extends ActionForm {
	private String name; //名称
	private int selfTest; //是否自助方式,用户答卷时自动组卷
	private int monthQuestionNumber; //每月考试题数,自助方式时有效
	private int yearQuestionNumber; //年度考试题数,自助方式时有效
	private int examDayLimit; //日考试次数上限,自助方式时有效
	private int score; //总分
	private int examTime; //考试时长,以分钟为单位
	private String knowledgeIds; //知识类别ID
	private String knowledges; //知识类别
	private String itemIds; //项目分类ID
	private String items; //项目分类
	private int isDeleted = 0; //是否已删除
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Set examQuestionTypes; //考试题型
	private Set examDifficultyLevels; //考试难度等级
	private Set examPosts; //适用的岗位
	private Set examCorrectors; //批改人
	
	//扩展属性
	private List questionTypes; //题型列表
	private List difficultyLevels; //难易程度列表
	private String examPostIds; //适用的岗位ID
	private String examPostNames; //适用的岗位名称ID
	private String examCorrectorIds; //批改人ID
	private String examCorrectorNames; //批改人姓名
	
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
	 * @return the examDayLimit
	 */
	public int getExamDayLimit() {
		return examDayLimit;
	}
	/**
	 * @param examDayLimit the examDayLimit to set
	 */
	public void setExamDayLimit(int examDayLimit) {
		this.examDayLimit = examDayLimit;
	}
	/**
	 * @return the examTime
	 */
	public int getExamTime() {
		return examTime;
	}
	/**
	 * @param examTime the examTime to set
	 */
	public void setExamTime(int examTime) {
		this.examTime = examTime;
	}
	/**
	 * @return the itemIds
	 */
	public String getItemIds() {
		return itemIds;
	}
	/**
	 * @param itemIds the itemIds to set
	 */
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	/**
	 * @return the items
	 */
	public String getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(String items) {
		this.items = items;
	}
	/**
	 * @return the knowledgeIds
	 */
	public String getKnowledgeIds() {
		return knowledgeIds;
	}
	/**
	 * @param knowledgeIds the knowledgeIds to set
	 */
	public void setKnowledgeIds(String knowledgeIds) {
		this.knowledgeIds = knowledgeIds;
	}
	/**
	 * @return the knowledges
	 */
	public String getKnowledges() {
		return knowledges;
	}
	/**
	 * @param knowledges the knowledges to set
	 */
	public void setKnowledges(String knowledges) {
		this.knowledges = knowledges;
	}
	/**
	 * @return the monthQuestionNumber
	 */
	public int getMonthQuestionNumber() {
		return monthQuestionNumber;
	}
	/**
	 * @param monthQuestionNumber the monthQuestionNumber to set
	 */
	public void setMonthQuestionNumber(int monthQuestionNumber) {
		this.monthQuestionNumber = monthQuestionNumber;
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the selfTest
	 */
	public int getSelfTest() {
		return selfTest;
	}
	/**
	 * @param selfTest the selfTest to set
	 */
	public void setSelfTest(int selfTest) {
		this.selfTest = selfTest;
	}
	/**
	 * @return the yearQuestionNumber
	 */
	public int getYearQuestionNumber() {
		return yearQuestionNumber;
	}
	/**
	 * @param yearQuestionNumber the yearQuestionNumber to set
	 */
	public void setYearQuestionNumber(int yearQuestionNumber) {
		this.yearQuestionNumber = yearQuestionNumber;
	}
	/**
	 * @return the examCorrectors
	 */
	public Set getExamCorrectors() {
		return examCorrectors;
	}
	/**
	 * @param examCorrectors the examCorrectors to set
	 */
	public void setExamCorrectors(Set examCorrectors) {
		this.examCorrectors = examCorrectors;
	}
	/**
	 * @return the examPosts
	 */
	public Set getExamPosts() {
		return examPosts;
	}
	/**
	 * @param examPosts the examPosts to set
	 */
	public void setExamPosts(Set examPosts) {
		this.examPosts = examPosts;
	}
	/**
	 * @return the examCorrectorIds
	 */
	public String getExamCorrectorIds() {
		return examCorrectorIds;
	}
	/**
	 * @param examCorrectorIds the examCorrectorIds to set
	 */
	public void setExamCorrectorIds(String examCorrectorIds) {
		this.examCorrectorIds = examCorrectorIds;
	}
	/**
	 * @return the examCorrectorNames
	 */
	public String getExamCorrectorNames() {
		return examCorrectorNames;
	}
	/**
	 * @param examCorrectorNames the examCorrectorNames to set
	 */
	public void setExamCorrectorNames(String examCorrectorNames) {
		this.examCorrectorNames = examCorrectorNames;
	}
	/**
	 * @return the examPostIds
	 */
	public String getExamPostIds() {
		return examPostIds;
	}
	/**
	 * @param examPostIds the examPostIds to set
	 */
	public void setExamPostIds(String examPostIds) {
		this.examPostIds = examPostIds;
	}
	/**
	 * @return the examPostNames
	 */
	public String getExamPostNames() {
		return examPostNames;
	}
	/**
	 * @param examPostNames the examPostNames to set
	 */
	public void setExamPostNames(String examPostNames) {
		this.examPostNames = examPostNames;
	}
	/**
	 * @return the examDifficultyLevels
	 */
	public Set getExamDifficultyLevels() {
		return examDifficultyLevels;
	}
	/**
	 * @param examDifficultyLevels the examDifficultyLevels to set
	 */
	public void setExamDifficultyLevels(Set examDifficultyLevels) {
		this.examDifficultyLevels = examDifficultyLevels;
	}
	/**
	 * @return the examQuestionTypes
	 */
	public Set getExamQuestionTypes() {
		return examQuestionTypes;
	}
	/**
	 * @param examQuestionTypes the examQuestionTypes to set
	 */
	public void setExamQuestionTypes(Set examQuestionTypes) {
		this.examQuestionTypes = examQuestionTypes;
	}
	/**
	 * @return the difficultyLevels
	 */
	public List getDifficultyLevels() {
		return difficultyLevels;
	}
	/**
	 * @param difficultyLevels the difficultyLevels to set
	 */
	public void setDifficultyLevels(List difficultyLevels) {
		this.difficultyLevels = difficultyLevels;
	}
	/**
	 * @return the questionTypes
	 */
	public List getQuestionTypes() {
		return questionTypes;
	}
	/**
	 * @param questionTypes the questionTypes to set
	 */
	public void setQuestionTypes(List questionTypes) {
		this.questionTypes = questionTypes;
	}
	/**
	 * @return the isDeleted
	 */
	public int getIsDeleted() {
		return isDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}