package com.yuanluesoft.msa.exam.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试(msa_exam)
 * @author linchuan
 *
 */
public class MsaExam extends Record {
	private String name; //名称
	private String speciality; //专业
	private String period; //期数
	private String examPaperCode; //试卷代码
	private String examTime; //考试时间
	private String examAddress; //考试地点
	private long creatorId; //创建者ID
	private String creator; //创建者
	private Timestamp created; //创建时间
	private Timestamp publishTime; //发布时间
	private long importId; //导入日志ID
	private Set subjects; //科目列表
	private Set transcripts; //成绩单列表
	
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
	 * @return the examAddress
	 */
	public String getExamAddress() {
		return examAddress;
	}
	/**
	 * @param examAddress the examAddress to set
	 */
	public void setExamAddress(String examAddress) {
		this.examAddress = examAddress;
	}
	/**
	 * @return the examPaperCode
	 */
	public String getExamPaperCode() {
		return examPaperCode;
	}
	/**
	 * @param examPaperCode the examPaperCode to set
	 */
	public void setExamPaperCode(String examPaperCode) {
		this.examPaperCode = examPaperCode;
	}
	/**
	 * @return the examTime
	 */
	public String getExamTime() {
		return examTime;
	}
	/**
	 * @param examTime the examTime to set
	 */
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	/**
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
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
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}
	/**
	 * @return the speciality
	 */
	public String getSpeciality() {
		return speciality;
	}
	/**
	 * @param speciality the speciality to set
	 */
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	/**
	 * @return the subjects
	 */
	public Set getSubjects() {
		return subjects;
	}
	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(Set subjects) {
		this.subjects = subjects;
	}
	/**
	 * @return the transcripts
	 */
	public Set getTranscripts() {
		return transcripts;
	}
	/**
	 * @param transcripts the transcripts to set
	 */
	public void setTranscripts(Set transcripts) {
		this.transcripts = transcripts;
	}
	/**
	 * @return the publishTime
	 */
	public Timestamp getPublishTime() {
		return publishTime;
	}
	/**
	 * @param publishTime the publishTime to set
	 */
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}
}