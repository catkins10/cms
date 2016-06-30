package com.yuanluesoft.enterprise.exam.pojo;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:答卷(exam_test)
 * @author linchuan
 *
 */
public class ExamTest extends Record {
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
		
	/**
	 * 获取题型数
	 * @return
	 */
	public double getQuestionTypeNumber() {
		return examTestQuestionTypes==null ? 0 : examTestQuestionTypes.size();
	}
	
	/**
	 * 获取成绩
	 * @return
	 */
	public String getScoreAsText() {
		if(status==ExamService.EXAM_TEST_STATUS_TESTING) {
			return null;
		}
		else if(status==ExamService.EXAM_TEST_STATUS_CANCEL) {
			return "弃考";
		}
		else if(status==ExamService.EXAM_TEST_STATUS_TOCONRRECT) {
			return "待批改";
		}
		return new DecimalFormat("#.#").format(score);
	}
	
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
}