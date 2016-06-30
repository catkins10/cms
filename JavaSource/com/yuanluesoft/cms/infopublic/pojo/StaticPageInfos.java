package com.yuanluesoft.cms.infopublic.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的政府信息资源(cms_static_page_infos)
 * @author linchuan
 *
 */
public class StaticPageInfos extends StaticPageRecordList {
	private long directoryId; //隶属的信息公开目录ID

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