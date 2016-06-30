package com.yuanluesoft.wechat.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:菜单项(wechat_menu_item)
 * @author linchuan
 *
 */
public class WechatMenuItem extends Record {
	private long accountId; //公众号配置ID
	private long parentItemId; //父菜单项ID
	private String name; //菜单名称
	private int type; //类型,0/父菜单,1/链接,2/触发点击事件
	private String url; //链接地址
	private float priority; //优先级
	private Set subItems; //子项
	
	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentItemId
	 */
	public long getParentItemId() {
		return parentItemId;
	}
	/**
	 * @param parentItemId the parentItemId to set
	 */
	public void setParentItemId(long parentItemId) {
		this.parentItemId = parentItemId;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	 * @return the subItems
	 */
	public Set getSubItems() {
		return subItems;
	}
	/**
	 * @param subItems the subItems to set
	 */
	public void setSubItems(Set subItems) {
		this.subItems = subItems;
	}
}