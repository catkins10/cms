package com.yuanluesoft.bidding.project.pojo.supervise;

/**
 * 招标前期资料备案(bidding_project_prophase)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectProphase extends SuperviseBiddingProjectComponent {
	private String submitted; //已提交资料列表 

	/**
	 * @return the submitted
	 */
	public String getSubmitted() {
		return submitted;
	}

	/**
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
}
