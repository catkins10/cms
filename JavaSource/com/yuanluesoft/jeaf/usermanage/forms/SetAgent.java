package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SetAgent extends ActionForm {
	private String userName;
	private String agentIds;
	private String agentNames;
	
	/**
	 * @return the agentIds
	 */
	public String getAgentIds() {
		return agentIds;
	}
	/**
	 * @param agentIds the agentIds to set
	 */
	public void setAgentIds(String agentIds) {
		this.agentIds = agentIds;
	}
	/**
	 * @return the agentNames
	 */
	public String getAgentNames() {
		return agentNames;
	}
	/**
	 * @param agentNames the agentNames to set
	 */
	public void setAgentNames(String agentNames) {
		this.agentNames = agentNames;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}