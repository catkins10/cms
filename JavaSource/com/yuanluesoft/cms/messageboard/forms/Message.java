package com.yuanluesoft.cms.messageboard.forms;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceForm;

/**
 * 
 * @author linchuan
 *
 */
public class Message extends PublicServiceForm {
	private boolean continueSubmit; //继续提交
	private String serializableRecord; //序列化后的记录
	private String faqQuestion; //匹配的常见问题
	private String faqAnswer; //匹配的常见问题解答
	
	/**
	 * @return the continueSubmit
	 */
	public boolean isContinueSubmit() {
		return continueSubmit;
	}
	/**
	 * @param continueSubmit the continueSubmit to set
	 */
	public void setContinueSubmit(boolean continueSubmit) {
		this.continueSubmit = continueSubmit;
	}
	/**
	 * @return the faqAnswer
	 */
	public String getFaqAnswer() {
		return faqAnswer;
	}
	/**
	 * @param faqAnswer the faqAnswer to set
	 */
	public void setFaqAnswer(String faqAnswer) {
		this.faqAnswer = faqAnswer;
	}
	/**
	 * @return the faqQuestion
	 */
	public String getFaqQuestion() {
		return faqQuestion;
	}
	/**
	 * @param faqQuestion the faqQuestion to set
	 */
	public void setFaqQuestion(String faqQuestion) {
		this.faqQuestion = faqQuestion;
	}
	/**
	 * @return the serializableRecord
	 */
	public String getSerializableRecord() {
		return serializableRecord;
	}
	/**
	 * @param serializableRecord the serializableRecord to set
	 */
	public void setSerializableRecord(String serializableRecord) {
		this.serializableRecord = serializableRecord;
	}
}