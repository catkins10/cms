package com.yuanluesoft.jeaf.opinionmanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 意见类型配置(opinion_type)
 * @author linchuan
 *
 */
public class OpinionType extends Record {
	private String businessClassName; //业务对象类名称
	private String opinionType; //意见类型
	private char required = '0'; //是否必须填写
	private String inputPrompt; //没有填写时提示信息,默认:尚未填写意见
	private float priority; //优先级
	
	/**
	 * @return the businessClassName
	 */
	public String getBusinessClassName() {
		return businessClassName;
	}
	/**
	 * @param businessClassName the businessClassName to set
	 */
	public void setBusinessClassName(String businessClassName) {
		this.businessClassName = businessClassName;
	}
	/**
	 * @return the opinionType
	 */
	public String getOpinionType() {
		return opinionType;
	}
	/**
	 * @param opinionType the opinionType to set
	 */
	public void setOpinionType(String opinionType) {
		this.opinionType = opinionType;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the required
	 */
	public char getRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(char required) {
		this.required = required;
	}
	/**
	 * @return the inputPrompt
	 */
	public String getInputPrompt() {
		return inputPrompt;
	}
	/**
	 * @param inputPrompt the inputPrompt to set
	 */
	public void setInputPrompt(String inputPrompt) {
		this.inputPrompt = inputPrompt;
	}
}