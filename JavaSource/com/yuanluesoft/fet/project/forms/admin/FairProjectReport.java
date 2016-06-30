package com.yuanluesoft.fet.project.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FairProjectReport extends ActionForm {
	private String fairName; //选中的活动
	private int fairNumber; //活动界别
	/**
	 * @return the fairName
	 */
	public String getFairName() {
		return fairName;
	}
	/**
	 * @param fairName the fairName to set
	 */
	public void setFairName(String fairName) {
		this.fairName = fairName;
	}
	/**
	 * @return the fairNumber
	 */
	public int getFairNumber() {
		return fairNumber;
	}
	/**
	 * @param fairNumber the fairNumber to set
	 */
	public void setFairNumber(int fairNumber) {
		this.fairNumber = fairNumber;
	}
}