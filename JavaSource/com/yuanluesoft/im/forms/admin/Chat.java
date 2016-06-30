package com.yuanluesoft.im.forms.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Chat extends ActionForm {
	private int isGroupChat; //是否讨论组
	private int isCustomerService; //是否客服对话
	private int isEnd; //是否结束,给客服使用
	private long siteId; //站点ID,给客服使用
	private Timestamp created; //创建时间
	private Set chatPersons; //对话用户
	
	//扩展属性
	private List talks; //发言记录
	
	/**
	 * @return the chatPersons
	 */
	public Set getChatPersons() {
		return chatPersons;
	}
	/**
	 * @param chatPersons the chatPersons to set
	 */
	public void setChatPersons(Set chatPersons) {
		this.chatPersons = chatPersons;
	}
	/**
	 * @return the isGroupChat
	 */
	public int getIsGroupChat() {
		return isGroupChat;
	}
	/**
	 * @param isGroupChat the isGroupChat to set
	 */
	public void setIsGroupChat(int isGroupChat) {
		this.isGroupChat = isGroupChat;
	}
	/**
	 * @return the isCustomerService
	 */
	public int getIsCustomerService() {
		return isCustomerService;
	}
	/**
	 * @param isCustomerService the isCustomerService to set
	 */
	public void setIsCustomerService(int isCustomerService) {
		this.isCustomerService = isCustomerService;
	}
	/**
	 * @return the isEnd
	 */
	public int getIsEnd() {
		return isEnd;
	}
	/**
	 * @param isEnd the isEnd to set
	 */
	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
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
	 * @return the talks
	 */
	public List getTalks() {
		return talks;
	}
	/**
	 * @param talks the talks to set
	 */
	public void setTalks(List talks) {
		this.talks = talks;
	}
}