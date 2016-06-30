/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.definition.TransactMode;

/**
 * 
 * @author linchuan
 *
 */
public class ActivityInstance extends com.yuanluesoft.workflow.client.model.wapi.ActivityInstance implements Serializable {
	private Timestamp created; //创建时间
	private double deadline; //办理期限
	private String transactMode; //办理方式
	private int voteNumber; //办理方式为表决时的有效票数
	private List workItemList; //工作项列表
	private WorkItem lastCompletedWorkItem; //最后结束的工作项
	private com.yuanluesoft.workflow.client.model.wapi.ActivityInstance previousActivityInstance; //上一环节
	private Element previousParticipant; //上一办理人
	
	public String getTransactModeTitle() {
		if(TransactMode.TRANSACT_MODE_SINGLE.equals(transactMode)) {
			return "单人";
		}
		else if(TransactMode.TRANSACT_MODE_SEQUENCE.equals(transactMode)) {
			return "多人顺序";
		}
		else if(TransactMode.TRANSACT_MODE_PARALLEL.equals(transactMode)) {
			return "多人并行";
		}
		else if(TransactMode.TRANSACT_MODE_VOTE.equals(transactMode)) {
			return "表决(" + voteNumber + ")";
		}
		return "";
	}

	/**
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return Returns the deadline.
	 */
	public double getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline The deadline to set.
	 */
	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	/**
	 * @return Returns the transactMode.
	 */
	public String getTransactMode() {
		return transactMode;
	}
	/**
	 * @param transactMode The transactMode to set.
	 */
	public void setTransactMode(String transactMode) {
		this.transactMode = transactMode;
	}
	/**
	 * @return Returns the voteNumber.
	 */
	public int getVoteNumber() {
		return voteNumber;
	}
	/**
	 * @param voteNumber The voteNumber to set.
	 */
	public void setVoteNumber(int voteNumber) {
		this.voteNumber = voteNumber;
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
	public com.yuanluesoft.workflow.client.model.wapi.ActivityInstance getPreviousActivityInstance() {
		return previousActivityInstance;
	}
	/**
	 * @param previousActivityInstance The previousActivityInstance to set.
	 */
	public void setPreviousActivityInstance(com.yuanluesoft.workflow.client.model.wapi.ActivityInstance previousActivityInstance) {
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
	/**
	 * @return Returns the lastCompletedWorkItem.
	 */
	public WorkItem getLastCompletedWorkItem() {
		return lastCompletedWorkItem;
	}
	/**
	 * @param lastCompletedWorkItem The lastCompletedWorkItem to set.
	 */
	public void setLastCompletedWorkItem(WorkItem lastCompletedWorkItem) {
		this.lastCompletedWorkItem = lastCompletedWorkItem;
	}
}
