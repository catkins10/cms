package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:时间输入
 * @author linchuan
 *
 */
public class TimeInputParameter {
	private boolean secondEnabled = false; //是否要输入秒,默认false

	/**
	 * @return the secondEnabled
	 */
	public boolean isSecondEnabled() {
		return secondEnabled;
	}

	/**
	 * @param secondEnabled the secondEnabled to set
	 */
	public void setSecondEnabled(boolean secondEnabled) {
		this.secondEnabled = secondEnabled;
	}
}