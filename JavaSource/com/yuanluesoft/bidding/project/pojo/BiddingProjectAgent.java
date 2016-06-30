package com.yuanluesoft.bidding.project.pojo;


/**
 * 中选代理(bidding_project_agent)
 * @author linchuan
 *
 */
public class BiddingProjectAgent extends BiddingProjectComponent {
	private long agentId; //中选代理ID
	private String agentName; //中选代理
	private String agentLinkman; //联系人
	private String agentTel; //电话
	private String agentCertificateNumber; //资格证书号
	private String agentLevel; //资质等级
	private String agentRepresentative; //法人代表
	
	private BiddingProjectAgentDraw agentDraw; //代理抽签
	
	/**
	 * @return the agentCertificateNumber
	 */
	public String getAgentCertificateNumber() {
		return agentCertificateNumber;
	}
	/**
	 * @param agentCertificateNumber the agentCertificateNumber to set
	 */
	public void setAgentCertificateNumber(String agentCertificateNumber) {
		this.agentCertificateNumber = agentCertificateNumber;
	}
	/**
	 * @return the agentId
	 */
	public long getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return the agentLevel
	 */
	public String getAgentLevel() {
		return agentLevel;
	}
	/**
	 * @param agentLevel the agentLevel to set
	 */
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	/**
	 * @return the agentLinkman
	 */
	public String getAgentLinkman() {
		return agentLinkman;
	}
	/**
	 * @param agentLinkman the agentLinkman to set
	 */
	public void setAgentLinkman(String agentLinkman) {
		this.agentLinkman = agentLinkman;
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the agentRepresentative
	 */
	public String getAgentRepresentative() {
		return agentRepresentative;
	}
	/**
	 * @param agentRepresentative the agentRepresentative to set
	 */
	public void setAgentRepresentative(String agentRepresentative) {
		this.agentRepresentative = agentRepresentative;
	}
	/**
	 * @return the agentTel
	 */
	public String getAgentTel() {
		return agentTel;
	}
	/**
	 * @param agentTel the agentTel to set
	 */
	public void setAgentTel(String agentTel) {
		this.agentTel = agentTel;
	}
	/**
	 * @return the agentDraw
	 */
	public BiddingProjectAgentDraw getAgentDraw() {
		return agentDraw;
	}
	/**
	 * @param agentDraw the agentDraw to set
	 */
	public void setAgentDraw(BiddingProjectAgentDraw agentDraw) {
		this.agentDraw = agentDraw;
	}
}
