package com.yuanluesoft.cms.onlineservice.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 网上办事:常见问题解答(onlineservice_item_faq)
 * @author linchuan
 *
 */
public class OnlineServiceItemFaq extends Record {
	private long itemId; //办理事项ID
	private String question; //问题
	private String answer; //解答
	private double priority; //优先级
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private OnlineServiceItem item; //办理事项
	
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
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
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
	 * @return the item
	 */
	public OnlineServiceItem getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(OnlineServiceItem item) {
		this.item = item;
	}
}