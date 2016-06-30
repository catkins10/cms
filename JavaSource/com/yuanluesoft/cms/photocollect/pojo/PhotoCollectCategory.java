package com.yuanluesoft.cms.photocollect.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 图片征集:分类配置(cms_photo_collect_category)
 * @author linchuan
 *
 */
public class PhotoCollectCategory extends Record {
	private String category; //分类
	private String columnIds; //同步栏目ID
	private String columnNames; //同步栏目名称
	private long siteId; //绑定的站点ID
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the columnIds
	 */
	public String getColumnIds() {
		return columnIds;
	}
	/**
	 * @param columnIds the columnIds to set
	 */
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}
	/**
	 * @return the columnNames
	 */
	public String getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
}