package com.yuanluesoft.msa.seafarer.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 合格证书:合格的考生(msa_certification_examinee)
 * @author linchuan
 *
 */
public class MsaCertificationExaminee extends Record {
	private long examId; //考试ID
	private String name; //姓名
	private String level; //申考等级
	private String job; //申考职务
	private String examResult; //考试结果
	private String result; //评估结果
	private String sn; //合格证明序列号
	private Date grantDate; //发放日期
	private Date receiveDate; //领取日期
	private String identityCard; //身份证号码
	private String permit; //准考证号码
	private MsaCertificationExam exam; //考试
	
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
	 * @return the examResult
	 */
	public String getExamResult() {
		return examResult;
	}
	/**
	 * @param examResult the examResult to set
	 */
	public void setExamResult(String examResult) {
		this.examResult = examResult;
	}
	/**
	 * @return the grantDate
	 */
	public Date getGrantDate() {
		return grantDate;
	}
	/**
	 * @param grantDate the grantDate to set
	 */
	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}
	/**
	 * @return the identityCard
	 */
	public String getIdentityCard() {
		return identityCard;
	}
	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
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
	 * @return the permit
	 */
	public String getPermit() {
		return permit;
	}
	/**
	 * @param permit the permit to set
	 */
	public void setPermit(String permit) {
		this.permit = permit;
	}
	/**
	 * @return the receiveDate
	 */
	public Date getReceiveDate() {
		return receiveDate;
	}
	/**
	 * @param receiveDate the receiveDate to set
	 */
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the exam
	 */
	public MsaCertificationExam getExam() {
		return exam;
	}
	/**
	 * @param exam the exam to set
	 */
	public void setExam(MsaCertificationExam exam) {
		this.exam = exam;
	}
}