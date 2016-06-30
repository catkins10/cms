package com.yuanluesoft.msa.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:科目(msa_exam_subject)
 * @author linchuan
 *
 */
public class MsaExamSubject extends Record {
	private long examId; //考试ID
	private String subject; //科目名称
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}