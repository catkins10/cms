package com.yuanluesoft.cms.messageboard.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 留言板:常见问题(cms_message_board_faq)
 * @author linchuan
 *
 */
public class MessageBoardFaq extends Record {
	private String question; //问题
	private String answer; //答案
	private String firstKeyword; //第一个关键词
	private String otherKeywords; //其它关键词
	private long siteId; //绑定的站点ID
	
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the firstKeyword
	 */
	public String getFirstKeyword() {
		return firstKeyword;
	}
	/**
	 * @param firstKeyword the firstKeyword to set
	 */
	public void setFirstKeyword(String firstKeyword) {
		this.firstKeyword = firstKeyword;
	}
	/**
	 * @return the otherKeywords
	 */
	public String getOtherKeywords() {
		return otherKeywords;
	}
	/**
	 * @param otherKeywords the otherKeywords to set
	 */
	public void setOtherKeywords(String otherKeywords) {
		this.otherKeywords = otherKeywords;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}