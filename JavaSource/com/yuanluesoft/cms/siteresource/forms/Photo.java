package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.cms.siteresource.pojo.SiteResourcePhoto;

/**
 * 
 * @author chuan
 *
 */
public class Photo extends Resource {
	private SiteResourcePhoto photo = new SiteResourcePhoto(); //图集
	
	/**
	 * @return the photo
	 */
	public SiteResourcePhoto getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(SiteResourcePhoto photo) {
		this.photo = photo;
	}
}