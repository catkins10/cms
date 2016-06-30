/*
 * Created on 2006-9-18
 *
 */
package com.yuanluesoft.j2oa.receival.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 模板配置(receival_config_template)
 * @author linchuan
 *
 */
public class ReceivalTemplateConfig extends Record {
	private String handlingHtmlTemplate; //办理单HTML模板
	
	/**
	 * @return the handlingHtmlTemplate
	 */
	public String getHandlingHtmlTemplate() {
		return handlingHtmlTemplate;
	}
	/**
	 * @param handlingHtmlTemplate the handlingHtmlTemplate to set
	 */
	public void setHandlingHtmlTemplate(String handlingHtmlTemplate) {
		this.handlingHtmlTemplate = handlingHtmlTemplate;
	}
}