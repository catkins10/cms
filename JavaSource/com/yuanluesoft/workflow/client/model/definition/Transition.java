/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import java.util.List;

import com.yuanluesoft.workflow.client.model.resource.DataField;

/**
 * 
 * @author linchuan
 *
 */
public class Transition extends com.yuanluesoft.jeaf.graphicseditor.model.Transition {
	private WorkflowProcess workflowProcess; //流程过程
	private String conditionDescription; //条件描述
	private String conditionType; //条件类型:CONDITION/DEFAULTEXCEPTION/EXCEPTION/OTHERWISE
	private List conditionDetailList; //条件列表
	private DataField signField; //签名字段
	private String signMode; //签名方式(普通/手写)
	
	/**
	 * 获取标题
	 * @return
	 */
	public String getTitle() {
		String title = getName();
		if(title==null || title.equals("")) {
			if("CONDITION".equals(getConditionType())) {
				if(getConditionDetailList()!=null) {
					ConditionDetail conditionDetail = (ConditionDetail)getConditionDetailList().get(0);
					title = conditionDetail.getTitle().substring(2) + (getConditionDetailList().size()>1 ? "...":"");
				}
			}
			else if("OTHERWISE".equals(getConditionType())) {
				title = "其他";
			}
			else if("EXCEPTION".equals(getConditionType())) {
				title = "异常";
			}
		}
		return title;
	}
	
	/**
	 * @return Returns the conditionDescription.
	 */
	public String getConditionDescription() {
		return conditionDescription;
	}
	/**
	 * @param conditionDescription The conditionDescription to set.
	 */
	public void setConditionDescription(String conditionDescription) {
		this.conditionDescription = conditionDescription;
	}
	/**
	 * @return Returns the conditionDetailList.
	 */
	public List getConditionDetailList() {
		return conditionDetailList;
	}
	/**
	 * @param conditionDetailList The conditionDetailList to set.
	 */
	public void setConditionDetailList(List conditionDetailList) {
		this.conditionDetailList = conditionDetailList;
	}
	/**
	 * @return Returns the conditionType.
	 */
	public String getConditionType() {
		return conditionType;
	}
	/**
	 * @param conditionType The conditionType to set.
	 */
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
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
	 * @return Returns the signField.
	 */
	public DataField getSignField() {
		return signField;
	}
	/**
	 * @param signField The signField to set.
	 */
	public void setSignField(DataField signField) {
		this.signField = signField;
	}
	/**
	 * @return Returns the signMode.
	 */
	public String getSignMode() {
		return signMode;
	}
	/**
	 * @param signMode The signMode to set.
	 */
	public void setSignMode(String signMode) {
		this.signMode = signMode;
	}
}
