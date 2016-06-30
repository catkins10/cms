/*
 * Created on 2006-8-24
 *
 */
package com.yuanluesoft.eai.server.model;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Workflow extends Element {
    private List subFlows; //子流程列表
    private List managers; //管理员列表
    
    /**
     * @return Returns the subFlows.
     */
    public List getSubFlows() {
        return subFlows;
    }
    /**
     * @param subFlows The subFlows to set.
     */
    public void setSubFlows(List subFlows) {
        this.subFlows = subFlows;
    }
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
