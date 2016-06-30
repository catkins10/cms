package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:难度(exam_exam_difficulty)
 * @author linchuan
 *
 */
public class ExamDifficultyLevel extends Record {
	private long examId; //考试ID
	private String difficulty; //难度
	private double ratio; //比例
	
	/**
	 * @return the difficulty
	 */
	public String getDifficulty() {
		return difficulty;
	}
	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
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
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}
	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}