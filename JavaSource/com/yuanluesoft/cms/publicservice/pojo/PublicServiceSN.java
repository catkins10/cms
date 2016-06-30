package com.yuanluesoft.cms.publicservice.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公众服务:编号(cms_publicservice_sn)
 * @author linchuan
 *
 */
public class PublicServiceSN extends Record {
	private Date day; //日期
	private int sn; //编号
	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
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
}
