package com.yuanluesoft.enterprise.exam.model;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.enterprise.exam.pojo.ExamPaperQuestion;

/**
 * 答卷题型
 * @author linchuan
 *
 */
public class ExamTestQuestionType {
	private String questionType; //题型
	private List examTestQuestions; //题目

	/**
	 * 获取总分
	 * @return
	 */
	public double getScore() {
		if(examTestQuestions==null || examTestQuestions.isEmpty()) {
			return 0;
		}
		double score = 0;
		for(Iterator iterator = examTestQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			score += examPaperQuestion.getQuestionScore();
		}
		return score;
	}
	
	/**
	 * 获取得分
	 * @return
	 */
	public String getTestScore() {
		if(examTestQuestions==null || examTestQuestions.isEmpty()) {
			return "0";
		}
		double score = 0;
		for(Iterator iterator = examTestQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			if(examPaperQuestion.getExamTestAnswer().getCorrectorId()==0) {
				return "";
			}
			score += examPaperQuestion.getExamTestAnswer().getScore();
		}
		return new DecimalFormat("#.#").format(score);
	}
	
	/**
	 * 获取批改人
	 * @return
	 */
	public String getCorrectors() {
		if(examTestQuestions==null || examTestQuestions.isEmpty()) {
			return null;
		}
		String correctors = null;
		for(Iterator iterator = examTestQuestions.iterator(); iterator.hasNext();) {
			ExamPaperQuestion examPaperQuestion = (ExamPaperQuestion)iterator.next();
			if(examPaperQuestion.getExamTestAnswer().getCorrectorId()>10) {
				if(correctors==null) {
					correctors = examPaperQuestion.getExamTestAnswer().getCorrector();
				}
				else if(("、" + correctors + "、").indexOf("、" + examPaperQuestion.getExamTestAnswer().getCorrector() + "、")==-1) {
					correctors += "、" + examPaperQuestion.getExamTestAnswer().getCorrector();
				}
			}
		}
		return correctors;
	}
	
	/**
	 * 获取题目数
	 * @return
	 */
	public double getQuestionNumber() {
		return examTestQuestions==null ? 0 : examTestQuestions.size();
	}
	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}
	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the examTestQuestions
	 */
	public List getExamTestQuestions() {
		return examTestQuestions;
	}

	/**
	 * @param examTestQuestions the examTestQuestions to set
	 */
	public void setExamTestQuestions(List examTestQuestions) {
		this.examTestQuestions = examTestQuestions;
	}
}