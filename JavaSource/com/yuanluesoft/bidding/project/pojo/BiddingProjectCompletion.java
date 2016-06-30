package com.yuanluesoft.bidding.project.pojo;

import java.sql.Date;

/**
 * 竣工(bidding_project_completion)
 * @author linchuan
 *
 */
public class BiddingProjectCompletion extends BiddingProjectComponent {
	private Date completeDate; //竣工时间

	/**
	 * @return the completeDate
	 */
	public Date getCompleteDate() {
		return completeDate;
	}

	/**
	 * @param completeDate the completeDate to set
	 */
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
}
