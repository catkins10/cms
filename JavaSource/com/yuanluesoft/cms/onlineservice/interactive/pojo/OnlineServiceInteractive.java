package com.yuanluesoft.cms.onlineservice.interactive.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 网上办事:互动
 * @author linchuan
 *
 */
public class OnlineServiceInteractive extends PublicService {
	private long itemId; //办理事项ID
	private String itemName; //办理事项名称
	private long creatorId; //创建人ID(网上注册用户ID)
	
	//拓展属性
	private String loginName; //用户登录用户名,提交时有效,用来获取SessionInfo
	
	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}