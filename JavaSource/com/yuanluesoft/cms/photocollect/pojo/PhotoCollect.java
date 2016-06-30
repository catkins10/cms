package com.yuanluesoft.cms.photocollect.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 图片征集(cms_photo_collect)
 * @author linchuan
 *
 */
public class PhotoCollect extends PublicService {
	private String category; //图片分类
	private int downloadable = 1; //可否下载
	
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
	 * @return the downloadable
	 */
	public int getDownloadable() {
		return downloadable;
	}
	/**
	 * @param downloadable the downloadable to set
	 */
	public void setDownloadable(int downloadable) {
		this.downloadable = downloadable;
	}
}