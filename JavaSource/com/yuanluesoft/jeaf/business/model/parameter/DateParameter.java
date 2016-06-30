package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数：日期类型
 * @author linchuan
 *
 */
public class DateParameter {
	private String defaultValue; //默认值
	private String displayFormat; //显示格式

	/**
	 * @return the displayFormat
	 */
	public String getDisplayFormat() {
		return displayFormat;
	}

	/**
	 * @param displayFormat the displayFormat to set
	 */
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}