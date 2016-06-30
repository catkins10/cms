/*
 * Created on 2006-6-15
 *
 */
package com.yuanluesoft.j2oa.reimburse.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 报销配置(reimburse_config)
 * @author linchuan
 *
 */
public class ReimburseConfig extends Record {
	private String types; //报销类别

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
