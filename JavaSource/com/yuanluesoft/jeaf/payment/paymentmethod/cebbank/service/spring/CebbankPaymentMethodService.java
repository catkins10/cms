package com.yuanluesoft.jeaf.payment.paymentmethod.cebbank.service.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;

import com.csii.payment.client.core.CebMerchantSignVerify;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.cebbank.pojo.CebbankOrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 光大银行支付服务
 * @author linchuan
 *
 */
public class CebbankPaymentMethodService implements PaymentMethodService {
	private DatabaseService databaseService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getB2BAccountTypes()
	 */
	public String getB2BAccountTypes() {
		return "企业";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getB2CAccountTypes()
	 */
	public String getB2CAccountTypes() {
		return "个人";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getIconUrl()
	 */
	public String getIconUrl() {
		return Environment.getContextPath() + "/jeaf/payment/cebbank/logo/cebbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "光大银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("B2C支付URL", "b2cPaymentURL", null, "https://www.cebbank.com/per/preEpayLogin.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2B支付URL", "b2bPaymentURL", null, "https://ebank.cebbank.com/cebent/preEpayLogin.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2C支付查询URL", "b2cPaymentQueryURL", null, "https://www.cebbank.com/per/QueryMerchantEpay.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2B支付查询URL", "b2bPaymentQueryURL", null, "https://www.cebbank.com/ent/QueryMerchantEpay.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("商户ID", "merchantId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("支付时用的IP", "paymentIp", "WEB服务器IP", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("支付时用的端口", "paymentPort", "WEB服务器端口", "80", false));
		return parameterDefines;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.service.PaymentMethodService#redirectToBankPage(com.yuanluesoft.jeaf.payment.pojo.Payment, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String transactName = "企业".equals(payment.getAccountType()) ? "EPER" : "IPER"; //交易代码
		String transactURL = paymentMerchant.getParameterValue("企业".equals(payment.getAccountType()) ? "b2bPaymentURL" : "b2cPaymentURL"); //URL
		String plain = "transId=" + transactName + //交易代码IPER/EPER
					   "~|~merchantId=" + paymentMerchant.getParameterValue("merchantId") + //商户代码
					   "~|~orderId=" + payment.getId() + //订单号
					   "~|~transAmt=" + payment.getMoney() + //交易金额
					   "~|~transDateTime=" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss") + //交易时间
					   "~|~currencyType=001" + //币种
					   "~|~customerName=" + payment.getPayerName() + //订货人姓名
					   "~|~productInfo=" + payment.getPaymentReason() + //商品信息
					   "~|~customerEMail=" + //订货人EMAIL 
					   "~|~merURL=" + ("http://" + paymentMerchant.getParameterValue("paymentIp") + ":" + paymentMerchant.getParameterValue("paymentPort") + request.getContextPath() + "/jeaf/payment/receiveNotice.shtml?paymentId=" + payment.getId()) + //商户URL,receiveNotice.shtml目前没有,没有实现的必要
					   "~|~merURL1=" + ("http://" + paymentMerchant.getParameterValue("paymentIp") + ":" + paymentMerchant.getParameterValue("paymentPort") + request.getContextPath() + "/jeaf/payment/completePayment.shtml?paymentId=" + payment.getId()) + //商户URL1
					   "~|~msgExt="; //附加信息
		if(Logger.isDebugEnabled()) {
			Logger.debug("CebbankPaymentMethodService: redirectToBankPage, request is " + plain);
		}
		String signatureBody = CebMerchantSignVerify.merchantSignData_ABA(plain); //密文
		try {
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("<html>");
			writer.write("	<body onload=\"document.forms[0].submit()\">");
			writer.write("		<form action=\"" + transactURL + "\" method=\"post\">");
			writer.write("			<input type=\"hidden\" name=\"TransName\" value=\"" + transactName + "\">");
			writer.write("			<input type=\"hidden\" name=\"Plain\" value=\"" + plain + "\">");
			writer.write("			<input type=\"hidden\" name=\"Signature\" value=\"" + signatureBody + "\">");
			writer.write("		</form>");
			writer.write("	</body>");
			writer.write("</html>");
		}
		catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPayment(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		//请求查询交易记录
		String plain = "transId=IQSR" +
					   "~|~merchantId=" + paymentMerchant.getParameterValue("merchantId") +
					   "~|~originalorderId=" + payment.getId() + //测试单据："200902022189"，金额必须为10元
					   "~|~originalTransAmt=" + payment.getMoney() +
					   "~|~originalTransDateTime=" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss"); //TODO 测试  "20090202218900";
		String signature = CebMerchantSignVerify.merchantSignData_ABA(plain);
		NameValuePair[] valuePairs = {new NameValuePair("TransName", "IQSR"), new NameValuePair("Plain", plain), new NameValuePair("Signature", signature)};
		String ret;
		try {
			ret = HttpUtils.doPost(paymentMerchant.getParameterValue("企业".equals(payment.getAccountType()) ? "b2bPaymentQueryURL" : "b2cPaymentQueryURL"), null, valuePairs, null).getResponseBody();
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("CebbankPaymentMethodService: payment query, request is " + plain + ", response is " + ret);
		}
		if(ret==null) {
			return null;
		}
		//解析返回值
		int beginIndex = ret.indexOf("ResponseCode=");
		if(beginIndex==-1) {
			return null;
		}
		beginIndex += "ResponseCode=".length();
		int endIndex = ret.indexOf("Plain=", beginIndex);
		if(endIndex==-1) {
			return null;
		}
		String responseCode = ret.substring(beginIndex, endIndex).trim(); //返回码
		if(!"0000".equals(responseCode)) {
			throw new ServiceException(responseCode);
		}
		beginIndex = endIndex + "Plain=".length();
		endIndex = ret.indexOf("Signature=", beginIndex);
		if(endIndex==-1) {
			return null;
		}
		plain = ret.substring(beginIndex, endIndex).trim(); //返回内容明文
		signature = ret.substring(endIndex + "Signature=".length()).trim(); //返回内容密文
		//校验返回值
		if(!CebMerchantSignVerify.merchantVerifyPayGate_ABA(signature, plain)) {
			return null; //校验不通过
		}
		//解析返回内容,Plain=transId=IQSR~|~originalTransId=ZF01~|~orderId=200902022189~|~transSeqNo=901239710367~|~merchantId=370310000003~|~transDateTime=20090202~|~ppDateTime=20080101~|~clearingDate=20090420~|~transAmt=10~|~transAmt1=0~|~feeAmt=0~|~transStatus=00~|~respCode=AAAAAAA~|~
		//保存交易记录明细
		CebbankOrder order = (CebbankOrder)databaseService.findRecordByHql("from CebbankOrder CebbankOrder where CebbankOrder.paymentId=" + payment.getId());
		boolean isNew = (order==null);
		if(isNew) { //没有记录过
			order = new CebbankOrder();
			order.setId(UUIDLongGenerator.generateId()); //ID
			order.setPaymentId(payment.getId()); //支付ID
		}
		order.setOriginalTransId(getStringValue(plain, "originalTransId")); //原交易代码,ZF01/支付,ZF02/退货 
		order.setOrderId(getStringValue(plain, "orderId")); //订单号
		order.setTransSeqNo(getStringValue(plain, "transSeqNo")); //支付系统交易流水号
		order.setMerchantId(getStringValue(plain, "merchantId")); //商户ID
		order.setTransDateTime(new Timestamp(getDateValue(plain, "transDateTime").getTime())); //交易时间
		order.setPpDateTime(new Timestamp(getDateValue(plain, "ppDateTime").getTime())); //支付系统交易时间
		order.setClearingDate(getDateValue(plain, "clearingDate")); //清算日期
		order.setTransAmt(getDoubleValue(plain, "transAmt")); //交易金额
		order.setTransAmt1(getDoubleValue(plain, "transAmt1")); //已退货金额,对支付交易有效
		order.setFeeAmt(getDoubleValue(plain, "feeAmt")); //手续费金额,支付时为收取手续费金额,退货时为退手续费金额
		order.setTransStatus(getStringValue(plain, "transStatus")); //交易状态,00 交易成功/01交易失败/02撤消成功/03 部分退货/04 全部退货/05 预授权确认成功/06 预授权撤销成功/99 交易超时
		order.setRespCode(getStringValue(plain, "respCode")); //响应代码
		if(isNew) {
			databaseService.saveRecord(order);
		}
		else {
			databaseService.updateRecord(order);
		}
		
		//返回交易结果
		PaymentResult result = new PaymentResult();
		result.setTransactTime(order.getTransDateTime()); //交易时间
		result.setTransactMoney(order.getTransAmt()); //实际交易金额
		result.setFee(order.getFeeAmt()); //手续费
		result.setTransactSn(order.getTransSeqNo()); //支付系统流水号
		result.setTransactSuccess("00".equals(order.getTransStatus()) ? '1' : '0'); //是否交易成功
		result.setBankOrderId(order.getOrderId()); //银行端订单ID
		//交易失败原因
		String failedReason = null;
		if("01".equals(order.getTransStatus())) {
			failedReason = "交易失败";
		}
		else if("99".equals(order.getTransStatus())) {
			failedReason = "交易超时";
		}
		result.setFailedReason(failedReason); //交易失败原因
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPaymentByCallbackURL(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String)
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		return null; //不需要实现
	}
	
	/**
	 * 获取字符串值
	 * @param plain
	 * @param propertyName
	 * @return
	 */
	protected String getStringValue(String plain, String propertyName) {
		int beginIndex = plain.indexOf(propertyName + "=");
		if(beginIndex==-1) {
			return null;
		}
		beginIndex += propertyName.length() + 1;
		int endIndex = plain.indexOf("~|~", beginIndex);
		return endIndex==-1 ? plain.substring(beginIndex) : plain.substring(beginIndex, endIndex);
	}
	
	/**
	 * 获取浮点数字
	 * @param plain
	 * @param propertyName
	 * @return
	 */
	protected double getDoubleValue(String plain, String propertyName) {
		return Double.parseDouble(getStringValue(plain, propertyName));
	}
	
	/**
	 * 解析时间
	 * @param plain
	 * @param propertyName
	 * @return
	 */
	protected Timestamp getTimestampValue(String plain, String propertyName) {
		try {
			return DateTimeUtils.parseTimestamp(getStringValue(plain, propertyName), "yyyyMMddHHmmss");
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 解析日期
	 * @param plain
	 * @param propertyName
	 * @return
	 */
	protected Date getDateValue(String plain, String propertyName) {
		try {
			return DateTimeUtils.parseDate(getStringValue(plain, propertyName), "yyyyMMdd");
		}
		catch (ParseException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#parseTransactions(java.lang.String)
	 */
	public List parseTransactions(String transactionsFilePath) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#createTransferFile(java.util.List, java.lang.String)
	 */
	public String createTransferFile(List transfers, String transferFilePath, String subject, PaymentMerchant paymentMerchant) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#listTransactions(java.sql.Date, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public List listTransactions(Date day, PaymentMerchant paymentMerchant) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#transfer(java.util.List, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public void transfer(List transfers, PaymentMerchant paymentMerchant) throws ServiceException {
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getBalance(com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public double getBalance(PaymentMerchant paymentMerchant) throws ServiceException {
		return 0;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}