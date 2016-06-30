package com.yuanluesoft.bidding.project.pojo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 招标时间安排及其他(bidding_project_plan)
 * @author linchuan
 *
 */
public class BiddingProjectPlan extends BiddingProjectComponent {
	private Date inviteDate; //发布招标邀请书时间
	private String inviteMedia; //发布招标邀请书媒体,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
	private Timestamp buyDocumentBegin; //购买招标文件开始时间
	private Timestamp buyDocumentEnd; //购买招标文件结束时间
	private String buyDocumentAddress; //购买招标文件地址
	private Timestamp askEnd; //招标文件质疑时限
	private String askMedia; //招标文件质疑地点,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
	private Timestamp submitTime; //投标文件的递交截止时间
	private String submitAddress; //投标文件的递交地点,福州市建设工程交易管理中心
	private Timestamp bidopeningTime; //开标、评标时间
	private String bidopeningAddress; //开标、评标地点,福州市建设工程交易管理中心
	private Date pitchonDate; //确定中标人
	private String pitchonAddress; //确定中标人地点,福州市建设工程交易管理中心
	private int publicPitchonDays; //中标结果公示,默认10天
	private String publicPitchonMedia; //中标结果公示媒体
	private int noticeDays; //发放中标通知书时间,默认15天
	private String noticeAddress; //发放中标通知书地点,招标人
	private int archiveDays; //书面报告备案时间,默认15天
	private String archiveAddress; //书面报告备案地点,招标人
	private char signUpExported = '0'; //是否已经导出报名清单
	private BiddingProject project; //项目信息,为显示代理产生方式增加
	
	private BiddingProjectTender tender; //招标公告
	
	/**
	 * @return the archiveAddress
	 */
	public String getArchiveAddress() {
		return archiveAddress;
	}
	/**
	 * @param archiveAddress the archiveAddress to set
	 */
	public void setArchiveAddress(String archiveAddress) {
		this.archiveAddress = archiveAddress;
	}
	/**
	 * @return the archiveDays
	 */
	public int getArchiveDays() {
		return archiveDays;
	}
	/**
	 * @param archiveDays the archiveDays to set
	 */
	public void setArchiveDays(int archiveDays) {
		this.archiveDays = archiveDays;
	}
	/**
	 * @return the askEnd
	 */
	public Timestamp getAskEnd() {
		return askEnd;
	}
	/**
	 * @param askEnd the askEnd to set
	 */
	public void setAskEnd(Timestamp askEnd) {
		this.askEnd = askEnd;
	}
	/**
	 * @return the askMedia
	 */
	public String getAskMedia() {
		return askMedia;
	}
	/**
	 * @param askMedia the askMedia to set
	 */
	public void setAskMedia(String askMedia) {
		this.askMedia = askMedia;
	}
	/**
	 * @return the bidopeningAddress
	 */
	public String getBidopeningAddress() {
		return bidopeningAddress;
	}
	/**
	 * @param bidopeningAddress the bidopeningAddress to set
	 */
	public void setBidopeningAddress(String bidopeningAddress) {
		this.bidopeningAddress = bidopeningAddress;
	}
	/**
	 * @return the bidopeningTime
	 */
	public Timestamp getBidopeningTime() {
		return bidopeningTime;
	}
	/**
	 * @param bidopeningTime the bidopeningTime to set
	 */
	public void setBidopeningTime(Timestamp bidopeningTime) {
		this.bidopeningTime = bidopeningTime;
	}
	/**
	 * @return the buyDocumentAddress
	 */
	public String getBuyDocumentAddress() {
		return buyDocumentAddress;
	}
	/**
	 * @param buyDocumentAddress the buyDocumentAddress to set
	 */
	public void setBuyDocumentAddress(String buyDocumentAddress) {
		this.buyDocumentAddress = buyDocumentAddress;
	}
	/**
	 * @return the buyDocumentBegin
	 */
	public Timestamp getBuyDocumentBegin() {
		return buyDocumentBegin;
	}
	/**
	 * @param buyDocumentBegin the buyDocumentBegin to set
	 */
	public void setBuyDocumentBegin(Timestamp buyDocumentBegin) {
		this.buyDocumentBegin = buyDocumentBegin;
	}
	/**
	 * @return the buyDocumentEnd
	 */
	public Timestamp getBuyDocumentEnd() {
		return buyDocumentEnd;
	}
	/**
	 * @param buyDocumentEnd the buyDocumentEnd to set
	 */
	public void setBuyDocumentEnd(Timestamp buyDocumentEnd) {
		this.buyDocumentEnd = buyDocumentEnd;
	}
	/**
	 * @return the inviteDate
	 */
	public Date getInviteDate() {
		return inviteDate;
	}
	/**
	 * @param inviteDate the inviteDate to set
	 */
	public void setInviteDate(Date inviteDate) {
		this.inviteDate = inviteDate;
	}
	/**
	 * @return the inviteMedia
	 */
	public String getInviteMedia() {
		return inviteMedia;
	}
	/**
	 * @param inviteMedia the inviteMedia to set
	 */
	public void setInviteMedia(String inviteMedia) {
		this.inviteMedia = inviteMedia;
	}
	/**
	 * @return the noticeAddress
	 */
	public String getNoticeAddress() {
		return noticeAddress;
	}
	/**
	 * @param noticeAddress the noticeAddress to set
	 */
	public void setNoticeAddress(String noticeAddress) {
		this.noticeAddress = noticeAddress;
	}
	/**
	 * @return the noticeDays
	 */
	public int getNoticeDays() {
		return noticeDays;
	}
	/**
	 * @param noticeDays the noticeDays to set
	 */
	public void setNoticeDays(int noticeDays) {
		this.noticeDays = noticeDays;
	}
	/**
	 * @return the pitchonAddress
	 */
	public String getPitchonAddress() {
		return pitchonAddress;
	}
	/**
	 * @param pitchonAddress the pitchonAddress to set
	 */
	public void setPitchonAddress(String pitchonAddress) {
		this.pitchonAddress = pitchonAddress;
	}
	/**
	 * @return the publicPitchonDays
	 */
	public int getPublicPitchonDays() {
		return publicPitchonDays;
	}
	/**
	 * @param publicPitchonDays the publicPitchonDays to set
	 */
	public void setPublicPitchonDays(int publicPitchonDays) {
		this.publicPitchonDays = publicPitchonDays;
	}
	/**
	 * @return the publicPitchonMedia
	 */
	public String getPublicPitchonMedia() {
		return publicPitchonMedia;
	}
	/**
	 * @param publicPitchonMedia the publicPitchonMedia to set
	 */
	public void setPublicPitchonMedia(String publicPitchonMedia) {
		this.publicPitchonMedia = publicPitchonMedia;
	}
	/**
	 * @return the submitAddress
	 */
	public String getSubmitAddress() {
		return submitAddress;
	}
	/**
	 * @param submitAddress the submitAddress to set
	 */
	public void setSubmitAddress(String submitAddress) {
		this.submitAddress = submitAddress;
	}
	/**
	 * @return the submitTime
	 */
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	/**
	 * @param submitTime the submitTime to set
	 */
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	/**
	 * @return the pitchonDate
	 */
	public Date getPitchonDate() {
		return pitchonDate;
	}
	/**
	 * @param pitchonDate the pitchonDate to set
	 */
	public void setPitchonDate(Date pitchonDate) {
		this.pitchonDate = pitchonDate;
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
	/**
	 * @return the project
	 */
	public BiddingProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(BiddingProject project) {
		this.project = project;
	}
	/**
	 * @return the signUpExported
	 */
	public char getSignUpExported() {
		return signUpExported;
	}
	/**
	 * @param signUpExported the signUpExported to set
	 */
	public void setSignUpExported(char signUpExported) {
		this.signUpExported = signUpExported;
	}
}
