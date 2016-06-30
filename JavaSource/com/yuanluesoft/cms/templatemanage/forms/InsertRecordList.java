package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.view.model.View;


/**
 * 
 * @author linchuan
 *
 */
public class InsertRecordList extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String recordListName; //URL参数:记录列表名称
	private boolean searchResults; //URL参数:是否搜索结果
	private boolean embedRecordList; //URL参数:是否内嵌的记录列表
	private boolean privateRecordList; //URL参数:是否页面私有的记录列表
	private String recordClassName; //URL参数:记录类名称,privateRecordList=true时有效
	private String redirectApplicationName; //URL参数:重定向后的应用名称
	private String redirectRecordListName; //URL参数:重定向后的记录列表名称
	
	private View view; //记录列表
	private String viewTitle; //记录列表标题
	private String templateExtendURL; //模板配置对话框扩展
	private String emptyPrompt; //无记录时的提示信息
	
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
	 * @return the recordListName
	 */
	public String getRecordListName() {
		return recordListName;
	}
	/**
	 * @param recordListName the recordListName to set
	 */
	public void setRecordListName(String recordListName) {
		this.recordListName = recordListName;
	}
	/**
	 * @return the searchResults
	 */
	public boolean isSearchResults() {
		return searchResults;
	}
	/**
	 * @param searchResults the searchResults to set
	 */
	public void setSearchResults(boolean searchResults) {
		this.searchResults = searchResults;
	}
	/**
	 * @return the embedRecordList
	 */
	public boolean isEmbedRecordList() {
		return embedRecordList;
	}
	/**
	 * @param embedRecordList the embedRecordList to set
	 */
	public void setEmbedRecordList(boolean embedRecordList) {
		this.embedRecordList = embedRecordList;
	}
	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}
	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}
	/**
	 * @return the templateExtendURL
	 */
	public String getTemplateExtendURL() {
		return templateExtendURL;
	}
	/**
	 * @param templateExtendURL the templateExtendURL to set
	 */
	public void setTemplateExtendURL(String templateExtendURL) {
		this.templateExtendURL = templateExtendURL;
	}
	/**
	 * @return the privateRecordList
	 */
	public boolean isPrivateRecordList() {
		return privateRecordList;
	}
	/**
	 * @param privateRecordList the privateRecordList to set
	 */
	public void setPrivateRecordList(boolean privateRecordList) {
		this.privateRecordList = privateRecordList;
	}
	/**
	 * @return the emptyPrompt
	 */
	public String getEmptyPrompt() {
		return emptyPrompt;
	}
	/**
	 * @param emptyPrompt the emptyPrompt to set
	 */
	public void setEmptyPrompt(String emptyPrompt) {
		this.emptyPrompt = emptyPrompt;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
	/**
	 * @return the redirectApplicationName
	 */
	public String getRedirectApplicationName() {
		return redirectApplicationName;
	}
	/**
	 * @param redirectApplicationName the redirectApplicationName to set
	 */
	public void setRedirectApplicationName(String redirectApplicationName) {
		this.redirectApplicationName = redirectApplicationName;
	}
	/**
	 * @return the redirectRecordListName
	 */
	public String getRedirectRecordListName() {
		return redirectRecordListName;
	}
	/**
	 * @param redirectRecordListName the redirectRecordListName to set
	 */
	public void setRedirectRecordListName(String redirectRecordListName) {
		this.redirectRecordListName = redirectRecordListName;
	}
	/**
	 * @return the viewTitle
	 */
	public String getViewTitle() {
		return viewTitle;
	}
	/**
	 * @param viewTitle the viewTitle to set
	 */
	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}
}