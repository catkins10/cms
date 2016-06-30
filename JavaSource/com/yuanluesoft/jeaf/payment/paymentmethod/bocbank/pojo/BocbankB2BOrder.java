package com.yuanluesoft.jeaf.payment.paymentmethod.bocbank.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 中国银行:B2B订单(payment_bocbank_b2b_order)
 * @author linchuan
 *
 */
public class BocbankB2BOrder extends Record {
	private long paymentId; //支付单ID
	private String bocNo; //商户号,网关商户号
	private String orderNo; //商户订单号,商户系统产生的订单号；数字与26个英文字母以及中划线（-）和下划线（_）
	private long orderSeq; //订单流水,网关保存的订单唯一流水号
	private String curCode; //订单币种,目前只支持001-人民币 固定填001
	private double orderAmount; //订单金额,订单总金额
	private String recvTime; //接单时间,网关接收订单的时间 格式：YYYYMMDDHHMISS 其中时间为24小时格式，例:2010年3月2日下午4点5分28秒表示为20100302160528
	private String lastTranTime; //最后交易时间,后台最近一次交易的处理时间
	private double refundedAmount; //订单累计退款金额,针对一笔订单支持多次退款，该域表示订单成功退款交易的累计金额
	private String orderStatus; //订单状态,订单状态 0:未确认/1:确认/T1:支付成功/T2:支付失败/T3:支付故障/ 10:过期失效
	private String actnumT; //收款账号,收款账户账号，收款账户为他行有可能超过18位
	private String actnamT; //收款账户名称,收款账户的户名 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private String bocFlag; //中行标志,收款账户是否是中行的标志  0:他行/1:中行 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private String ibkNamT; //收款行名称,中行标志表示为他行时必填 收款账户开户行名称，最长29位汉字符 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private String cnapsNo; //人行行号,中行标志表示为他行时必填 人民银行分配给各行的支付行行号 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private String ibkNumT; //收款省行联行号,中行标志表示为中行时必填 收款账户所属省行联行号（收款账户是中行账户时） 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private String orgidtT; //收款行机构号,可选项 收款账户开户行机构号（收款账户是中行账户时，旧线为orgidt机构号，新线要求是bancs机构号） 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
	private char orderType; //订单类型,订单类型为0-B2B直付;订单类型为1-B2B保付;
	private String ibknumF; //付款省行联行号,付款账户所属省行联行号，为保护客户信息，我行暂不提供该数据项，以****反馈商户。
	private String actnumF; //付款账号,付款账户账号，为保护客户信息，我行暂不提供该数据项，以****反馈商户
	private String actnamF; //付款账户名称,付款账户的户名，为保护客户信息，我行暂不提供该数据项，以****反馈商户
	
	/**
	 * @return the actnamF
	 */
	public String getActnamF() {
		return actnamF;
	}
	/**
	 * @param actnamF the actnamF to set
	 */
	public void setActnamF(String actnamF) {
		this.actnamF = actnamF;
	}
	/**
	 * @return the actnamT
	 */
	public String getActnamT() {
		return actnamT;
	}
	/**
	 * @param actnamT the actnamT to set
	 */
	public void setActnamT(String actnamT) {
		this.actnamT = actnamT;
	}
	/**
	 * @return the actnumF
	 */
	public String getActnumF() {
		return actnumF;
	}
	/**
	 * @param actnumF the actnumF to set
	 */
	public void setActnumF(String actnumF) {
		this.actnumF = actnumF;
	}
	/**
	 * @return the actnumT
	 */
	public String getActnumT() {
		return actnumT;
	}
	/**
	 * @param actnumT the actnumT to set
	 */
	public void setActnumT(String actnumT) {
		this.actnumT = actnumT;
	}
	/**
	 * @return the bocFlag
	 */
	public String getBocFlag() {
		return bocFlag;
	}
	/**
	 * @param bocFlag the bocFlag to set
	 */
	public void setBocFlag(String bocFlag) {
		this.bocFlag = bocFlag;
	}
	/**
	 * @return the bocNo
	 */
	public String getBocNo() {
		return bocNo;
	}
	/**
	 * @param bocNo the bocNo to set
	 */
	public void setBocNo(String bocNo) {
		this.bocNo = bocNo;
	}
	/**
	 * @return the cnapsNo
	 */
	public String getCnapsNo() {
		return cnapsNo;
	}
	/**
	 * @param cnapsNo the cnapsNo to set
	 */
	public void setCnapsNo(String cnapsNo) {
		this.cnapsNo = cnapsNo;
	}
	/**
	 * @return the curCode
	 */
	public String getCurCode() {
		return curCode;
	}
	/**
	 * @param curCode the curCode to set
	 */
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	/**
	 * @return the ibkNamT
	 */
	public String getIbkNamT() {
		return ibkNamT;
	}
	/**
	 * @param ibkNamT the ibkNamT to set
	 */
	public void setIbkNamT(String ibkNamT) {
		this.ibkNamT = ibkNamT;
	}
	/**
	 * @return the ibknumF
	 */
	public String getIbknumF() {
		return ibknumF;
	}
	/**
	 * @param ibknumF the ibknumF to set
	 */
	public void setIbknumF(String ibknumF) {
		this.ibknumF = ibknumF;
	}
	/**
	 * @return the ibkNumT
	 */
	public String getIbkNumT() {
		return ibkNumT;
	}
	/**
	 * @param ibkNumT the ibkNumT to set
	 */
	public void setIbkNumT(String ibkNumT) {
		this.ibkNumT = ibkNumT;
	}
	/**
	 * @return the lastTranTime
	 */
	public String getLastTranTime() {
		return lastTranTime;
	}
	/**
	 * @param lastTranTime the lastTranTime to set
	 */
	public void setLastTranTime(String lastTranTime) {
		this.lastTranTime = lastTranTime;
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
	public long getOrderSeq() {
		return orderSeq;
	}
	/**
	 * @param orderSeq the orderSeq to set
	 */
	public void setOrderSeq(long orderSeq) {
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
	 * @return the orderType
	 */
	public char getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(char orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the orgidtT
	 */
	public String getOrgidtT() {
		return orgidtT;
	}
	/**
	 * @param orgidtT the orgidtT to set
	 */
	public void setOrgidtT(String orgidtT) {
		this.orgidtT = orgidtT;
	}
	/**
	 * @return the recvTime
	 */
	public String getRecvTime() {
		return recvTime;
	}
	/**
	 * @param recvTime the recvTime to set
	 */
	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}
	/**
	 * @return the refundedAmount
	 */
	public double getRefundedAmount() {
		return refundedAmount;
	}
	/**
	 * @param refundedAmount the refundedAmount to set
	 */
	public void setRefundedAmount(double refundedAmount) {
		this.refundedAmount = refundedAmount;
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