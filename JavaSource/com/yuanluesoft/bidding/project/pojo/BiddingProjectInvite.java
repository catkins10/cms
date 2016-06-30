package com.yuanluesoft.bidding.project.pojo;

/**
 * 被邀请企业(bidding_project_invite)
 * @author linchuan
 *
 */
public class BiddingProjectInvite extends BiddingProjectComponent {
	private long enterpriseId; //企业ID
	private String enterpriseName; //企业名称
	
	/**
	 * @return the enterpriseId
	 */
	public long getEnterpriseId() {
		return enterpriseId;
	}
	/**
	 * @param enterpriseId the enterpriseId to set
	 */
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
}
