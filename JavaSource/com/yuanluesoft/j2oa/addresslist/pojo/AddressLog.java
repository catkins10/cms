/*
 * Created on 2006-5-25
 *
 */
package com.yuanluesoft.j2oa.addresslist.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 来往记录(address_log)
 * @author linchuan
 *
 */
public class AddressLog extends Record {
	private long personId; //联系人ID
	private Date time; //发生时间
	private String content; //事件描述
	
    /**
     * @return Returns the content.
     */
    public java.lang.String getContent() {
        return content;
    }
    /**
     * @param content The content to set.
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }
    /**
     * @return Returns the personId.
     */
    public long getPersonId() {
        return personId;
    }
    /**
     * @param personId The personId to set.
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }
    /**
     * @return Returns the time.
     */
    public java.sql.Date getTime() {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(java.sql.Date time) {
        this.time = time;
    }
}
