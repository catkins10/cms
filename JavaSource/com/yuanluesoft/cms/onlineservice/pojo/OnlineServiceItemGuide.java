package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:办理指南(onlineservice_item_guide)
 * @author linchuan
 *
 */
public class OnlineServiceItemGuide extends Record {
	private long itemId; //办理事项ID
	private String condition; //申办条件
	private String according; //办理依据
	private String program; //办理程序
	private String legalLimit; //法定时限
	private String timeLimit; //承诺时限
	private String chargeAccording; //收费依据
	private String chargeStandard; //收费标准
	private String legalRight; //法律权利,申请人法律权利及申诉途径
	private String responsibleDepartment; //责任部门
	private String address; //办理地点
	private String traffic; //交通线路
	private String transactor; //经办人
	private String transactorTel; //经办人联系电话
	private String acceptLimit; //受理数量限制
	private String acceptTime; //受理时间
	private String superviseLevel; //监管等级
	private String complaintTel; //监督投诉电话
	private String visaState; //签证情况
	private String punishType; //处罚种类,类别为行政处罚时有效
	private String discretionRule; //裁量规则,类别为行政处罚、行政强制时有效
	private String coerciveMeasure; //强制措施,类别为行政强制时有效
	private String acceptMode; //受理形式
	private String publicMode; //公开形式
	private String publicRange; //公开范围
	private String publicTime; //公开时间

	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the according
	 */
	public String getAccording() {
		return according;
	}
	/**
	 * @param according the according to set
	 */
	public void setAccording(String according) {
		this.according = according;
	}
	/**
	 * @return the chargeAccording
	 */
	public String getChargeAccording() {
		return chargeAccording;
	}
	/**
	 * @param chargeAccording the chargeAccording to set
	 */
	public void setChargeAccording(String chargeAccording) {
		this.chargeAccording = chargeAccording;
	}
	/**
	 * @return the chargeStandard
	 */
	public String getChargeStandard() {
		return chargeStandard;
	}
	/**
	 * @param chargeStandard the chargeStandard to set
	 */
	public void setChargeStandard(String chargeStandard) {
		this.chargeStandard = chargeStandard;
	}
	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	/**
	 * @return the legalRight
	 */
	public String getLegalRight() {
		return legalRight;
	}
	/**
	 * @param legalRight the legalRight to set
	 */
	public void setLegalRight(String legalRight) {
		this.legalRight = legalRight;
	}
	/**
	 * @return the program
	 */
	public String getProgram() {
		return program;
	}
	/**
	 * @param program the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}
	/**
	 * @return the timeLimit
	 */
	public String getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the traffic
	 */
	public String getTraffic() {
		return traffic;
	}
	/**
	 * @param traffic the traffic to set
	 */
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	/**
	 * @return the transactor
	 */
	public String getTransactor() {
		return transactor;
	}
	/**
	 * @param transactor the transactor to set
	 */
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	/**
	 * @return the transactorTel
	 */
	public String getTransactorTel() {
		return transactorTel;
	}
	/**
	 * @param transactorTel the transactorTel to set
	 */
	public void setTransactorTel(String transactorTel) {
		this.transactorTel = transactorTel;
	}
	/**
	 * @return the visaState
	 */
	public String getVisaState() {
		return visaState;
	}
	/**
	 * @param visaState the visaState to set
	 */
	public void setVisaState(String visaState) {
		this.visaState = visaState;
	}
	/**
	 * @return the acceptLimit
	 */
	public String getAcceptLimit() {
		return acceptLimit;
	}
	/**
	 * @param acceptLimit the acceptLimit to set
	 */
	public void setAcceptLimit(String acceptLimit) {
		this.acceptLimit = acceptLimit;
	}
	/**
	 * @return the acceptTime
	 */
	public String getAcceptTime() {
		return acceptTime;
	}
	/**
	 * @param acceptTime the acceptTime to set
	 */
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	/**
	 * @return the complaintTel
	 */
	public String getComplaintTel() {
		return complaintTel;
	}
	/**
	 * @param complaintTel the complaintTel to set
	 */
	public void setComplaintTel(String complaintTel) {
		this.complaintTel = complaintTel;
	}
	/**
	 * @return the coerciveMeasure
	 */
	public String getCoerciveMeasure() {
		return coerciveMeasure;
	}
	/**
	 * @param coerciveMeasure the coerciveMeasure to set
	 */
	public void setCoerciveMeasure(String coerciveMeasure) {
		this.coerciveMeasure = coerciveMeasure;
	}
	/**
	 * @return the discretionRule
	 */
	public String getDiscretionRule() {
		return discretionRule;
	}
	/**
	 * @param discretionRule the discretionRule to set
	 */
	public void setDiscretionRule(String discretionRule) {
		this.discretionRule = discretionRule;
	}
	/**
	 * @return the punishType
	 */
	public String getPunishType() {
		return punishType;
	}
	/**
	 * @param punishType the punishType to set
	 */
	public void setPunishType(String punishType) {
		this.punishType = punishType;
	}
	/**
	 * @return the publicMode
	 */
	public String getPublicMode() {
		return publicMode;
	}
	/**
	 * @param publicMode the publicMode to set
	 */
	public void setPublicMode(String publicMode) {
		this.publicMode = publicMode;
	}
	/**
	 * @return the publicRange
	 */
	public String getPublicRange() {
		return publicRange;
	}
	/**
	 * @param publicRange the publicRange to set
	 */
	public void setPublicRange(String publicRange) {
		this.publicRange = publicRange;
	}
	/**
	 * @return the publicTime
	 */
	public String getPublicTime() {
		return publicTime;
	}
	/**
	 * @param publicTime the publicTime to set
	 */
	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}
	/**
	 * @return the superviseLevel
	 */
	public String getSuperviseLevel() {
		return superviseLevel;
	}
	/**
	 * @param superviseLevel the superviseLevel to set
	 */
	public void setSuperviseLevel(String superviseLevel) {
		this.superviseLevel = superviseLevel;
	}
	/**
	 * @return the responsibleDepartment
	 */
	public String getResponsibleDepartment() {
		return responsibleDepartment;
	}
	/**
	 * @param responsibleDepartment the responsibleDepartment to set
	 */
	public void setResponsibleDepartment(String responsibleDepartment) {
		this.responsibleDepartment = responsibleDepartment;
	}
	/**
	 * @return the acceptMode
	 */
	public String getAcceptMode() {
		return acceptMode;
	}
	/**
	 * @param acceptMode the acceptMode to set
	 */
	public void setAcceptMode(String acceptMode) {
		this.acceptMode = acceptMode;
	}
	/**
	 * @return the legalLimit
	 */
	public String getLegalLimit() {
		return legalLimit;
	}
	/**
	 * @param legalLimit the legalLimit to set
	 */
	public void setLegalLimit(String legalLimit) {
		this.legalLimit = legalLimit;
	}
}
