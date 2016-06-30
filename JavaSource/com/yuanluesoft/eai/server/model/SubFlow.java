/*
 * Created on 2006-8-24
 *
 */
package com.yuanluesoft.eai.server.model;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class SubFlow extends Element implements Serializable, Cloneable {
    private List managers; //管理员列表
    
    /**
     * @return Returns the managers.
     */
    public List getManagers() {
        return managers;
    }
    /**
     * @param managers The managers to set.
     */
    public void setManagers(List managers) {
        this.managers = managers;
    }
}
