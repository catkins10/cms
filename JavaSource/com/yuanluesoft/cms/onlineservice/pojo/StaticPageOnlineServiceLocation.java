package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageElement;

/**
 * 静态页面:使用到的网上办事位置(cms_static_service_location)
 * @author linchuan
 *
 */
public class StaticPageOnlineServiceLocation extends StaticPageElement {
	private long directoryId; //目录ID

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
}