package com.yuanluesoft.jeaf.dialog.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ColorDialog extends ActionForm {
	private String colorValue; //颜色值
	private String script; //选择后执行的脚本
	
	/**
	 * @return the colorValue
	 */
	public String getColorValue() {
		return colorValue;
	}
	/**
	 * @param colorValue the colorValue to set
	 */
	public void setColorValue(String colorValue) {
		this.colorValue = colorValue;
	}
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
}