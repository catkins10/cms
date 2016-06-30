package com.yuanluesoft.enterprise.exam.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.enterprise.exam.pojo.Exam;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class ExamPaper extends ActionForm {
	private long examId; //考试ID
	private String examPaperName; //考卷名称
	private int resit; //是否补考,只出原来做错的题目
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //截止时间,如果有指定截止时间,则要求在截止时间前完成,否则,时间以用户开始答卷为准
	private int onComputer; //计算机作答
	private double score; //总分
	private int status; //状态,0/未发布,1/发布
	private Timestamp  created; //出卷时间
	private long creatorId; //出卷人ID
	private String creator; //出卷人
	private Timestamp releaseTime; //发布时间
	private long releasePersonId; //发布人ID
	private String releasePerson; //发布人
	private Set paperQuestions; //出题记录
	private Set visitors; //参加人员
	private Exam exam; //考试
	
	//扩展属性
	private RecordVisitorList testUsers = new RecordVisitorList(); //参加考试的人员
	
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
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
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
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
	 * @return the onComputer
	 */
	public int getOnComputer() {
		return onComputer;
	}
	/**
	 * @param onComputer the onComputer to set
	 */
	public void setOnComputer(int onComputer) {
		this.onComputer = onComputer;
	}
	/**
	 * @return the paperQuestions
	 */
	public Set getPaperQuestions() {
		return paperQuestions;
	}
	/**
	 * @param paperQuestions the paperQuestions to set
	 */
	public void setPaperQuestions(Set paperQuestions) {
		this.paperQuestions = paperQuestions;
	}
	/**
	 * @return the resit
	 */
	public int getResit() {
		return resit;
	}
	/**
	 * @param resit the resit to set
	 */
	public void setResit(int resit) {
		this.resit = resit;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the examPaperName
	 */
	public String getExamPaperName() {
		return examPaperName;
	}
	/**
	 * @param examPaperName the examPaperName to set
	 */
	public void setExamPaperName(String examPaperName) {
		this.examPaperName = examPaperName;
	}
	/**
	 * @return the testUsers
	 */
	public RecordVisitorList getTestUsers() {
		return testUsers;
	}
	/**
	 * @param testUsers the testUsers to set
	 */
	public void setTestUsers(RecordVisitorList testUsers) {
		this.testUsers = testUsers;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the releasePerson
	 */
	public String getReleasePerson() {
		return releasePerson;
	}
	/**
	 * @param releasePerson the releasePerson to set
	 */
	public void setReleasePerson(String releasePerson) {
		this.releasePerson = releasePerson;
	}
	/**
	 * @return the releasePersonId
	 */
	public long getReleasePersonId() {
		return releasePersonId;
	}
	/**
	 * @param releasePersonId the releasePersonId to set
	 */
	public void setReleasePersonId(long releasePersonId) {
		this.releasePersonId = releasePersonId;
	}
	/**
	 * @return the releaseTime
	 */
	public Timestamp getReleaseTime() {
		return releaseTime;
	}
	/**
	 * @param releaseTime the releaseTime to set
	 */
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	/**
	 * @return the exam
	 */
	public Exam getExam() {
		return exam;
	}
	/**
	 * @param exam the exam to set
	 */
	public void setExam(Exam exam) {
		this.exam = exam;
	}
}