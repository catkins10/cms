package com.yuanluesoft.jeaf.payment.paymentmethod.bocbank.service.spring;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bocnet.common.security.PKCS7Tool;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.bocbank.pojo.BocbankB2BOrder;
import com.yuanluesoft.jeaf.payment.paymentmethod.bocbank.pojo.BocbankB2COrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.HttpResponse;

/**
 * 中国银行支付服务
 * @author linchuan
 *
 */
public class BocbankPaymentMethodService implements PaymentMethodService {
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
		return Environment.getContextPath() + "/jeaf/payment/bocbank/logo/bocbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "中国银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("B2C支付URL", "b2cPaymentURL", null, "https://ebspay.boc.cn/PGWPortal/RecvOrder.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2C查询URL", "b2cQueryURL", null, "https://ebspay.boc.cn/PGWPortal/CommonQueryOrder.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2C商户号", "b2cMerchantNo", null, null, false));
		
		parameterDefines.add(new PaymentMethodParameterDefine("B2B支付URL", "b2bPaymentURL", null, "https://ebspay.boc.cn/PGWPortal/B2BRecvOrder.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2B查询URL", "b2bQueryURL", null, "https://ebspay.boc.cn/PGWPortal/CommonB2BQueryOrder.do", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2B商户号", "b2bBocNo", null, null, false));
		
		parameterDefines.add(new PaymentMethodParameterDefine("证书库路径", "keyStorePath", "证书在服务器上的存放路径，证书类型为pfx", new File(Environment.getWebinfPath() + "jeaf/payment/bocbank/merchant.pfx").getPath(), false));
		parameterDefines.add(new PaymentMethodParameterDefine("证书库口令", "keyStorePassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("签名私钥口令", "keyPassword", "一般与证书库口令相同", null, true));		
		return parameterDefines;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.service.PaymentMethodService#redirectToBankPage(com.yuanluesoft.jeaf.payment.pojo.Payment, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		if(!"企业".equals(payment.getAccountType())) { //借记卡,信用卡
			redirectToB2cBankPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
		else { //企业
			redirectToB2bBankPage(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
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
	public void redirectToB2cBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		try {
			//签名,商户订单号|订单时间|订单币种|订单金额|商户号orderNo|orderTime|curCode|orderAmount|merchantNo
			String orderTime = DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss");
			String orderAmount = new DecimalFormat("0.00").format(payment.getMoney());
			String merchantNo = paymentMerchant.getParameterValue("b2cMerchantNo");
			String data = payment.getId() + "|" + //商户订单号
						  orderTime + "|" + //订单时间
						  "001|" + //订单币种
						  orderAmount + "|" + //订单金额
						  merchantNo; //商户号
			String signature = sign(data, paymentMerchant);
			
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write("<html>");
			writer.write("	<body onload=\"document.forms[0].submit()\">");
			writer.write("		<form name=\"sendOrder\" method=\"POST\" action=\"" + paymentMerchant.getParameterValue("b2cPaymentURL") + "\">");
			writer.write("			<INPUT NAME=\"merchantNo\" TYPE=\"hidden\" value=\"" + merchantNo + "\">"); //商户号	merchantNo	X(20)	必填 OC商户ID
			writer.write("			<INPUT NAME=\"payType\" TYPE=\"hidden\" value=\"1\">"); //支付类型	payType	X(10) 必填 商户支付服务类型 1：网上购物
			writer.write("			<INPUT NAME=\"orderNo\" TYPE=\"hidden\" value=\"" + payment.getId() + "\">"); //商户订单号	orderNo	X(19)	必填 商户系统产生的订单号
			writer.write("			<INPUT NAME=\"curCode\" TYPE=\"hidden\" value=\"001\">"); //订单币种	curCode	X(3)	必填 目前只支持001：人民币 固定填001
			writer.write("			<INPUT NAME=\"orderAmount\" TYPE=\"hidden\" value=\"" + orderAmount + "\">"); //订单金额	orderAmount	X(13)	必填 格式：整数位不前补零,小数位补齐2位 即：不超过10位整数位+1位小数点+2位小数 无效格式如123，.10，1.1,有效格式如1.00，0.10
			writer.write("			<INPUT NAME=\"orderTime\" TYPE=\"hidden\" value=\"" + orderTime + "\">"); //订单时间	orderTime	9(14)	必填 格式：YYYYMMDDHHMISS 其中时间为24小时格式，例:2010年3月2日下午4点5分28秒表示为20100302160528
			writer.write("			<INPUT NAME=\"orderNote\" TYPE=\"hidden\" value=\"" + (payment.getPaymentReason()==null ? "" : payment.getPaymentReason().substring(0, Math.min(60, payment.getPaymentReason().length())))  + "\">"); //订单说明	orderNote	X(200)	必填 订单描述，，要求如果全中文最多允许60个汉字长度
			writer.write("			<INPUT NAME=\"orderUrl\" TYPE=\"hidden\" value=\"" + paymentCompleteCallbackURL + "\">"); //商户接收通知URL	orderUrl	X(100)	必填 客户支付完成后银行向商户发送支付结果，商户系统负责接收银行通知的URL
			//writer.write("			<INPUT NAME=\"orderTimeoutDate\" TYPE=\"hidden\" value=\"" +  + "\">"); //超时时间	orderTimeoutDate	9(14)	选填（一般商户无需上送，仅当商户需指定支付截止时间时上送。） 格式：YYYYMMDDHHMISS 其中时间为24小时格式，例:2010年3月2日下午4点5分28秒表示为20100302160528。其含义为：如客户在该时间点仍未确认支付，则该笔支付将超时失败。
			writer.write("			<INPUT NAME=\"signData\" TYPE=\"hidden\" value=\"" + signature + "\">"); //商户签名数据	signData	X(4000)	必填 商户签名数据串格式，各项数据用管道符分隔：商户订单号|订单时间|订单币种|订单金额|商户号orderNo|orderTime|curCode|orderAmount|merchantNo
			writer.write("		</form>");
			writer.write("	</body>");
			writer.write("</html>");
		}
		catch(Exception e) {
			throw new ServiceException(e);
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
	public void redirectToB2bBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		try {
			//签名,商户号|商户订单号|订单币种|订单金额|订单时间 bocNo|orderNo|curCode|orderAmount|orderTime
			String orderTime = DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss");
			String orderAmount = new DecimalFormat("0.00").format(payment.getMoney());
			String bocNo = paymentMerchant.getParameterValue("b2bBocNo");
			String data = bocNo + "|" + //商户号
						  payment.getId() + "|" + //商户订单号
						  "001|" + //订单币种
						  orderAmount + "|" + //订单金额
						  orderTime; //订单时间
			String signature = sign(data, paymentMerchant);
			
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write("<html>");
			writer.write("	<body onload=\"document.forms[0].submit()\">");
			writer.write("		<form name=\"sendOrder\" method=\"POST\" action=\"" + paymentMerchant.getParameterValue("b2bPaymentURL") + "\">");
			writer.write("			<INPUT NAME=\"bocNo\" TYPE=\"hidden\" value=\"" + bocNo + "\">"); //商户号 bocNo	varchar(20)	必填 网关商户号
			writer.write("			<INPUT NAME=\"orderNo\" TYPE=\"hidden\" value=\"" + payment.getId() + "\">"); //商户订单号	orderNo	varchar(20)	必填 商户系统产生的订单号；数字与26个英文字母以及中划线（-）和下划线（_）
			writer.write("			<INPUT NAME=\"curCode\" TYPE=\"hidden\" value=\"001\">"); //订单币种	curCode	varchar(3)	必填 目前只支持001-人民币 固定填001
			writer.write("			<INPUT NAME=\"orderAmount\" TYPE=\"hidden\" value=\"" + orderAmount + "\">"); //订单金额	orderAmount	number(15,2)	必填 订单总金额  格式： 不超过12位整数位+1位小数点+2位小数 无效格式如123，.10，1.131,有效格式如1.10，0.10
			writer.write("			<INPUT NAME=\"orderTime\" TYPE=\"hidden\" value=\"" + orderTime + "\">"); //订单时间	orderTime	varchar(14)	必填 商户端生成的订单时间  格式：YYYYMMDDHHMISS 其中时间为24小时格式，例:2010年3月2日下午4点5分28秒表示为20100302160528
			writer.write("			<INPUT NAME=\"orderNote\" TYPE=\"hidden\" value=\"" + (payment.getPaymentReason()==null ? "" : payment.getPaymentReason().substring(0, Math.min(60, payment.getPaymentReason().length())))  + "\">"); //订单说明	orderNote	varchar(200)	非必填 订单描述，要求如果全中文最多允许60个汉字长度
			writer.write("			<INPUT NAME=\"orderUrl\" TYPE=\"hidden\" value=\"" + paymentCompleteCallbackURL + "\">"); //通知商户URL	orderUrl	varchar(200)	必填 网关完成交易获得明确交易状态后向该URL发送通知
			writer.write("			<INPUT NAME=\"signData\" TYPE=\"hidden\" value=\"" + signature + "\">"); //数字签名	signData	varchar(4000)	必填 商户签名数据串格式，各项数据用管道符分隔：  商户号|商户订单号|订单币种|订单金额|订单时间bocNo|orderNo|curCode|orderAmount|orderTime
			writer.write("		</form>");
			writer.write("	</body>");
			writer.write("</html>");
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 签名
	 * @param data
	 * @param paymentMerchant
	 * @return
	 * @throws Exception
	 */
	private String sign(String data, PaymentMerchant paymentMerchant) throws Exception {
		//String keyStorePath: 证书库路径
		//String keyStorePassword: 证书库口令
		//String keyPassword: 签名私钥口令，一般与证书库口令相同
		PKCS7Tool tool = PKCS7Tool.getSigner(paymentMerchant.getParameterValue("keyStorePath"), paymentMerchant.getParameterValue("keyStorePassword"), paymentMerchant.getParameterValue("keyPassword"));
		//签名，返回signature：base64格式的签名结果
		//byte[] data: 明文字符串
		return tool.sign(data.getBytes());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPayment(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		if(!"企业".equals(payment.getAccountType())) { //借记卡,信用卡
			return queryB2cPayment(payment, paymentMerchant);
		}
		else { //企业
			return queryB2bPayment(payment, paymentMerchant);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPaymentByCallbackURL(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String)
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		return null; //不需要实现
	}
	
	/**
	 * b2c支付查询
	 * @param payment
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2cPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		try {
			String merchantNo = paymentMerchant.getParameterValue("b2cMerchantNo");
			NameValuePair[] nameValuePairs = new NameValuePair[3];
			nameValuePairs[0] = new NameValuePair("merchantNo", merchantNo); //商户号	merchantNo	X(20)	BOC商户ID
			nameValuePairs[1] = new NameValuePair("orderNos", "" + payment.getId()); //商户订单号字符串	orderNos	X(1000)	商户系统产生的订单号，支持输入多个订单号进行查询，最多支持50个订单号的查询 格式：orderNo|orderNo|orderNo 例如：4|11|10|12|13
			nameValuePairs[2] = new NameValuePair("signData", sign(merchantNo + ":" + payment.getId(), paymentMerchant)); //商户签名信息	signData	　X(4000)	商户签名数据串格式，各项数据用冒号分隔（其中多笔订单号使用管道符分隔）： 商户号:商户订单号字符串merchantNo:orderNos
			HttpResponse httpResponse = HttpUtils.doPost(paymentMerchant.getParameterValue("b2cQueryURL"), null, nameValuePairs, null); // serverKeyStoreFile, serverKeyStoreType, serverKeyStorePassword, clientKeyStoreFile, clientKeyStoreType, clientKeyStorePassword, 60000); //"https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet"
			if(httpResponse==null) {
				throw new ServiceException();
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("BocbankPaymentMethodService: b2c payment query result is " + httpResponse.getResponseBody());
			}
			
			/*
			 * 返回数据格式
			 * <?xml version="1.0" encoding="utf-8" ?> 
				<res>
				<header>
					<msgId>0000204</msgId> 
					<hdlSts>A</hdlSts> 
					<bdFlg>0</bdFlg> 
					<rtnCd /> 
				</header>
				<body>
					<orderTrans>
						<merchantNo>333555</merchantNo> 
						<orderNo>1225</orderNo> 
						<orderSeq>12851</orderSeq> 
						<orderStatus>0</orderStatus> 
						<cardTyp>1</cardTyp> 
						<acctNo>5555510100123456789</acctNo> 
				       <holderName>张三</holderName>
				       <ibknum>07428</ibknum>
						<payTime>20110624171313</payTime> 
						<payAmount>14.00</payAmount> 
						<visitorIp>127.0.0.1</visitorIp> 
						<visitorRefer /> 
					</orderTrans>
					<orderTrans>
						<merchantNo>333555</merchantNo> 
						<orderNo>1226</orderNo> 
						<orderSeq>12112</orderSeq> 
						<orderStatus>0</orderStatus> 
						<cardTyp>1</cardTyp> 
						<acctNo>5555510100123456789</acctNo> 
				       <holderName>张三</holderName>
				       <ibknum>07428</ibknum>
						<payTime>20110624171313</payTime> 
						<payAmount>100.00</payAmount> 
						<visitorIp>127.0.0.1</visitorIp> 
						<visitorRefer /> 
					</orderTrans>
				</body>
				</res>
			 */
			
			//解析xml文件,获取需要的信息
			SAXReader reader = new SAXReader();
			StringReader strReader = new StringReader(httpResponse.getResponseBody());
			Element xmlResponse = reader.read(strReader).getRootElement();
			Element xmlHeader = xmlResponse.element("header");
			if(!"A".equals(xmlHeader.elementText("hdlSts"))) { //HandleStatus 处理状态	<hdlSts>	[1..1]	[A|B|K]	A-成功  B-失败  K-未明
				throw new ServiceException("hdlSts:" + xmlHeader.elementText("hdlSts"));
			}
			if(!"0".equals(xmlHeader.elementText("bdFlg"))) { //BodyFlag 业务体报文块存在标识	<bdFlg>	[1..1]	[0|1]	0-有包体 1-无包体
				return null;
			}
			Element xmlBody = xmlResponse.element("body");
			if(xmlBody==null) {
				return null;
			}
			Element xmlOrder = xmlBody.element("orderTrans");
			//保存交易记录明细
			BocbankB2COrder order = (BocbankB2COrder)databaseService.findRecordByHql("from BocbankB2COrder BocbankB2COrder where BocbankB2COrder.paymentId=" + payment.getId());
			boolean isNew = (order==null);
			if(isNew) { //没有记录过
				order = new BocbankB2COrder();
				order.setId(UUIDLongGenerator.generateId()); //ID
				order.setPaymentId(payment.getId()); //支付ID
			}
			order.setMerchantNo(xmlOrder.elementText("merchantNo")); //商户号,BOC商户ID
			order.setOrderNo(xmlOrder.elementText("orderNo")); //BOC商户ID,商户系统产生的订单号
			order.setOrderSeq(xmlOrder.elementText("orderSeq")); //银行订单流水号,银行的订单流水号（银行产生的订单唯一标识）
			order.setOrderStatus(xmlOrder.elementText("orderStatus")); //订单状态,订单状态：0-未处理 1-支付 4-未明 5-失败
			order.setCardTyp(xmlOrder.elementText("cardTyp")); //银行卡类别,银行卡类别：01：中行借记卡 02：中行信用卡，信用卡（分行卡） 04：中行信用卡，信用卡（总行卡） 11：银联借记卡 21：VISA借记卡 22：VISA信用卡 31：MC 借记卡 32：MC 信用卡 42：运通卡 52：大来卡 62：JCB卡
			order.setAcctNo(xmlOrder.elementText("acctNo")); //支付卡号,为保护个人客户信息，我行暂不提供该数据项，以****反馈
			order.setHolderName(xmlOrder.elementText("holderName")); //持卡人姓名,为保护个人客户信息，我行暂不提供该数据项，以****反馈
			order.setIbknum(xmlOrder.elementText("ibknum")); //支付卡省行联行号,为保护个人客户信息，我行暂不提供该数据项，以****反馈
			order.setPayTime(xmlOrder.elementText("payTime")); //支付时间,支付交易的日期时间 格式：YYYYMMDDHHMISS
			order.setPayAmount(xmlOrder.elementText("payAmount")); //支付金额,支付金额，格式：整数位不前补零,小数位补齐2位 即：不超过10位整数位+1位小数点+2位小数  无效格式如123，.10，1.1,有效格式如1.00，0.10
			order.setVisitorIp(xmlOrder.elementText("visitorIp")); //访问者IP,客户通过网银支付时的IP地址信息
			order.setVisitorRefer(xmlOrder.elementText("visitorRefer")); //访问者Refer信息,客户浏览器跳转至网银支付登录界面前所在页面的URL（urlEncode格式）
			if(isNew) {
				databaseService.saveRecord(order);
			}
			else {
				databaseService.updateRecord(order);
			}
			//返回交易结果
			PaymentResult result = new PaymentResult();
			result.setTransactTime(DateTimeUtils.parseTimestamp(order.getPayTime(), "yyyyMMddHHmmsss")); //交易时间
			result.setTransactMoney(Double.parseDouble(order.getPayAmount())); //实际交易金额
			result.setFee(0); //手续费
			result.setTransactSn(order.getOrderSeq()); //支付系统流水号
			result.setBankOrderId(order.getOrderSeq()); //银行端订单ID
			result.setTransactSuccess("1".equals(order.getOrderStatus()) ? '1' : '0'); //是否交易成功，订单状态,订单状态：0-未处理 1-支付 4-未明 5-失败
			result.setFailedReason(null); //交易失败原因
			return result;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/**
	 * b2b支付查询
	 * @param payment
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	private PaymentResult queryB2bPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		try {
			String bocNo = paymentMerchant.getParameterValue("b2bBocNo");
			NameValuePair[] nameValuePairs = new NameValuePair[3];
			nameValuePairs[0] = new NameValuePair("bocNo", bocNo); //商户号	bocNo	varchar(20)	必填 网关商户号
			nameValuePairs[1] = new NameValuePair("orderNos", "" + payment.getId()); //商户订单号字符串	orderNos	varchar(210)	必填 商户系统产生的订单号，支持输入多个订单号进行查询，最多支持10个订单号的查询，订单号之间用冒号分隔 格式：orderNo:orderNo:orderNo 例如：4:11:10:12:13
			nameValuePairs[2] = new NameValuePair("signData", sign(bocNo + "|" + payment.getId(), paymentMerchant)); //数字签名	signData	Varchar(4000)	必填 商户签名数据串格式，各项数据用管道符分隔：商户号|商户订单号字符串 bocNo|orderNos
			HttpResponse httpResponse = HttpUtils.doPost(paymentMerchant.getParameterValue("b2bQueryURL"), null, nameValuePairs, null); // serverKeyStoreFile, serverKeyStoreType, serverKeyStorePassword, clientKeyStoreFile, clientKeyStoreType, clientKeyStorePassword, 60000); //"https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet"
			if(httpResponse==null) {
				throw new ServiceException();
			}
			if(Logger.isDebugEnabled()) {
				Logger.debug("BocbankPaymentMethodService: b2b payment query result is " + httpResponse.getResponseBody());
			}
			
			/*
			 * 返回数据格式
			 * <?xml version="1.0" encoding="utf-8" ?> 
				<res>
				    <header>
				         <errCode>E000000009</errCode>
				         <errMsg>today.is.holiday</errMsg>
				    </header>
				    <body>
				         <orderTrans>
				                  <bocNo>104110070118888</bocNo>
				                  <orderNo>4</orderNo> 
				                  <orderSeq>9</orderSeq>
				                  <curCode>001</curCode>
				                  <orderAmount>200.12</orderAmount>
				                  <recvTime>20090604000000</recvTime>  
				                  <lastTranTime>20090605000000</lastTranTime>
				             <refundedAmount>200.12</refundedAmount>
				                  <orderStatus>1</orderStatus>  
				                  <actnumT></ actnumT >  
				                  <actnamT></ actnamT >  
				                  <bocFlag></ bocFlag > 
				                  <ibkNamT></ ibkNamT> 
				                  <cnapsNo></ cnapsNo > 
				                  <ibknumT></ ibknumT > 
				                  <orgidtT></ orgidtT > 
				                  <orderType>0</ orderType >
				           <ibknumF>****</ ibknumF >
				           <actnumF>****</ actnumF >  
				                  <actnamF>****</ actnamF >  
				         </orderTrans>
				      
				         ……
				         </body>
				</res>
			 */
			
			//解析xml文件,获取需要的信息
			SAXReader reader = new SAXReader();
			StringReader strReader = new StringReader(httpResponse.getResponseBody());
			Element xmlResponse = reader.read(strReader).getRootElement();
			Element xmlBody = xmlResponse.element("body");
			if(xmlBody==null) {
				return null;
			}
			Element xmlOrder = xmlBody.element("orderTrans");
			//保存交易记录明细
			BocbankB2BOrder order = (BocbankB2BOrder)databaseService.findRecordByHql("from BocbankB2BOrder BocbankB2BOrder where BocbankB2BOrder.paymentId=" + payment.getId());
			boolean isNew = (order==null);
			if(isNew) { //没有记录过
				order = new BocbankB2BOrder();
				order.setId(UUIDLongGenerator.generateId()); //ID
				order.setPaymentId(payment.getId()); //支付ID
			}
			order.setBocNo(xmlOrder.elementText("bocNo")); //商户号,网关商户号
			order.setOrderNo(xmlOrder.elementText("orderNo")); //商户订单号,商户系统产生的订单号；数字与26个英文字母以及中划线（-）和下划线（_）
			order.setOrderSeq(Long.parseLong(xmlOrder.elementText("orderSeq"))); //订单流水,网关保存的订单唯一流水号
			order.setCurCode(xmlOrder.elementText("curCode")); //订单币种,目前只支持001-人民币 固定填001
			order.setOrderAmount(Double.parseDouble(xmlOrder.elementText("orderAmount"))); //订单金额,订单总金额
			order.setRecvTime(xmlOrder.elementText("recvTime")); //接单时间,网关接收订单的时间 格式：YYYYMMDDHHMISS 其中时间为24小时格式，例:2010年3月2日下午4点5分28秒表示为20100302160528
			order.setLastTranTime(xmlOrder.elementText("lastTranTime")); //最后交易时间,后台最近一次交易的处理时间
			order.setRefundedAmount(Double.parseDouble(xmlOrder.elementText("refundedAmount"))); //订单累计退款金额,针对一笔订单支持多次退款，该域表示订单成功退款交易的累计金额
			order.setOrderStatus(xmlOrder.elementText("orderStatus")); //订单状态,订单状态 0:未确认/1:确认/T1:支付成功/T2:支付失败/T3:支付故障/ 10:过期失效
			order.setActnumT(xmlOrder.elementText("actnumT")); //收款账号,收款账户账号，收款账户为他行有可能超过18位
			order.setActnamT(xmlOrder.elementText("actnamT")); //收款账户名称,收款账户的户名 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setBocFlag(xmlOrder.elementText("bocFlag")); //中行标志,收款账户是否是中行的标志  0:他行/1:中行 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setIbkNamT(xmlOrder.elementText("ibkNamT")); //收款行名称,中行标志表示为他行时必填 收款账户开户行名称，最长29位汉字符 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setCnapsNo(xmlOrder.elementText("cnapsNo")); //人行行号,中行标志表示为他行时必填 人民银行分配给各行的支付行行号 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setIbkNumT(xmlOrder.elementText("ibkNumT")); //收款省行联行号,中行标志表示为中行时必填 收款账户所属省行联行号（收款账户是中行账户时） 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setOrgidtT(xmlOrder.elementText("orgidtT")); //收款行机构号,可选项 收款账户开户行机构号（收款账户是中行账户时，旧线为orgidt机构号，新线要求是bancs机构号） 受控于订单类型属性，订单类型为0-B2B直付时，该信息为空；订单类型为1-B2B保付时，该信息有值
			order.setOrderType(xmlOrder.elementText("orderType").charAt(0)); //订单类型,订单类型为0-B2B直付;订单类型为1-B2B保付;
			order.setIbknumF(xmlOrder.elementText("ibknumF")); //付款省行联行号,付款账户所属省行联行号，为保护客户信息，我行暂不提供该数据项，以****反馈商户。
			order.setActnumF(xmlOrder.elementText("actnumF")); //付款账号,付款账户账号，为保护客户信息，我行暂不提供该数据项，以****反馈商户
			order.setActnamF(xmlOrder.elementText("actnamF")); //付款账户名称,付款账户的户名，为保护客户信息，我行暂不提供该数据项，以****反馈商户
			if(isNew) {
				databaseService.saveRecord(order);
			}
			else {
				databaseService.updateRecord(order);
			}
			//返回交易结果
			PaymentResult result = new PaymentResult();
			result.setTransactTime(DateTimeUtils.parseTimestamp(order.getLastTranTime()==null ? order.getRecvTime() : order.getLastTranTime(), "yyyyMMddHHmmsss")); //交易时间
			result.setTransactMoney(order.getOrderAmount()); //实际交易金额
			result.setFee(0); //手续费
			result.setTransactSn("" + order.getOrderSeq()); //支付系统流水号
			result.setBankOrderId("" + order.getOrderSeq()); //银行端订单ID
			result.setTransactSuccess("T1".equals(order.getOrderStatus()) ? '1' : '0'); //是否交易成功，订单状态,订单状态 0:未确认/1:确认/T1:支付成功/T2:支付失败/T3:支付故障/ 10:过期失效
			result.setFailedReason(null); //交易失败原因
			return result;
		}
		catch(Exception e) {
			Logger.exception(e);
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
	
	public static void main(String[] args) throws Exception {
		Payment payment = new Payment();
		payment.setId(123l);
		PaymentMerchant paymentMerchant = new PaymentMerchant();
		paymentMerchant.setParameterValue("b2cQueryURL", "https://ebspay.boc.cn/PGWPortal/CommonQueryOrder.do");
		paymentMerchant.setParameterValue("b2bQueryURL", "http://180.168.146.75:81/PGWPortal/CommonB2BQueryOrder.do");
		paymentMerchant.setParameterValue("b2cMerchantNo", "104110059475555");
		paymentMerchant.setParameterValue("b2bBocNo", "5664");
		paymentMerchant.setParameterValue("keyStorePath", new File(Environment.getWebinfPath() + "jeaf/payment/bocbank/merchant.pfx").getPath());
		paymentMerchant.setParameterValue("keyStorePassword", "111111");
		paymentMerchant.setParameterValue("keyPassword", "111111");		
		new BocbankPaymentMethodService().queryB2bPayment(payment, paymentMerchant);
	}
}