package com.yuanluesoft.j2oa.receival.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateConfig extends ActionForm {
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