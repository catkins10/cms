package com.yuanluesoft.bidding.enterprise.pojo;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 企业:资质(bidding_enterprise_cert)
 * @author linchuan
 *
 */
public class BiddingEnterpriseCert extends Record {
	private long enterpriseId; //企业ID
	private String type; //资质类型,施工/监理/招标代理/房地产
	private String certificateNumber; //资质证书编号
	private String level; //资质等级
	private Date approvalDate; //取得资质时间
	private String approvalNumber; //批准文号
	private String lib; //代理名录库
	private String remark; //备注
	private long alterId; //变更记录ID
	
	private Set surveies; //年检记录
	private BiddingEnterprise enterprise; //企业
	
	/**
	 * @return the libArray
	 */
	public String[] getLibArray() {
		return lib==null ? null : lib.split("，");
	}

	/**
	 * @param libArray the libArray to set
	 */
	public void setLibArray(String[] libArray) {
		this.lib = ListUtils.join(libArray, "，", false);
	}

	/**
	 * @return the approvalDate
	 */
	public Date getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	/**
	 * @return the approvalNumber
	 */
	public String getApprovalNumber() {
		return approvalNumber;
	}

	/**
	 * @param approvalNumber the approvalNumber to set
	 */
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	/**
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}

	/**
	 * @param certificateNumber the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

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
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the surveies
	 */
	public Set getSurveies() {
		return surveies;
	}

	/**
	 * @param surveies the surveies to set
	 */
	public void setSurveies(Set surveies) {
		this.surveies = surveies;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the alterId
	 */
	public long getAlterId() {
		return alterId;
	}

	/**
	 * @param alterId the alterId to set
	 */
	public void setAlterId(long alterId) {
		this.alterId = alterId;
	}

	/**
	 * @return the lib
	 */
	public String getLib() {
		return lib;
	}

	/**
	 * @param lib the lib to set
	 */
	public void setLib(String lib) {
		this.lib = lib;
	}

	/**
	 * @return the enterprise
	 */
	public BiddingEnterprise getEnterprise() {
		return enterprise;
	}

	/**
	 * @param enterprise the enterprise to set
	 */
	public void setEnterprise(BiddingEnterprise enterprise) {
		this.enterprise = enterprise;
	}
}
