package com.yuanluesoft.enterprise.exam.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:答题(exam_test_answer)
 * @author linchuan
 *
 */
public class ExamTestAnswer extends Record {
	private long testId; //答卷ID
	private long paperQuestionId; //出题记录ID
	private String answer; //答案,多选题、填空题答案用\r\n分隔
	private double score; //得分
	private long correctorId; //批改人ID
	private String corrector; //批改人姓名
	private String remark; //批改说明
	private Set wrongQuestions; //错题本
	
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the corrector
	 */
	public String getCorrector() {
		return corrector;
	}
	/**
	 * @param corrector the corrector to set
	 */
	public void setCorrector(String corrector) {
		this.corrector = corrector;
	}
	/**
	 * @return the correctorId
	 */
	public long getCorrectorId() {
		return correctorId;
	}
	/**
	 * @param correctorId the correctorId to set
	 */
	public void setCorrectorId(long correctorId) {
		this.correctorId = correctorId;
	}
	/**
	 * @return the paperQuestionId
	 */
	public long getPaperQuestionId() {
		return paperQuestionId;
	}
	/**
	 * @param paperQuestionId the paperQuestionId to set
	 */
	public void setPaperQuestionId(long paperQuestionId) {
		this.paperQuestionId = paperQuestionId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return the testId
	 */
	public long getTestId() {
		return testId;
	}
	/**
	 * @param testId the testId to set
	 */
	public void setTestId(long testId) {
		this.testId = testId;
	}
	/**
	 * @return the wrongQuestions
	 */
	public Set getWrongQuestions() {
		return wrongQuestions;
	}
	/**
	 * @param wrongQuestions the wrongQuestions to set
	 */
	public void setWrongQuestions(Set wrongQuestions) {
		this.wrongQuestions = wrongQuestions;
	}
}