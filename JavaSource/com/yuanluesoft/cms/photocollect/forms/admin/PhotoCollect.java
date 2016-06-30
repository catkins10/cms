/**
 * 
 */
package com.yuanluesoft.cms.photocollect.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 *
 * @author LinChuan
 *
 */
public class PhotoCollect extends PublicServiceAdminForm {
	private String category; //图片分类
	private int downloadable; //可否下载
	
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
