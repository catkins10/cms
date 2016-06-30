package com.yuanluesoft.bbs.article.pojo;

import com.yuanluesoft.cms.pagebuilder.pojo.StaticPageRecordList;

/**
 * 静态页面:使用到的论坛文章(cms_static_page_bbs_articles)
 * @author linchuan
 *
 */
public class StaticPageBbsArticles extends StaticPageRecordList {
	private long directoryId; //隶属的论坛目录ID

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