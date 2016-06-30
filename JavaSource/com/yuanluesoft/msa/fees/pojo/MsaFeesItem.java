package com.yuanluesoft.msa.fees.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 规费征缴：收费细项(msa_fees_item)
 * @author linchuan
 *
 */
public class MsaFeesItem extends Record {
	private long feesId; //收费基本信息ID
	private String examCategory; //考试类别
	private String address; //院校
	private Date examTime; //考试时间
	private double examineeNumber; //考生人数
	private double charge; //费用
	private String period; //期数
	private String paymentMode; //缴费方式
	private long unitId; //负责单位ID
	private String unitName; //负责单位
	private Timestamp completeTime; //完成时间
	private long transactorId; //经办人ID
	private String transactor; //经办人姓名
	private String feedback; //完成情况说明
	private String remark; //备注
	
	/**
	 * 是否办结
	 * @return
	 */
	public String getIsComplete() {
		return completeTime==null ? "未办结" : "已办结";
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
	 * @return the charge
	 */
	public double getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(double charge) {
		this.charge = charge;
	}
	/**
	 * @return the completeTime
	 */
	public Timestamp getCompleteTime() {
		return completeTime;
	}
	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}
	/**
	 * @return the examCategory
	 */
	public String getExamCategory() {
		return examCategory;
	}
	/**
	 * @param examCategory the examCategory to set
	 */
	public void setExamCategory(String examCategory) {
		this.examCategory = examCategory;
	}
	/**
	 * @return the examineeNumber
	 */
	public double getExamineeNumber() {
		return examineeNumber;
	}
	/**
	 * @param examineeNumber the examineeNumber to set
	 */
	public void setExamineeNumber(double examineeNumber) {
		this.examineeNumber = examineeNumber;
	}
	/**
	 * @return the examTime
	 */
	public Date getExamTime() {
		return examTime;
	}
	/**
	 * @param examTime the examTime to set
	 */
	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}
	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return the feesId
	 */
	public long getFeesId() {
		return feesId;
	}
	/**
	 * @param feesId the feesId to set
	 */
	public void setFeesId(long feesId) {
		this.feesId = feesId;
	}
	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
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
	 * @return the transactorId
	 */
	public long getTransactorId() {
		return transactorId;
	}
	/**
	 * @param transactorId the transactorId to set
	 */
	public void setTransactorId(long transactorId) {
		this.transactorId = transactorId;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}