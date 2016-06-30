package com.yuanluesoft.bidding.project.forms;



/**
 * 
 * @author yuanlue
 *
 */
public class Project extends com.yuanluesoft.bidding.project.forms.admin.Project {
	private String adjustBidopeningTime; //发布补充通知时是否调整开标时间

	/**
	 * @return the adjustBidopeningTime
	 */
	public String getAdjustBidopeningTime() {
		return adjustBidopeningTime;
	}

	/**
	 * @param adjustBidopeningTime the adjustBidopeningTime to set
	 */
	public void setAdjustBidopeningTime(String adjustBidopeningTime) {
		this.adjustBidopeningTime = adjustBidopeningTime;
	}
}