package com.yuanluesoft.jeaf.payment.paymentmethod.cebbank.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 光大银行:支付记录(payment_cebbank_order)
 * @author linchuan
 *
 */
public class CebbankOrder extends Record {
	private long paymentId; //支付单ID
	private String originalTransId; //原交易代码,ZF01/支付,ZF02/退货 
	private String orderId; //订单号
	private String transSeqNo; //支付系统交易流水号
	private String merchantId; //商户ID
	private Timestamp transDateTime; //交易时间
	private Timestamp ppDateTime; //支付系统交易时间
	private Date clearingDate; //清算日期
	private double transAmt; //交易金额
	private double transAmt1; //已退货金额,对支付交易有效
	private double feeAmt; //手续费金额,支付时为收取手续费金额,退货时为退手续费金额
	private String transStatus; //交易状态,00 交易成功/01交易失败/02撤消成功/03 部分退货/04 全部退货/05 预授权确认成功/06 预授权撤销成功/99 交易超时
	private String respCode; //响应代码
	
	/**
	 * @return the clearingDate
	 */
	public Date getClearingDate() {
		return clearingDate;
	}
	/**
	 * @param clearingDate the clearingDate to set
	 */
	public void setClearingDate(Date clearingDate) {
		this.clearingDate = clearingDate;
	}
	/**
	 * @return the feeAmt
	 */
	public double getFeeAmt() {
		return feeAmt;
	}
	/**
	 * @param feeAmt the feeAmt to set
	 */
	public void setFeeAmt(double feeAmt) {
		this.feeAmt = feeAmt;
	}
	/**
	 * @return the merchantId
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the originalTransId
	 */
	public String getOriginalTransId() {
		return originalTransId;
	}
	/**
	 * @param originalTransId the originalTransId to set
	 */
	public void setOriginalTransId(String originalTransId) {
		this.originalTransId = originalTransId;
	}
	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}
	/**
	 * @return the ppDateTime
	 */
	public Timestamp getPpDateTime() {
		return ppDateTime;
	}
	/**
	 * @param ppDateTime the ppDateTime to set
	 */
	public void setPpDateTime(Timestamp ppDateTime) {
		this.ppDateTime = ppDateTime;
	}
	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	/**
	 * @param respCode the respCode to set
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	/**
	 * @return the transAmt
	 */
	public double getTransAmt() {
		return transAmt;
	}
	/**
	 * @param transAmt the transAmt to set
	 */
	public void setTransAmt(double transAmt) {
		this.transAmt = transAmt;
	}
	/**
	 * @return the transAmt1
	 */
	public double getTransAmt1() {
		return transAmt1;
	}
	/**
	 * @param transAmt1 the transAmt1 to set
	 */
	public void setTransAmt1(double transAmt1) {
		this.transAmt1 = transAmt1;
	}
	/**
	 * @return the transDateTime
	 */
	public Timestamp getTransDateTime() {
		return transDateTime;
	}
	/**
	 * @param transDateTime the transDateTime to set
	 */
	public void setTransDateTime(Timestamp transDateTime) {
		this.transDateTime = transDateTime;
	}
	/**
	 * @return the transSeqNo
	 */
	public String getTransSeqNo() {
		return transSeqNo;
	}
	/**
	 * @param transSeqNo the transSeqNo to set
	 */
	public void setTransSeqNo(String transSeqNo) {
		this.transSeqNo = transSeqNo;
	}
	/**
	 * @return the transStatus
	 */
	public String getTransStatus() {
		return transStatus;
	}
	/**
	 * @param transStatus the transStatus to set
	 */
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
}
