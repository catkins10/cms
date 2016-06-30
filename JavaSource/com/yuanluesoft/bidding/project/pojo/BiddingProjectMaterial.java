package com.yuanluesoft.bidding.project.pojo;

/**
 * 招标文件实质性内容(bidding_project_material)
 * @author linchuan
 *
 */
public class BiddingProjectMaterial extends BiddingProjectComponent {
	private String body; //正文
	private BiddingProjectTender tender; //招标公告

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
	 * @return the tender
	 */
	public BiddingProjectTender getTender() {
		return tender;
	}

	/**
	 * @param tender the tender to set
	 */
	public void setTender(BiddingProjectTender tender) {
		this.tender = tender;
	}
}
