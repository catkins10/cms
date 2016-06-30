package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:隐藏字段
 * @author linchuan
 *
 */
public class HiddenParameter {
	private boolean newFormOnly = false; //是否只在创建新记录时有效,默认false

	/**
	 * @return the newFormOnly
	 */
	public boolean isNewFormOnly() {
		return newFormOnly;
	}

	/**
	 * @param newFormOnly the newFormOnly to set
	 */
	public void setNewFormOnly(boolean newFormOnly) {
		this.newFormOnly = newFormOnly;
	}
}