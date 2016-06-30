/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.workflow.client.model.definition.Transition;
import com.yuanluesoft.workflow.client.model.wapi.ProcessInstance;

/**
 * 
 * @author linchuan
 *
 */
public class TransitionInstance implements Serializable {
	private Transition transitionDefine;
	private String transitionDefineId; //连接ID
	private String fromElementId; //入口元素ID
	private String toElementId; //出口元素ID
	private Timestamp created; //创建时间
	private List fieldValueList; //字段值列表,流程跳转的依据
	private ProcessInstance processInstance; //过程实例
	
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
	 * @return Returns the fieldValueList.
	 */
	public List getFieldValueList() {
		return fieldValueList;
	}
	/**
	 * @param fieldValueList The fieldValueList to set.
	 */
	public void setFieldValueList(List fieldValueList) {
		this.fieldValueList = fieldValueList;
	}
	
	/**
	 * @return Returns the fromElementId.
	 */
	public String getFromElementId() {
		return fromElementId;
	}
	/**
	 * @param fromElementId The fromElementId to set.
	 */
	public void setFromElementId(String fromElementId) {
		this.fromElementId = fromElementId;
	}
	/**
	 * @return Returns the toElementId.
	 */
	public String getToElementId() {
		return toElementId;
	}
	/**
	 * @param toElementId The toElementId to set.
	 */
	public void setToElementId(String toElementId) {
		this.toElementId = toElementId;
	}
	/**
	 * @return Returns the transitionDefineId.
	 */
	public String getTransitionDefineId() {
		return transitionDefineId;
	}
	/**
	 * @param transitionDefineId The transitionDefineId to set.
	 */
	public void setTransitionDefineId(String transitionDefineId) {
		this.transitionDefineId = transitionDefineId;
	}
	/**
	 * @return Returns the processInstance.
	 */
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	/**
	 * @param processInstance The processInstance to set.
	 */
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	/**
	 * @return Returns the transitionDefine.
	 */
	public Transition getTransitionDefine() {
		return transitionDefine;
	}
	/**
	 * @param transitionDefine The transitionDefine to set.
	 */
	public void setTransitionDefine(Transition transitionDefine) {
		this.transitionDefine = transitionDefine;
	}
}
