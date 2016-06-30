package com.yuanluesoft.telex.receive.base.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:代签收人(telex_sign_agent)
 * @author linchuan
 *
 */
public class TelegramSignAgent extends Record {
	private String name; //姓名
	private long orgId; //所属组织机构ID
	private String orgName; //所属组织机构
	private String certificateName; //证件名称
	private String certificateCode; //证件号码
	private char sex = 'F'; //性别,F/M
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	/**
	 * @return the certificateCode
	 */
	public String getCertificateCode() {
		return certificateCode;
	}
	/**
	 * @param certificateCode the certificateCode to set
	 */
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	/**
	 * @return the certificateName
	 */
	public String getCertificateName() {
		return certificateName;
	}
	/**
	 * @param certificateName the certificateName to set
	 */
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
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
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
