/*
 * Created on 2006-5-26
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
public class EAI extends Element {
	private List groups; //分组列表
	private List applications; //应用列表
	private List links; //链接列表
	private List transitions; //连接列表
	private List managers; //管理员
	private List visitors; //访问者
	private String description;
	private EAIExtend extend;
	
    /**
     * @return Returns the applications.
     */
    public List getApplications() {
        return applications;
    }
    /**
     * @param applications The applications to set.
     */
    public void setApplications(List applications) {
        this.applications = applications;
    }
    /**
     * @return Returns the links.
     */
    public List getLinks() {
        return links;
    }
    /**
     * @param links The links to set.
     */
    public void setLinks(List links) {
        this.links = links;
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
     * @return Returns the transitions.
     */
    public List getTransitions() {
        return transitions;
    }
    /**
     * @param transitions The transitions to set.
     */
    public void setTransitions(List transitions) {
        this.transitions = transitions;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the extend.
     */
    public EAIExtend getExtend() {
        return extend;
    }
    /**
     * @param extend The extend to set.
     */
    public void setExtend(EAIExtend extend) {
        this.extend = extend;
    }
}
