package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

import com.yuanluesoft.workflow.client.model.instance.WorkflowInstance;

/**
 * 流程界面
 * @author LinChuan
 *
 */
public class WorkflowInterface {
	private WorkflowInstance workflowInstance; //流程运转记录
	private String activityName; //当前环节名称
	private String activityId; //当前环节定义ID
	private String workflowName; //当前流程名称
	private String workflowDefinitionId; //当前流程ID
	private boolean workflowTest; //是个工作流测试
	private String workItemId; //工作项ID
	private int workflowInstanceState; //工作流实例状态
	private String participant; //办理人
	private String participantId; //办理人ID
	private boolean isAgent; //是否代理人
		
	private String subForm; //子表单
	private List actions; //操作按钮名称列表
	private boolean sendEnable; //是否显示发送
	private boolean completeEnable; //是否显示办理完毕
	private boolean reverseEnable; //是否显示回退
	private boolean undoEnable; //是否显示取回
	private boolean transmitEnable; //是否显示转办
	private boolean addParticipantsEnable; //是否显示增加办理人
	
	private boolean locked;
	private String lockPersonName;
	
	/**
	 * @return Returns the actions.
	 */
	public List getActions() {
		return actions;
	}
	/**
	 * @param actions The actions to set.
	 */
	public void setActions(List actions) {
		this.actions = actions;
	}
	/**
	 * @return Returns the completeEnable.
	 */
	public boolean isCompleteEnable() {
		return completeEnable;
	}
	/**
	 * @param completeEnable The completeEnable to set.
	 */
	public void setCompleteEnable(boolean completeEnable) {
		this.completeEnable = completeEnable;
	}
	/**
	 * @return Returns the reverseEnable.
	 */
	public boolean isReverseEnable() {
		return reverseEnable;
	}
	/**
	 * @param reverseEnable The reverseEnable to set.
	 */
	public void setReverseEnable(boolean reverseEnable) {
		this.reverseEnable = reverseEnable;
	}
	/**
	 * @return Returns the sendEnable.
	 */
	public boolean isSendEnable() {
		return sendEnable;
	}
	/**
	 * @param sendEnable The sendEnable to set.
	 */
	public void setSendEnable(boolean sendEnable) {
		this.sendEnable = sendEnable;
	}
	/**
	 * @return Returns the subForm.
	 */
	public String getSubForm() {
		return subForm;
	}
	/**
	 * @param subForm The subForm to set.
	 */
	public void setSubForm(String subForm) {
		this.subForm = subForm;
	}
	/**
	 * @return Returns the undoEnable.
	 */
	public boolean isUndoEnable() {
		return undoEnable;
	}
	/**
	 * @param undoEnable The undoEnable to set.
	 */
	public void setUndoEnable(boolean undoEnable) {
		this.undoEnable = undoEnable;
	}
	/**
	 * @return Returns the workflowInstance.
	 */
	public WorkflowInstance getWorkflowInstance() {
		return workflowInstance;
	}
	/**
	 * @param workflowInstance The workflowInstance to set.
	 */
	public void setWorkflowInstance(WorkflowInstance workflowInstance) {
		this.workflowInstance = workflowInstance;
	}
	/**
	 * @return Returns the activityName.
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName The activityName to set.
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return Returns the workflowName.
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName The workflowName to set.
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return Returns the workflowTest.
	 */
	public boolean isWorkflowTest() {
		return workflowTest;
	}
	/**
	 * @param workflowTest The workflowTest to set.
	 */
	public void setWorkflowTest(boolean workflowTest) {
		this.workflowTest = workflowTest;
	}
	/**
	 * @return Returns the workflowDefinitionId.
	 */
	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}
	/**
	 * @param workflowDefinitionId The workflowDefinitionId to set.
	 */
	public void setWorkflowDefinitionId(String workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}
	/**
	 * @return Returns the workItemId.
	 */
	public String getWorkItemId() {
		return workItemId;
	}
	/**
	 * @param workItemId The workItemId to set.
	 */
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	/**
	 * @return Returns the transmitEnable.
	 */
	public boolean isTransmitEnable() {
		return transmitEnable;
	}
	/**
	 * @param transmitEnable The transmitEnable to set.
	 */
	public void setTransmitEnable(boolean transmitEnable) {
		this.transmitEnable = transmitEnable;
	}
	/**
	 * @return Returns the activityId.
	 */
	public String getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the workflowInstanceState.
	 */
	public int getWorkflowInstanceState() {
		return workflowInstanceState;
	}
	/**
	 * @param workflowInstanceState The workflowInstanceState to set.
	 */
	public void setWorkflowInstanceState(int workflowInstanceState) {
		this.workflowInstanceState = workflowInstanceState;
	}
	/**
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		return locked;
	}
	/**
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/**
	 * @return Returns the lockPersonName.
	 */
	public String getLockPersonName() {
		return lockPersonName;
	}
	/**
	 * @param lockPersonName The lockPersonName to set.
	 */
	public void setLockPersonName(String lockPersonName) {
		this.lockPersonName = lockPersonName;
	}
    /**
     * @return Returns the isAgent.
     */
    public boolean isAgent() {
        return isAgent;
    }
    /**
     * @param isAgent The isAgent to set.
     */
    public void setAgent(boolean isAgent) {
        this.isAgent = isAgent;
    }
    /**
     * @return Returns the participant.
     */
    public String getParticipant() {
        return participant;
    }
    /**
     * @param participant The participant to set.
     */
    public void setParticipant(String participant) {
        this.participant = participant;
    }
    /**
     * @return Returns the participantId.
     */
    public String getParticipantId() {
        return participantId;
    }
    /**
     * @param participantId The participantId to set.
     */
    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
	/**
	 * @return the addParticipantsEnable
	 */
	public boolean isAddParticipantsEnable() {
		return addParticipantsEnable;
	}
	/**
	 * @param addParticipantsEnable the addParticipantsEnable to set
	 */
	public void setAddParticipantsEnable(boolean addParticipantsEnable) {
		this.addParticipantsEnable = addParticipantsEnable;
	}
}
