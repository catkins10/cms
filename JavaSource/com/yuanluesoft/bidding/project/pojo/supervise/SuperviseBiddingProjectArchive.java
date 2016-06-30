package com.yuanluesoft.bidding.project.pojo.supervise;

/**
 * 归档(bidding_project_archive)
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectArchive extends SuperviseBiddingProjectComponent {
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