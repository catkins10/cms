package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:申报材料(onlineservice_item_material)
 * @author linchuan
 *
 */
public class OnlineServiceItemMaterial extends Record {
	private long itemId; //办理事项ID
	private String name; //申报材料名称
	private String description; //申报说明
	private float priority; //优先级
	private String tableName; //表格名称
	private String tableURL; //表格URL
	private String exampleURL; //样表URL
	private String remark; //备注
	
	private OnlineServiceItem item; //办理事项
	
	/**
	 * 获取对外显示的标题
	 * @return
	 */
	public String getTitle() {
		return (name==null || name.isEmpty()) && tableName!=null && !tableName.isEmpty() ? tableName + "(表格下载)" : name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the exampleURL
	 */
	public String getExampleURL() {
		return exampleURL;
	}
	/**
	 * @param exampleURL the exampleURL to set
	 */
	public void setExampleURL(String exampleURL) {
		this.exampleURL = exampleURL;
	}
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
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the tableURL
	 */
	public String getTableURL() {
		return tableURL;
	}
	/**
	 * @param tableURL the tableURL to set
	 */
	public void setTableURL(String tableURL) {
		this.tableURL = tableURL;
	}
	/**
	 * @return the item
	 */
	public OnlineServiceItem getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(OnlineServiceItem item) {
		this.item = item;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}