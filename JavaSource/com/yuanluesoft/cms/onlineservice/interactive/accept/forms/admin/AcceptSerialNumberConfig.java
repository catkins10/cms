/*
 * Created on 2005-8-17
 *
 */
package com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *流水号配置
 * @author zyh
 *
 */
public class AcceptSerialNumberConfig extends ActionForm {

	private String content;//内容格式

	/**
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content 要设置的 content
	 */
	public void setContent(String content) {
		this.content = content;
	}
}