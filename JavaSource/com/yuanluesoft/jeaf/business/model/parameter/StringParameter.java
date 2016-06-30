package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数：文本类型
 * @author linchuan
 *
 */
public class StringParameter {
	private String defaultValue; //默认值
	private String numerationFormat; //编号规则,defaultValue为{NUMERATION}时有效
	private boolean singleByteCharacters; //是否单字节字符,如果不是,文本输入框的最大长度=字段长度/2
	
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
	/**
	 * @return the singleByteCharacters
	 */
	public boolean isSingleByteCharacters() {
		return singleByteCharacters;
	}
	/**
	 * @param singleByteCharacters the singleByteCharacters to set
	 */
	public void setSingleByteCharacters(boolean singleByteCharacters) {
		this.singleByteCharacters = singleByteCharacters;
	}
}