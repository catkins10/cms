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
public class Link extends Element {
	private String title; //应用标题
    private String url; //链接地址
    private String iconUrl; //图标URL
    
    private List managers; //管理员列表
    private List visitors; //访问者列表
    private String applicationSetName; //所属应用集合名称
    
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
     * @return Returns the applicationSetName.
     */
    public String getApplicationSetName() {
        return applicationSetName;
    }
    /**
     * @param applicationSetName The applicationSetName to set.
     */
    public void setApplicationSetName(String applicationSetName) {
        this.applicationSetName = applicationSetName;
    }
	/**
	 * @return the href
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param href the href to set
	 */
	public void setUrl(String href) {
		this.url = href;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
