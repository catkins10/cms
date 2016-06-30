package com.yuanluesoft.telex.base.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:意见填写人(telex_opinion_person)
 * @author linchuan
 *
 */
public class TelegramOpinionPerson extends Record {
	private String personName; //填写人姓名

	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
