package com.yuanluesoft.municipal.facilities.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 设施监控案件(facilities_event)
 * @author linchuan
 *
 */
public class FacilitiesEvent extends WorkflowData {
	private String eventNumber; //案件编号
	private String source; //案件来源,信息采集员/PDA即办/PDA承办/12345诉求/12345来电/110联动/公众举报/短息举报/领导批办
	private String zone; //区域
	private String observer; //上报人
	private String observerNumber; //上报号码
	private String reporter; //举报人
	private String contect; //联系方式
	private char isReceipt = '0'; //是否需回复
	private String receiptMode; //回复方式,电话回复/短信回复
	private String receiptTo; //回复对象
	private String recorder; //接线员
	private String partCode; //部件编号
	private int timeLimit; //处理时限
	private String level; //案件等级,一般性案件/重大案件
	private String category; //案件分类
	private String childCategory; //案件子分类
	private String duplicate; //重复案件,非重复案件/1次复案件/2次复案件/3次及以上重复案件
	private double XPos; //定位结果X
	private double YPos; //定位结果Y
	private String position; //事发位置
	private String description; //案件描述
	private String remark; //备注
	private String creator; //登记人
	private long creatorId; //登记人ID
	private Timestamp created; //登记时间
	private char isTruly = '0'; //事件是否真实
	private String truthDescription; //真实性检查情况
	private char fixed = '0'; //是否处理完毕	
	private String fixDescription; //处理情况说明
	
	/**
	 * 获取等级对应的图片
	 * @return
	 */
	public String getLevelImage() {
		return "重大案件".equals(level) ? "icon/redFlag.gif" : "icon/blueFlag.gif";
	}
	
	/**
	 * 获取时限对应的图片
	 * @return
	 */
	public String getLimitImage() {
		long timeLeft = getTimeLeft();
		if(timeLeft<0) { //超时
			return "icon/redLamp.gif";
		}
		else if(timeLeft < 60000 * 5) { //即将超时(5分钟)
			return "icon/yellowLamp.gif";
		}
		else { //未超时
			return "icon/greenLamp.gif";
		}
	}
	
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
	 * @return the childCategory
	 */
	public String getChildCategory() {
		return childCategory;
	}
	/**
	 * @param childCategory the childCategory to set
	 */
	public void setChildCategory(String childCategory) {
		this.childCategory = childCategory;
	}
	/**
	 * @return the contect
	 */
	public String getContect() {
		return contect;
	}
	/**
	 * @param contect the contect to set
	 */
	public void setContect(String contect) {
		this.contect = contect;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the duplicate
	 */
	public String getDuplicate() {
		return duplicate;
	}
	/**
	 * @param duplicate the duplicate to set
	 */
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}
	/**
	 * @return the isReceipt
	 */
	public char getIsReceipt() {
		return isReceipt;
	}
	/**
	 * @param isReceipt the isReceipt to set
	 */
	public void setIsReceipt(char isReceipt) {
		this.isReceipt = isReceipt;
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
	 * @return the observer
	 */
	public String getObserver() {
		return observer;
	}
	/**
	 * @param observer the observer to set
	 */
	public void setObserver(String observer) {
		this.observer = observer;
	}
	/**
	 * @return the observerNumber
	 */
	public String getObserverNumber() {
		return observerNumber;
	}
	/**
	 * @param observerNumber the observerNumber to set
	 */
	public void setObserverNumber(String observerNumber) {
		this.observerNumber = observerNumber;
	}
	/**
	 * @return the partCode
	 */
	public String getPartCode() {
		return partCode;
	}
	/**
	 * @param partCode the partCode to set
	 */
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the receiptMode
	 */
	public String getReceiptMode() {
		return receiptMode;
	}
	/**
	 * @param receiptMode the receiptMode to set
	 */
	public void setReceiptMode(String receiptMode) {
		this.receiptMode = receiptMode;
	}
	/**
	 * @return the receiptTo
	 */
	public String getReceiptTo() {
		return receiptTo;
	}
	/**
	 * @param receiptTo the receiptTo to set
	 */
	public void setReceiptTo(String receiptTo) {
		this.receiptTo = receiptTo;
	}
	/**
	 * @return the recorder
	 */
	public String getRecorder() {
		return recorder;
	}
	/**
	 * @param recorder the recorder to set
	 */
	public void setRecorder(String recorder) {
		this.recorder = recorder;
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
	 * @return the reporter
	 */
	public String getReporter() {
		return reporter;
	}
	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the xPos
	 */
	public double getXPos() {
		return XPos;
	}
	/**
	 * @param pos the xPos to set
	 */
	public void setXPos(double pos) {
		XPos = pos;
	}
	/**
	 * @return the yPos
	 */
	public double getYPos() {
		return YPos;
	}
	/**
	 * @param pos the yPos to set
	 */
	public void setYPos(double pos) {
		YPos = pos;
	}
	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
	}
	/**
	 * @param zone the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}

	/**
	 * @return the fixDescription
	 */
	public String getFixDescription() {
		return fixDescription;
	}

	/**
	 * @param fixDescription the fixDescription to set
	 */
	public void setFixDescription(String fixDescription) {
		this.fixDescription = fixDescription;
	}

	/**
	 * @return the fixed
	 */
	public char getFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(char fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the isTruly
	 */
	public char getIsTruly() {
		return isTruly;
	}

	/**
	 * @param isTruly the isTruly to set
	 */
	public void setIsTruly(char isTruly) {
		this.isTruly = isTruly;
	}

	/**
	 * @return the truthDescription
	 */
	public String getTruthDescription() {
		return truthDescription;
	}

	/**
	 * @param truthDescription the truthDescription to set
	 */
	public void setTruthDescription(String truthDescription) {
		this.truthDescription = truthDescription;
	}

	/**
	 * @return the eventNumber
	 */
	public String getEventNumber() {
		return eventNumber;
	}

	/**
	 * @param eventNumber the eventNumber to set
	 */
	public void setEventNumber(String eventNumber) {
		this.eventNumber = eventNumber;
	}
}