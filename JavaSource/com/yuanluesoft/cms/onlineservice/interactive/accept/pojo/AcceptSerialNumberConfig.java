/*
 * Created on 2004-8-19
 *
 */
package com.yuanluesoft.cms.onlineservice.interactive.accept.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * ：流水号配置()
 * @author zyh
 *
 */
public class AcceptSerialNumberConfig extends Record {

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
