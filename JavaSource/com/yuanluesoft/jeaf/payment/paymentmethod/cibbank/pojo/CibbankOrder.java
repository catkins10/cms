package com.yuanluesoft.jeaf.payment.paymentmethod.cibbank.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 兴业银行:支付记录(payment_cibbank_order)
 * @author linchuan
 *
 */
public class CibbankOrder extends Record {
	private long paymentId; //支付单ID
	private String transactId; //交易流水号,最大30
	private String code; //错误代码,0-没有错误 非0-错误代码，最大5
	private String message; //错误信息,CODE非0时，错误信息，0时不返回，最大不定
	private Timestamp orderDate; //订单日期,格式为：yyyy-MM-dd
	private String orderNum; //订单号,最长30
	private double orderAmount; //订单金额,最长整数位最长15位，小数2位
	private String orderStatus; //订单状态,0-未支付 1-买方处理中 2-支付完成 3-订单撤销 4-订单过期
	private char paymentSuccess = '0'; //是否成功,由于订单状态在不同情况下解释不同，特此增加本字段
	private String fromAccountId; //支付账户代号,最大30
	private String fromAccountName; //支付账户户名,最大50
	private String fromCity; //支付人所在城市
	private String toAccountId; //收款账号,最大32
	private String toAccountName; //收款账户名称,最大50
	private String toBankName; //收款人开户行名称,最大50，它行时必回
	private String toBankNum; //收报行号,长度=12
	private String toCity; //兑付城市,最大30,它行时必回
	private String chequeNum; //凭证号,8位
	private double transactAmount; //支付金额,整数位最长15位，小数2位
	private String purpose; //用途,最大30
	private String memo; //备注,最大60
	private Timestamp dueDate; //结算日期,格式为：yyyy-MM-dd
	
	/**
	 * @return the chequeNum
	 */
	public String getChequeNum() {
		return chequeNum;
	}
	/**
	 * @param chequeNum the chequeNum to set
	 */
	public void setChequeNum(String chequeNum) {
		this.chequeNum = chequeNum;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the fromAccountId
	 */
	public String getFromAccountId() {
		return fromAccountId;
	}
	/**
	 * @param fromAccountId the fromAccountId to set
	 */
	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	/**
	 * @return the fromAccountName
	 */
	public String getFromAccountName() {
		return fromAccountName;
	}
	/**
	 * @param fromAccountName the fromAccountName to set
	 */
	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * @return the toAccountId
	 */
	public String getToAccountId() {
		return toAccountId;
	}
	/**
	 * @param toAccountId the toAccountId to set
	 */
	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}
	/**
	 * @return the toAccountName
	 */
	public String getToAccountName() {
		return toAccountName;
	}
	/**
	 * @param toAccountName the toAccountName to set
	 */
	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}
	/**
	 * @return the toBankName
	 */
	public String getToBankName() {
		return toBankName;
	}
	/**
	 * @param toBankName the toBankName to set
	 */
	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}
	/**
	 * @return the toBankNum
	 */
	public String getToBankNum() {
		return toBankNum;
	}
	/**
	 * @param toBankNum the toBankNum to set
	 */
	public void setToBankNum(String toBankNum) {
		this.toBankNum = toBankNum;
	}
	/**
	 * @return the toCity
	 */
	public String getToCity() {
		return toCity;
	}
	/**
	 * @param toCity the toCity to set
	 */
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	/**
	 * @return the transactAmount
	 */
	public double getTransactAmount() {
		return transactAmount;
	}
	/**
	 * @param transactAmount the transactAmount to set
	 */
	public void setTransactAmount(double transactAmount) {
		this.transactAmount = transactAmount;
	}
	/**
	 * @return the transactId
	 */
	public String getTransactId() {
		return transactId;
	}
	/**
	 * @param transactId the transactId to set
	 */
	public void setTransactId(String transactId) {
		this.transactId = transactId;
	}
	/**
	 * @return the dueDate
	 */
	public Timestamp getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the orderDate
	 */
	public Timestamp getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the paymentSuccess
	 */
	public char getPaymentSuccess() {
		return paymentSuccess;
	}
	/**
	 * @param paymentSuccess the paymentSuccess to set
	 */
	public void setPaymentSuccess(char paymentSuccess) {
		this.paymentSuccess = paymentSuccess;
	}
	/**
	 * @return the fromCity
	 */
	public String getFromCity() {
		return fromCity;
	}
	/**
	 * @param fromCity the fromCity to set
	 */
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
}