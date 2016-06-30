package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的网上办事目录(cms_static_page_service_dirs)
 * @author linchuan
 *
 */
public class StaticPageOnlineServiceDirectories extends StaticPageRecordList {
	private long directoryId; //目录ID
	private char directoryType = '0'; //目录类型,0/子目录,1/父目录,2/兄弟目录

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
	 * @return the directoryType
	 */
	public char getDirectoryType() {
		return directoryType;
	}

	/**
	 * @param directoryType the directoryType to set
	 */
	public void setDirectoryType(char directoryType) {
		this.directoryType = directoryType;
	}
}