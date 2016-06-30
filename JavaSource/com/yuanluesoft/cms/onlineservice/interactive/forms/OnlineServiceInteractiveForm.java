package com.yuanluesoft.cms.onlineservice.interactive.forms;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.publicservice.forms.PublicServiceForm;

/**
 * 网上办事互动表单
 * @author linchuan
 *
 */
public class OnlineServiceInteractiveForm extends PublicServiceForm {
	private long itemId; //办理事项ID
	private String itemName; //办理事项名称
	private long creatorId; //创建人ID,网上注册用户ID
	
	private OnlineServiceItem serviceItem; //办理事项
	
	private boolean detail; //是否获取详情
	
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
	 * @return the serviceItem
	 */
	public OnlineServiceItem getServiceItem() {
		return serviceItem;
	}
	/**
	 * @param serviceItem the serviceItem to set
	 */
	public void setServiceItem(OnlineServiceItem serviceItem) {
		this.serviceItem = serviceItem;
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
	 * @return the detail
	 */
	public boolean isDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(boolean detail) {
		this.detail = detail;
	}
}