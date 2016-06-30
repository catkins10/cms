package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:批改人员(exam_exam_corrector)
 * @author linchuan
 *
 */
public class ExamCorrector extends Record {
	private long examId; //考试ID
	private long correctorId; //批改人ID
	private String corrector; //批改人姓名
	
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
}