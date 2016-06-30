package com.yuanluesoft.cms.sitemanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 栏目:相关链接(cms_column_relation_link)
 * @author linchuan
 *
 */
public class WebColumnRelationLink extends Record {
	private long columnId; //栏目ID
	private long relationColumnId; //关联栏目ID
	private String linkName; //链接名称
	private String linkUrl; //链接地址
	private Timestamp linkTime; //发布时间
	private float priority; //优先级
	private int halt; //是否停用

	/**
	 * 获取链接
	 * @return
	 */
	public String getUrl() {
		if(linkUrl==null || linkUrl.isEmpty()) {
			return "";
		}
		else {
			return "{FINAL}" + linkUrl;
		}
	}
	
	/**
	 * @return the columnId
	 */
	public long getColumnId() {
		return columnId;
	}
	/**
	 * @param columnId the columnId to set
	 */
	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}
	/**
	 * @return the linkName
	 */
	public String getLinkName() {
		return linkName;
	}
	/**
	 * @param linkName the linkName to set
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	/**
	 * @return the linkTime
	 */
	public Timestamp getLinkTime() {
		return linkTime;
	}
	/**
	 * @param linkTime the linkTime to set
	 */
	public void setLinkTime(Timestamp linkTime) {
		this.linkTime = linkTime;
	}
	/**
	 * @return the linkUrl
	 */
	public String getLinkUrl() {
		return linkUrl;
	}
	/**
	 * @param linkUrl the linkUrl to set
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the relationColumnId
	 */
	public long getRelationColumnId() {
		return relationColumnId;
	}
	/**
	 * @param relationColumnId the relationColumnId to set
	 */
	public void setRelationColumnId(long relationColumnId) {
		this.relationColumnId = relationColumnId;
	}
	/**
	 * @return the halt
	 */
	public int getHalt() {
		return halt;
	}
	/**
	 * @param halt the halt to set
	 */
	public void setHalt(int halt) {
		this.halt = halt;
	}
}