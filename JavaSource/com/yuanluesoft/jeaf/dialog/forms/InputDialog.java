package com.yuanluesoft.jeaf.dialog.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class InputDialog extends ActionForm {
	private String script; //URL参数:选择后执行的脚本
	private List inputs; //输入字段列表

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	/**
	 * @return the inputs
	 */
	public List getInputs() {
		return inputs;
	}
	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(List inputs) {
		this.inputs = inputs;
	}
}