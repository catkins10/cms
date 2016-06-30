/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.workflow.client.model.definition.Decision;
import com.yuanluesoft.workflow.client.model.wapi.ActivityInstance;

/**
 * 
 * @author linchuan
 *
 */
public class DecisionInstance extends ActivityInstance implements Serializable {
	private Timestamp routed; //路由时间
	private Decision decisionDefinition; //决策定义
	private List applicationReturnList; //过程执行结果
	
	/**
	 * @return Returns the routed.
	 */
	public Timestamp getRouted() {
		return routed;
	}
	/**
	 * @param routed The routed to set.
	 */
	public void setRouted(Timestamp routed) {
		this.routed = routed;
	}
	/**
	 * @return Returns the decisionDefinition.
	 */
	public Decision getDecisionDefinition() {
		return decisionDefinition;
	}
	/**
	 * @param decisionDefinition The decisionDefinition to set.
	 */
	public void setDecisionDefinition(Decision decisionDefinition) {
		this.decisionDefinition = decisionDefinition;
	}
	/**
	 * @return Returns the applicationReturnList.
	 */
	public List getApplicationReturnList() {
		return applicationReturnList;
	}
	/**
	 * @param applicationReturnList The applicationReturnList to set.
	 */
	public void setApplicationReturnList(List applicationReturnList) {
		this.applicationReturnList = applicationReturnList;
	}
}
