package com.yuanluesoft.enterprise.exam.model;

/**
 * 成绩单
 * @author linchuan
 *
 */
public class ExamTranscript {
	private long personId; //用户ID
	private String personName; //用户名
	private int testedQuestionNumber; //实际完成的题量
	private double testScore; //考试总分
	private int testPosition; //考试名次
	
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
}