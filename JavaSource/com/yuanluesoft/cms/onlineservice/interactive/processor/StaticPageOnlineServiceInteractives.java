package com.yuanluesoft.cms.onlineservice.interactive.processor;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的网上办事互动(cms_static_service_interactive)
 * @author linchuan
 *
 */
public class StaticPageOnlineServiceInteractives extends StaticPageRecordList {
	private long directoryId; //隶属的目录ID
	private long itemId; //隶属的事项ID
	
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
}