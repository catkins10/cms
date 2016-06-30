package com.yuanluesoft.bidding.enterprise.forms.admin;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingJobholder;

/**
 * 
 * @author linchuan
 *
 */
public class Jobholder extends Enterprise {
	private BiddingJobholder jobholder = new BiddingJobholder();

	/**
	 * @return the jobholder
	 */
	public BiddingJobholder getJobholder() {
		return jobholder;
	}

	/**
	 * @param jobholder the jobholder to set
	 */
	public void setJobholder(BiddingJobholder jobholder) {
		this.jobholder = jobholder;
	}
}