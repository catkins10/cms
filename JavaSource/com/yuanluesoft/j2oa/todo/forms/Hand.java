package com.yuanluesoft.j2oa.todo.forms;

import java.util.Set;


/**
 * 
 * @author LinChuan
*
 */
public class Hand extends Todo {
	private Set handPersons; //交办人列表
	//反馈
	private String feedback;
	//办理人列表
	private String handPersonNames;
	private String handPersonIds;

	/**
	 * @return Returns the feedback.
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback The feedback to set.
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return Returns the handPersonIds.
	 */
	public String getHandPersonIds() {
		return handPersonIds;
	}
	/**
	 * @param handPersonIds The handPersonIds to set.
	 */
	public void setHandPersonIds(String handPersonIds) {
		this.handPersonIds = handPersonIds;
	}
	/**
	 * @return Returns the handPersonNames.
	 */
	public String getHandPersonNames() {
		return handPersonNames;
	}
	/**
	 * @param handPersonNames The handPersonNames to set.
	 */
	public void setHandPersonNames(String handPersonNames) {
		this.handPersonNames = handPersonNames;
	}
	/**
	 * @return the handPersons
	 */
	public Set getHandPersons() {
		return handPersons;
	}
	/**
	 * @param handPersons the handPersons to set
	 */
	public void setHandPersons(Set handPersons) {
		this.handPersons = handPersons;
	}
}