package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工程项目:建设单位(bidding_project_owner)
 * @author linchuan
 *
 */
public class BiddingProjectOwner extends Record {
	private String owner; //建设单位名称
	private String ownerType; //建设单位性质,全民
	private String ownerRepresentative; //建设单位法人代表
	private String ownerLinkman; //建设单位联系人
	private String ownerLinkmanIdCard; //建设单位联系人身份证
	private String ownerTel; //建设单位联系电话
	private String ownerFax; //建设单位传真
	private String ownerMail; //建设单位电子邮件
	
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the ownerFax
	 */
	public String getOwnerFax() {
		return ownerFax;
	}
	/**
	 * @param ownerFax the ownerFax to set
	 */
	public void setOwnerFax(String ownerFax) {
		this.ownerFax = ownerFax;
	}
	/**
	 * @return the ownerLinkman
	 */
	public String getOwnerLinkman() {
		return ownerLinkman;
	}
	/**
	 * @param ownerLinkman the ownerLinkman to set
	 */
	public void setOwnerLinkman(String ownerLinkman) {
		this.ownerLinkman = ownerLinkman;
	}
	/**
	 * @return the ownerLinkmanIdCard
	 */
	public String getOwnerLinkmanIdCard() {
		return ownerLinkmanIdCard;
	}
	/**
	 * @param ownerLinkmanIdCard the ownerLinkmanIdCard to set
	 */
	public void setOwnerLinkmanIdCard(String ownerLinkmanIdCard) {
		this.ownerLinkmanIdCard = ownerLinkmanIdCard;
	}
	/**
	 * @return the ownerMail
	 */
	public String getOwnerMail() {
		return ownerMail;
	}
	/**
	 * @param ownerMail the ownerMail to set
	 */
	public void setOwnerMail(String ownerMail) {
		this.ownerMail = ownerMail;
	}
	/**
	 * @return the ownerRepresentative
	 */
	public String getOwnerRepresentative() {
		return ownerRepresentative;
	}
	/**
	 * @param ownerRepresentative the ownerRepresentative to set
	 */
	public void setOwnerRepresentative(String ownerRepresentative) {
		this.ownerRepresentative = ownerRepresentative;
	}
	/**
	 * @return the ownerTel
	 */
	public String getOwnerTel() {
		return ownerTel;
	}
	/**
	 * @param ownerTel the ownerTel to set
	 */
	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}
	/**
	 * @return the ownerType
	 */
	public String getOwnerType() {
		return ownerType;
	}
	/**
	 * @param ownerType the ownerType to set
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
}
