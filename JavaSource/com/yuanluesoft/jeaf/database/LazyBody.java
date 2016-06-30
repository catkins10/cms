package com.yuanluesoft.jeaf.database;

/**
 * 延迟加载的正文
 * @author linchuan
 *
 */
public class LazyBody extends Record {
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