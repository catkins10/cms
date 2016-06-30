/*
 * Created on 2006-6-29
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;

import com.yuanluesoft.workflow.client.model.wapi.ActivityInstance;

/**
 * 
 * @author linchuan
 *
 */
public class QueryActivityInstance extends ActivityInstance implements Serializable {
    private Timestamp routed; //路由时间
    
    /**
     * @return Returns the routed.
     */
    public Timestamp getRouted() {
        return routed;
    }
    /**
     * @param routed The routed to set.
     */
    public void setRouted(Timestamp routed) {
        this.routed = routed;
    }
}
