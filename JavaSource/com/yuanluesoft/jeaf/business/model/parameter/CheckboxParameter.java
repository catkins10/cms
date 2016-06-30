package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:复选框
 * @author linchuan
 *
 */
public class CheckboxParameter {
	private String value; //值
	private String label; //标题
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}