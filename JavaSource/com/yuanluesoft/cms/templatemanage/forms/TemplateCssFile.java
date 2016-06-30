package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateCssFile extends ActionForm {
	private String fileName; //URL参数:CSS文件名称
	private String script; //URL参数:CSS保存后执行的脚本
	private String cssContent; //css内容

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the cssContent
	 */
	public String getCssContent() {
		return cssContent;
	}

	/**
	 * @param cssContent the cssContent to set
	 */
	public void setCssContent(String cssContent) {
		this.cssContent = cssContent;
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