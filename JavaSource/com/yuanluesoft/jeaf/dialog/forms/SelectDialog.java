package com.yuanluesoft.jeaf.dialog.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SelectDialog extends ActionForm {
	private boolean multiSelect; //URL参数:是否多选
	private String param; //URL参数:参数
	private String script; //URL参数:选择后执行的脚本
	private String title; //URL参数:对话框标题
	private String key; //URL参数:对话框过滤
	private String separator; //URL参数:分隔符,缺省为逗号
	
	private boolean anonymousAlways; //是否强制为匿名访问
	private boolean closeDialogOnOK = true; //“确定”后关闭对话框
	
	/**
	 * @return Returns the multiSelect.
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}
	/**
	 * @param multiSelect The multiSelect to set.
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	/**
	 * @return Returns the param.
	 */
	public String getParam() {
		return param;
	}
	/**
	 * @param param The param to set.
	 */
	public void setParam(String param) {
		this.param = param;
	}
	/**
	 * @return Returns the script.
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script The script to set.
	 */
	public void setScript(String script) {
		this.script = script;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		setFormTitle(title);
		this.title = title;
	}
	/**
	 * @return Returns the key.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return Returns the separator.
	 */
	public String getSeparator() {
		if(separator==null || separator.equals("")) {
			separator = ",";
		}
		return separator;
	}
	/**
	 * @param separator The separator to set.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	/**
	 * @return the anonymousAlways
	 */
	public boolean isAnonymousAlways() {
		return anonymousAlways;
	}
	/**
	 * @param anonymousAlways the anonymousAlways to set
	 */
	public void setAnonymousAlways(boolean anonymousAlways) {
		this.anonymousAlways = anonymousAlways;
	}
	/**
	 * @return the closeDialogOnOK
	 */
	public boolean isCloseDialogOnOK() {
		return closeDialogOnOK;
	}
	/**
	 * @param closeDialogOnOK the closeDialogOnOK to set
	 */
	public void setCloseDialogOnOK(boolean closeDialogOnOK) {
		this.closeDialogOnOK = closeDialogOnOK;
	}
}