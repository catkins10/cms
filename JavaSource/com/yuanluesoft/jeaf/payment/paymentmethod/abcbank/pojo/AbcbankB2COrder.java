package com.yuanluesoft.jeaf.payment.paymentmethod.abcbank.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 农业银行:B2C支付记录(payment_abcbank_b2c_order)
 * @author linchuan
 *
 */
public class AbcbankB2COrder extends Record {
	private long paymentId; //支付单ID
	private String orderNo; //订单编号
	private double orderAmount; //订单金额
	private String orderDesc; //订单说明,查询详细信息时才回传
	private String orderDate; //订单日期,查询详细信息时才回传
	private String orderTime; //订单时间,查询详细信息时才回传
	private String orderURL; //订单网址,查询详细信息时才回传
	private double payAmount; //支付金额
	private double refundAmount; //退货金额
	private String orderStatus; //订单状态,00：订单已取消  01：订单已建立，等待支付  02：消费者已支付，等待支付结果  03：订单已支付（支付成功）  04：订单已结算（支付成功）  05：订单已退款  99：订单支付失败 
	private String errorMessage; //交易失败原因
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the orderAmount
	 */
	public double getOrderAmount() {
		return orderAmount;
	}
	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the orderDesc
	 */
	public String getOrderDesc() {
		return orderDesc;
	}
	/**
	 * @param orderDesc the orderDesc to set
	 */
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}
	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	/**
	 * @return the orderURL
	 */
	public String getOrderURL() {
		return orderURL;
	}
	/**
	 * @param orderURL the orderURL to set
	 */
	public void setOrderURL(String orderURL) {
		this.orderURL = orderURL;
	}
	/**
	 * @return the payAmount
	 */
	public double getPayAmount() {
		return payAmount;
	}
	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
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
	 * @return the refundAmount
	 */
	public double getRefundAmount() {
		return refundAmount;
	}
	/**
	 * @param refundAmount the refundAmount to set
	 */
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
}
