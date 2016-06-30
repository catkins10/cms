package com.yuanluesoft.enterprise.exam.forms;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.enterprise.exam.pojo.Exam;
import com.yuanluesoft.enterprise.exam.pojo.ExamPaper;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ExamTest extends ActionForm {
	private long paperId; //考卷ID
	private long personId; //用户ID
	private String personName; //用户名
	private double score; //成绩
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //完成时间
	private int status; //状态,0/在考,1/待批改,2/完成,3/放弃
	private Set testAnswers; //答案列表
	//扩展属性
	private ExamPaper examPaper; //考卷
	private Exam exam; //考试
	private List examTestQuestionTypes; //考试题型(ExamTestQuestionType)列表
	
	private boolean isCorrect; //是否批改
	private boolean isCheck; //是否复核
	private String scoreAsText; //成绩(文本格式)
	
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
	 * @return the paperId
	 */
	public long getPaperId() {
		return paperId;
	}
	/**
	 * @param paperId the paperId to set
	 */
	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
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
	 * @return the testAnswers
	 */
	public Set getTestAnswers() {
		return testAnswers;
	}
	/**
	 * @param testAnswers the testAnswers to set
	 */
	public void setTestAnswers(Set testAnswers) {
		this.testAnswers = testAnswers;
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
	/**
	 * @return the examPaper
	 */
	public ExamPaper getExamPaper() {
		return examPaper;
	}
	/**
	 * @param examPaper the examPaper to set
	 */
	public void setExamPaper(ExamPaper examPaper) {
		this.examPaper = examPaper;
	}
	/**
	 * @return the examTestQuestionTypes
	 */
	public List getExamTestQuestionTypes() {
		return examTestQuestionTypes;
	}
	/**
	 * @param examTestQuestionTypes the examTestQuestionTypes to set
	 */
	public void setExamTestQuestionTypes(List examTestQuestionTypes) {
		this.examTestQuestionTypes = examTestQuestionTypes;
	}
	/**
	 * @return the isCorrect
	 */
	public boolean isCorrect() {
		return isCorrect;
	}
	/**
	 * @param isCorrect the isCorrect to set
	 */
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	/**
	 * @return the scoreAsText
	 */
	public String getScoreAsText() {
		return scoreAsText;
	}
	/**
	 * @param scoreAsText the scoreAsText to set
	 */
	public void setScoreAsText(String scoreAsText) {
		this.scoreAsText = scoreAsText;
	}
	/**
	 * @return the isCheck
	 */
	public boolean isCheck() {
		return isCheck;
	}
	/**
	 * @param isCheck the isCheck to set
	 */
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
}