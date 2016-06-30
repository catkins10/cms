package com.yuanluesoft.telex.receive.base.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 电报:收报登记(telex_receive_telegram)
 * @author linchuan
 *
 */
public class ReceiveTelegram extends Record {
	private String subject; //标题
	private String summary; //内容摘要
	private char isCryptic = '0'; //明/密
	private String sn; //原电号
	private String sequence; //流水号
	private String unitCode; //部委号
	private String category; //发电类型
	private String securityLevel; //密级
	private String telegramLevel; //等级
	private int pages; //页数
	private int telegramNumber; //份数
	private int totalPages; //总页数
	private Timestamp sendTime; //发电时间
	private String fromUnitIds; //发电单位ID列表
	private String fromUnitNames; //发电单位名称列表
	private String station; //来电台家
	private String toUnitIds; //受电单位ID列表
	private String toUnitNames; //受电单位名称列表
	private Timestamp receiveTime; //接收时间
	private long receiverId; //接受人ID
	private String receiverName; //接受人姓名
	private char needReturn = '0'; //是否需要退报
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private Timestamp archiveTime; //归档时间
	private long archivePersonId; //归档人ID
	private String archivePersonName; //归档人姓名
	private String remark; //备注
	private Set signs; //签收人列表
	private Set opinions; //意见列表
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the fromUnitIds
	 */
	public String getFromUnitIds() {
		return fromUnitIds;
	}
	/**
	 * @param fromUnitIds the fromUnitIds to set
	 */
	public void setFromUnitIds(String fromUnitIds) {
		this.fromUnitIds = fromUnitIds;
	}
	/**
	 * @return the fromUnitNames
	 */
	public String getFromUnitNames() {
		return fromUnitNames;
	}
	/**
	 * @param fromUnitNames the fromUnitNames to set
	 */
	public void setFromUnitNames(String fromUnitNames) {
		this.fromUnitNames = fromUnitNames;
	}
	/**
	 * @return the isCryptic
	 */
	public char getIsCryptic() {
		return isCryptic;
	}
	/**
	 * @param isCryptic the isCryptic to set
	 */
	public void setIsCryptic(char isCryptic) {
		this.isCryptic = isCryptic;
	}
	/**
	 * @return the needReturn
	 */
	public char getNeedReturn() {
		return needReturn;
	}
	/**
	 * @param needReturn the needReturn to set
	 */
	public void setNeedReturn(char needReturn) {
		this.needReturn = needReturn;
	}
	/**
	 * @return the opinions
	 */
	public Set getOpinions() {
		return opinions;
	}
	/**
	 * @param opinions the opinions to set
	 */
	public void setOpinions(Set opinions) {
		this.opinions = opinions;
	}
	/**
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}
	/**
	 * @return the receiverId
	 */
	public long getReceiverId() {
		return receiverId;
	}
	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}
	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return the receiveTime
	 */
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
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
	 * @return the securityLevel
	 */
	public String getSecurityLevel() {
		return securityLevel;
	}
	/**
	 * @param securityLevel the securityLevel to set
	 */
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the signs
	 */
	public Set getSigns() {
		return signs;
	}
	/**
	 * @param signs the signs to set
	 */
	public void setSigns(Set signs) {
		this.signs = signs;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the station
	 */
	public String getStation() {
		return station;
	}
	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the telegramLevel
	 */
	public String getTelegramLevel() {
		return telegramLevel;
	}
	/**
	 * @param telegramLevel the telegramLevel to set
	 */
	public void setTelegramLevel(String telegramLevel) {
		this.telegramLevel = telegramLevel;
	}
	/**
	 * @return the telegramNumber
	 */
	public int getTelegramNumber() {
		return telegramNumber;
	}
	/**
	 * @param telegramNumber the telegramNumber to set
	 */
	public void setTelegramNumber(int telegramNumber) {
		this.telegramNumber = telegramNumber;
	}
	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}
	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	/**
	 * @return the toUnitIds
	 */
	public String getToUnitIds() {
		return toUnitIds;
	}
	/**
	 * @param toUnitIds the toUnitIds to set
	 */
	public void setToUnitIds(String toUnitIds) {
		this.toUnitIds = toUnitIds;
	}
	/**
	 * @return the toUnitNames
	 */
	public String getToUnitNames() {
		return toUnitNames;
	}
	/**
	 * @param toUnitNames the toUnitNames to set
	 */
	public void setToUnitNames(String toUnitNames) {
		this.toUnitNames = toUnitNames;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	/**
	 * @return the archivePersonId
	 */
	public long getArchivePersonId() {
		return archivePersonId;
	}
	/**
	 * @param archivePersonId the archivePersonId to set
	 */
	public void setArchivePersonId(long archivePersonId) {
		this.archivePersonId = archivePersonId;
	}
	/**
	 * @return the archivePersonName
	 */
	public String getArchivePersonName() {
		return archivePersonName;
	}
	/**
	 * @param archivePersonName the archivePersonName to set
	 */
	public void setArchivePersonName(String archivePersonName) {
		this.archivePersonName = archivePersonName;
	}
	/**
	 * @return the archiveTime
	 */
	public Timestamp getArchiveTime() {
		return archiveTime;
	}
	/**
	 * @param archiveTime the archiveTime to set
	 */
	public void setArchiveTime(Timestamp archiveTime) {
		this.archiveTime = archiveTime;
	}
}
