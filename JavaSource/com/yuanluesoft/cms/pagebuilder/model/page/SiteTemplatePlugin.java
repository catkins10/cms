package com.yuanluesoft.cms.pagebuilder.model.page;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 模板插件
 * @author linchuan
 *
 */
public class SiteTemplatePlugin extends CloneableObject {
	private String path; //插件路径
	private boolean privatePlugin; //是否私有插件
	private String templateActions; //插件中包含的模板操作
	private String pageElementActions; //插件中包含的页面元素操作
	
	/**
	 * @return the pageElementActions
	 */
	public String getPageElementActions() {
		return pageElementActions;
	}
	/**
	 * @param pageElementActions the pageElementActions to set
	 */
	public void setPageElementActions(String pageElementActions) {
		this.pageElementActions = pageElementActions;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the privatePlugin
	 */
	public boolean isPrivatePlugin() {
		return privatePlugin;
	}
	/**
	 * @param privatePlugin the privatePlugin to set
	 */
	public void setPrivatePlugin(boolean privatePlugin) {
		this.privatePlugin = privatePlugin;
	}
	/**
	 * @return the templateActions
	 */
	public String getTemplateActions() {
		return templateActions;
	}
	/**
	 * @param templateActions the templateActions to set
	 */
	public void setTemplateActions(String templateActions) {
		this.templateActions = templateActions;
	}
}
