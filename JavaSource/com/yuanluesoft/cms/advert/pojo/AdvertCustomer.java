package com.yuanluesoft.cms.advert.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 广告管理:广告客户(advert_customer)
 * @author linchuan
 *
 */
public class AdvertCustomer extends Record {
	private long siteId; //站点ID
	private String name; //名称
	private String linkman; //联系人
	private String tel; //联系电话
	private double account; //账户余额
	private Timestamp lastTopupTime; //最后充值时间
	private Timestamp created; //创建时间
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Set adverts; //广告列表
	
	/**
	 * @return the account
	 */
	public double getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(double account) {
		this.account = account;
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
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the lastTopupTime
	 */
	public Timestamp getLastTopupTime() {
		return lastTopupTime;
	}
	/**
	 * @param lastTopupTime the lastTopupTime to set
	 */
	public void setLastTopupTime(Timestamp lastTopupTime) {
		this.lastTopupTime = lastTopupTime;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the adverts
	 */
	public Set getAdverts() {
		return adverts;
	}
	/**
	 * @param adverts the adverts to set
	 */
	public void setAdverts(Set adverts) {
		this.adverts = adverts;
	}
}