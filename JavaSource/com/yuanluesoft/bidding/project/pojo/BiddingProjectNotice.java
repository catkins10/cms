package com.yuanluesoft.bidding.project.pojo;

import java.sql.Date;

/**
 * 中标通知书(bidding_project_notice)
 * @author linchuan
 *
 */
public class BiddingProjectNotice extends BiddingProjectComponent {
	private String noticeNumber; //通知书编号
	private long pitchonEnterpriseId; //中标人ID
	private String pitchonEnterprise; //中标人
	private double pitchonPrice; //中标价
	private int timeLimit; //总工期
	private String keysTimeLimit; //关键节点工期要求
	private int contractDays; //签订合同时限,天
	private String contractAddress; //签订合同地点
	private String quality; //工程质量
	private String stage; //标段
	private Date biddingDate; //投标日期
	private String manager; //项目经理
	private String tenderee; //招标人
	private String tendereeRepresentative; //法定代表人
	private Date noticeDate; //通知时间
	private String body; //正文,没有固定模板时使用
	
	/**
	 * @return the biddingDate
	 */
	public Date getBiddingDate() {
		return biddingDate;
	}
	/**
	 * @param biddingDate the biddingDate to set
	 */
	public void setBiddingDate(Date biddingDate) {
		this.biddingDate = biddingDate;
	}
	/**
	 * @return the contractAddress
	 */
	public String getContractAddress() {
		return contractAddress;
	}
	/**
	 * @param contractAddress the contractAddress to set
	 */
	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
	/**
	 * @return the contractDays
	 */
	public int getContractDays() {
		return contractDays;
	}
	/**
	 * @param contractDays the contractDays to set
	 */
	public void setContractDays(int contractDays) {
		this.contractDays = contractDays;
	}
	/**
	 * @return the keysTimeLimit
	 */
	public String getKeysTimeLimit() {
		return keysTimeLimit;
	}
	/**
	 * @param keysTimeLimit the keysTimeLimit to set
	 */
	public void setKeysTimeLimit(String keysTimeLimit) {
		this.keysTimeLimit = keysTimeLimit;
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
	 * @return the noticeDate
	 */
	public Date getNoticeDate() {
		return noticeDate;
	}
	/**
	 * @param noticeDate the noticeDate to set
	 */
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	/**
	 * @return the pitchonEnterprise
	 */
	public String getPitchonEnterprise() {
		return pitchonEnterprise;
	}
	/**
	 * @param pitchonEnterprise the pitchonEnterprise to set
	 */
	public void setPitchonEnterprise(String pitchonEnterprise) {
		this.pitchonEnterprise = pitchonEnterprise;
	}
	/**
	 * @return the pitchonEnterpriseId
	 */
	public long getPitchonEnterpriseId() {
		return pitchonEnterpriseId;
	}
	/**
	 * @param pitchonEnterpriseId the pitchonEnterpriseId to set
	 */
	public void setPitchonEnterpriseId(long pitchonEnterpriseId) {
		this.pitchonEnterpriseId = pitchonEnterpriseId;
	}
	/**
	 * @return the pitchonPrice
	 */
	public double getPitchonPrice() {
		return pitchonPrice;
	}
	/**
	 * @param pitchonPrice the pitchonPrice to set
	 */
	public void setPitchonPrice(double pitchonPrice) {
		this.pitchonPrice = pitchonPrice;
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
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	/**
	 * @return the tenderee
	 */
	public String getTenderee() {
		return tenderee;
	}
	/**
	 * @param tenderee the tenderee to set
	 */
	public void setTenderee(String tenderee) {
		this.tenderee = tenderee;
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
	/**
	 * @return the tendereeRepresentative
	 */
	public String getTendereeRepresentative() {
		return tendereeRepresentative;
	}
	/**
	 * @param tendereeRepresentative the tendereeRepresentative to set
	 */
	public void setTendereeRepresentative(String tendereeRepresentative) {
		this.tendereeRepresentative = tendereeRepresentative;
	}
	/**
	 * @return the noticeNumber
	 */
	public String getNoticeNumber() {
		return noticeNumber;
	}
	/**
	 * @param noticeNumber the noticeNumber to set
	 */
	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
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
}
