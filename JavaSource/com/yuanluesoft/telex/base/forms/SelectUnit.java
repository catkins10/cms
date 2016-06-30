package com.yuanluesoft.telex.base.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SelectUnit extends ActionForm {
	private String param;				//URL参数:参数
	private String script;				//URL参数:选择后执行的脚本
	private String title;				//URL参数:对话框标题
	private String separator;			//URL参数:分隔符,缺省为逗号
	private List telegramUnits; 		//单位列表
	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
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
	/**
	 * @return the telegramUnits
	 */
	public List getTelegramUnits() {
		return telegramUnits;
	}
	/**
	 * @param telegramUnits the telegramUnits to set
	 */
	public void setTelegramUnits(List telegramUnits) {
		this.telegramUnits = telegramUnits;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}