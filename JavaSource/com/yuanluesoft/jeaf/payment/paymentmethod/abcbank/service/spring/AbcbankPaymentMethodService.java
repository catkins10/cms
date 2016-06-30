package com.yuanluesoft.jeaf.payment.paymentmethod.abcbank.service.spring;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitrust.b2b.trustpay.client.b2b.DownloadTrnxRequest;
import com.hitrust.b2b.trustpay.client.b2b.FundTransferRequest;
import com.hitrust.b2b.trustpay.client.b2b.QueryTrnxRequest;
import com.hitrust.b2b.trustpay.client.b2b.TrnxInfo;
import com.hitrust.b2b.trustpay.client.b2b.TrnxItems;
import com.hitrust.b2b.trustpay.client.b2b.TrnxRemarks;
import com.hitrust.trustpay.client.TrxResponse;
import com.hitrust.trustpay.client.XMLDocument;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.PaymentRequest;
import com.hitrust.trustpay.client.b2c.QueryOrderRequest;
import com.hitrust.trustpay.client.b2c.RefundRequest;
import com.hitrust.trustpay.client.b2c.SettleFile;
import com.hitrust.trustpay.client.b2c.SettleRequest;
import com.hitrust.trustpay.client.b2c.VoidPaymentRequest;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.abcbank.pojo.AbcbankB2BOrder;
import com.yuanluesoft.jeaf.payment.paymentmethod.abcbank.pojo.AbcbankB2COrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 农业银行支付服务
 * @author linchuan
 *
 */
public class AbcbankPaymentMethodService implements PaymentMethodService {
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
		return "农行卡支付,国际卡支付";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getIconUrl()
	 */
	public String getIconUrl() {
		return Environment.getContextPath() + "/jeaf/payment/abcbank/logo/abcbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "农业银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("收款方账号", "b2bAccountDBNo", "必要信息", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("收款方账户名", "b2bAccountDBName", "必要信息", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("收款方账户开户行联行号", "b2bAccountDBBank", "必要信息", null, false));
		return parameterDefines;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.service.PaymentMethodService#redirectToBankPage(com.yuanluesoft.jeaf.payment.pojo.Payment, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		if("企业".equals(payment.getAccountType())) {
			redirectToB2BPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
		else { //农行卡支付,国际卡支付（个人）
			redirectToB2CPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
	}
	
	/**
	 * 打开B2C支付页面
	 * @param payment
	 * @param aymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToB2CPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//测试:订单查询
		//queryB2CPayment(payment);

		//测试:取消订单
		//cancelB2CPayment(payment);
		
		//测试:退货
		//returnB2CPayment(payment);
		
		//测试:获取对账单
		getB2CStatement(DateTimeUtils.add(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, -1));
		
		//生成订单对象
		Order order = new Order();
		order.setOrderNo(payment.getId() + ""); //设定订单编号 （必要信息）
		order.setOrderDesc(payment.getPaymentReason()); //设定订单说明
		order.setOrderDate(DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyy/MM/dd")); //设定订单日期 （必要信息 - YYYY/MM/DD）
		order.setOrderTime(DateTimeUtils.formatTimestamp(payment.getCreated(), "HH:mm:ss")); //设定订单时间 （必要信息 - HH:MM:SS）
		order.setOrderAmount(payment.getMoney()); //设定订单金额 （必要信息）
		order.setOrderURL(payment.getPaymentFrom()==null ? "" : payment.getPaymentFrom()); //设定订单网址

		//生成支付请求对象
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setOrder(order); //设定支付请求的订单 （必要信息）
		paymentRequest.setProductType(PaymentRequest.PRD_TYPE_ONE); //设定商品种类 （必要信息）
		                                              //PaymentRequest.PRD_TYPE_ONE：非实体商品，如服务、IP卡、下载MP3、...
		                                              //PaymentRequest.PRD_TYPE_TWO：实体商品
		paymentRequest.setPaymentType("农行卡支付".equals(payment.getAccountType()) ? PaymentRequest.PAY_TYPE_ABC : PaymentRequest.PAY_TYPE_INT); //设定支付类型
		                                              //PaymentRequest.PAY_TYPE_ABC：农行卡支付
		                                              //PaymentRequest.PAY_TYPE_INT：国际卡支付
		paymentRequest.setNotifyType("1");   //设定商户通知方式
		                                      //0：URL页面通知
		                                      //1：服务器通知
		paymentRequest.setResultNotifyURL(paymentCompleteCallbackURL); //设定支付结果回传网址 （必要信息）
		paymentRequest.setMerchantRemarks(""); //设定商户备注信息
		
		//传送支付请求并取得支付网址
		TrxResponse trxResponse = paymentRequest.postRequest();
		if(trxResponse.isSuccess()) { //支付请求提交成功，将客户端导向支付页面
			try {
				response.sendRedirect(trxResponse.getValue("PaymentURL"));
			} 
			catch (IOException e) {
				throw new ServiceException(e.getMessage());
			}
		}
		else { //支付请求提交失败，商户自定后续动作
			Logger.error("AbcbankPaymentMethodService error: return code is " + trxResponse.getReturnCode() + "," + trxResponse.getErrorMessage());
			throw new ServiceException();
		}
	}
	
	/**
	 * 打开B2B支付页面
	 * @param payment
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToB2BPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//测试:订单查询
		//queryB2BPayment(payment);
		
		//测试:获取对账单
		//getB2BStatement(DateTimeUtils.date());
		
		//生成TrnxInfo对象
		TrnxItems trnxItems = new TrnxItems(); //可选信息，如果不需要显示交易明细信息，只需要创建一个空的TrnxItems对象
		//trnxItems.addTrnxItem(new TrnxItem("0001", "显示器", 1000.00f, 2));
		
		TrnxRemarks trnxRemarks = new TrnxRemarks(); //可选信息，如果不需要显示备注信息，只需要创建一个空的TrnxRemarks对象
		//trnxRemarks.addTrnxRemark(new TrnxRemark("合同号",  "555000000"));
		
		TrnxInfo tTrnxInfo = new TrnxInfo();
		tTrnxInfo.setTrnxOpr("TrnxOperator0001");
		tTrnxInfo.setTrnxRemarks(trnxRemarks);
		tTrnxInfo.setTrnxItems(trnxItems);
		
		//生成直接支付请求对象
		FundTransferRequest transferRequest = new FundTransferRequest();
		transferRequest.setTrnxInfo(tTrnxInfo); //设定交易细项 必要信息）
		transferRequest.setMerchantTrnxNo(payment.getId() + "");  //设定商户交易编号（必要信息）
		transferRequest.setTrnxAmount(payment.getMoney()); //设定交易金额必要信息）
		transferRequest.setTrnxDate(DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyy/MM/dd")); //设定交易日期（必要信息）
		transferRequest.setTrnxTime(DateTimeUtils.formatTimestamp(payment.getCreated(), "HH:mm:ss")); //设定交易时间（必要信息）
		transferRequest.setAccountDBNo(paymentMerchant.getParameterValue("b2bAccountDBNo")); //设定收款方账号（必要信息）
		transferRequest.setAccountDBName(paymentMerchant.getParameterValue("b2bAccountDBName")); //设定收款方账户名（必要信息）
		transferRequest.setAccountDBBank(paymentMerchant.getParameterValue("b2bAccountDBBank")); //设定收款方账户开户行联行号（必要信息）
		transferRequest.setResultNotifyURL(paymentCompleteCallbackURL); //设定交易结果回传网址（必要信息）
		transferRequest.setMerchantRemarks(""); //设定商户备注信息
		
		//传送直接支付请求并取得支付网址
		com.hitrust.b2b.trustpay.client.TrxResponse trxResponse = transferRequest.postRequest();
		if(trxResponse.isSuccess()) {//直接支付请求提交成功,将客户端导向出示买方企业客户证书页面（下面注释的4行程序的参数值商户仍然可以取到）
			//out.println("TrnxType       = [" + tTrxResponse.getValue("TrnxType"  )     + "]<br>");
			//out.println("TrnxAMT        = [" + tTrxResponse.getValue("TrnxAMT"  )      + "]<br>");
			//out.println("MerchantID     = [" + tTrxResponse.getValue("MerchantID"  )   + "]<br>");
			//out.println("MerchantTrnxNo = [" + tTrxResponse.getValue("MerchantTrnxNo") + "]<br>");
			try {
				response.sendRedirect(trxResponse.getValue("PaymentURL"));
			}
			catch (IOException e) {
				throw new ServiceException(e.getMessage());
			}
		}
		else { //直接支付请求提交失败，商户自定后续动作
			throw new ServiceException("ReturnCode=" + trxResponse.getReturnCode  () + ", ErrorMessage=" + trxResponse.getErrorMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPayment(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		if("企业".equals(payment.getAccountType())) {
			return queryB2BPayment(payment, paymentMerchant);
		}
		else { //农行卡支付,国际卡支付（个人）
			return queryB2CPayment(payment, paymentMerchant);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPaymentByCallbackURL(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String)
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		return null; //不需要实现
	}

	/**
	 * 查询B2C支付结果
	 * @param payment
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2CPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		//生成商户订单查询请求对象 
		QueryOrderRequest request = new QueryOrderRequest(); 
		request.setOrderNo(payment.getId() + "");  //订单号（必要信息） 
		request.enableDetailQuery(false); //是否查询详细信息 （必要信息） 
		 
		//传送商户订单查询请求并取得订单查询结果 
		TrxResponse response = request.postRequest(); 
		//判断商户订单查询结果状态，进行后续操作 
		if(!response.isSuccess()) { //商户订单查询失败
			throw new ServiceException("ReturnCode=" + response.getReturnCode  () + ", ErrorMessage=" + response.getErrorMessage());
		}
		//生成订单对象 
		Order queryOrder = new Order(new XMLDocument(response.getValue("Order")));
		//保存交易记录明细
		AbcbankB2COrder order = (AbcbankB2COrder)databaseService.findRecordByHql("from AbcbankB2COrder AbcbankB2COrder where AbcbankB2COrder.paymentId=" + payment.getId());
		boolean isNew = (order==null);
		if(isNew) { //没有记录过
			order = new AbcbankB2COrder();
			order.setId(UUIDLongGenerator.generateId()); //ID
			order.setPaymentId(payment.getId()); //支付ID
		}
		order.setOrderNo(queryOrder.getOrderNo()); //订单编号
		order.setOrderAmount(queryOrder.getOrderAmount()); //订单金额
		order.setOrderDesc(queryOrder.getOrderDesc()); //订单说明,查询详细信息时才回传
		order.setOrderDate(queryOrder.getOrderDate()); //订单日期,查询详细信息时才回传
		order.setOrderTime(queryOrder.getOrderTime()); //订单时间,查询详细信息时才回传
		order.setOrderURL(queryOrder.getOrderURL()); //订单网址,查询详细信息时才回传
		order.setPayAmount(queryOrder.getPayAmount()); //支付金额
		order.setRefundAmount(queryOrder.getRefundAmount()); //退货金额
		order.setOrderStatus(queryOrder.getOrderStatus()); //订单状态,00：订单已取消  01：订单已建立，等待支付  02：消费者已支付，等待支付结果  03：订单已支付（支付成功）  04：订单已结算（支付成功）  05：订单已退款  99：订单支付失败
		order.setErrorMessage(null);
		if("99".equals(queryOrder.getOrderStatus())) { //99：订单支付失败
			order.setErrorMessage(response.getErrorMessage()); //交易失败原因
		}
		if(isNew) {
			getDatabaseService().saveRecord(order); //保存订单
		}
		else {
			getDatabaseService().updateRecord(order); //更新订单
		}
		PaymentResult result = new PaymentResult();
		result.setTransactTime(DateTimeUtils.now()); //交易时间,由于农行没有返回,使用当前时间
		result.setTransactMoney(queryOrder.getPayAmount()); //实际交易金额
		result.setFee(0); //手续费
		result.setTransactSn(queryOrder.getOrderNo()); //支付系统流水号
		result.setTransactSuccess("03".equals(queryOrder.getOrderStatus()) || "04".equals(queryOrder.getOrderStatus()) ? '1' : '0'); //是否交易成功,03：订单已支付（支付成功）  04：订单已结算（支付成功）
		result.setBankOrderId(queryOrder.getOrderNo()); //银行端订单ID
		//交易失败原因
		result.setFailedReason(order.getErrorMessage()); //交易失败原因
		return result;
	}
	
	/**
	 * 查询B2B支付结果
	 * @param payment
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2BPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		//生成交易查询请求对象 
		QueryTrnxRequest trnxRequest = new QueryTrnxRequest(); 
		trnxRequest.setMerchantTrnxNo(payment.getId() + "");//设定商户交易编号（必要信息） 
		trnxRequest.setMerchantRemarks(""); //设定商户备注信息 
		  
		//传送交易查询请求并取得交易网址 
		com.hitrust.b2b.trustpay.client.TrxResponse trxResponse = trnxRequest.postRequest(); 
		if(!trxResponse.isSuccess()) { //交易查询请求提交失败
			throw new ServiceException("ReturnCode=" + trxResponse.getReturnCode  () + ", ErrorMessage=" + trxResponse.getErrorMessage());
		}
		//交易查询请求提交成功
		//保存交易记录明细
		AbcbankB2BOrder order = (AbcbankB2BOrder)databaseService.findRecordByHql("from AbcbankB2BOrder AbcbankB2BOrder where AbcbankB2BOrder.paymentId=" + payment.getId());
		boolean isNew = (order==null);
		if(isNew) { //没有记录过
			order = new AbcbankB2BOrder();
			order.setId(UUIDLongGenerator.generateId()); //ID
			order.setPaymentId(payment.getId()); //支付ID
		}
		
		order.setMerchantID(trxResponse.getValue("MerchantID")); //商户代码
		order.setCorporationCustomerNo(trxResponse.getValue("CorporationCustomerNo")); //买方企业客户代码
		order.setMerchantTrnxNo(trxResponse.getValue("MerchantTrnxNo")); //商户交易编号
		order.setTrnxSN(trxResponse.getValue("TrnxSN")); //交易流水号
		order.setTrnxType(trxResponse.getValue("TrnxType")); //交易类型
		order.setTrnxAMT(Double.parseDouble(trxResponse.getValue("TrnxAMT"))); //交易金额
		order.setAccountNo(trxResponse.getValue("AccountNo")); //付款方账号
		order.setAccountName(trxResponse.getValue("AccountName")); //付款方名称
		order.setAccountBank(trxResponse.getValue("AccountBank")); //付款方账户行联行号
		order.setAccountDBNo(trxResponse.getValue("AccountDBNo")); //收款方账号
		order.setAccountDBName(trxResponse.getValue("AccountDBName")); //收款方名称
		order.setAccountDBBank(trxResponse.getValue("AccountDBBank")); //收款方账户行联行号
		order.setTrnxTime(trxResponse.getValue("TrnxTime")); //交易时间
		order.setTrnxStatus(trxResponse.getValue("TrnxStatus")); //交易状态,2为成功
		order.setErrorMessage(null); //交易执行失败原因
		if(!"2".equals(order.getTrnxStatus())) { //交易状态,2为成功
			order.setErrorMessage(trxResponse.getErrorMessage()); //交易执行失败原因
		}
		
		if(isNew) {
			getDatabaseService().saveRecord(order); //保存订单
		}
		else {
			getDatabaseService().updateRecord(order); //更新订单
		}
		
		PaymentResult result = new PaymentResult();
		result.setTransactTime(DateTimeUtils.now()); //交易时间,由于农行没有返回,使用当前时间
		result.setTransactMoney(order.getTrnxAMT()); //实际交易金额
		result.setFee(0); //手续费
		result.setTransactSn(order.getTrnxSN()); //支付系统流水号
		result.setTransactSuccess("2".equals(order.getTrnxStatus()) ? '1' : '0'); //是否交易成功,交易状态,2为成功
		result.setBankOrderId(order.getMerchantTrnxNo()); //银行端订单ID
		//交易失败原因
		result.setFailedReason(order.getErrorMessage()); //交易失败原因
		return result;
	}
	
	/**
	 * 取消B2C订单
	 * @param payment
	 * @return
	 * @throws ServiceException
	 */
	protected boolean cancelB2CPayment(Payment payment) throws ServiceException {
		//生成取消支付请求对象 
		VoidPaymentRequest request = new VoidPaymentRequest(); 
		request.setOrderNo(payment.getId() + "");  //订单号（必要信息） 
		 
		//传送取消支付请求并取得取消支付结果 
		TrxResponse response = request.postRequest(); 
		 
		//判断取消支付结果状态，进行后续操作 
		if(!response.isSuccess()) {
			//取消支付失败 
			Logger.debug("ReturnCode=" + response.getReturnCode  () + ", ErrorMessage=" + response.getErrorMessage());
			return false; 
		}
		//取消支付成功
		Logger.debug("TrxType   = [" + response.getValue("TrxType"  ) + "]"); 
		Logger.debug("OrderNo   = [" + response.getValue("OrderNo"  ) + "]"); 
		Logger.debug("PayAmount = [" + response.getValue("PayAmount") + "]"); 
		Logger.debug("BatchNo   = [" + response.getValue("BatchNo"  ) + "]"); 
		Logger.debug("VoucherNo = [" + response.getValue("VoucherNo") + "]"); 
		Logger.debug("HostDate  = [" + response.getValue("HostDate" ) + "]"); 
		Logger.debug("HostTime  = [" + response.getValue("HostTime" ) + "]");
		return true;
	}
	
	/**
	 * B2C退货
	 * @param payment
	 * @return
	 * @throws ServiceException
	 */
	protected boolean returnB2CPayment(Payment payment) throws ServiceException {
		//生成退货请求对象 
		RefundRequest request = new RefundRequest(); 
		request.setOrderNo(payment.getId() + "");  //订单号   （必要信息） 
		request.setTrxAmount(payment.getMoney());  //退货金额 （必要信息） 
		 
		//发送退货请求并取得退货结果 
		TrxResponse response = request.postRequest(); 
		 
		//判断退货结果状态，进行后续操作 
		if(!response.isSuccess()) {
			//退货失败 
			Logger.debug("ReturnCode=" + response.getReturnCode  () + ", ErrorMessage=" + response.getErrorMessage());
			return false; 
		}
		//退货成功 
		Logger.debug("TrxType   = [" + response.getValue("TrxType"  ) + "]"); 
		Logger.debug("OrderNo   = [" + response.getValue("OrderNo"  ) + "]"); 
		Logger.debug("TrxAmount = [" + response.getValue("TrxAmount") + "]"); 
		Logger.debug("BatchNo   = [" + response.getValue("BatchNo"  ) + "]"); 
		Logger.debug("VoucherNo = [" + response.getValue("VoucherNo") + "]"); 
		Logger.debug("HostDate  = [" + response.getValue("HostDate" ) + "]"); 
		Logger.debug("HostTime  = [" + response.getValue("HostTime" ) + "]");
		return true;
	}
	
	/**
	 * 获取B2C对账单
	 * @param statementDate
	 * @throws ServiceException
	 */
	protected void getB2CStatement(Date statementDate) throws ServiceException {
		//生成商户对账单下载请求对象 
		SettleRequest request = new SettleRequest(); 
		request.setSettleDate(DateTimeUtils.formatDate(statementDate, "yyyy/MM/dd"));               //对账日期YYYY/MM/DD （必要信息） 
		request.setSettleType(SettleFile.SETTLE_TYPE_TRX);//对账类型 （必要信息） SettleFile.SETTLE_TYPE_TRX：交易对账单 

		//传送商户对账单下载请求并取得对账单 
		TrxResponse response = request.postRequest(); 

		//判断商户对账单下载结果状态，进行后续操作 
		if(!response.isSuccess()) {
			//商户账单下载失败 
			Logger.debug("ReturnCode=" + response.getReturnCode  () + ", ErrorMessage=" + response.getErrorMessage());
			return; 
		}
		//商户对账单下载成功，生成对账单对象 
		SettleFile settleFile = new SettleFile(response); 
		Logger.debug("SettleDate        = [" + settleFile.getSettleDate       () + "]");
		Logger.debug("SettleType        = [" + settleFile.getSettleType       () + "]");
		Logger.debug("NumOfPayments     = [" + settleFile.getNumOfPayments    () + "]");
		Logger.debug("SumOfPayAmount    = [" + settleFile.getSumOfPayAmount   () + "]");
		Logger.debug("NumOfRefunds      = [" + settleFile.getNumOfRefunds     () + "]");
		Logger.debug("SumOfRefundAmount = [" + settleFile.getSumOfRefundAmount() + "]");

		//取得对账单明细 
		String[] tRecords = settleFile.getDetailRecords(); 
		for(int i = 0; i < tRecords.length; i++) { 
			Logger.debug("Record-" + i + " = [" + tRecords[i] + "]"); 
		}
	}
	
	/**
	 * 获取B2B对账单
	 * @param statementDate
	 * @throws ServiceException
	 */
	protected void getB2BStatement(Date statementDate) throws ServiceException {
		//生成下载交易记录请求对象 
		DownloadTrnxRequest request = new DownloadTrnxRequest(); 
		request.setMerchantTrnxDate(DateTimeUtils.formatDate(statementDate, "yyyy/MM/dd"));           //设定商户交易编号    （必要信息） 
		request.setMerchantRemarks("");             //设定商户备注信息 
		  
		//传送下载交易记录请求并取得支付网址 
		com.hitrust.b2b.trustpay.client.TrxResponse response = request.postRequest(); 
		if(!response.isSuccess()) {
			//下载交易记录请求提交失败，商户自定后续动作
			Logger.debug("ReturnCode=" + response.getReturnCode  () + ", ErrorMessage=" + response.getErrorMessage());
			return;
		}
		//下载交易记录请求提交成功 
		XMLDocument detailRecords = new XMLDocument(response.getValue("TrnxDetail")); 
		ArrayList records = detailRecords.getDocuments("TrnxRecord"); 
		String[] iRecord = new String[records.size()]; 
		if(records.size() > 0) { 
			for(int i = 0; i < records.size(); i++) { 
				iRecord[i] = ((XMLDocument)records.get(i)).toString(); 
				Logger.debug("Record-" + i + " = [" + iRecord[i] + "]"); 
			} 
		} 
		else { 
			Logger.debug("指定的日期里没有交易记录"); 
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