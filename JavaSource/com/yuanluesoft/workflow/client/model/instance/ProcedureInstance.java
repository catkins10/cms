/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.definition.Procedure;
import com.yuanluesoft.workflow.client.model.wapi.ActivityInstance;

/**
 * 
 * @author linchuan
 *
 */
public class ProcedureInstance extends ActivityInstance implements Serializable {
	private Timestamp routed; //路由时间
	private Procedure procedureDefinition; //执行过程模型
	private List applicationReturnList; //过程执行结果
	private List workItemList; //工作项列表
	private ActivityInstance previousActivityInstance; //上一环节
	private Element previousParticipant; //上一办理人
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
	 * @return Returns the procedureDefinition.
	 */
	public Procedure getProcedureDefinition() {
		return procedureDefinition;
	}
	/**
	 * @param procedureDefinition The procedureDefinition to set.
	 */
	public void setProcedureDefinition(Procedure procedureDefinition) {
		this.procedureDefinition = procedureDefinition;
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
	/**
	 * @return Returns the workItemList.
	 */
	public List getWorkItemList() {
		return workItemList;
	}
	/**
	 * @param workItemList The workItemList to set.
	 */
	public void setWorkItemList(List workItemList) {
		this.workItemList = workItemList;
	}
	/**
	 * @return Returns the previousActivityInstance.
	 */
	public ActivityInstance getPreviousActivityInstance() {
		return previousActivityInstance;
	}
	/**
	 * @param previousActivityInstance The previousActivityInstance to set.
	 */
	public void setPreviousActivityInstance(
			ActivityInstance previousActivityInstance) {
		this.previousActivityInstance = previousActivityInstance;
	}
	/**
	 * @return Returns the previousParticipant.
	 */
	public Element getPreviousParticipant() {
		return previousParticipant;
	}
	/**
	 * @param previousParticipant The previousParticipant to set.
	 */
	public void setPreviousParticipant(Element previousParticipant) {
		this.previousParticipant = previousParticipant;
	}
}
