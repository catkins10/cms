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
public class Application extends Element {
    private String title; //标题
    private String url; //链接地址
    private String iconUrl; //图标URL
	private String description;	//描述
	private boolean workflowSupport; //是否支持工作流
    private boolean navigateDisabled; //不在导航时显示
	private List manageUnits; //管理单元
    private List managers; //管理员列表
    private List visitors; //访问者列表
    private List workflows; //流程列表
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
     * @return Returns the workflows.
     */
    public List getWorkflows() {
        return workflows;
    }
    /**
     * @param workflows The workflows to set.
     */
    public void setWorkflows(List workflows) {
        this.workflows = workflows;
    }
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the manageUnits
	 */
	public List getManageUnits() {
		return manageUnits;
	}
	/**
	 * @param manageUnits the manageUnits to set
	 */
	public void setManageUnits(List manageUnits) {
		this.manageUnits = manageUnits;
	}
	/**
	 * @return the navigateDisabled
	 */
	public boolean isNavigateDisabled() {
		return navigateDisabled;
	}
	/**
	 * @param navigateDisabled the navigateDisabled to set
	 */
	public void setNavigateDisabled(boolean navigateDisabled) {
		this.navigateDisabled = navigateDisabled;
	}
	/**
	 * @return the workflowSupport
	 */
	public boolean isWorkflowSupport() {
		return workflowSupport;
	}
	/**
	 * @param workflowSupport the workflowSupport to set
	 */
	public void setWorkflowSupport(boolean workflowSupport) {
		this.workflowSupport = workflowSupport;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
