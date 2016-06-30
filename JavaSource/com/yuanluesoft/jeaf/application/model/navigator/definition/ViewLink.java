/*
 * Created on 2005-9-18
 *
 */
package com.yuanluesoft.jeaf.application.model.navigator.definition;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class ViewLink extends CloneableObject {
	private String application; //应用名称
	private String view; //视图名称
	private String title; //标题
	private String local; //从本系统中引入的视图
	private String iconURL; //视图图标URL
	
	/**
	 * @return Returns the local.
	 */
	public String getLocal() {
		return local;
	}
	/**
	 * @param local The local to set.
	 */
	public void setLocal(String local) {
		this.local = local;
	}
	/**
	 * @return Returns the view.
	 */
	public String getView() {
		return view;
	}
	/**
	 * @param view The view to set.
	 */
	public void setView(String view) {
		this.view = view;
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
	/**
	 * @return Returns the application.
	 */
	public String getApplication() {
		return application;
	}
	/**
	 * @param application The application to set.
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	/**
	 * @return the iconURL
	 */
	public String getIconURL() {
		return iconURL;
	}
	/**
	 * @param iconURL the iconURL to set
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
}