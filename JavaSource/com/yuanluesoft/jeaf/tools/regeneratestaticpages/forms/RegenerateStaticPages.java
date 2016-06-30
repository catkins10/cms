package com.yuanluesoft.jeaf.tools.regeneratestaticpages.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class RegenerateStaticPages extends ActionForm {
	private String regenerateMode; //模式,包括：重建全部站点和栏目页面, 按URL重建(按url，可以加"%",这样可以匹配更多的页面), 按记录类名称(遍历所有的记录,调用rebuildByModifiedObject)
	private String dynamicUrl; //动态页面的URL,regenerateMode=2时有效
	private String recordClassName; //记录类名称,regenerateMode=3时有效
	
	/**
	 * @return the dynamicUrl
	 */
	public String getDynamicUrl() {
		return dynamicUrl;
	}
	/**
	 * @param dynamicUrl the dynamicUrl to set
	 */
	public void setDynamicUrl(String dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
	/**
	 * @return the regenerateMode
	 */
	public String getRegenerateMode() {
		return regenerateMode;
	}
	/**
	 * @param regenerateMode the regenerateMode to set
	 */
	public void setRegenerateMode(String regenerateMode) {
		this.regenerateMode = regenerateMode;
	}
}