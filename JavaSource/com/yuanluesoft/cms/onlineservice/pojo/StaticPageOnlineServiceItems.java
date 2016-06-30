package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的网上办事事项(cms_static_page_service_items)
 * @author linchuan
 *
 */
public class StaticPageOnlineServiceItems extends StaticPageRecordList {
	private long directoryId; //隶属的目录ID
	private String itemTypes; //事项类型

	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}

	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}

	/**
	 * @return the itemTypes
	 */
	public String getItemTypes() {
		return itemTypes;
	}

	/**
	 * @param itemTypes the itemTypes to set
	 */
	public void setItemTypes(String itemTypes) {
		this.itemTypes = itemTypes;
	}
}