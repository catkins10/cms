package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:项目分类(exam_question_item)
 * @author linchuan
 *
 */
public class QuestionItem extends Record {
	private long questionId; //试题ID
	private long itemId; //项目分类ID
	private String item; //项目分类
	
	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}
	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
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