/*
 * Created on 2006-7-3
 *
 */
package com.yuanluesoft.j2oa.leave.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 请假配置(leave_config)
 * @author linchuan
 *
 */
public class LeaveConfig extends Record {
	private String types; //请假类别

    /**
     * @return Returns the types.
     */
    public java.lang.String getTypes() {
        return types;
    }
    /**
     * @param types The types to set.
     */
    public void setTypes(java.lang.String types) {
        this.types = types;
    }
}
