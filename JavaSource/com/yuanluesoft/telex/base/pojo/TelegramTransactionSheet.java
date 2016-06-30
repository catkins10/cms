package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 办理单(telex_transaction_sheet)
 * @author linchuan
 *
 */
public class TelegramTransactionSheet extends Record {
	private String name; //名称
	private String telegramType; //所属电报类型,密收/密发/明收/明发
	private String body; //办理单HTML
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
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the telegramType
	 */
	public String getTelegramType() {
		return telegramType;
	}
	/**
	 * @param telegramType the telegramType to set
	 */
	public void setTelegramType(String telegramType) {
		this.telegramType = telegramType;
	}
}
