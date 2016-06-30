package com.yuanluesoft.bidding.project.pojo;


/**
 * 预审公示(bidding_project_preapproval)
 * @author linchuan
 *
 */
public class BiddingProjectPreapproval extends BiddingProjectComponent {
	private String body; //内容,金润导出的XML

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
