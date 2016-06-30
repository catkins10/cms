/*
 * Created on 2006-8-23
 *
 */
package com.yuanluesoft.eai.server.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationSet implements Serializable, Cloneable {
    private String name; //名称
	private String title; //标题
    private List groups; //分组
    
    /**
     * @return Returns the groups.
     */
    public List getGroups() {
        return groups;
    }
    /**
     * @param groups The groups to set.
     */
    public void setGroups(List groups) {
        this.groups = groups;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
