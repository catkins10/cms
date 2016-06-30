package com.yuanluesoft.cms.siteresource.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author chuan
 *
 */
public class SetTop extends ActionForm {
	private long resourceId; //资源ID
	private String columnIds; //所属栏目ID列表
	private long[] selectedDirectoryIds; //置顶的栏目或站点ID列表
	private Date expire; //截至时间
	
	/**
	 * @return the columnIds
	 */
	public String getColumnIds() {
		return columnIds;
	}
	/**
	 * @param columnIds the columnIds to set
	 */
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}
	/**
	 * @return the resourceId
	 */
	public long getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * @return the selectedDirectoryIds
	 */
	public long[] getSelectedDirectoryIds() {
		return selectedDirectoryIds;
	}
	/**
	 * @param selectedDirectoryIds the selectedDirectoryIds to set
	 */
	public void setSelectedDirectoryIds(long[] selectedDirectoryIds) {
		this.selectedDirectoryIds = selectedDirectoryIds;
	}
	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}
	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
}