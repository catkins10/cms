package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:发报等级(telex_telegram_level)
 * @author linchuan
 *
 */
public class TelegramLevel extends Record {
	private String level; //等级
	
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
}
