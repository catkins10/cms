package com.yuanluesoft.jeaf.payment.paymentmethod.ccbbank.service.spring;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import CCBSign.RSASig;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.model.Transaction;
import com.yuanluesoft.jeaf.payment.model.Transfer;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.ccbbank.pojo.CcbbankOrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ExcelUtils;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 建设银行支付服务
 * @author lmiky
 *
 */
public class CcbbankPaymentMethodService implements PaymentMethodService {
	private DatabaseService databaseService; //数据库服务
	
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
		return Environment.getContextPath() + "/jeaf/payment/ccbbank/logo/ccbbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "建设银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("支付URL", "paymentURL", null, "https://ibsbjstar.ccb.com.cn/app/ccbMain", false));
		parameterDefines.add(new PaymentMethodParameterDefine("商户ID", "merchantId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("商户柜台代码", "posId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("分行代码", "branchId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("币种编码", "curCode", "缺省为01(人民币)", "01", false));
		parameterDefines.add(new PaymentMethodParameterDefine("支付查询密码", "queryPassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("公钥", "publicKey", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("备注1", "remark1", "默认为空字符串,网银不处理,直接传到城综网", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("备注2", "remark2", "默认为空字符串,网银不处理,直接传到城综网", null, false));
		//外联平台商户接口
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户客户号", "ebsMerchantCustId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户操作员号", "ebsMerchantUserId", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户操作员密码", "ebsMerchantPassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户柜台号", "ebsMerchantPosCode", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户服务IP", "ebsMerchantServerIP", null, "127.0.0.1", false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台商户服务端口", "ebsMerchantServerPort", null, "10019", false));
		//外联平台企业查询接口
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询帐号", "ebsEnterpriseAccNo", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询客户号", "ebsEnterpriseCustId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询操作员号", "ebsEnterpriseUserId", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询操作员密码", "ebsEnterprisePassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询服务IP", "ebsEnterpriseServerIP", null, "127.0.0.1", false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业查询服务端口", "ebsEnterpriseServerPort", null, "10018", false));
		//外联平台企业转账接口
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账帐号", "ebsEnterpriseTransferAccNo", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账客户号", "ebsEnterpriseTransferCustId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账操作员号", "ebsEnterpriseTransferUserId", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账操作员密码", "ebsEnterpriseTransferPassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账服务IP", "ebsEnterpriseTransferServerIP", null, "127.0.0.1", false));
		parameterDefines.add(new PaymentMethodParameterDefine("外联平台企业转账服务端口", "ebsEnterpriseTransferServerPort", null, "10018", false));
		return parameterDefines;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#redirectToBankPage(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		if("企业".equals(payment.getAccountType())) {
			redirectToB2BPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
		else {
			redirectToB2CPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
	}
	
	/**
	 * 重定向到B2C支付页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	protected void redirectToB2CPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String transactURL = "";
		try {
			String publicKey = paymentMerchant.getParameterValue("publicKey");
			String macStr = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") + //商户代码	CHAR(15)	Y	由建行统一分配
							"&POSID=" + paymentMerchant.getParameterValue("posId") + //商户柜台代码	CHAR(9)	Y	由建行统一分配
							"&BRANCHID=" + paymentMerchant.getParameterValue("branchId") + //分行代码	CHAR(9)	Y	由建行统一指定
							"&ORDERID=" + payment.getId() + //定单号	CHAR(30)	Y	由商户提供，最长30位
							"&PAYMENT=" + payment.getMoney() + //付款金额	NUMBER(16,2)	Y	由商户提供，按实际金额给出
							"&CURCODE=01" + //币种	CHAR(2)	Y	缺省为01－人民币（只支持人民币支付）
							"&TXCODE=520100" + //交易码	CHAR(6)	Y	由建行统一分配为520100
							"&REMARK1=" + //备注1		N	传输认证信息	 
							"&REMARK2=" + //备注2	CHAR(30)	N	网银不处理，直接传到城综网
							"&TYPE=1" + //接口类型	CHAR(1)	Y	0- 非钓鱼接口 1- 防钓鱼接口 目前该字段以银行开关为准，如果有该字段则需要传送以下字段。
							"&PUB=" + (publicKey.substring(publicKey.length()-30)) + //公钥后30位	CHAR(30)	Y	仅作为源串参加MD5摘要，不作为参数传递
							"&GATEWAY=T1" + //网关类型	VARCHAR(100)	Y	默认送T1
							"&CLIENTIP=" + //客户端IP	CHAR(40)	N	客户在商户系统中的IP
							"&REGINFO=" + //客户注册信息 CHAR(256)	N	客户在商户系统中注册的信息，中文需使用escape编码
							"&PROINFO=" + //商品信息	CHAR(256)	N	客户购买的商品 中文需使用escape编码
							"&REFERER=" + //商户URL	CHAR(100)	N	商户送空值即可，银行从后台读取商户设置的一级域名，如www.ccb.com则设为： “ccb.com”，最多允许设置三个不同的域名，格式为：****.com| ****.com.cn|****.net）
							"&PROJECTNO=" + payment.getId(); //项目编号	CHAR(60)	N	招投标项目号 （由商户提供）
			transactURL += paymentMerchant.getParameterValue("paymentURL") + "?" + macStr + "&MAC=" + Encoder.getInstance().md5Encode(macStr).toLowerCase(); 
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: redirectToBankPage, request is " + transactURL);
			}
			response.sendRedirect(transactURL);
		}
		catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 重定向到B2C支付页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	protected void redirectToB2CPage_520100(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String transactURL = "";
		try {
			String macStr = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") + 
						    "&POSID=" + paymentMerchant.getParameterValue("posId") + 
						    "&BRANCHID=" + paymentMerchant.getParameterValue("branchId") + 
						    "&ORDERID="+ payment.getId() +  
						    "&PAYMENT=" + payment.getMoney() + 
						    "&CURCODE=" + paymentMerchant.getParameterValue("curCode") + 
						    "&TXCODE=520100" + //交易码，个人客户由建设银行统一分配为520100;企业客户由建设银行统一分配为690401
						    "&REMARK1=" + (paymentMerchant.getParameterValue("remark1")==null ? "" : paymentMerchant.getParameterValue("remark1")) + 
						    "&REMARK2=" + (paymentMerchant.getParameterValue("remark2")==null ? "" : paymentMerchant.getParameterValue("remark2"));
			transactURL += paymentMerchant.getParameterValue("paymentURL") + "?" + macStr + "&MAC=" + Encoder.getInstance().md5Encode(macStr).toLowerCase(); 
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: redirectToBankPage, request is " + transactURL);
			}
			response.sendRedirect(transactURL);
		}
		catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 重定向到B2B支付页面(690401接口)
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	protected void redirectToB2BPage_690401(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String transactURL = "";
		try {
			String macStr = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") + 
						    "&POSID=" + paymentMerchant.getParameterValue("posId") + 
						    "&BRANCHID=" + paymentMerchant.getParameterValue("branchId") + 
						    "&ORDERID="+ payment.getId() +  
						    "&PAYMENT=" + payment.getMoney() + 
						    "&CURCODE=" + paymentMerchant.getParameterValue("curCode") + 
						    "&TXCODE=690401" + //交易码，个人客户由建设银行统一分配为520100;企业客户由建设银行统一分配为690401
						    "&REMARK1=" + (paymentMerchant.getParameterValue("remark1")==null ? "" : paymentMerchant.getParameterValue("remark1")) + 
						    "&REMARK2=" + (paymentMerchant.getParameterValue("remark2")==null ? "" : paymentMerchant.getParameterValue("remark2"));
			transactURL += paymentMerchant.getParameterValue("paymentURL") + "?" + macStr + "&MAC=" + Encoder.getInstance().md5Encode(macStr).toLowerCase(); 
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: redirectToBankPage, request is " + transactURL);
			}
			response.sendRedirect(transactURL);
		}
		catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 重定向到B2B支付页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	protected void redirectToB2BPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		String transactURL = "";
		try {
			String macStr = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") + //MERCHANTID	商户代码	CHAR(21)	Y	由建行统一分配
						    "&POSID=" + paymentMerchant.getParameterValue("posId") + //POSID	商户柜台代码	CHAR(9)	Y	由建行统一分配
						    "&BRANCHID=" + paymentMerchant.getParameterValue("branchId") + //BRANCHID	分行代码	CHAR(9)	Y	由建行统一指定
						    "&ORDERID=" + payment.getId() + //ORDERID	订单号	CHAR(30)	Y	由商户提供，最长30位 
						    "&PAYMENT=" + payment.getMoney() + //PAYMENT	付款金额	NUMBER(16,2)	Y	由商户提供，按实际金额给出
						    "&CURCODE=" + paymentMerchant.getParameterValue("curCode") + //CURCODE	币种	CHAR(2)	Y	缺省为01－人民币
						    "&TXCODE=690421" + //TXCODE	交易码	CHAR(6)	Y	690421固定
						    "&REMARK1=" + (paymentMerchant.getParameterValue("remark1")==null ? "" : paymentMerchant.getParameterValue("remark1")) + 
						    "&REMARK2=" + (paymentMerchant.getParameterValue("remark2")==null ? "" : paymentMerchant.getParameterValue("remark2")) +
						    "&PROJECTNO=" + payment.getId() +  //PROJECTNO	项目号	CHAR(60)	N	由商户提供
							"&PAYACCNO=" + //PAYACCNO	指定支付账号	CHAR(32)	N	由商户提供 客户支付时必须选择此账号
							"&ACCTYPE=" + //ACCTYPE	验证基本户	CHAR(1)	N	1-验证支付账户性质是否基本户
							"&ENDTIME=" + //ENDTIME	订单截止时间	CHAR(14)	N	yyyyMMddHHMISS
							"&TYPE="; //TYPE	接口类型	CHAR(1)	N	1- 开通银行防钓鱼控制的订单接口，后面的参数和值都必输并且都参与MAC计算
			transactURL += paymentMerchant.getParameterValue("paymentURL") + "?" + macStr + "&MAC=" + Encoder.getInstance().md5Encode(macStr).toLowerCase(); 
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: redirectToBankPage, request is " + transactURL);
			}
			response.sendRedirect(transactURL);
		}
		catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPayment(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		PaymentResult paymentResult = null;
		if("企业".equals(payment.getAccountType())) {
			paymentResult = queryB2BPayment(payment, paymentMerchant, 0); //先查未结算流水
			if(paymentResult==null) {
				paymentResult = queryB2BPayment(payment, paymentMerchant, 1); //查已结算流水
			}
		}
		else {
			paymentResult = queryB2CPayment(payment, paymentMerchant, 0); //先查未结算流水
			if(paymentResult==null) {
				paymentResult = queryB2CPayment(payment, paymentMerchant, 1); //查已结算流水
			}
		}
		return paymentResult;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPaymentByCallbackURL(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String)
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		callbackQueryString = RequestUtils.getParameterValue(callbackQueryString, "query", "utf-8");
		if(Logger.isDebugEnabled()) {
			Logger.debug("CcbbankPaymentMethodService: process callback url, query string is " + callbackQueryString);
		}
		if("企业".equals(payment.getAccountType())) {
			return queryB2BPaymentByCallbackURL(payment, paymentMerchant, callbackQueryString);
		}
		else {
			return queryB2CPaymentByCallbackURL(payment, paymentMerchant, callbackQueryString);
		}
	}
	
	/**
	 * 企业支付,按回调URL查询
	 * @param payment
	 * @param paymentMerchant
	 * @param callbackQueryString
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2BPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		/*建行将订单信息和处理结果实时传送到商户网站，建行使用标准MD5withRSA算法对给商户的响应信息进行数字签名，该签名结果同时发往商户网站，由商户端的签名校验程序（建行提供）进行签名验证，建行以此保证反馈信息的真实性和不可抵赖性，商户可据此发货。
		建行网站生成以下信息，传送到商户网站：
		字段	名称	类型	是否必输	备注
		MPOSID	商户柜台代码	CHAR(9)	Y	
		ORDER_NUMBER	订单号	CHAR(30)	Y	
		CUST_ID	付款客户号	CHAR(20)	Y	
		ACC_NO	付款账号	CHAR(32)	Y	
		ACC_NAME	付款账户名称	CHAR(40)	Y	
		AMOUNT	付款金额	NUMBER(16,2)	Y	
		STATUS	支付结果	CHAR(1)	Y	2-成功5-失败
		REMARK1	备注一	CHAR(60)	N	
		REMARK2	备注二	CHAR(60)	N	
		TRAN_FLAG	付款方式	CHAR(1)	Y	N-对公账户支付
		TRAN_TIME	交易时间	CHAR(14)	Y	yyyyMMddHHMISS
		BRANCH_NAME	付款分行名称	CHAR(40)	Y	
		REFERER	Referer信息	CHAR(100)	N	跳转到网银之前的系统 Referer，防钓鱼时返还
		SIGNSTRING	数字签名加密串	CHAR(256)	Y	
		
		注：以上各域，除“数字签名加密串”和“最后一级复核员是否审核通过”域的值以外，其余值全部参与数字签名的运算。
		支付结果(STATUS)含义：2：支付成功5：交易失败6：交易不确定
		付款方式(TRAN_FLAG)含义：N：对公账户支付
		参与签名的原串不要带有字段名称，仅为字段内容拼接。
		*/
		try {
			RSASig rsa = new RSASig();
			rsa.setPublicKey(paymentMerchant.getParameterValue("publicKey"));
			String signSrc = "";
			String[] parameterNames= {"MPOSID", "ORDER_NUMBER", "CUST_ID", "ACC_NO", "ACC_NAME", "AMOUNT", "STATUS", "REMARK1", "REMARK2", "TRAN_FLAG", "TRAN_TIME", "BRANCH_NAME"};
			for(int i=0; i<parameterNames.length; i++) {
				signSrc += RequestUtils.getParameterValue(callbackQueryString, parameterNames[i], "gbk");
			}
			if(!rsa.verifySigature(RequestUtils.getParameterValue(callbackQueryString, "SIGNSTRING", "gbk"), signSrc)) {
				throw new ServiceException("check ccb sign failed.");
			}
			//获取交易记录
			CcbbankOrder order = (CcbbankOrder)databaseService.findRecordByHql("from CcbbankOrder CcbbankOrder where CcbbankOrder.paymentId=" + payment.getId());
			boolean isNew = (order==null);
			if(isNew) {
				order = new CcbbankOrder();
				order.setId(UUIDLongGenerator.generateId());
				order.setPaymentId(payment.getId());
			}
			order.setOrderStatus(RequestUtils.getParameterValue(callbackQueryString, "STATUS", "gbk")); //支付结果(STATUS)含义：2：支付成功5：交易失败6：交易不确定
			order.setOrderAmount(Double.parseDouble(RequestUtils.getParameterValue(callbackQueryString, "AMOUNT", "gbk"))); //支付金额
			//order.setRefundAmount(); //退款金额
			try {
				order.setOrderDate(DateTimeUtils.parseTimestamp(RequestUtils.getParameterValue(callbackQueryString, "TRAN_TIME", "gbk"), "yyyyMMddHHmmss")); //TRAN_TIME	交易时间	CHAR(14)	Y	yyyyMMddHHMISS
			}
			catch(Exception e) {
				
			}
			order.setOrderId(RequestUtils.getParameterValue(callbackQueryString, "ORDER_NUMBER", "gbk"));
			if(isNew) {
				databaseService.saveRecord(order); //保存订单
			}
			else {
				databaseService.updateRecord(order); //更新订单
			}
			PaymentResult result = new PaymentResult();
			result.setTransactTime(order.getOrderDate()); //交易时间
			result.setTransactMoney(order.getOrderAmount()); //实际交易金额
			result.setFee(0); //手续费
			//result.setTransactSn(); //支付系统流水号
			result.setTransactSuccess("2".equals(order.getOrderStatus()) ? '1' : '0'); //支付结果(STATUS)含义：2：支付成功5：交易失败6：交易不确定
			result.setBankOrderId(order.getOrderId()); //银行端订单ID
			result.setPayerAccount(RequestUtils.getParameterValue(callbackQueryString, "ACC_NO", "gbk")); //ACC_NO	付款账号	CHAR(32)	Y	
			result.setPayerAccountName(RequestUtils.getParameterValue(callbackQueryString, "ACC_NAME", "gbk")); //ACC_NAME	付款账户名称	CHAR(40)	Y	
			return result;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 个人支付,按回调URL查询
	 * @param payment
	 * @param paymentMerchant
	 * @param callbackQueryString
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2CPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		/*
		POSID=572600130&BRANCHID=350000000&ORDERID=70308196888650000&PAYMENT=600.0&CURCODE=01&REMARK1=&REMARK2=&SUCCESS=Y&ACCDATE=20140609&TRADEDATE=20140609134446&PROJECTNO=70308196888650000&SIGN=36dda0bb0a06cf349f94963cbf8c3c77b8fff2a99d824e25403e3a221adc9ef52456c45b3b0f0b23f07a51bb04b96145d80b8d77436bb088f4629f61792ff1a69eacc65f981945b1eadc1630d9bedf2d8d82ff304945457f9b1c487ae92d6eae3c72f9f72e5f19ed22ca07dd3e43234ca3799fd356fe2c53426e220a640bd018 
		建行网站生成以下信息，传送到商户网站：
		POSID	商户柜台代码	CHAR(9)	从商户传送的信息中获得
		BRANCHID	分行代码	CHAR(9)	从商户传送的信息中获得
		ORDERID	定单号	CHAR(30)	从商户传送的信息中获得
		PAYMENT	付款金额	NUMBER(16,2)	从商户传送的信息中获得
		CURCODE	币种	CHAR(2)	从商户传送的信息中获得
		REMARK1	备注一	CHAR(30)	从商户传送的信息中获得
		REMARK2	备注二	CHAR(30)	从商户传送的信息中获得
		SUCCESS	成功标志	CHAR(1)	成功时返回Y
		SIGN	数字签名	CHAR(256)	

		站点间接口的参数传送仍然采用普通的URL方式，信息包含在CGI参数，具体如下所示：
		HTTP://MERCHANT.WEB.SITE/MERCHANT_CGI?POSID=000000000&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&REMARK1＝19991101&REMARK2=merchantname&SUCCESS=Y&SIGN=4b3ef029516193b7d969ac1840083635a3e0901b8cd526caa44c1a072f496d7f0d4bca3942c0d9030bede37c7809b835cec787eb39e18b7596a724fba9805b24714dfbb0f4a3fb430b32e075254a114d4c38a0ac52ef46a0ad33dec3fbfc15417402a1399e65e46996c0cf49fc7ffca9222f8cd693c8376b6f928828967bec42
		注：?前的URL由商户在签约时提供
		参与签名运算的字符及其顺序如下
		POSID=000000000&BRANCHID=110000000&ORDERID=19991101234&PAYMENT=500.00&CURCODE=01&REMARK1＝19991101&REMARK2=merchantname&SUCCESS=Y
		注：字符串中变量名必须是大写字母。
		*/
		try {
			RSASig rsa = new RSASig();
			rsa.setPublicKey(paymentMerchant.getParameterValue("publicKey"));
			String signSrc = callbackQueryString.substring(callbackQueryString.indexOf("POSID="), callbackQueryString.indexOf("&SIGN="));
			if(!rsa.verifySigature(RequestUtils.getParameterValue(callbackQueryString, "SIGN", "gbk"), signSrc)) {
				throw new ServiceException("check ccb sign failed.");
			}
			//获取交易记录
			CcbbankOrder order = (CcbbankOrder)databaseService.findRecordByHql("from CcbbankOrder CcbbankOrder where CcbbankOrder.paymentId=" + payment.getId());
			boolean isNew = (order==null);
			if(isNew) {
				order = new CcbbankOrder();
				order.setId(UUIDLongGenerator.generateId());
				order.setPaymentId(payment.getId());
			}
			order.setOrderStatus(RequestUtils.getParameterValue(callbackQueryString, "SUCCESS", "gbk")); //支付成功（STATUSCODE为1）表示支付成功，支付流水显示定单状态为成功
			order.setOrderAmount(Double.parseDouble(RequestUtils.getParameterValue(callbackQueryString, "PAYMENT", "gbk"))); //支付金额
			//order.setRefundAmount(); //退款金额
			try {
				order.setOrderDate(DateTimeUtils.parseTimestamp(RequestUtils.getParameterValue(callbackQueryString, "TRADEDATE", "gbk"), "yyyyMMddHHmmss")); //TRAN_TIME	交易时间	CHAR(14)	Y	yyyyMMddHHMISS
			}
			catch(Exception e) {
				
			}
			order.setOrderId(RequestUtils.getParameterValue(callbackQueryString, "ORDERID", "gbk"));
			if(isNew) {
				databaseService.saveRecord(order); //保存订单
			}
			else {
				databaseService.updateRecord(order); //更新订单
			}
			PaymentResult result = new PaymentResult();
			result.setTransactTime(order.getOrderDate()); //交易时间
			result.setTransactMoney(order.getOrderAmount()); //实际交易金额
			result.setFee(0); //手续费
			//result.setTransactSn(); //支付系统流水号
			result.setTransactSuccess("Y".equals(order.getOrderStatus()) ? '1' : '0'); //支付成功（STATUSCODE为1）表示支付成功，支付流水显示定单状态为成功
			result.setBankOrderId(order.getOrderId()); //银行端订单ID
			return result;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * B2C订单查询
	 * @param payment
	 * @param paymentMerchant
	 * @param kind 流水状态KIND 必输项（当日只有未结算流水可供查询）0 未结算流水 1 已结算流水
	 * @return
	 * @throws ServiceException
	 */
	public PaymentResult queryB2CPayment(Payment payment, PaymentMerchant paymentMerchant, int kind) throws ServiceException {
		return queryB2BPayment(payment, paymentMerchant, kind);
	}
	
	/**
	 * B2C订单查询
	 * @param payment
	 * @param paymentMerchant
	 * @param kind 流水状态KIND 必输项（当日只有未结算流水可供查询）0 未结算流水 1 已结算流水
	 * @return
	 * @throws ServiceException
	 */
	protected PaymentResult queryB2CPayment_410408(Payment payment, PaymentMerchant paymentMerchant, int kind) throws ServiceException {
		//请求查询交易记录加密url
		String macQueryRequest = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") + //1.商户代码 MERCHANTID 必输项 商户开设网上支付功能时，银行为商户指定的代码
								 "&BRANCHID=" + paymentMerchant.getParameterValue("branchId") + //2.分行代码 BRANCHID  必输项 商户办理结算账户所在的分行
								 "&POSID=" + paymentMerchant.getParameterValue("posId") + //3.柜台号 POSID 必输项 商户指定的柜台号
								 "&ORDERDATE=" + //4.定单日期 ORDERDATE（ORDERDATE与ORDERID必须有一个输入）  非必输项，OPERATOR元素必须有,但值可为空 查询定单的日期，格式为YYYYMMDD，共8位。例如:20050607(表示2005年6月7日)
								 "&BEGORDERTIME=" + //5.定单开始时间 BEGORDERTIME （已结算流水查询只支持按日期查询，时间只参与MAC运算。未结算流水查询支持按时间段查询。不输入时默认为00:00:00） 非必输项 ，BEGORDERTIME元素必须有,但值可为空 查询定单的开始时间，格式为hh，共8位，如00:00:00。 合法的时间格式：00   00:00   03:34:23   23:59:59   如果只有2位数字，最大为24，否则为23 。2位数字可解释为分钟和秒的值都为0，如12相当于12:00:00，。分钟和秒的最大值为59。非法的时间格式：25   24:00   23:60   23,59,59   23:59:61   0:12:2 结束时间具有相同的规则
								 "&ENDORDERTIME=" + //6.定单截止时间 ENDORDERTIME（已结算流水查询只支持按日期查询，时间只参与MAC运算。未结算流水查询支持按时间段查询。不输入时默认为23:59:59） 非必输项，ENDORDERTIME元素必须有,但值可为空 查询定单的截止时间，格式为hh，共8位，如23:59:59。时间格式参考开始时间
								 "&ORDERID=" + payment.getId() + //7.订单号 ORDERID（ORDERDATE与ORDERID必须有一个输入） 非必输项，ORDERID元素必须有,但值可为空（输入订单号则只按订单号查询，时间段与日期即使输入也不起作用。不输入订单号，则按日期与时间段查询）
								 "&QUPWD=" + //8.查询密码 QUPWD 必输项,主管或操作员的登录密码(不参加MAC计算，但是商户必须在隐藏域中发送，可参考模拟器方法。)
								 "&TXCODE=410408" + //10.交易码 TXCODE 必输项 交易码TXCODE=410408，这个参数的值是固定的，不可以修改
								 "&TYPE=0" + //9.流水类型TYPE 必输项 0支付流水  1退款流水
								 "&KIND=" + kind + //11.流水状态KIND 必输项（当日只有未结算流水可供查询）0 未结算流水 1 已结算流水
								 "&STATUS=3" + //12.交易状态STATUS 必输项 0失败 1成功 2不确定 3全部（已结算流水查询不支持全部）
								 "&SEL_TYPE=3" + //13.查询方式SEL_TYPE 必输项  1页面形式 2文件返回形式 (提供TXT和XML格式文件的下载) 3 XML页面形式
								 "&PAGE=1" + //14.页码PAGE 必输项，输入将要查询的页码
								 "&OPERATOR=" + //15.操作员 OPERATOR 非必输项。OPERATOR元素必须有,但值可为空。主管查询的时候为空
								 "&CHANNEL="; //16.预留字段 CHANNEL 现值为空，但CHANNEL元素必须有
		//请求查询交易记录,与上面的差别是查询密码不为空,即查询密码不参与mac加密
		String queryRequest = null;
		try {
			queryRequest = paymentMerchant.getParameterValue("paymentURL") + 
						   "?" + macQueryRequest.replaceAll("QUPWD=", "QUPWD=" + paymentMerchant.getParameterValue("queryPassword")) +
						   "&MAC=" + Encoder.getInstance().md5Encode(macQueryRequest).toLowerCase();
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: payment query, url is " + queryRequest);
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		Element xmlResponse = null;
		try {
			Map headers = new HashMap();
			headers.put("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
			String response = HttpUtils.getHttpContent(queryRequest, null, true, headers, 60000).getResponseBody();
			if(Logger.isDebugEnabled()) {
				Logger.debug("CcbbankPaymentMethodService: query result is " + response);
			}
			//解析xml文件,获取需要的信息
			SAXReader reader = new SAXReader();
			StringReader strReader = new StringReader(response.replaceFirst("(?s).*(<\\?.*)", "$1"));
			xmlResponse = reader.read(strReader).getRootElement();
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		/*
		 返回数据格式:
		 <DOCUMENT>
		 	<RETURN_CODE>交易返回码，成功时总为000000</RETURN_CODE>
			<RETURN_MSG>交易返回提示信息，成功时为空</RETURN_MSG>
			<CURPAGE>当前页</CURPAGE>
			<PAGECOUNT>总页数</PAGECOUNT>
			<TOTAL>总笔数</TOTAL>
			<PAYAMOUNT>支付总金额</PAYAMOUNT>
			<REFUNDAMOUNT>退款总金额</REFUNDAMOUNT>
			<QUERYORDER>
				<MERCHANTID>商户代码</MERCHANTID>
				<BRANCHID>商户所在分行</BRANCHID>
				<POSID>商户的POS号</POSID>
				<ORDERID>订单号</ORDERID>
				<ORDERDATE>支付/退款交易时间</ORDERDATE>
				<ACCDATE>记账日期</ACCDATE>
				<AMOUNT>支付金额</AMOUNT>
				<STATUSCODE>支付/退款状态码</STATUSCODE> 
				<!-- 
					支付失败（STATUSCODE为0）表示支付失败。支付流水显示定单状态为失败。
					支付成功（STATUSCODE为1）表示支付成功，支付流水显示定单状态为成功
					已部分退款（STATUSCODE为3）表示支付成功并已退过部分款项，支付流水显示定单状态为已部分退款。
					已全额退款（STATUSCODE为4）表示支付成功并已全额退款，支付流水显示定单状态为已全额退款。
					不确定交易，支付不确定（STATUSCODE为2或5） 表示暂无法确定交易是否成功，支付流水显示定单状态为待银行确认。
				-->
				<STATUS>支付/退款状态</STATUS> <!--失败，成功，待银行确认，已部分退款，已全额退款-->
				<REFUND>退款金额</REFUND>
				<SIGN>签名串</SIGN>
			</QUERYORDER>
		</DOCUMENT>
		*/
	    if("0250E0200001".equals(xmlResponse.elementText("RETURN_CODE")) && "流水记录不存在".equals(xmlResponse.elementText("RETURN_MSG"))) {
			return null;
		}
	    if(!"000000".equals(xmlResponse.elementText("RETURN_CODE"))) { //<RETURN_CODE>交易返回码，成功时总为000000</RETURN_CODE>
			throw new ServiceException(xmlResponse.elementText("RETURN_MSG"));
		}
		Element xmlOrder = xmlResponse.element("QUERYORDER");
		if(xmlOrder==null) {
			return null;
		}
		//签名校验
		String signSrc = "MERCHANTID=" + paymentMerchant.getParameterValue("merchantId") +
						 "&BRANCHID=" + paymentMerchant.getParameterValue("branchId") +
						 "&POSID=" + paymentMerchant.getParameterValue("posId") +
						 "&ORDERID=" + payment.getId() +
						 "&ORDERDATE=" + xmlOrder.elementText("ORDERDATE") +
						 "&ACCDATE=" + xmlOrder.elementText("ACCDATE") +
						 "&AMOUNT=" + xmlOrder.elementText("AMOUNT") +
						 "&STATUSCODE=" + xmlOrder.elementText("STATUSCODE") +
						 "&STATUS=" + xmlOrder.elementText("STATUS") +
						 "&REFUND=" +  xmlOrder.elementText("REFUND");
		RSASig rsa = new RSASig();
		rsa.setPublicKey(paymentMerchant.getParameterValue("publicKey"));
		if(!rsa.verifySigature(xmlOrder.elementText("SIGN"), signSrc)) {
			throw new ServiceException("check ccb sign failed.");
		}
		//获取交易记录
		CcbbankOrder order = (CcbbankOrder)databaseService.findRecordByHql("from CcbbankOrder CcbbankOrder where CcbbankOrder.paymentId=" + payment.getId());
		boolean isNew = (order==null);
		if(isNew) {
			order = new CcbbankOrder();
			order.setId(UUIDLongGenerator.generateId());
			order.setPaymentId(payment.getId());
		}
		order.setOrderStatus(xmlOrder.elementText("STATUSCODE")); //支付成功（STATUSCODE为1）表示支付成功，支付流水显示定单状态为成功
		order.setOrderAmount(Double.parseDouble(xmlOrder.elementText("AMOUNT"))); //支付金额
		order.setRefundAmount(Double.parseDouble(xmlOrder.elementText("REFUND"))); //退款金额
		try {
			order.setOrderDate(DateTimeUtils.parseTimestamp(xmlOrder.elementText("ORDERDATE"), "yyyyMMddHHmmss")); //支付/退款交易时间 20120905221825
		}
		catch(Exception e) {
			throw new ServiceException(e.getMessage());
		}
		order.setOrderId(xmlOrder.elementText("ORDERID"));
		if(isNew) {
			databaseService.saveRecord(order); //保存订单
		}
		else {
			databaseService.updateRecord(order); //更新订单
		}
		PaymentResult result = new PaymentResult();
		result.setTransactTime(order.getOrderDate()); //交易时间
		result.setTransactMoney(order.getOrderAmount()); //实际交易金额
		result.setFee(0); //手续费
		result.setTransactSn(order.getOrderId()); //支付系统流水号
		result.setTransactSuccess("1".equals(order.getOrderStatus()) ? '1' : '0'); //支付成功（STATUSCODE为1）表示支付成功，支付流水显示定单状态为成功
		result.setBankOrderId(order.getOrderId()); //银行端订单ID
		return result;
	}
	
	/**
	 * 调用（5W1001）B2C外联平台启动连接交易
	 * @param paymentMerchant
	 * @throws ServiceException
	 */
	private void ebsMerchantConnect(PaymentMerchant paymentMerchant) throws ServiceException {
		LinkedHashMap txInfos = new LinkedHashMap();
		//调用（5W1001）B2C外联平台启动连接交易
		txInfos.put("REM1", ""); //备注1 varChar(32) T 
		txInfos.put("REM2", ""); //备注2 varChar(32) T
		sendEbsRequest("5W1001", txInfos, null, null, paymentMerchant);
	}
	
	/**
	 * 调用（6W9101）客户端连接交易
	 * @param paymentMerchant
	 * @throws ServiceException
	 */
	private void ebsEnterpriseConnect(PaymentMerchant paymentMerchant) throws ServiceException {
		LinkedHashMap txInfos = new LinkedHashMap();
		//调用（6W9101）客户端连接交易
		txInfos.put("REM1", ""); //备注1 varChar(32) T 
		txInfos.put("REM2", ""); //备注2 varChar(32) T
		sendEbsRequest("6W9101", txInfos, null, null, paymentMerchant);
	}
	
	/**
	 * B2B订单查询, 调用商户外联接口：（5W1012）商户流水查询(招投标专用)
	 * @param payment
	 * @param paymentMerchant
	 * @param kind 流水类型 Char(1) F 0:未结流水,1:已结流水
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2BPayment(Payment payment, PaymentMerchant paymentMerchant, int kind) throws ServiceException {
		for(int i=0; i<5; i++) {
			ebsMerchantConnect(paymentMerchant); //调用（5W1001）B2C外联平台启动连接交易
			LinkedHashMap txInfos = new LinkedHashMap();
			//txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //起始日期 varChar(8) T
			//txInfos.put("STARTHOUR", "00"); //开始小时 varChar(2) T
			//txInfos.put("STARTMIN", "59"); //开始分钟 varChar(2) T 
			//txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //截止日期 varChar(8) T
			//txInfos.put("ENDHOUR", "23"); //结束小时 varChar(2) T
			//txInfos.put("ENDMIN", "39"); //结束分钟 varChar(2) T
			txInfos.put("KIND", "" + kind); //流水类型 Char(1) F 0:未结流水,1:已结流水
			txInfos.put("ORDER", "" + payment.getId()); //订单号   varChar(30) F 按订单号查询时，时间段不起作用
			txInfos.put("ACCOUNT", ""); //结算账户号 varChar(30) T 暂不用
			txInfos.put("DEXCEL", "1"); //文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件
			txInfos.put("MONEY", ""); //金额     Decimal(16,2) T 不支持以金额查询
			txInfos.put("NORDERBY", "1"); //排序     Char(1) F 1:交易日期,2:订单号
			txInfos.put("PAGE", "1"); //当前页次 Int F 
			txInfos.put("POS_CODE", paymentMerchant.getParameterValue("ebsMerchantPosCode")); //柜台号   varChar(9) T 
			txInfos.put("STATUS", "3"); //流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部 
			txInfos.put("PROJECT_NO", "" + payment.getId()); //项目编号	varChar(60)	非空	招投标商户特有字段	
			txInfos.put("PAY_METHOD", "2"); //支付方式	Char(1)	非空	0-在线支付 1-线下支付 2-全部
			Element xmlElement = sendEbsRequest("5W1012", txInfos, null, null, paymentMerchant);
			/*
			 * <?xml version="1.0" encoding="GB2312" standalone="yes" ?> 
			  <TX>
			   <REQUEST_SN>请求序列码</REQUEST_SN> 
			   <CUST_ID>商户号</CUST_ID> 
			   <TX_CODE>5W1012</TX_CODE> 
			   <RETURN_CODE>响应码</RETURN_CODE> 
			   <RETURN_MSG>响应信息</RETURN_MSG>
			   <LANGUAGE>CN</LANGUAGE>
			  <TX_INFO>
			   <CUR_PAGE>当前页次</CUR_PAGE> 
			   <PAGE_COUNT>总页次</PAGE_COUNT> 
			   <LIST>
			    <TRAN_DATE>交易日期</TRAN_DATE> 
			    <ACC_DATE>记账日期</ACC_DATE> 
			    <ORDER>订单号</ORDER> 
			    <ACCOUNT>付款方账号</ACCOUNT> 
			    <ACCOUNT_NAME>付款方户名</ACCOUNT_NAME> 
			    <PAYMENT_MONEY>支付金额</PAYMENT_MONEY> 
			    <REFUND_MONEY>退款金额</REFUND_MONEY> 
			    <POS_ID>柜台号</POS_ID> 
			    <REM1>备注1</REM1>> 
			    <REM2>备注2</REM2> 
			   <PROJECT_NO>项目编号</PROJECT_NO>
			    <ORDER_STATUS>订单状态</ORDER_STATUS> 
			    <PAY_METHOD>支付方式</PAY_METHOD> 
			   </LIST>
			   <NOTICE>提示信息</NOTICE>
			  </TX_INFO>
			  </TX>
			 */
			String returnCode = xmlElement.elementText("RETURN_CODE");
			if("0250E0200001".equals(returnCode) && "流水记录不存在".equals(xmlElement.elementText("RETURN_MSG"))) {
				return null;
			}
			if(!"000000".equals(returnCode)) {
				if(i<4) {
					continue;
				}
				throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
			}
			Element xmlTxInfo = xmlElement.element("TX_INFO");
			Element xmlList = xmlTxInfo.element("LIST");
			
			//获取交易记录
			CcbbankOrder order = (CcbbankOrder)databaseService.findRecordByHql("from CcbbankOrder CcbbankOrder where CcbbankOrder.paymentId=" + payment.getId());
			boolean isNew = (order==null);
			if(isNew) {
				order = new CcbbankOrder();
				order.setId(UUIDLongGenerator.generateId());
				order.setPaymentId(payment.getId());
			}
			order.setOrderStatus(xmlList.elementText("ORDER_STATUS")); //ORDER_STATUS	订单状态	Char(1)	可空	0:失败,1:成功,2:待银行确认,3:已部分退款,4:已全额退款,5:待银行确认
			order.setOrderAmount(Double.parseDouble(xmlList.elementText("PAYMENT_MONEY"))); //PAYMENT_MONEY	支付金额	Decimal(16,2)	可空
			order.setRefundAmount(Double.parseDouble(xmlList.elementText("REFUND_MONEY"))); //REFUND_MONEY	退款金额	Decimal(16,2)	可空
			try {
				order.setOrderDate(DateTimeUtils.parseTimestamp(xmlList.elementText("TRAN_DATE"), "yyyy-MM-dd HH:mm:ss")); //TRAN_DATE	交易日期	varChar(32)	可空	格式：YYYY-MM-DD HH:MI:SS	
			}
			catch(Exception e) {
				throw new ServiceException(e.getMessage());
			}
			order.setOrderId(xmlList.elementText("ORDER")); //ORDER	订单号	varChar(30)	可空
			if(isNew) {
				databaseService.saveRecord(order); //保存订单
			}
			else {
				databaseService.updateRecord(order); //更新订单
			}
			PaymentResult result = new PaymentResult();
			result.setTransactTime(order.getOrderDate()); //交易时间
			result.setTransactMoney(order.getOrderAmount()); //实际交易金额
			result.setFee(0); //手续费
			result.setTransactSn(order.getOrderId()); //支付系统流水号
			result.setTransactSuccess("1".equals(order.getOrderStatus()) ? '1' : '0'); //ORDER_STATUS	订单状态	Char(1)	可空	0:失败,1:成功,2:待银行确认,3:已部分退款,4:已全额退款,5:待银行确认
			result.setBankOrderId(order.getOrderId()); //银行端订单ID
			result.setPayerAccount(xmlList==null ? null : xmlList.elementText("ACCOUNT")); //ACCOUNT 付款方账号 varChar(30) T
			result.setPayerAccountName(xmlList==null ? null : xmlList.elementText("ACCOUNT_NAME")); //ACCOUNT_NAME 付款方户名
			return result;
		}
		throw new ServiceException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#parseTransactions(java.lang.String)
	 */
	public List parseTransactions(String transactionsFilePath) throws ServiceException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(transactionsFilePath)); //构造Workbook（工作薄）对象
			Sheet sheet = wb.getSheet(0);
			List transactions = new ArrayList();
			//从第9行开始
			for(int i=9; i<sheet.getRows(); i++) {
				//交易日期	交易时间	凭证种类	凭证号	发生额/元	(借方)  发生额/元(贷方)	余额/元	钞汇标志	对方户名	对方账号	摘要	备注	账户明细编号-交易流水号	企业流水号	本方账号	本方账户名称	本方账户开户机构
				//2012-07-02	09:11:33	建设银行进帐单	1050352002153385	--	6,000.00	3,578,045.93	钞户	福建省恒通路桥工程有限公司	35001672433052507457	投标保证金	投标保证金	1373-3506724336FPAVG31YW		35001676107052505220	南平市招标投标服务中心	建行延平支行
				Cell[] cells = sheet.getRow(i);
				String date = ExcelUtils.getCellString(cells, 0);
				String time = ExcelUtils.getCellString(cells, 1);
				if(date==null || date.trim().isEmpty() || time==null || time.trim().isEmpty()) {
					continue;
				}
				Transaction transaction = new Transaction();
				try {
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(date.trim() + " " + time.trim(), "yyyy-MM-dd HH:mm:ss")); //交易时间
				}
				catch(Exception e) {
					continue;
				}
				transaction.setVoucherType(cells[2].getContents()); //凭证种类
				transaction.setVoucherNumber(cells[3].getContents()); //凭证号
				transaction.setMoney(ExcelUtils.getCellDouble(cells, 4)); //发生额/元	
				if(transaction.getMoney()==0) {
					transaction.setMoney(ExcelUtils.getCellDouble(cells, 5));
				}
				else {
					transaction.setMoney(-transaction.getMoney());
				}
				transaction.setRemaining(ExcelUtils.getCellDouble(cells, 6)); //余额/元
				transaction.setPeerAccountName(cells[8].getContents()); //对方户名
				transaction.setPeerAccount(cells[9].getContents()); //对方帐号
				transaction.setSummary(cells[10].getContents()); //摘要
				transaction.setRemark(cells[11].getContents()); //备注
				transaction.setTransactionNumber(cells[12].getContents()); //账户明细编号-交易流水号
				transaction.setMyAccount(cells[14].getContents()); //本方账号
				transaction.setMyAccountName(cells[15].getContents()); //本方账户名称
				transaction.setMyBank(cells[16].getContents()); //本方账户开户机构
				transactions.add(transaction);
			}
			return transactions;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				wb.close();
			}
			catch (Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#createTransferFile(java.util.List, java.lang.String)
	 */
	public String createTransferFile(List transfers, String transferFilePath, String subject, PaymentMerchant paymentMerchant) throws ServiceException {
		transferFilePath += subject + ".txt";
		FileWriter writer = null;
		try {
			writer = new FileWriter(transferFilePath);
			writer.write(generateTransferText(transfers, paymentMerchant));
			return transferFilePath;
		}
		catch(IOException e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				writer.close();
			} 
			catch(IOException e) {
				
			}
		}
	}
	
	/**
	 * 生成转账文本
	 * @param transfers
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	private String generateTransferText(List transfers, PaymentMerchant paymentMerchant) throws ServiceException {
		String ccbTransfer = "";
		for(int i=0; i<(transfers==null ? 0 : transfers.size()); i++) {
			Transfer transfer = (Transfer)transfers.get(i);
			ccbTransfer += StringUtils.formatNumber(i+1, 3, false) + "|" + //字段一：序号 文本格式，有前导“0” 从001、002、003……按顺序编排
						   transfer.getFromUnit() + "|" + //字段二：汇出单位 文本格式 南平中心为“南平市招标投标服务中心”，各县市不同
						   transfer.getFromUnitAcount() + "|" + //字段三：汇出帐号 长数值格式，应生成数字串，不能为科学计数法 南平中心为“35001676107052505220”，各县市不同
						   (transfer.getFromBankFirstCode()!=null && !transfer.getFromBankFirstCode().isEmpty() ? transfer.getFromBankFirstCode() : paymentMerchant.getParameterValue("branchId")) + "|" + //字段四：汇出行一级分行 数值格式，不能为科学计数法 南平及所辖各县市均为“350000000”
						   transfer.getToUnitAccount() + "|" + //字段五：收款帐号 长数值格式，应生成数字串，不能为科学计数法 数据从单位网银卸出的流水中提取
						   transfer.getToUnit() + "|" + //字段六：收款单位 文本格式 数据从单位网银卸出的流水中提取
						   (transfer.getToBankFirstCode()==null ? "" : transfer.getToBankFirstCode()) + "|" + //字段七：收款单位一级分行 数值格式，可空（不是“0”）
						   transfer.getToUnitBank() + "|" + //字段八：收款单位开户行 文本格式，必填项 数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
						   (transfer.getToBankCode()==null ? "" : transfer.getToBankCode()) + "|" + //字段九：收款单位联行号 数值格式，可空（不是“0”）
						   (transfer.getToUnitOrgCode()==null ? "" : transfer.getToUnitOrgCode()) + "|" + //字段十：收款单位机构号 数值格式，可空（不是“0”）
						   (transfer.getToUnitBank().indexOf("建行")!=-1 || transfer.getToUnitBank().indexOf("建设银行")!=-1 ? "1" : "0") + "|" + //字段十一：行内外 数值格式，客户是建行账户填“1”，其它银行填“0”
						   StringUtils.format(new Double(transfer.getMoney()), "#.##", null) + "|" + //字段十二：金额 长数值格式，应生成数字串，不能为科学计数法 数据从单位网银卸出的流水中提取
						   (transfer.getCurrency().equals("人民币") ? "1" : "0") + "|" + //字段十三：币种 数值格式，为“1”，代表人民币
						   transfer.getUses() + "\r\n"; //字段十四：用途 文本格式 由系统根据标书名称自行填写
		}
		return ccbTransfer;
	}
	
	/**
	 * 调用调用外联平台接口：（5W1011）招投标商户线下转账订单查询, 已作废, 目前使用6W0300查询
	 * @param day
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public List listTransactions5W1011(Date day, PaymentMerchant paymentMerchant) throws ServiceException {
		ebsMerchantConnect(paymentMerchant); //调用（5W1001）B2C外联平台启动连接交易
		//调用外联平台接口：（5W1011）招投标商户线下转账订单查询
		List transactions = new ArrayList();
		String indexString = "";
		for(int i=1; ; i++) {
			LinkedHashMap txInfos = new LinkedHashMap();
			txInfos.put("CUST_ON", paymentMerchant.getParameterValue("ebsEnterpriseCustId")); //7	CUST_ON	商户的企业网银签约客户号	varChar(21)	非空
			txInfos.put("ACCOUNT", paymentMerchant.getParameterValue("ebsEnterpriseAccNo")); //8	ACCOUNT  	商户保证金结算账户号	varChar(32)	非空			
			txInfos.put("START", DateTimeUtils.formatDate(day, "yyyyMMdd")); //9	START    	起始日期	varChar(8)	非空	YYYYMMDD	 　 
			txInfos.put("END", DateTimeUtils.formatDate(day, "yyyyMMdd")); //10	END      	截止日期	varChar(8)	非空	YYYYMMDD 　 
			txInfos.put("PAGE", "" + i); //12	PAGE     	当前页次	Int	非空		
			txInfos.put("OTHER_ACCOUNT", ""); //15 OTHER_ACCOUNT	对方账户账号	varChar(32)	可空	对方来帐户	
			txInfos.put("MONEY", ""); //13	MONEY    	金额    	Decimal(16,2)	可空 
			txInfos.put("ORDER", ""); //14	ORDER    	摘要	varChar(40)	可空	此处填写订单号
			txInfos.put("INDEX_STRING", indexString); //17	INDEX_STRING	定位串	varChar(40)	可空	从返回报文里获取，如果不分页，可传空。第一次查询为空。
			Element xmlElement = sendEbsRequest("5W1011", txInfos, null, null, paymentMerchant);
			if(xmlElement==null) {
				break;
			}
			/*
			 <TX>
			  <REQUEST_SN>1376035391828</REQUEST_SN>
			  <CUST_ID>105350793990009</CUST_ID>
			  <TX_CODE>5W1011</TX_CODE>
			  <RETURN_CODE>000000</RETURN_CODE>
			  <RETURN_MSG/>
			  <LANGUAGE/>
			  <TX_INFO>
			        <CUR_PAGE>1</CUR_PAGE>
			        <PAGE_COUNT>1</PAGE_COUNT>
			        <INDEX_STRING>2013080956151996703_AP2+5+1+5</INDEX_STRING>
			   <LIST>
			    <TRAN_SQ>35067243350XBNJ9GIY</TRAN_SQ>
			    <TRAN_DATE>2013/07/27</TRAN_DATE>
			    <TRAN_TIME>22:50:43</TRAN_TIME>
			    <ORDER>保证金退还:201304023016</ORDER>
			    <ACCOUNT>35001672433052502058</ACCOUNT>
			    <MONEY>0.06</MONEY>
			    <OTHER_ACCOUNT>35001676107052509930</OTHER_ACCOUNT>
			    <OTHER_ACCOUNT_NAME>福建勇士电子商务有限公司</OTHER_ACCOUNT_NAME>
			   </LIST>
			        <NOTICE>提示信息</NOTICE>
			  </TX_INFO>
			</TX>
			 */
			String returnCode = xmlElement.elementText("RETURN_CODE");
			/*<RETURN_CODE>8350ZX100000</RETURN_CODE>
			  <RETURN_MSG>该帐户无满足条件的明细</RETURN_MSG>*/
			if("8350ZX100000".equals(returnCode) && ",该帐户无满足条件的明细,公共预处理失败,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) { //该帐户无满足条件的明细
				return null;
			}
			/*if("8350ZXXCMFJZ".equals(returnCode) && ",暂时未能处理您的请求，请咨询在线客服或致电95533。,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) { //该帐户无满足条件的明细
				return null;
			}*/
			if(!"000000".equals(returnCode)) {
				throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
			}
			Element xmlTxInfo = xmlElement.element("TX_INFO");
			int curPage = Integer.parseInt(xmlTxInfo.elementText("CUR_PAGE")); //当前页次
			int pageCount = Integer.parseInt(xmlTxInfo.elementText("PAGE_COUNT")); //总页次
			if(pageCount==0) {
				break;
			}
			indexString = xmlTxInfo.elementText("INDEX_STRING");
			//处理明细列表
			for(Iterator iterator = xmlTxInfo.elementIterator("LIST"); iterator.hasNext();) {
				Element xmlDetail = (Element)iterator.next();
				Transaction transaction = new Transaction();
				transaction.setTransactionNumber(xmlDetail.elementText("TRAN_SQ")); //13	TRAN_SQ	交易流水号	varChar(20)	可空		 
				//交易时间, TRAN_DATE 发生日期 YYYY/MM/DD F 交易发生日期（Char） TRAN_TIME 发生时间 HHMISS T 交易发生时间（Char）
				try {
					String tranDate = xmlDetail.elementText("TRAN_DATE");
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(tranDate + " " + xmlDetail.elementText("TRAN_TIME"), (tranDate.indexOf('/')==-1 ? "yyyyMMdd HH:mm:ss" : "yyyy/MM/dd HH:mm:ss")));
				}
				catch(ParseException e) {
					throw new ServiceException(e);
				}
				transaction.setRemark(xmlDetail.elementText("ORDER")); //19	ORDER    	摘要	varChar(40)	可空
				transaction.setMoney(Double.parseDouble(xmlDetail.elementText("MONEY")) * (transaction.getRemark()!=null && transaction.getRemark().indexOf("保证金退还")!=-1 ? -1 : 1)); //16	MONEY    	转账金额    	Decimal(16,2)	可空	 
				transaction.setMyAccount(xmlDetail.elementText("ACCOUNT")); //14	ACCOUNT  	商户保证金结算账户号	varChar(32)	可空
				transaction.setMyAccountName(xmlDetail.elementText("ACCOUNT_NAME")); //15	ACCOUNT_NAME	商户保证金结算户名	varChar(32)	可空	
				transaction.setPeerAccount(xmlDetail.elementText("OTHER_ACCOUNT")); //17	OTHER_ACCOUNT	对方账户账号	varChar(30)	可空	对方帐户	
				transaction.setPeerAccountName(xmlDetail.elementText("OTHER_ACCOUNT_NAME")); //18	OTHER_ACCOUNT_NAME	对方账户名称	varChar(30)	非空	对方户名	
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}
		}
		return transactions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#listTransactions(java.sql.Date, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public List listTransactions6W0300(Date day, PaymentMerchant paymentMerchant) throws ServiceException {
		//调用外联平台接口：（6W0300）历史明细查询交易
		List transactions = new ArrayList();
		int retryTimes = 0;
		for(int i=1; ; i++) {
			ebsEnterpriseConnect(paymentMerchant); //调用（6W9101）客户端连接交易
			LinkedHashMap txInfos = new LinkedHashMap();
			txInfos.put("ACC_NO", paymentMerchant.getParameterValue("ebsEnterpriseAccNo")); //帐号 varChar(32) F 查询帐号 
			txInfos.put("START_DATE", DateTimeUtils.formatDate(day, "yyyyMMdd")); //起始日期 YYYYMMDD F 查询起始日期（Char） 
			txInfos.put("END_DATE", DateTimeUtils.formatDate(day, "yyyyMMdd")); //结束日期 YYYYMMDD F 查询截至日期（Char） 
			txInfos.put("START_PAGE", "" + i); //起始页次 Int F 整数>0
			txInfos.put("POSTSTR", ""); //查询定位串(252定位串) varChar(40) T 分行可选，对应于必输分行则必须输入 
			txInfos.put("CONDITION1", ""); //备注1 varChar(32) T 备注1  NPZTB
			txInfos.put("CONDITION2", ""); //备注2 varChar(32) T 备注2
			Element xmlElement = sendEbsRequest("6W0300", txInfos, null, null, paymentMerchant);
			if(xmlElement==null) {
				break;
			}
			String returnCode = xmlElement.elementText("RETURN_CODE");
			/*<RETURN_CODE>8350ZX100000</RETURN_CODE>
			  <RETURN_MSG>该帐户无满足条件的明细</RETURN_MSG>*/
			if("8350ZX100000".equals(returnCode) && ",该帐户无满足条件的明细,公共预处理失败,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) { //该帐户无满足条件的明细
				return null;
			}
			//if("8350ZXXNAFJZ".equals(returnCode) && ",暂时未能处理您的请求，请咨询在线客服或致电95533。,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) {
			//	return null;
			//}
			/*if("8350ZXXCMFJZ".equals(returnCode) && ",暂时未能处理您的请求，请咨询在线客服或致电95533。,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) { //该帐户无满足条件的明细
				return null;
			}*/
			if(!"000000".equals(returnCode)) {
				if(retryTimes<5) { //重试5次
					retryTimes++;
					i--;
					continue;
				}
				throw new ServiceException(returnCode + "," + xmlElement.elementText("RETURN_MSG"));
			}
			retryTimes = 0;
			Element xmlTxInfo = xmlElement.element("TX_INFO");
			int curPage = Integer.parseInt(xmlTxInfo.elementText("CUR_PAGE")); //当前页次
			int pageCount = Integer.parseInt(xmlTxInfo.elementText("PAGE_COUNT")); //总页次
			String myAccount = xmlTxInfo.elementText("ACC_NO"); //本方账号
			String myAccountName = xmlTxInfo.elementText("ACC_NAME"); //本方账户名称
			String myBank = xmlTxInfo.elementText("ACC_DEPT"); //本方账户开户机构
			//处理明细列表
			for(Iterator iterator = xmlTxInfo.elementIterator("DETAIL"); iterator.hasNext();) {
				Element xmlDetail = (Element)iterator.next();
				Transaction transaction = new Transaction();
				//交易时间, TRAN_DATE 发生日期 YYYY/MM/DD F 交易发生日期（Char） TRAN_TIME 发生时间 HHMISS T 交易发生时间（Char）
				try {
					String tranDate = xmlDetail.elementText("TRAN_DATE");
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(tranDate + " " + xmlDetail.elementText("TRAN_TIME"), (tranDate.indexOf('/')==-1 ? "yyyyMMdd HH:mm:ss" : "yyyy/MM/dd HH:mm:ss")));
				}
				catch (ParseException e) {
					throw new ServiceException(e);
				}
				transaction.setVoucherType(xmlDetail.elementText("CREDIT_TYPE")); //CREDIT_TYPE 凭证种类 varChar(10) F 5个汉字 
				transaction.setVoucherNumber(xmlDetail.elementText("CREDIT_NO")); //CREDIT_NO 凭证号码 Char(12) F
				String dORc = xmlDetail.elementText("dORc");
				transaction.setMoney(Double.parseDouble(xmlDetail.elementText("AMOUNT")) * ("0".equals(dORc) ? -1 : 1)); //AMOUNT 发生金额 Decimal(16,2) F   dORc 借贷标志 Char(1) F 0:借 1:贷 
				transaction.setRemaining(Double.parseDouble(xmlDetail.elementText("BALANCE"))); //BALANCE 余额 Decimal(16,2) F 
				transaction.setPeerAccountName(xmlDetail.elementText("ACC_NAME1")); //ACC_NAME1 对方账户名称 varChar(40) T 20个汉字 
				transaction.setPeerAccount(xmlDetail.elementText("ACC_NO1")); //ACC_NO1 对方账号 varChar(32) T 
				transaction.setSummary(xmlDetail.elementText("ABSTRACT")); //ABSTRACT 摘要 varChar(12) F 6个汉字 
				String individual1 = xmlDetail.elementText("INDIVIDUAL1");
				String individual2 = xmlDetail.elementText("INDIVIDUAL2");
				transaction.setRemark(individual1==null ? individual2 : individual1 + (individual2==null ? "" : "," + individual2)); //备注 INDIVIDUAL1 自定义输出内容1 varChar(99) T  INDIVIDUAL2 自定义输出内容2 varChar(99) T 
				transaction.setTransactionNumber(xmlDetail.elementText("PAY_SEQ_NUM")); //PAY_SEQ_NUM 企业流水号 varChar(65) T 
				if(transaction.getTransactionNumber()==null || transaction.getTransactionNumber().isEmpty()) {
					transaction.setTransactionNumber(individual2);
				}
				transaction.setMyAccount(myAccount); //本方账号
				transaction.setMyAccountName(myAccountName); //本方账户名称
				transaction.setMyBank(myBank); //本方账户开户机构
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}
		}
		return transactions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#listTransactions(java.sql.Date, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public List listTransactions(Date day, PaymentMerchant paymentMerchant) throws ServiceException {
		//调用外联平台接口：（6WY101）一点接入活期账户明细查询
		List transactions = new ArrayList();
		int retryTimes = 0;
		for(int i=1; ; i++) {
			ebsEnterpriseConnect(paymentMerchant); //调用（6W9101）客户端连接交易
			LinkedHashMap txInfos = new LinkedHashMap();
			/*
			7 ACCNO1 帐号 varChar(32) F 　  
			8 STARTDATE 开始时间 YYYYMMDD F 　  
			9 ENDDATE 结束时间 YYYYMMDD F 　  
			10 BARGAIN_FLAG 交易方向 Char(1) T 　  
			11 CHECK_ACC_NO 对方账户 varChar(32) T 　  
			12 CHECK_ACC_NAME  对方帐户名称 varChar(60) T 　  
			13 REMARK 摘要 varChar(99) T 　  
			14 LOW_AMT 最小金额 Decimal(16,2) T 　  
			15 HIGH_AMT 最大金额 Decimal(16,2) T 　  
			16 PAGE 起始页      　 T 查询页次，整数>0  
			17 POSTSTR 定位串 　 T 第一次查询为空  
			18 TOTAL_RECORD 每页记录数 　 T 默认为10，大于0小于等于200，后续查询时的输入值，必须与首次查询设定的值相等  
			19 DET_NO 活存帐户明细号 Int T 此明细号用于快速查询某条记录以后的明细数据(可参考返回报文DET_NO填写此值) */		
			txInfos.put("ACCNO1", paymentMerchant.getParameterValue("ebsEnterpriseAccNo")); //帐号 帐号 varChar(32) F
			txInfos.put("STARTDATE", DateTimeUtils.formatDate(day, "yyyyMMdd")); //开始时间 YYYYMMDD F 
			txInfos.put("ENDDATE", DateTimeUtils.formatDate(day, "yyyyMMdd")); //结束时间 YYYYMMDD F 
			txInfos.put("PAGE", "" + i); //起始页      　 T 查询页次，整数>0
			txInfos.put("TOTAL_RECORD", "50"); //每页记录数 　 T 默认为10，大于0小于等于200，后续查询时的输入值，必须与首次查询设定的值相等   
			Element xmlElement = sendEbsRequest("6WY101", txInfos, null, null, paymentMerchant);
			if(xmlElement==null) {
				break;
			}
			String returnCode = xmlElement.elementText("RETURN_CODE");
			//if("8350ZXXNAFJZ".equals(returnCode) && ",暂时未能处理您的请求，请咨询在线客服或致电95533。,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) {
			//	return null;
			//}
			/*if("8350ZXXCMFJZ".equals(returnCode) && ",暂时未能处理您的请求，请咨询在线客服或致电95533。,".indexOf("," + xmlElement.elementText("RETURN_MSG") + ",")!=-1) { //该帐户无满足条件的明细
				return null;
			}*/
			if(!"000000".equals(returnCode)) {
				if(retryTimes<5) { //重试5次
					retryTimes++;
					i--;
					continue;
				}
				throw new ServiceException(returnCode + "," + xmlElement.elementText("RETURN_MSG"));
			}
			retryTimes = 0;
			/*
			Transaction_Body  
			7 ACCNO1 本方账号 　 F 　  
			8 CURR_COD 币种 　 F 　  
			9 ACC_NAME 本方账号名称 　 F 　  
			10 ACC_ORGAN 本方账号开户机构 　 F 　  
			11 ACC_STATE 本方账号状态 　 F 正常、冻结、部分冻结等   
			12 INTR 利率 　 T 对存款账户为利率   
			13 TOTAL_PAGE 总页次 　 F 　  
			14 PAGE 当前页次 　 F 　  
			15 POSTSTR 定位串 　 F 说明：例如返回定位串2010012110797991203_AP2+50+11+20+
			“+50+11+20+”50是总的记录数，11是本次交易返回的第一条记录数，20是本次交易返回的最后一条记录数。  
			16 FLAG 是否可生成标识位 　 T 0 可生成 ，1 未可生成   
			17 FILE_LOCSTR 生成文件定位串 　 T 　  
			　 　 　 　 　 　  
			DETAILLIST（多条记录） 　 　 　 　 　  
			18 TRANDATE 交易日期 　 T 　  
			19 TRANTIME 交易时间 　 T 　  
			20 CRE_TYP 凭证种类 　 T 　  
			21 CRE_NO 凭证号码 　 T 　  
			22 MESSAGE 摘要 　 T 　  
			23 AMT 发生额 　 T 为借方发生额或贷方发生额  
			24 AMT1 余额 　 T 　  
			25 FLAG1 借贷标志 　 T 　  
			26 ACCNO2 对方账号 　 T 对方账号  
			27 ACC_NAME1 对方户名 　 T 对方账户名称   
			28 FLAG2 交易钞汇标志 　 T 0－钞户 1－汇户  
			29 TRAN_FLOW 交易流水号 　 T 　  
			30 BFLOW 企业支付流水号 　 T 　  
			31 DET_NO 活存帐户明细号 　 T 　  
			32 DET 备注 　 T 　 
			*/
			Element xmlTxInfo = xmlElement.element("TX_INFO");
			int curPage = Integer.parseInt(xmlTxInfo.elementText("PAGE")); //当前页次
			int pageCount = Integer.parseInt(xmlTxInfo.elementText("TOTAL_PAGE")); //总页次
			String myAccount = xmlTxInfo.elementText("ACCNO1"); //本方账号
			String myAccountName = xmlTxInfo.elementText("ACC_NAME"); //本方账户名称
			String myBank = xmlTxInfo.elementText("ACC_ORGAN"); //本方账户开户机构
			//处理明细列表
			Element xmlDetailList = xmlTxInfo.element("DETAILLIST");
			for(Iterator iterator = xmlDetailList==null ? null : xmlDetailList.elementIterator("DETAILINFO"); iterator!=null && iterator.hasNext();) {
				Element xmlDetail = (Element)iterator.next();
				Transaction transaction = new Transaction();
				//交易时间, TRANDATE 交易日期, TRANTIME 交易时间
				String tranDate = xmlDetail.elementText("TRANDATE");
				if(tranDate==null || tranDate.isEmpty()) {
					continue;
				}
				try {
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(tranDate + " " + xmlDetail.elementText("TRANTIME"), (tranDate.indexOf('/')==-1 ? "yyyyMMdd HH:mm:ss" : "yyyy/MM/dd HH:mm:ss")));
				}
				catch (ParseException e) {
					throw new ServiceException(e);
				}
				transaction.setVoucherType(xmlDetail.elementText("CRE_TYP")); //凭证种类 
				transaction.setVoucherNumber(xmlDetail.elementText("CRE_NO")); //凭证号码
				String dORc = xmlDetail.elementText("FLAG1"); //借贷标志
				transaction.setMoney(Double.parseDouble(xmlDetail.elementText("AMT")) * ("0".equals(dORc) ? -1 : 1)); //发生额 　 T 为借方发生额或贷方发生额
				transaction.setRemaining(Double.parseDouble(xmlDetail.elementText("AMT1"))); //余额
				transaction.setPeerAccountName(xmlDetail.elementText("ACC_NAME1")); //对方户名
				transaction.setPeerAccount(xmlDetail.elementText("ACCNO2")); //对方账号 
				transaction.setSummary(xmlDetail.elementText("MESSAGE")); //摘要
				transaction.setRemark(xmlDetail.elementText("DET")); //备注
				transaction.setTransactionNumber(xmlDetail.elementText("TRAN_FLOW")); //交易流水号
				if(transaction.getTransactionNumber()==null || transaction.getTransactionNumber().isEmpty()) {
					transaction.setTransactionNumber(xmlDetail.elementText("BFLOW")); //企业支付流水号
				}
				transaction.setMyAccount(myAccount); //本方账号
				transaction.setMyAccountName(myAccountName); //本方账户名称
				transaction.setMyBank(myBank); //本方账户开户机构
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}
		}
		return transactions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#transfer(java.util.List, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public void transfer6W8040(List transfers, PaymentMerchant paymentMerchant) throws ServiceException {
		//余额校验
		double balance = getBalance(paymentMerchant);
		double totalTransfer = 0;
		for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
			Transfer transfer = (Transfer)iteratorTransfer.next();
			totalTransfer += transfer.getMoney();
		}
		if(totalTransfer > balance) {
			for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
				Transfer transfer = (Transfer)iteratorTransfer.next();
				transfer.setSuccess(false);
				transfer.setError("可用余额不足");
			}
			return;
		}
		//调用（6W9101）客户端连接交易
		ebsEnterpriseConnect(paymentMerchant);
		//调用外联平台接口：（6W8040）外联批量付款，每个交易请求一次
		for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
			Transfer transfer = (Transfer)iteratorTransfer.next();
			try {
				LinkedHashMap txInfos = new LinkedHashMap();
				txInfos.put("AMOUNT", "" + transfer.getMoney()); //AMOUNT 总金额 Decimal(16,2) T 如有值，则校验与文件中累加金额是否相符 
				txInfos.put("COUNT", "1"); //COUNT 总笔数 Decimal(7,0) T 如有值，则校验与文件中总笔数是否相符,  + transfers.size() 
				txInfos.put("CHK_RECVNAME", "0"); //CHK_RECVNAME 户名校验 CHAR(1) T 行内转账收款账户户名校验 1:校验 0：不校验
				txInfos.put("FILE_CTX", generateTransferText(ListUtils.generateList(transfer), paymentMerchant)); //FILE_CTX 付款文件内容 varChar F 说明见下
				Element xmlElement = sendEbsRequest("6W8040", txInfos, "", "", paymentMerchant);
				String returnCode = xmlElement.elementText("RETURN_CODE");
				if(!"000000".equals(returnCode)) {
					transfer.setSuccess(false);
					transfer.setError(xmlElement.elementText("RETURN_MSG"));
					continue;
				}
				Element xmlTxInfo = xmlElement.element("TX_INFO");
				for(Iterator iterator = xmlTxInfo.elementIterator("ERROR_LIST"); iterator.hasNext();) {
					Element xmlError = (Element)iterator.next();
					int index = Integer.parseInt(xmlError.elementText("SERIAL_NUM")) - 1; //SERIAL_NUM 序号 varChar(6) T 对应文件中的序号
					if(index==0) {
						transfer.setSuccess(false);
						transfer.setError(xmlError.elementText("ERROR_MSG"));
					}
				}
				for(Iterator iterator = xmlTxInfo.elementIterator("SUCCESS_LIST"); iterator.hasNext();) {
					Element xmlError = (Element)iterator.next();
					int index = Integer.parseInt(xmlError.elementText("SERIAL_NUM")) - 1; //SERIAL_NUM 序号 varChar(6) T 对应文件中的序号
					if(index==0) {
						//TODO:调用（6W0500）转帐交易结果查询接口
						transfer.setSuccess(true);
					}
				}
			}
			catch(Exception e) {
				Logger.exception(e);
				transfer.setSuccess(false);
				transfer.setError("转账异常" + (e.getMessage()==null ? "" : ":" + e.getMessage()));
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#transfer(java.util.List, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public void transfer(List transfers, PaymentMerchant paymentMerchant) throws ServiceException {
		//余额校验
		double balance = getBalance(paymentMerchant);
		double totalTransfer = 0;
		for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
			Transfer transfer = (Transfer)iteratorTransfer.next();
			totalTransfer += transfer.getMoney();
		}
		if(totalTransfer > balance) {
			for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
				Transfer transfer = (Transfer)iteratorTransfer.next();
				transfer.setSuccess(false);
				transfer.setError("可用余额不足");
			}
			return;
		}
		//调用（6W9101）客户端连接交易
		ebsEnterpriseConnect(paymentMerchant);
		//调用外联平台接口：（6W8020）非自动转帐交易
		for(Iterator iteratorTransfer = transfers.iterator(); iteratorTransfer.hasNext();) {
			Transfer transfer = (Transfer)iteratorTransfer.next();
			try {
				LinkedHashMap txInfos = new LinkedHashMap();
				txInfos.put("PAY_ACCNO", transfer.getFromUnitAcount()); //PAY_ACCNO 转出帐户号 varChar(32) F 　  
				txInfos.put("RECV_ACCNO", transfer.getToUnitAccount()); //RECV_ACCNO 转入帐户号 varChar(32) F 　  
				txInfos.put("RECV_ACC_NAME", transfer.getToUnit()); //RECV_ACC_NAME 转入帐户名称 varChar(60) F 　  
				txInfos.put("RECV_OPENACC_DEPT", transfer.getToUnitBank()); //RECV_OPENACC_DEPT 转入帐户开户机构名称 varChar(41) F 　  
				if(transfer.getToBankCode()!=null && !transfer.getToBankCode().isEmpty()) {
					txInfos.put("RECV_UBANKNO", transfer.getToBankCode()); //RECV_UBANKNO 转入帐户联行号 Char(12) T 人行支付联行号
				}
				txInfos.put("AMOUNT", StringUtils.format(new Double(transfer.getMoney()), "#.##", null)); //AMOUNT 金额 Decimal(16,2) F 　  
				txInfos.put("CUR_TYPE", (transfer.getCurrency().equals("人民币") ? "01" : "0")); //CUR_TYPE 币种 Char(2) F 　  
				txInfos.put("USEOF",  transfer.getUses()); //USEOF 用途 varChar(38) F 　  
				txInfos.put("REM1", ""); //REM1 备注1 varChar(32) T 备注1  
				txInfos.put("REM2", ""); //REM2 备注2 varChar(32) T 备注2
				Element xmlElement = sendEbsRequest("6W8020", txInfos, "", "", paymentMerchant);
				String returnCode = xmlElement.elementText("RETURN_CODE");
				if(!"000000".equals(returnCode)) {
					transfer.setSuccess(false);
					transfer.setError(xmlElement.elementText("RETURN_MSG"));
					continue;
				}
				//Element xmlTxInfo = xmlElement.element("TX_INFO");
				/*
				  8 CREDIT_NO 凭证号 Char(12) F 　  
				  9 DEAL_TYPE 处理方式 Char(1) F 返回状态为9时，调用6W0600查询处理结果  
				  10 VALIDATE_CODE 验证码 varChar(30) T 新加，交易成功时返回  
				  11  INDIVIDUAL1 自定义输出内容1 varChar(99) T 　  
				  12 INDIVIDUAL_NAME2 自定义输出名称2 varChar(99) T 分行自定义输出名称2  
				  13  INDIVIDUAL2 自定义输出内容2 varChar(99) T 　  
				  14 REM1 备注1 varChar(32) T 　  
				  15 REM2 备注2 varChar(32) T 　 
				 */
				transfer.setSuccess(true);
			}
			catch(Exception e) {
				Logger.exception(e);
				transfer.setSuccess(false);
				transfer.setError("转账异常" + (e.getMessage()==null ? "" : ":" + e.getMessage()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getBalance(com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public double getBalance(PaymentMerchant paymentMerchant) throws ServiceException {
		//调用（6W9101）客户端连接交易
		ebsEnterpriseConnect(paymentMerchant);
		//调用（6W0100）余额查询交易
		LinkedHashMap txInfos = new LinkedHashMap();
		txInfos.put("ACC_NO", paymentMerchant.getParameterValue("ebsEnterpriseAccNo")); //帐号 varChar(32) F 查询帐号 
		Element xmlElement = sendEbsRequest("6W0100", txInfos, null, null, paymentMerchant);
		String returnCode = xmlElement.elementText("RETURN_CODE");
		if(!"000000".equals(returnCode)) {
			throw new ServiceException(returnCode + "," + xmlElement.elementText("RETURN_MSG"));
		}
		Element xmlTxInfo = xmlElement.element("TX_INFO");
		return Double.parseDouble(xmlTxInfo.elementText("BALANCE1")); //可用余额
	}
	
	/**
	 * 批量转账，已作废
	 * @param transfers
	 * @param paymentMerchant
	 * @throws ServiceException
	 */
	public void batchTransfer(List transfers, PaymentMerchant paymentMerchant) throws ServiceException {
		//调用（6W9101）客户端连接交易
		ebsEnterpriseConnect(paymentMerchant);
		//调用外联平台接口：（6W8040）外联批量付款
		double amount = 0;
		for(int i=0; i<(transfers==null ? 0 : transfers.size()); i++) {
			Transfer transfer = (Transfer)transfers.get(i);
			amount += transfer.getMoney();
		}
		LinkedHashMap txInfos = new LinkedHashMap();
		txInfos.put("AMOUNT", "" + amount); //AMOUNT 总金额 Decimal(16,2) T 如有值，则校验与文件中累加金额是否相符 
		txInfos.put("COUNT", "" + transfers.size()); //COUNT 总笔数 Decimal(7,0) T 如有值，则校验与文件中总笔数是否相符 
		txInfos.put("CHK_RECVNAME", "1"); //CHK_RECVNAME 户名校验 CHAR(1) T 行内转账收款账户户名校验 1:校验 0：不校验
		txInfos.put("FILE_CTX", generateTransferText(transfers, paymentMerchant)); //FILE_CTX 付款文件内容 varChar F 说明见下，最大9999字节
		Element xmlElement = sendEbsRequest("6W8040", txInfos, "", "", paymentMerchant);
		String returnCode = xmlElement.elementText("RETURN_CODE");
		if(!"000000".equals(returnCode)) {
			throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
		}
		Element xmlTxInfo = xmlElement.element("TX_INFO");
		for(Iterator iterator = xmlTxInfo.elementIterator("ERROR_LIST"); iterator.hasNext();) {
			Element xmlError = (Element)iterator.next();
			int index = Integer.parseInt(xmlError.elementText("SERIAL_NUM")) - 1; //SERIAL_NUM 序号 varChar(6) T 对应文件中的序号
			Transfer transfer = (Transfer)transfers.get(index);
			transfer.setSuccess(false);
			transfer.setError(xmlError.elementText("ERROR_MSG"));
		}
		for(Iterator iterator = xmlTxInfo.elementIterator("SUCCESS_LIST"); iterator.hasNext();) {
			Element xmlError = (Element)iterator.next();
			int index = Integer.parseInt(xmlError.elementText("SERIAL_NUM")) - 1; //SERIAL_NUM 序号 varChar(6) T 对应文件中的序号
			Transfer transfer = (Transfer)transfers.get(index);
			transfer.setSuccess(true);
		}
	}
	
	/**
	 * 执行EBS请求
	 * @param reuqestType 0/商户外联, 1/企业外联查询, 2/企业外联转账
	 * @param txCode
	 * @param txInfos
	 * @param signInfo
	 * @param signCert
	 * @param paymentMerchant
	 * @return
	 * @throws Exception
	 */
	private Element sendEbsRequest(String txCode, LinkedHashMap txInfos, String signInfo, String signCert, PaymentMerchant paymentMerchant) throws ServiceException {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		Reader dataReader = null;
		Socket socket = null;
		String custId, userId, password, serverIP, serverPort;
		if(txCode.startsWith("5W")) { //商户外联
			custId = "ebsMerchantCustId";
			userId = "ebsMerchantUserId";
			password = "ebsMerchantPassword";
			serverIP = "ebsMerchantServerIP";
			serverPort = "ebsMerchantServerPort";
		}
		else if(txCode.equals("6W8040") || txCode.equals("6W8020")) { //付款
			//外联平台企业转账接口
			custId = "ebsEnterpriseTransferCustId";
			userId = "ebsEnterpriseTransferUserId";
			password = "ebsEnterpriseTransferPassword";
			serverIP = "ebsEnterpriseTransferServerIP";
			serverPort = "ebsEnterpriseTransferServerPort";
		}
		else { //企业外联,不是转账
			//外联平台企业查询接口
			custId = "ebsEnterpriseCustId";
			userId = "ebsEnterpriseUserId";
			password = "ebsEnterprisePassword";
			serverIP = "ebsEnterpriseServerIP";
			serverPort = "ebsEnterpriseServerPort";
		}
		try {
			socket = new Socket();
			String ip = paymentMerchant.getParameterValue(serverIP);
			int port;
			try {
				port = Integer.parseInt(paymentMerchant.getParameterValue(serverPort));
			}
			catch(Exception e) {
				return null;
			}
			socket.connect(new InetSocketAddress(ip, port), 8000); //连接超时8秒
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" + 
								"	<REQUEST_SN>" + System.currentTimeMillis() + "</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								"	<CUST_ID>" + paymentMerchant.getParameterValue(custId) + "</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>" + paymentMerchant.getParameterValue(userId) + "</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>" + paymentMerchant.getParameterValue(password) + "</PASSWORD>" + //密码 varChar(32) F 操作员密码 
								"	<TX_CODE>" + txCode + "</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN
								"	<TX_INFO>";
			for(Iterator iterator = txInfos.keySet().iterator(); iterator.hasNext();) {
				String propertyName = (String)iterator.next();
				requestXml += 	"		<" + propertyName + ">" + 
										((String)txInfos.get(propertyName)).replaceAll("&", "&amp;")
										   								   .replaceAll(" ", "&nbsp;")
										   								   .replaceAll("<", "&lt;")
										   								   .replaceAll(">", "&gt;")
										   								   .replaceAll("\"", "&quot;")
										   								   .replaceAll("'", "&apos;") +
										"</" + propertyName + ">";
			}
			requestXml += 		"	</TX_INFO>";
			if(signInfo!=null) {
				requestXml += "		<SIGN_INFO>" + signInfo + "</SIGN_INFO>" + //签名信息 varChar(254) T
							  "		<SIGNCERT>" + signCert + "</SIGNCERT>"; //签名CA信息 varChar(254) T ,客户采用socket连接时，建行客户端自动添加
			}
			requestXml += 		"</TX>";
			if(Logger.isInfoEnabled()) {
				Logger.info("Ccbbank EBS " + txCode + " request: " + requestXml);
			}
			outputStream.write(requestXml.getBytes("gbk")); //gb2312
			outputStream.flush();
			/*
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			*/
			dataReader = new InputStreamReader(inputStream);
			StringBuffer xml = new StringBuffer();
			char[] buffer = new char[4096];
			for(int readLen = dataReader.read(buffer); readLen!=-1; readLen = dataReader.read(buffer)) {
				xml.append(buffer, 0, readLen);
			}
			if(Logger.isInfoEnabled()) {
				Logger.info("Ccbbank EBS " + txCode + " response: " + xml.toString());
			}
			SAXReader reader = new SAXReader();
			Element xmlElement = reader.read(new StringReader(xml.toString().replaceAll("&nbsp;", ""))).getRootElement();
			return xmlElement;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				inputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				dataReader.close();
			}
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				socket.close();
			} 
			catch (Exception e) {
				
			}
		}
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