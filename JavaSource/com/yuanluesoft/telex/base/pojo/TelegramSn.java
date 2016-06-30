package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:发报流水号(telex_telegram_sn)
 * @author linchuan
 *
 */
public class TelegramSn extends Record {
	private int year; //年度
	private char isSend = '0'; //收/发
	private char isCryptic = '0'; //明/密
	private int sn; //流水号
	
	/**
	 * @return the isCryptic
	 */
	public char getIsCryptic() {
		return isCryptic;
	}
	/**
	 * @param isCryptic the isCryptic to set
	 */
	public void setIsCryptic(char isCryptic) {
		this.isCryptic = isCryptic;
	}
	/**
	 * @return the sn
	 */
	public int getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the isSend
	 */
	public char getIsSend() {
		return isSend;
	}
	/**
	 * @param isSend the isSend to set
	 */
	public void setIsSend(char isSend) {
		this.isSend = isSend;
	}
}
