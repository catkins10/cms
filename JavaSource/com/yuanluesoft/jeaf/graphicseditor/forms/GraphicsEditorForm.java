package com.yuanluesoft.jeaf.graphicseditor.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 图形编辑器表单
 * @author chuan
 *
 */
public class GraphicsEditorForm extends ActionForm {
	private String json; //配置
	private String js; //脚本

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
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}
}