package com.yuanluesoft.microblog.model;

/**
 * 微博平台参数定义
 * @author linchuan
 *
 */
public class MicroblogPlatformParameterDefine {
	private String label; //标题
	private String name; //参数名称
	private String remark; //填写说明
	private String defaultValue; //默认值
	private boolean isPassword; //是否密码
	
	public MicroblogPlatformParameterDefine(String label, String name, String remark, String defaultValue, boolean isPassword) {
		super();
		this.label = label;
		this.name = name;
		this.remark = remark;
		this.defaultValue = defaultValue;
		this.isPassword = isPassword;
	}
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * @return the isPassword
	 */
	public boolean isPassword() {
		return isPassword;
	}

	/**
	 * @param isPassword the isPassword to set
	 */
	public void setPassword(boolean isPassword) {
		this.isPassword = isPassword;
	}
}