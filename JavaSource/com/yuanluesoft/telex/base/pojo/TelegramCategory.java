package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:发报种类(telex_telegram_category)
 * @author linchuan
 *
 */
public class TelegramCategory extends Record {
	private String category; //类型
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}