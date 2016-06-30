package com.yuanluesoft.cms.siteresource.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 站点资源:相关链接(cms_resource_relation_link)
 * @author linchuan
 *
 */
public class SiteResourceRelationLink extends Record {
	private long resourceId; //资源ID
	private long relationResourceId; //关联资源ID
	private String linkName; //链接名称
	private String linkUrl; //链接地址
	private Timestamp linkTime; //发布时间
	private double priority; //优先级
	private int halt; //是否停用
	
	/**
	 * 获取链接
	 * @return
	 */
	public String getUrl() {
		if(linkUrl==null || linkUrl.isEmpty()) {
			return "";
		}
		else if(linkUrl.indexOf("/cms/siteresource/article.shtml")==-1) { //不是文章
			return "{FINAL}" + linkUrl;
		}
		else {
			return linkUrl;
		}
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
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the relationResourceId
	 */
	public long getRelationResourceId() {
		return relationResourceId;
	}
	/**
	 * @param relationResourceId the relationResourceId to set
	 */
	public void setRelationResourceId(long relationResourceId) {
		this.relationResourceId = relationResourceId;
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