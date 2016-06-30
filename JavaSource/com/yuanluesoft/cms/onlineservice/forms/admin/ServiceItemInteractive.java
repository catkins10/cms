package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.publicservice.pojo.PublicServiceOpinion;

/**
 * 
 * @author linchuan
 *
 */
public abstract class ServiceItemInteractive extends ServiceItem {
	private PublicServiceOpinion opinion = new PublicServiceOpinion(); //办理意见
	
	/**
	 * 获取互动记录
	 * @return
	 */
	public abstract OnlineServiceInteractive getInteractiveRecord();

	/**
	 * @return the opinion
	 */
	public PublicServiceOpinion getOpinion() {
		return opinion;
	}

	/**
	 * @param opinion the opinion to set
	 */
	public void setOpinion(PublicServiceOpinion opinion) {
		this.opinion = opinion;
	}
}