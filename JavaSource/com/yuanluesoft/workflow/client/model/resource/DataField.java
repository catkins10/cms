/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.workflow.client.model.resource;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class DataField extends Element implements Serializable {
	private boolean basicType; //是否是基本类型
	private String type; //类型:INTEGER/FLOAT/STRING/DATETIME/BOOLEAN/ENUM
	private String initialValue; //初始值
	private int length; //长度
	private boolean sign; //是否签名字段
	private boolean multiSign; //是否多人签名
	private String trueTitle; //当类型为BOOLEAN时,值为true时的标题
	private String falseTitle; //当类型为BOOLEAN时,值为false时的标题

	/**
	 * @return Returns the initialValue.
	 */
	public String getInitialValue() {
		return initialValue;
	}
	/**
	 * @param initialValue The initialValue to set.
	 */
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}
	/**
	 * @return Returns the length.
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length The length to set.
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return Returns the sign.
	 */
	public boolean isSign() {
		return sign;
	}
	/**
	 * @param sign The sign to set.
	 */
	public void setSign(boolean sign) {
		this.sign = sign;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return Returns the basicType.
	 */
	public boolean isBasicType() {
		return basicType;
	}
	/**
	 * @param basicType The basicType to set.
	 */
	public void setBasicType(boolean basicType) {
		this.basicType = basicType;
	}
	
	/**
	 * @return Returns the falseTitle.
	 */
	public String getFalseTitle() {
		return falseTitle;
	}
	/**
	 * @param falseTitle The falseTitle to set.
	 */
	public void setFalseTitle(String falseTitle) {
		this.falseTitle = falseTitle;
	}
	/**
	 * @return Returns the trueTitle.
	 */
	public String getTrueTitle() {
		return trueTitle;
	}
	/**
	 * @param trueTitle The trueTitle to set.
	 */
	public void setTrueTitle(String trueTitle) {
		this.trueTitle = trueTitle;
	}
	/**
	 * @return Returns the multiSign.
	 */
	public boolean isMultiSign() {
		return multiSign;
	}
	/**
	 * @param multiSign The multiSign to set.
	 */
	public void setMultiSign(boolean multiSign) {
		this.multiSign = multiSign;
	}
}
