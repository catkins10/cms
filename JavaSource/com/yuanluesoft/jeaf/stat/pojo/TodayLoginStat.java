/**
 * 
 */
package com.yuanluesoft.jeaf.stat.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 当天是否登录过
 * @author LinChuan
 * 
 */
public class TodayLoginStat extends Record {
	private long personId;
	
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
}
