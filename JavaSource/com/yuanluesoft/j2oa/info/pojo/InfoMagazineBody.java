package com.yuanluesoft.j2oa.info.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息采编:刊物正文(info_magazine_body)
 * @author linchuan
 *
 */
public class InfoMagazineBody extends Record {
	private String body; //正文

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
}