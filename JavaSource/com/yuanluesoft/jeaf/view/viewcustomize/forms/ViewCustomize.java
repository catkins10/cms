/*
 * Created on 2005-4-7
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.view.viewcustomize.model.ViewCustom;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustomize extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	private ViewCustom viewCustom = new ViewCustom(); //视图定制结果
	private List sourceColumns; //源列表
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the viewCustom
	 */
	public ViewCustom getViewCustom() {
		return viewCustom;
	}
	/**
	 * @param viewCustom the viewCustom to set
	 */
	public void setViewCustom(ViewCustom viewCustom) {
		this.viewCustom = viewCustom;
	}
	/**
	 * @return the sourceColumns
	 */
	public List getSourceColumns() {
		return sourceColumns;
	}
	/**
	 * @param sourceColumns the sourceColumns to set
	 */
	public void setSourceColumns(List sourceColumns) {
		this.sourceColumns = sourceColumns;
	}
}