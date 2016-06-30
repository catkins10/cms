package com.yuanluesoft.bidding.project.pojo;

/**
 * 答疑会议纪要(bidding_project_answer)
 * @author linchuan
 *
 */
public class BiddingProjectAnswer extends BiddingProjectComponent {
	private String body; //内容
	private char isPreapproval = '0'; //是否预审,目前没有使用
	
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
	/**
	 * @return the isPreapproval
	 */
	public char getIsPreapproval() {
		return isPreapproval;
	}
	/**
	 * @param isPreapproval the isPreapproval to set
	 */
	public void setIsPreapproval(char isPreapproval) {
		this.isPreapproval = isPreapproval;
	}
}
