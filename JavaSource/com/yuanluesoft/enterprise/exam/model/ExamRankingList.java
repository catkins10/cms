package com.yuanluesoft.enterprise.exam.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class ExamRankingList {
	private int monthTestQuestionNumber; //每月需要完成的题量
	private double testMaxScore = -1; //考试最高分
	private double testMinScore = Double.MAX_VALUE; //考试最低分
	private double testAverageScore; //考试平均分
	private List transcripts; //成绩单列表
	
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
	 * @return the transcripts
	 */
	public List getTranscripts() {
		return transcripts;
	}
	/**
	 * @param transcripts the transcripts to set
	 */
	public void setTranscripts(List transcripts) {
		this.transcripts = transcripts;
	}
}