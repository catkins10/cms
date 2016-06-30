package com.yuanluesoft.bidding.project.pojo;

import java.sql.Timestamp;


/**
 * 补充通知(bidding_project_supplement)
 * @author linchuan
 *
 */
public class BiddingProjectSupplement extends BiddingProjectComponent {
	private String body; //内容
	private char isPreapproval = '0'; //是否预审
	private Timestamp  created; //创建时间

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
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
