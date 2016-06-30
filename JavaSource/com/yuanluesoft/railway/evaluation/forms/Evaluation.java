package com.yuanluesoft.railway.evaluation.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Evaluation extends ActionForm {
	private int year; //年度
	private int month; //月份
	private long personId; //用户ID
	private String personName; //用户名
	private long postId; //岗位ID
	private String postName; //岗位名称
	private long departmentId; //部门ID
	private String departmentName; //部门名称
	private String workload = null; //工作量,null表示未考核
	private String departmentWeight = null; //部门权重,null表示未考核
	private int monthTestQuestionNumber; //每月需要完成的题量
	private int testedQuestionNumber; //实际完成的题量
	private double testScore; //考试总分
	private double testMaxScore; //考试最高分
	private double testMinScore; //考试最低分
	private double testAverageScore; //考试平均分
	private int testPosition; //考试名次
	private double testReward; //考试奖励/处罚
	private int mutualEvaluationVoteNumber; //互评投票数
	private int mutualEvaluationHighNumber; //互评靠前数
	private int mutualEvaluationLowNumber; //互评靠后数
	private double mutualEvaluationReward; //互评奖励/处罚
	private int eventLevelANumber; //铁路局问题A个数
	private int eventLevelBNumber; //铁路局问题B个数
	private int eventLevelCNumber; //铁路局问题C个数
	private int eventLevelDNumber; //铁路局问题D个数
	private double eventPunish; //铁路局问题处罚
	private String score = null; //综合评价成绩,null表示未考核
	
	/**
	 * 获取问题总数
	 * @return
	 */
	public String getEventLevelNumber() {
		int count = eventLevelANumber + eventLevelBNumber + eventLevelCNumber + eventLevelDNumber;
		return count==0 ? null : "" + count;
	}
	
	/**
	 * 获取互评情况说明
	 * @return
	 */
	public String getMutualEvaluationNumber() {
		return mutualEvaluationVoteNumber==0 ? null : mutualEvaluationHighNumber + "/" + mutualEvaluationLowNumber + "/" + mutualEvaluationVoteNumber;
	}
	
	/**
	 * @return the departmentId
	 */
	public long getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return the departmentWeight
	 */
	public String getDepartmentWeight() {
		return departmentWeight;
	}
	/**
	 * @param departmentWeight the departmentWeight to set
	 */
	public void setDepartmentWeight(String departmentWeight) {
		this.departmentWeight = departmentWeight;
	}
	/**
	 * @return the eventLevelANumber
	 */
	public int getEventLevelANumber() {
		return eventLevelANumber;
	}
	/**
	 * @param eventLevelANumber the eventLevelANumber to set
	 */
	public void setEventLevelANumber(int eventLevelANumber) {
		this.eventLevelANumber = eventLevelANumber;
	}
	/**
	 * @return the eventLevelBNumber
	 */
	public int getEventLevelBNumber() {
		return eventLevelBNumber;
	}
	/**
	 * @param eventLevelBNumber the eventLevelBNumber to set
	 */
	public void setEventLevelBNumber(int eventLevelBNumber) {
		this.eventLevelBNumber = eventLevelBNumber;
	}
	/**
	 * @return the eventLevelCNumber
	 */
	public int getEventLevelCNumber() {
		return eventLevelCNumber;
	}
	/**
	 * @param eventLevelCNumber the eventLevelCNumber to set
	 */
	public void setEventLevelCNumber(int eventLevelCNumber) {
		this.eventLevelCNumber = eventLevelCNumber;
	}
	/**
	 * @return the eventLevelDNumber
	 */
	public int getEventLevelDNumber() {
		return eventLevelDNumber;
	}
	/**
	 * @param eventLevelDNumber the eventLevelDNumber to set
	 */
	public void setEventLevelDNumber(int eventLevelDNumber) {
		this.eventLevelDNumber = eventLevelDNumber;
	}
	/**
	 * @return the eventPunish
	 */
	public double getEventPunish() {
		return eventPunish;
	}
	/**
	 * @param eventPunish the eventPunish to set
	 */
	public void setEventPunish(double eventPunish) {
		this.eventPunish = eventPunish;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the monthTestQuestionNumber
	 */
	public int getMonthTestQuestionNumber() {
		return monthTestQuestionNumber;
	}
	/**
	 * @param monthTestQuestionNumber the monthTestQuestionNumber to set
	 */
	public void setMonthTestQuestionNumber(int monthTestQuestionNumber) {
		this.monthTestQuestionNumber = monthTestQuestionNumber;
	}
	/**
	 * @return the mutualEvaluationHighNumber
	 */
	public int getMutualEvaluationHighNumber() {
		return mutualEvaluationHighNumber;
	}
	/**
	 * @param mutualEvaluationHighNumber the mutualEvaluationHighNumber to set
	 */
	public void setMutualEvaluationHighNumber(int mutualEvaluationHighNumber) {
		this.mutualEvaluationHighNumber = mutualEvaluationHighNumber;
	}
	/**
	 * @return the mutualEvaluationLowNumber
	 */
	public int getMutualEvaluationLowNumber() {
		return mutualEvaluationLowNumber;
	}
	/**
	 * @param mutualEvaluationLowNumber the mutualEvaluationLowNumber to set
	 */
	public void setMutualEvaluationLowNumber(int mutualEvaluationLowNumber) {
		this.mutualEvaluationLowNumber = mutualEvaluationLowNumber;
	}
	/**
	 * @return the mutualEvaluationReward
	 */
	public double getMutualEvaluationReward() {
		return mutualEvaluationReward;
	}
	/**
	 * @param mutualEvaluationReward the mutualEvaluationReward to set
	 */
	public void setMutualEvaluationReward(double mutualEvaluationReward) {
		this.mutualEvaluationReward = mutualEvaluationReward;
	}
	/**
	 * @return the mutualEvaluationVoteNumber
	 */
	public int getMutualEvaluationVoteNumber() {
		return mutualEvaluationVoteNumber;
	}
	/**
	 * @param mutualEvaluationVoteNumber the mutualEvaluationVoteNumber to set
	 */
	public void setMutualEvaluationVoteNumber(int mutualEvaluationVoteNumber) {
		this.mutualEvaluationVoteNumber = mutualEvaluationVoteNumber;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
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
	/**
	 * @return the postName
	 */
	public String getPostName() {
		return postName;
	}
	/**
	 * @param postName the postName to set
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the testAverageScore
	 */
	public double getTestAverageScore() {
		return testAverageScore;
	}
	/**
	 * @param testAverageScore the testAverageScore to set
	 */
	public void setTestAverageScore(double testAverageScore) {
		this.testAverageScore = testAverageScore;
	}
	/**
	 * @return the testedQuestionNumber
	 */
	public int getTestedQuestionNumber() {
		return testedQuestionNumber;
	}
	/**
	 * @param testedQuestionNumber the testedQuestionNumber to set
	 */
	public void setTestedQuestionNumber(int testedQuestionNumber) {
		this.testedQuestionNumber = testedQuestionNumber;
	}
	/**
	 * @return the testMaxScore
	 */
	public double getTestMaxScore() {
		return testMaxScore;
	}
	/**
	 * @param testMaxScore the testMaxScore to set
	 */
	public void setTestMaxScore(double testMaxScore) {
		this.testMaxScore = testMaxScore;
	}
	/**
	 * @return the testMinScore
	 */
	public double getTestMinScore() {
		return testMinScore;
	}
	/**
	 * @param testMinScore the testMinScore to set
	 */
	public void setTestMinScore(double testMinScore) {
		this.testMinScore = testMinScore;
	}
	/**
	 * @return the testPosition
	 */
	public int getTestPosition() {
		return testPosition;
	}
	/**
	 * @param testPosition the testPosition to set
	 */
	public void setTestPosition(int testPosition) {
		this.testPosition = testPosition;
	}
	/**
	 * @return the testReward
	 */
	public double getTestReward() {
		return testReward;
	}
	/**
	 * @param testReward the testReward to set
	 */
	public void setTestReward(double testReward) {
		this.testReward = testReward;
	}
	/**
	 * @return the testScore
	 */
	public double getTestScore() {
		return testScore;
	}
	/**
	 * @param testScore the testScore to set
	 */
	public void setTestScore(double testScore) {
		this.testScore = testScore;
	}
	/**
	 * @return the workload
	 */
	public String getWorkload() {
		return workload;
	}
	/**
	 * @param workload the workload to set
	 */
	public void setWorkload(String workload) {
		this.workload = workload;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}