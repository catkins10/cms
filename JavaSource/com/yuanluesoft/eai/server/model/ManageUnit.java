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
public class ManageUnit extends Element {
	private List visitors; //管理单元访问者列表
	
	/**
     * @return Returns the visitors.
     */
    public List getVisitors() {
        return visitors;
    }
    /**
     * @param visitors The visitors to set.
     */
    public void setVisitors(List visitors) {
        this.visitors = visitors;
    }
}