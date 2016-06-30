package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemCodeRule extends ActionForm {
	private String itemType; //事项类型,行政许可|行政处罚|行政确认|行政征收|行政强制|行政裁决|行政监督检查|行政给付|其他行政行为
	private String rule; //规则
	private char manualCodeEnabled = '0'; //是否允许手工编号
	
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the manualCodeEnabled
	 */
	public char getManualCodeEnabled() {
		return manualCodeEnabled;
	}
	/**
	 * @param manualCodeEnabled the manualCodeEnabled to set
	 */
	public void setManualCodeEnabled(char manualCodeEnabled) {
		this.manualCodeEnabled = manualCodeEnabled;
	}
	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}
}