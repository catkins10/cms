package com.yuanluesoft.msa.exam.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.msa.exam.pojo.MsaExam;

/**
 * 
 * @author linchuan
 *
 */
public class Transcript extends ActionForm {
	private long examId; //考试ID
	private String name; //姓名
	private String identityCard; //身份证号码
	private String permit; //准考证号码
	private String totalScore; //总成绩
	private String pass; //是否通过
	private String examLevel; //申考等级
	private String job; //申考职务
	private Set scores; //各科目成绩
	private MsaExam exam; //考试
	
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
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
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
	 * @return the totalScore
	 */
	public String getTotalScore() {
		return totalScore;
	}
	/**
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	/**
	 * @return the scores
	 */
	public Set getScores() {
		return scores;
	}
	/**
	 * @param scores the scores to set
	 */
	public void setScores(Set scores) {
		this.scores = scores;
	}
	/**
	 * @return the exam
	 */
	public MsaExam getExam() {
		return exam;
	}
	/**
	 * @param exam the exam to set
	 */
	public void setExam(MsaExam exam) {
		this.exam = exam;
	}
	/**
	 * @return the examLevel
	 */
	public String getExamLevel() {
		return examLevel;
	}
	/**
	 * @param examLevel the examLevel to set
	 */
	public void setExamLevel(String examLevel) {
		this.examLevel = examLevel;
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
}