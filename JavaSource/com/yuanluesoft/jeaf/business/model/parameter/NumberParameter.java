package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数：数字类型
 * @author linchuan
 *
 */
public class NumberParameter {
	private String minValue; //最小值
	private String maxValue; //最大值
	private String displayFormat; //显示格式
	private String defaultValue; //默认值
	private String numerationFormat; //defaultValue为{NUMERATION}时有效
	
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
	 * @return the maxValue
	 */
	public String getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the minValue
	 */
	public String getMinValue() {
		return minValue;
	}
	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
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
	/**
	 * @return the numerationFormat
	 */
	public String getNumerationFormat() {
		return numerationFormat;
	}
	/**
	 * @param numerationFormat the numerationFormat to set
	 */
	public void setNumerationFormat(String numerationFormat) {
		this.numerationFormat = numerationFormat;
	}
}