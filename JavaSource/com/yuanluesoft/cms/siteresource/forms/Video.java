package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.cms.siteresource.pojo.SiteResourceVideo;

/**
 * 
 * @author chuan
 *
 */
public class Video extends Resource {
	private SiteResourceVideo video = new SiteResourceVideo();

	/**
	 * @return the video
	 */
	public SiteResourceVideo getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	public void setVideo(SiteResourceVideo video) {
		this.video = video;
	}
}