/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.resource.SubForm;

/**
 * 
 * @author linchuan
 *
 */
public class Activity extends Element {
	private WorkflowProcess workflowProcess; //流程过程
	private double deadlineCondition; //截止期限
	private String deadlineException; //超时触发的异常名称
	//表单相关属性
	private SubForm subForm; //子表单
	private List actionList; //操作列表
	//办理人、查询人
	private List participantList; //办理人列表
	private List visitorList; //查询人列表
	//办理方式相关属性
	private List transactModeList; //办理方式列表
	private boolean autoSend; //是否自动发送
	private boolean transmitEnable; //是否允许转办
	private boolean addParticipantsEnable; //是否允许自行增加办理人
	private boolean agentDisable; //是否禁止代理人办理
	//直接切入流程相关属性
	private boolean direct; //是否允许从本环节直接切入流程
	private SubForm directSubForm; //直接切入时的子表单
	private List directActionList; //直接切入时的操作列表
	//退回、撤消相关属性
	private List reverseActivityList; //允许退回的环节
	private List undoActivityList; //允许从哪些环节撤消
	//子流程相关属性
	private List subFlowSubFormList; //子流程允许选择的子表单列表
	private List subFlowActionList; //子流程允许选择的操作列表
	private List subFlowDataFieldList; //子流程允许处理的字段列表
	private List subFlowApplicationList; //子流程允许执行的过程列表
	private List subFlowList; //子流程列表
	//消息通知相关属性
	private String messageLevel; //消息通知等级
	private double urgeHours; //催办周期,以小时为单位
	private boolean urgeHoursAdjustable; //催办周期可调整
	private String messageFormat; //消息格式
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.model.Element#getName()
	 */
	public String getName() {
		if(getId().equals("activity-all")) {
			return "[全部]";
		}
		else if(getId().equals("activity-previous")) {
			return "[上一环节]";
		}
		else if(getId().equals("activity-next")) {
			return "[下一环节]";
		}
		else if(getId().equals("activity-creator")) {
			return "[创建者]";
		}
		else {
			return super.getName();
		}
	}
	/**
	 * @return Returns the actionList.
	 */
	public List getActionList() {
		return actionList;
	}
	/**
	 * @param actionList The actionList to set.
	 */
	public void setActionList(List actionList) {
		this.actionList = actionList;
	}
    /**
     * @return Returns the agentDisable.
     */
    public boolean isAgentDisable() {
        return agentDisable;
    }
    /**
     * @param agentDisable The agentDisable to set.
     */
    public void setAgentDisable(boolean agentDisable) {
        this.agentDisable = agentDisable;
    }
	/**
	 * @return Returns the autoSend.
	 */
	public boolean isAutoSend() {
		return autoSend;
	}
	/**
	 * @param autoSend The autoSend to set.
	 */
	public void setAutoSend(boolean autoSend) {
		this.autoSend = autoSend;
	}
	/**
	 * @return Returns the deadlineCondition.
	 */
	public double getDeadlineCondition() {
		return deadlineCondition;
	}
	/**
	 * @param deadlineCondition The deadlineCondition to set.
	 */
	public void setDeadlineCondition(double deadlineCondition) {
		this.deadlineCondition = deadlineCondition;
	}
	/**
	 * @return Returns the deadlineException.
	 */
	public String getDeadlineException() {
		return deadlineException;
	}
	/**
	 * @param deadlineException The deadlineException to set.
	 */
	public void setDeadlineException(String deadlineException) {
		this.deadlineException = deadlineException;
	}
	/**
	 * @return Returns the direct.
	 */
	public boolean isDirect() {
		return direct;
	}
	/**
	 * @param direct The direct to set.
	 */
	public void setDirect(boolean direct) {
		this.direct = direct;
	}
	/**
	 * @return Returns the directActionList.
	 */
	public List getDirectActionList() {
		return directActionList;
	}
	/**
	 * @param directActionList The directActionList to set.
	 */
	public void setDirectActionList(List directActionList) {
		this.directActionList = directActionList;
	}
	/**
	 * @return Returns the directSubForm.
	 */
	public SubForm getDirectSubForm() {
		return directSubForm;
	}
	/**
	 * @param directSubForm The directSubForm to set.
	 */
	public void setDirectSubForm(SubForm directSubForm) {
		this.directSubForm = directSubForm;
	}
	/**
	 * @return Returns the participantList.
	 */
	public List getParticipantList() {
		return participantList;
	}
	/**
	 * @param participantList The participantList to set.
	 */
	public void setParticipantList(List participantList) {
		this.participantList = participantList;
	}
	/**
	 * @return Returns the queryList.
	 */
	public List getVisitorList() {
		return visitorList;
	}
	/**
	 * @param queryList The queryList to set.
	 */
	public void setVisitorList(List queryList) {
		this.visitorList = queryList;
	}
	/**
	 * @return Returns the reverseActivityList.
	 */
	public List getReverseActivityList() {
		return reverseActivityList;
	}
	/**
	 * @param reverseActivityList The reverseActivityList to set.
	 */
	public void setReverseActivityList(List reverseActivityList) {
		this.reverseActivityList = reverseActivityList;
	}
	/**
	 * @return Returns the subFlowActionList.
	 */
	public List getSubFlowActionList() {
		return subFlowActionList;
	}
	/**
	 * @param subFlowActionList The subFlowActionList to set.
	 */
	public void setSubFlowActionList(List subFlowActionList) {
		this.subFlowActionList = subFlowActionList;
	}
	/**
	 * @return Returns the subFlowList.
	 */
	public List getSubFlowList() {
		return subFlowList;
	}
	/**
	 * @param subFlowList The subFlowList to set.
	 */
	public void setSubFlowList(List subFlowList) {
		this.subFlowList = subFlowList;
	}
	/**
	 * @return Returns the subFlowSubFormList.
	 */
	public List getSubFlowSubFormList() {
		return subFlowSubFormList;
	}
	/**
	 * @param subFlowSubFormList The subFlowSubFormList to set.
	 */
	public void setSubFlowSubFormList(List subFlowSubFormList) {
		this.subFlowSubFormList = subFlowSubFormList;
	}
	/**
	 * @return Returns the subForm.
	 */
	public SubForm getSubForm() {
		return subForm;
	}
	/**
	 * @param subForm The subForm to set.
	 */
	public void setSubForm(SubForm subForm) {
		this.subForm = subForm;
	}
	/**
	 * @return Returns the transactModeList.
	 */
	public List getTransactModeList() {
		return transactModeList;
	}
	/**
	 * @param transactModeList The transactModeList to set.
	 */
	public void setTransactModeList(List transactModeList) {
		this.transactModeList = transactModeList;
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
	 * @return Returns the undoActivityList.
	 */
	public List getUndoActivityList() {
		return undoActivityList;
	}
	/**
	 * @param undoActivityList The undoActivityList to set.
	 */
	public void setUndoActivityList(List undoActivityList) {
		this.undoActivityList = undoActivityList;
	}
	
	/**
	 * @return Returns the subFlowApplicationList.
	 */
	public List getSubFlowApplicationList() {
		return subFlowApplicationList;
	}
	/**
	 * @param subFlowApplicationList The subFlowApplicationList to set.
	 */
	public void setSubFlowApplicationList(List subFlowApplicationList) {
		this.subFlowApplicationList = subFlowApplicationList;
	}
	/**
	 * @return Returns the subFlowDataFieldList.
	 */
	public List getSubFlowDataFieldList() {
		return subFlowDataFieldList;
	}
	/**
	 * @param subFlowDataFieldList The subFlowDataFieldList to set.
	 */
	public void setSubFlowDataFieldList(List subFlowDataFieldList) {
		this.subFlowDataFieldList = subFlowDataFieldList;
	}
	/**
	 * @return Returns the workflowProcess.
	 */
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}
	/**
	 * @param workflowProcess The workflowProcess to set.
	 */
	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}
	/**
	 * @return the messageLevel
	 */
	public String getMessageLevel() {
		return messageLevel;
	}
	/**
	 * @param messageLevel the messageLevel to set
	 */
	public void setMessageLevel(String messageLevel) {
		this.messageLevel = messageLevel;
	}
	/**
	 * @return the messageFormat
	 */
	public String getMessageFormat() {
		return messageFormat;
	}
	/**
	 * @param messageFormat the messageFormat to set
	 */
	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
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
	/**
	 * @return the urgeHours
	 */
	public double getUrgeHours() {
		return urgeHours;
	}
	/**
	 * @param urgeHours the urgeHours to set
	 */
	public void setUrgeHours(double urgeHours) {
		this.urgeHours = urgeHours;
	}
	/**
	 * @return the urgeHoursAdjustable
	 */
	public boolean isUrgeHoursAdjustable() {
		return urgeHoursAdjustable;
	}
	/**
	 * @param urgeHoursAdjustable the urgeHoursAdjustable to set
	 */
	public void setUrgeHoursAdjustable(boolean urgeHoursAdjustable) {
		this.urgeHoursAdjustable = urgeHoursAdjustable;
	}
}