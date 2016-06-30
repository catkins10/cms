package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:选择对话框
 * @author linchuan
 *
 */
public class SelectParameter {
	private String js; //需要引入的js文件
	private String execute; //选择时执行的脚本
	private boolean multiSelect; //是否多选
	private String separator; //分隔符
	private boolean selectOnly; //是否只能选择
	private String selectButtonStyleClass; //选择按钮样式
	
	/**
	 * @return the execute
	 */
	public String getExecute() {
		return execute;
	}
	/**
	 * @param execute the execute to set
	 */
	public void setExecute(String execute) {
		this.execute = execute;
	}
	/**
	 * @return the js
	 */
	public String getJs() {
		return js;
	}
	/**
	 * @param js the js to set
	 */
	public void setJs(String js) {
		this.js = js;
	}
	/**
	 * @return the selectButtonStyleClass
	 */
	public String getSelectButtonStyleClass() {
		return selectButtonStyleClass;
	}
	/**
	 * @param selectButtonStyleClass the selectButtonStyleClass to set
	 */
	public void setSelectButtonStyleClass(String selectButtonStyleClass) {
		this.selectButtonStyleClass = selectButtonStyleClass;
	}
	/**
	 * @return the selectOnly
	 */
	public boolean isSelectOnly() {
		return selectOnly;
	}
	/**
	 * @param selectOnly the selectOnly to set
	 */
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}
	/**
	 * @return the multiSelect
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}
	/**
	 * @param multiSelect the multiSelect to set
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
}