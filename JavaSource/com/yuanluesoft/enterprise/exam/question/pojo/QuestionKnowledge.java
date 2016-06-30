package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:知识类别(exam_question_knowledge)
 * @author linchuan
 *
 */
public class QuestionKnowledge extends Record {
	private long questionId; //试题ID
	private long knowledgeId; //知识类别ID
	private String knowledge; //知识类别
	
	/**
	 * @return the knowledge
	 */
	public String getKnowledge() {
		return knowledge;
	}
	/**
	 * @param knowledge the knowledge to set
	 */
	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}
	/**
	 * @return the knowledgeId
	 */
	public long getKnowledgeId() {
		return knowledgeId;
	}
	/**
	 * @param knowledgeId the knowledgeId to set
	 */
	public void setKnowledgeId(long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	/**
	 * @return the questionId
	 */
	public long getQuestionId() {
		return questionId;
	}
	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
}