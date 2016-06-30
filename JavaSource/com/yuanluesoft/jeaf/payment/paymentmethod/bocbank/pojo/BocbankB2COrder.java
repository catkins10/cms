package com.yuanluesoft.jeaf.payment.paymentmethod.bocbank.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 中国银行:B2C订单(payment_bocbank_b2c_order)
 * @author linchuan
 *
 */
public class BocbankB2COrder  extends Record {
	private long paymentId; //支付单ID
	private String merchantNo; //商户号,BOC商户ID
	private String orderNo; //BOC商户ID,商户系统产生的订单号
	private String orderSeq; //银行订单流水号,银行的订单流水号（银行产生的订单唯一标识）
	private String orderStatus; //订单状态,订单状态：0-未处理 1-支付 4-未明 5-失败
	private String cardTyp; //银行卡类别,银行卡类别：01：中行借记卡 02：中行信用卡，信用卡（分行卡） 04：中行信用卡，信用卡（总行卡） 11：银联借记卡 21：VISA借记卡 22：VISA信用卡 31：MC 借记卡 32：MC 信用卡 42：运通卡 52：大来卡 62：JCB卡
	private String acctNo; //支付卡号,为保护个人客户信息，我行暂不提供该数据项，以****反馈
	private String holderName; //持卡人姓名,为保护个人客户信息，我行暂不提供该数据项，以****反馈
	private String ibknum; //支付卡省行联行号,为保护个人客户信息，我行暂不提供该数据项，以****反馈
	private String payTime; //支付时间,支付交易的日期时间 格式：YYYYMMDDHHMISS
	private String payAmount; //支付金额,支付金额，格式：整数位不前补零,小数位补齐2位 即：不超过10位整数位+1位小数点+2位小数  无效格式如123，.10，1.1,有效格式如1.00，0.10
	private String visitorIp; //访问者IP,客户通过网银支付时的IP地址信息
	private String visitorRefer; //访问者Refer信息,客户浏览器跳转至网银支付登录界面前所在页面的URL（urlEncode格式）
	
	/**
	 * @return the acctNo
	 */
	public String getAcctNo() {
		return acctNo;
	}
	/**
	 * @param acctNo the acctNo to set
	 */
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	/**
	 * @return the cardTyp
	 */
	public String getCardTyp() {
		return cardTyp;
	}
	/**
	 * @param cardTyp the cardTyp to set
	 */
	public void setCardTyp(String cardTyp) {
		this.cardTyp = cardTyp;
	}
	/**
	 * @return the holderName
	 */
	public String getHolderName() {
		return holderName;
	}
	/**
	 * @param holderName the holderName to set
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	/**
	 * @return the ibknum
	 */
	public String getIbknum() {
		return ibknum;
	}
	/**
	 * @param ibknum the ibknum to set
	 */
	public void setIbknum(String ibknum) {
		this.ibknum = ibknum;
	}
	/**
	 * @return the merchantNo
	 */
	public String getMerchantNo() {
		return merchantNo;
	}
	/**
	 * @param merchantNo the merchantNo to set
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
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
	 * @return the orderSeq
	 */
	public String getOrderSeq() {
		return orderSeq;
	}
	/**
	 * @param orderSeq the orderSeq to set
	 */
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
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
	 * @return the payAmount
	 */
	public String getPayAmount() {
		return payAmount;
	}
	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	/**
	 * @return the payTime
	 */
	public String getPayTime() {
		return payTime;
	}
	/**
	 * @param payTime the payTime to set
	 */
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	/**
	 * @return the visitorIp
	 */
	public String getVisitorIp() {
		return visitorIp;
	}
	/**
	 * @param visitorIp the visitorIp to set
	 */
	public void setVisitorIp(String visitorIp) {
		this.visitorIp = visitorIp;
	}
	/**
	 * @return the visitorRefer
	 */
	public String getVisitorRefer() {
		return visitorRefer;
	}
	/**
	 * @param visitorRefer the visitorRefer to set
	 */
	public void setVisitorRefer(String visitorRefer) {
		this.visitorRefer = visitorRefer;
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
}