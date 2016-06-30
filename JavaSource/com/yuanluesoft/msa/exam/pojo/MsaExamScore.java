package com.yuanluesoft.msa.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 成绩单：单科成绩(msa_exam_score)
 * @author linchuan
 *
 */
public class MsaExamScore extends Record {
	private long transcriptId; //成绩单ID
	private String subject; //科目名称
	private String score; //成绩
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
	/**
	 * @return the transcriptId
	 */
	public long getTranscriptId() {
		return transcriptId;
	}
	/**
	 * @param transcriptId the transcriptId to set
	 */
	public void setTranscriptId(long transcriptId) {
		this.transcriptId = transcriptId;
	}
}