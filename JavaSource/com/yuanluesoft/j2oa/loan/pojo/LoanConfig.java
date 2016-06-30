/*
 * Created on 2006-6-27
 *
 */
package com.yuanluesoft.j2oa.loan.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 借款配置(loan_config)
 * @author linchuan
 *
 */
public class LoanConfig extends Record {
	private String types; //借款类别
	
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
