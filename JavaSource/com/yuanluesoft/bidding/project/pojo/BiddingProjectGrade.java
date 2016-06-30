package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 开标:投标单位评分(bidding_project_grade)
 * @author linchuan
 *
 */
public class BiddingProjectGrade extends Record {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private int serial; //序号
	private long enterpriseId; //企业ID
	private String enterpriseName; //企业名称
	private String seal; //密封情况
	private double price; //投标报价
	private String certificate; //执业证书编号
	private int timeLimit; //工期
	private String pledge; //保证金
	private String quality; //质量目标
	private String manager; //项目经理
	private String sign; //签名
	
	/**
	 * @return the certificate
	 */
	public String getCertificate() {
		return certificate;
	}
	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(String certificate) {
		this.certificate = certificate;
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
	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}
	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}
	/**
	 * @return the pledge
	 */
	public String getPledge() {
		return pledge;
	}
	/**
	 * @param pledge the pledge to set
	 */
	public void setPledge(String pledge) {
		this.pledge = pledge;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}
	/**
	 * @param quality the quality to set
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}
	/**
	 * @return the seal
	 */
	public String getSeal() {
		return seal;
	}
	/**
	 * @param seal the seal to set
	 */
	public void setSeal(String seal) {
		this.seal = seal;
	}
	/**
	 * @return the serial
	 */
	public int getSerial() {
		return serial;
	}
	/**
	 * @param serial the serial to set
	 */
	public void setSerial(int serial) {
		this.serial = serial;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the timeLimit
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
}
