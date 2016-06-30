package com.yuanluesoft.jeaf.payment.paymentmethod.icbcbank.service.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.com.infosec.icbc.ReturnValue;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.icbcbank.pojo.IcbcbankOrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.HttpResponse;

/**
 * 工商银行支付服务
 * @author linchuan
 *
 */
public class IcbcbankPaymentMethodService implements PaymentMethodService {
	private DatabaseService databaseService;
	private String paymentNotifyUrl; //通知URL,如果当前服务器端口是80(工行不允许使用非80端口的通知URL),不需要配置,如果不是,则需要配置,如:http://www.npzfcg.gov.cn/paymentNotify.asp,由asp负责跳转回来
	
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
		return "借记卡,信用卡";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getIconUrl()
	 */
	public String getIconUrl() {
		return Environment.getContextPath() + "/jeaf/payment/icbcbank/logo/icbcbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "工商银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("B2C商户帐号", "b2cMerAcct", null, null, false)); //1406049529022001554
		parameterDefines.add(new PaymentMethodParameterDefine("B2C商户代码", "b2cMerID", null, null, false)); //1406EC23730712
		parameterDefines.add(new PaymentMethodParameterDefine("B2B商户账号", "b2bShopAccNum", null, null, false)); //1406049529022001554
		parameterDefines.add(new PaymentMethodParameterDefine("B2B商户代码", "b2bShopCode", null, null, false)); //1406EC13438186
		parameterDefines.add(new PaymentMethodParameterDefine("B2B收款单位账号", "b2bPayeeAcct", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("证书路径", "certPath", "证书在服务器上的存放路径，证书必须包括：user.crt、user.key、user.pfx、server.jks", new File(Environment.getWebinfPath() + "jeaf/payment/icbcbank/").getPath(), false));
		parameterDefines.add(new PaymentMethodParameterDefine("证书密码", "clientCertPassword", null, null, true));
		//parameterDefines.add(new PaymentMethodParameterDefine("服务端证书密码", "serverCertPassword", null, null));
		parameterDefines.add(new PaymentMethodParameterDefine("B2C支付URL", "b2cPaymentURL", null, "https://B2C.icbc.com.cn/servlet/NewB2cMerPayReqServlet", false));
		parameterDefines.add(new PaymentMethodParameterDefine("B2B支付URL", "b2bPaymentURL", null, "https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet", false));
		parameterDefines.add(new PaymentMethodParameterDefine("支付查询URL", "paymentQueryURL", null, "https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet", false));
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
		String paymentReason = StringUtils.escape(payment.getPaymentReason());
		String tranData = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?>" +
						  "<B2CReq>" +
						  	"<interfaceName>ICBC_PERBANK_B2C</interfaceName>" + //接口名称	interfaceName	＝16	必输， 取值：“ICBC_PERBANK_B2C”
						  	"<interfaceVersion>1.0.0.11</interfaceVersion>" + //接口版本号	interfaceVersion	MAX(15)	必输， 取值：“1.0.0.11”
						  	"<orderInfo>" +
						  		"<orderDate>" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), "yyyyMMddHHmmss") + "</orderDate>" + //交易日期时间	orderDate	=14	必输，格式为：YYYYMMDDHHmmss 要求在银行系统当前时间的前1小时和后12小时范围内，否则判定交易时间非法。
						  		"<curType>001</curType>" + //支付币种	curType	= 3	必输， 用来区分一笔支付的币种，目前工行只支持使用人民币（001）支付。 取值： “001”
						  		"<merID>" + paymentMerchant.getParameterValue("b2cMerID") + "</merID>" + //商户代码	merID	MAX(20)	必输， 唯一确定一个商户的代码，由商户在工行开户时，由工行告知商户。
						  		"<subOrderInfoList>" +
						  			"<subOrderInfo>" +
						  				"<orderid>" + payment.getId() + "</orderid>" + //订单号	orderid	MAX(30)	必输，每笔订单都需要有不同的订单号；客户支付后商户网站产生的一个唯一的定单号，该订单号应该在相当长的时间内不重复。工行通过订单号加订单日期来唯一确认一笔订单的重复性。
						  				"<amount>" + Math.round(payment.getMoney() * 100) + "</amount>" + //订单金额	amount	MAX(10)	必输，每笔订单一个； 客户支付订单的总金额，一笔订单一个，以分为单位。不可以为零，必需符合金额标准。
						  				"<installmentTimes>1</installmentTimes>" + //分期付款期数	installmentTimes	MAX（2）	必输，每笔订单一个； 取值：1、3、6、9、12、18、24；1代表全额付款，必须为以上数值，否则订单校验不通过。
						  				"<merAcct>" + paymentMerchant.getParameterValue("b2cMerAcct") + "</merAcct>" + //商户账号	merAcct	MAX(19)	必输，每笔订单一个，可以相同； 商户入账账号，只能交易时指定。（商户付给银行手续费的账户，可以在开户的时候指定，也可以用交易指定方式；用交易指定方式则使用此商户账号）
						  				"<goodsID></goodsID>" + //商品编号	goodsID	MAX(30)	选输，每笔订单一个；
						  				"<goodsName>" + (paymentReason.length()<=60 ? paymentReason : paymentReason.substring(0, 60)) + "</goodsName>" + //商品名称	goodsName	MAX(60)	必输，每笔订单一个；
						  				"<goodsNum>1</goodsNum>" + //商品数量	goodsNum	MAX(10)	选输，每笔订单一个；
						  				"<carriageAmt>0</carriageAmt>" + //已含运费金额	carriageAmt	MAX(10)	选输，每笔订单一个；
						  			"</subOrderInfo>" +
						  		"</subOrderInfoList>" +
						  	"</orderInfo>" +
						  	"<custom>" +
						  		"<verifyJoinFlag>0</verifyJoinFlag>" + //检验联名标志	verifyJoinFlag	=1 必输，  取值“1”：客户支付时，网银判断该客户是否与商户联名，是则按上送金额扣帐，否则展现未联名错误；取值“0”：不检验客户是否与商户联名，按上送金额扣帐。
						  		"<Language>ZH_CN</Language>" + //语言版本	Language	MAX(10)	选输，默认为中文版 取值：“EN_US”为英文版； 取值：“ZH_CN”或其他为中文版。 注意：大小写敏感。
						  	"</custom>" +
						  	"<message>" +
						  		"<creditType>" + ("借记卡".equals(payment.getAccountType()) ? "0" : "1") + "</creditType>" + //支持订单支付的银行卡种类	creditType	= 1	必输 默认“2”。取值范围为0、1、2，其中0表示仅允许使用借记卡支付，1表示仅允许使用信用卡支付，2表示借记卡和信用卡都能对订单进行支付
						  		"<notifyType>HS</notifyType>" + //通知类型	notifyType	= 2	必输 在交易转账处理完成后把交易结果通知商户的处理模式。 取值“HS”：在交易完成后实时将通知信息以HTTP协议POST方式，主动发送给商户，发送地址为商户端随订单数据提交的接收工行支付结果的URL即表单中的merURL字段；取值“AG”：在交易完成后不通知商户。商户需使用浏览器登录工行的B2C商户服务网站，或者使用工行提供的客户端程序API主动获取通知信息。
						  		"<resultType>0</resultType>" + //结果发送类型	resultType	=1 选输 取值“0”：无论支付成功或者失败，银行都向商户发送交易通知信息； 取值“1”，银行只向商户发送交易成功的通知信息。只有通知方式为HS时此值有效，如果使用AG方式，可不上送此项，但签名数据中必须包含此项，取值可为空。
						  		"<merReference></merReference>" + //商户reference	merReference	MAX(200)	选输，上送商户网站域名，如果上送，工行会在客户支付订单时，校验商户上送域名与客户跳转工行支付页面之前网站域名的一致性。
						  		"<merCustomIp>" + request.getRemoteAddr() + "</merCustomIp>" + //"27.156.38.163" 客户端IP	merCustomIp	MAX(20)	选输，工行在支付页面显示该信息。
						  		"<goodsType>1</goodsType>" + //虚拟商品/实物商品标志位	goodsType	=1	必输 取值“0”：虚拟商品； 取值“1”，实物商品。
						  		"<merCustomID></merCustomID>" + //买家用户号	merCustomID	MAX(100)	选输
						  		"<merCustomPhone></merCustomPhone>" + //买家联系电话	merCustomPhone	MAX(20)	选输，当商户上送goodsType值为“0”，该项必输
						  		"<goodsAddress></goodsAddress>" + //收货地址	goodsAddress	MAX(200)	选输，当商户上送goodsType值为“1”，该项必输
						  		"<merOrderRemark></merOrderRemark>" + //订单备注	merOrderRemark	MAX(200)	选输，工行在支付页面显示该信息。
						  		"<merHint></merHint>" + //商城提示	merHint	MAX(120)	选输
						  		"<remark1></remark1>" + //备注字段1	remark1	MAX(100)	选输 单位：字节
						  		"<remark2></remark2>" + //备注字段2	remark2	MAX(100)	选输 单位：字节
						  		"<merURL>" + (paymentNotifyUrl==null ? paymentCompleteCallbackURL : paymentNotifyUrl) + "</merURL>" + //返回商户URL	merURL	MAX(1024)	必输 必须合法的URL，交易结束，将客户引导到商户的此url，即通过客户浏览器post交易结果信息到商户的此URL
						  		"<merVAR></merVAR>" + //返回商户变量	merVAR	MAX(1024)	选输 商户自定义，当返回银行结果时，作为一个隐藏域变量，商户可以用此变量维护session等等。由客户端浏览器支付完成后提交通知结果时是明文传输，建议商户对此变量使用额外安全防范措施，如签名、base64
						  	"</message>" +
						  "</B2CReq>";
		if(Logger.isDebugEnabled()) {
			Logger.debug("IcbcbankPaymentMethodService: redirectToB2cBankPage, tranData is " + tranData);
		}
		try {
		 	FileInputStream inCrt = new FileInputStream(paymentMerchant.getParameterValue("certPath") + "/user.crt");
			byte[] bcert = new byte[inCrt.available()];
			inCrt.read(bcert);
			inCrt.close();
			
			FileInputStream inKey = new FileInputStream(paymentMerchant.getParameterValue("certPath") + "/user.key");
			byte[] bkey = new byte[inKey.available()];
			inKey.read(bkey);
			inKey.close();

			byte[] byteSrc = tranData.getBytes();
		    byte[] sign = ReturnValue.sign(byteSrc, byteSrc.length, bkey, paymentMerchant.getParameterValue("clientCertPassword").toCharArray());
		    if(sign==null) {
				throw new ServiceException("签名失败,签名返回为空,请检查证书私钥和私钥保护口令是否正确");
			}
		    
	        response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("<html>");
			writer.write("	<body onload=\"document.forms[0].submit()\">");
			writer.write("		<form name=\"sendOrder\" method=\"POST\" action=\"" + paymentMerchant.getParameterValue("b2cPaymentURL") + "\">");
			writer.write("			<INPUT NAME=\"interfaceName\" TYPE=\"hidden\" value=\"ICBC_PERBANK_B2C\">"); //接口名称	interfaceName	MAX(30)	必输，取值：“ICBC_PERBANK_B2C”
			writer.write("			<INPUT NAME=\"interfaceVersion\" TYPE=\"hidden\" value=\"1.0.0.11\">"); //接口版本号	interfaceVersion	MAX(15)	必输， 取值：“1.0.0.11”
			writer.write("			<INPUT NAME=\"tranData\" TYPE=\"hidden\" value=\"" + new String(ReturnValue.base64enc(byteSrc)) + "\">"); //交易数据	tranData	无限制	必输，签名；整合所有交易数据形成的xml明文串，并做BASE64编码；具体格式定义见下文； 注意： 需有xml头属性；整个字段使用BASE64编码； xml明文中没有回车换行和多余空格；
			writer.write("			<INPUT NAME=\"merSignMsg\" TYPE=\"hidden\" value=\"" + new String(ReturnValue.base64enc(sign)) + "\">"); //订单签名数据	merSignMsg	无限制	必输， 商户使用工行提供的签名API和商户证书将tranData的xml明文串进行签名，得到二进制签名数据，然后进行BASE64编码后得到可视的merSignMsg； 注意：签名时是针对tranData的xml明文，不是将tranData进行BASE64编码后的串；
			writer.write("			<INPUT NAME=\"merCert\" TYPE=\"hidden\" value=\"" + new String(ReturnValue.base64enc(bcert)) + "\">"); //商城证书公钥	merCert	无限制	必输， 商户用二进制方式读取证书公钥文件后，进行BASE64编码后产生的字符串；
			writer.write("		</form>");
			writer.write("	</body>");
			writer.write("</html>");
		}
		catch(ServiceException e) {
			throw e;
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
	public void redirectToB2bBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		try {
			String paymentReason = StringUtils.escape(payment.getPaymentReason());
			paymentReason = paymentReason.length()<=60 ? paymentReason : paymentReason.substring(0, 60);
			String signSrc = "APIName=B2B" +
							 "&APIVersion=001.001.001.001" +
							 "&Shop_code=" + paymentMerchant.getParameterValue("b2bShopCode") +
							 "&MerchantURL=" + (paymentNotifyUrl==null ? paymentCompleteCallbackURL : paymentNotifyUrl) +
							 "&ContractNo=" + payment.getId() +
							 "&ContractAmt=" + Math.round(payment.getMoney() * 100) +
							 "&Account_cur=001&" +
							 "JoinFlag=2" +
							 "&SendType=0" +
							 "&TranTime=" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss") +
							 "&Shop_acc_num=" + paymentMerchant.getParameterValue("b2bShopAccNum") +
							 "&PayeeAcct=" + paymentMerchant.getParameterValue("b2bPayeeAcct");
			
			FileInputStream inCrt = new FileInputStream(paymentMerchant.getParameterValue("certPath") + "/user.crt");
			byte[] bcert = new byte[inCrt.available()];
			inCrt.read(bcert);
			inCrt.close();
			
			FileInputStream inKey = new FileInputStream(paymentMerchant.getParameterValue("certPath") + "/user.key");
			byte[] bkey = new byte[inKey.available()];
			inKey.read(bkey);
			inKey.close();
			
			byte[] byteSrc = signSrc.getBytes();
		    byte[] sign = ReturnValue.sign(byteSrc, byteSrc.length, bkey, paymentMerchant.getParameterValue("clientCertPassword").toCharArray());
		    if(sign==null) {
				throw new ServiceException("签名失败,签名返回为空,请检查证书私钥和私钥保护口令是否正确");
			}
			
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("<html>");
			writer.write("	<body onload=\"document.forms[0].submit()\">");
			writer.write("		<form method=\"POST\" action=\"" + paymentMerchant.getParameterValue("b2bPaymentURL") + "\">");
			writer.write("			<input type=\"hidden\" name=\"APIName\" value=\"B2B\">"); //接口名称	APIName	MAX (30)	必输，签名，上送“B2B”，区别大小写！
			writer.write("			<input type=\"hidden\" name=\"APIVersion\" value=\"001.001.001.001\">"); //接口版本号	APIVersion	=15	必输，签名,上送”001.001.001.001”
			writer.write("			<input type=\"hidden\" name=\"Shop_code\" value=\"" + paymentMerchant.getParameterValue("b2bShopCode") + "\">"); //商户代码	Shop_code	=14	必输，签名，唯一确定一个商户的代码，由商户在工行开户时，由工行告知商户
			writer.write("			<input type=\"hidden\" name=\"MerchantURL\" value=\"" + (paymentNotifyUrl==null ? paymentCompleteCallbackURL : paymentNotifyUrl) + "\">"); //支付结果信息通知程序地址	MerchantURL	MAX (200)	签名，使用HS接口模式的商户用来接收工行订单支付结果信息的程序名字和位置。
			writer.write("			<input type=\"hidden\" name=\"ContractNo\" value=\"" + payment.getId() + "\">"); //订单号	ContractNo	MAX(30)	必输，签名，客户支付后商户网站产生的一个唯一的定单号，该订单号应该在相当长的时间内不重复。工行通过订单号加订单日期来唯一确认一笔订单的重复性。
			writer.write("			<input type=\"hidden\" name=\"ContractAmt\" value=\"" + Math.round(payment.getMoney() * 100) + "\">"); //订单金额	ContractAmt	MAX(18)	必输，签名，客户支付订单的总金额，一笔订单一个，保留到分，无小数点，如金额为1.00元，上传数据为“100”
			writer.write("			<input type=\"hidden\" name=\"Account_cur\" value=\"001\">"); //支付币种	Account_cur	= 3	必输，签名，用来区分一笔支付的币种。目前工行只支持人民币（001）支付。定义如下：001      人民币
			writer.write("			<input type=\"hidden\" name=\"JoinFlag\" value=\"2\">"); //检验联名标志	JoinFlag		必输，签名，固定上送2
			writer.write("			<input type=\"hidden\" name=\"Mer_Icbc20_signstr\" value=\"" + new String(ReturnValue.base64enc(sign)) + "\">"); //订单签名数据	Mer_Icbc20_signstr	无限制	生成签名数据的方法见后面的说明
			writer.write("			<input type=\"hidden\" name=\"Cert\" value=\"" + new String(ReturnValue.base64enc(bcert)) + "\">"); //商城证书数据	Cert	无限制	必输，商户端读取本地商户证书文件后，再使用工行提供的API进行Base64编码后产生的商户证书数据字串。
			writer.write("			<input type=\"hidden\" name=\"SendType\" value=\"0\">"); //结果发送类型 SendType	=10 ----成功失败信息都发送 1 ---- 只发送成功信息	必输，签名，如果取0，工行向商户发送一笔订单的每一次交易结果，无论支付成功或者失败，如果取1，工行只向商户发送交易成功的通知信息。
			writer.write("			<input type=\"hidden\" name=\"TranTime\" value=\"" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMddHHmmss") + "\">"); //交易日期时间	TranTime	=14	必输，签名，YYYYMMDDHHmmss。与工行系统当前时间差为：前1小时，后12小时
			writer.write("			<input type=\"hidden\" name=\"Shop_acc_num\" value=\"" + paymentMerchant.getParameterValue("b2bShopAccNum") + "\">"); //商城账号	Shop_acc_num	MAX (19)	必输，签名
			writer.write("			<input type=\"hidden\" name=\"PayeeAcct\" value=\"" + paymentMerchant.getParameterValue("b2bPayeeAcct") + "\">"); //收款单位账号	PayeeAcct	MAX (19)	必输，签名
			writer.write("			<input type=\"hidden\" name=\"GoodsCode\" value=\"\">"); //商品编号	GoodsCode	MAX (30)	选输
			writer.write("			<input type=\"hidden\" name=\"GoodsName\" value=\"" + paymentReason + "\">"); //商品名称	GoodsName	MAX (60)	选输
			writer.write("			<input type=\"hidden\" name=\"Amount\" value=\"1\">"); //订单数量	Amount	MAX (10)	选输
			writer.write("			<input type=\"hidden\" name=\"TransFee\" value=\"0\">"); //已含运费金额	TransFee	MAX (18)	选输
			writer.write("			<input type=\"hidden\" name=\"ShopRemark\" value=\"\">"); //商城提示	ShopRemark	MAX (120)	选输，最多120字符
			writer.write("			<input type=\"hidden\" name=\"ShopRem\" value=\"\">"); //商城备注字段	ShopRem	MAX(100)	选输，最多100字符
			writer.write("		</form>");
			writer.write("	</body>");
			writer.write("</html>");
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
		String merReqData = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\"?>" +
							"<ICBCAPI>" +
								"<in>" +
									"<orderNum>" + payment.getId() + "</orderNum>" +
									"<tranDate>" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMdd") + "</tranDate>" +
									"<ShopCode>" + paymentMerchant.getParameterValue("b2cMerID") + "</ShopCode>" +
									"<ShopAccount>" + paymentMerchant.getParameterValue("b2cMerAcct") + "</ShopAccount>" +
								"</in>" +
							"</ICBCAPI>";
		NameValuePair[] nameValuePairs = new NameValuePair[3];
		nameValuePairs[0] = new NameValuePair("APIName", "EAPI"); //接口名称	APIName	MAX (30)	必输，签名，上送“EAPI”，区别大小写！
		nameValuePairs[1] = new NameValuePair("APIVersion", "企业".equals(payment.getAccountType()) ? "001.001.001.001" : "001.001.002.001"); //接口版本号	APIVersion	=15	必输，签名,上送”001.001.002.001”
		nameValuePairs[2] = new NameValuePair("MerReqData", merReqData); //请求数据,xml包（格式见下）	MerReqData
	
		String serverKeyStoreFile = paymentMerchant.getParameterValue("certPath") + "/server.jks"; //服务端证书文件, 运行 java InstallCert corporbank.icbc.com.cn 生成jssecacerts，然后重命名为server.jks
		String serverKeyStoreType = "JKS"; //服务端证书类型,JKS/PKCS12等
		String serverKeyStorePassword = "changeit"; //服务端证书密码
		String clientKeyStoreFile = paymentMerchant.getParameterValue("certPath") + "/user.pfx"; //客户端证书文件
		String clientKeyStoreType = "PKCS12"; //客户端证书类型,JKS/PKCS12等
		String clientKeyStorePassword = paymentMerchant.getParameterValue("clientCertPassword"); //客户端证书密码
		Element xmlResponse = null;
		String response;
		try {
			HttpResponse httpResponse = HttpUtils.doPost(paymentMerchant.getParameterValue("paymentQueryURL"), null, nameValuePairs, null, serverKeyStoreFile, serverKeyStoreType, serverKeyStorePassword, clientKeyStoreFile, clientKeyStoreType, clientKeyStorePassword, 60000); //"https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet"
			response = URLDecoder.decode(httpResponse.getResponseBody(), "gbk");
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("IcbcbankPaymentMethodService: b2c payment query result is " + response);
		}
		/**
			返回值如果不是XML时，则返回如下代码：
		  	40972	API查询的订单不存在
			40973	API查询过程中系统异常
			40976	API查询系统异常
			40977	商户证书信息错
			40978	解包商户请求数据报错
			40979	查询的订单不存在
			40980	API查询过程中系统异常
			40981	给商户打包返回数据错
			40982	系统错误
			40983	查询的订单不唯一
			40987	请求数据中接口名错误
			40947	商户代码或者商城账号有误
			40948	商城状态非法
			40949	商城类别非法
			40950	商城应用类别非法
			40951	商户证书id状态非法
			40952	商户证书id未绑定
			40953	商户id权限非法
			40954	检查商户状态时数据库异常
		 */
		if(!response.startsWith("<?xml")) {
			if(response.indexOf("40972")!=-1 || response.indexOf("40979")!=-1) {
				return null;
			}
			throw new ServiceException();
		}
		//解析xml文件,获取需要的信息
		SAXReader reader = new SAXReader();
		StringReader strReader = new StringReader(response);
		try {
			xmlResponse = reader.read(strReader).getRootElement();
		}
		catch (DocumentException e) {
			throw new ServiceException(e);
		}
		
		/*
		 	B2B返回数据格式:
		 	<?xml  version="1.0" encoding="GBK" standalone="no" ?>
			<ICBCAPI>
				<pub>
					<APIName>接口名称</APIName>
					<APIVersion>接口版本号</APIVersion>
			    </pub>
		        <in>
		            <orderNum>订单号</orderNum>
					<tranDate>交易日期</ tranDate>
					<ShopCode>商家号码</ShopCode>
					<ShopAccount>商城账号</ShopAccount>
				</in>
				<out>
					<tranSerialNum>指令序号<tranSerialNum/>
					<tranStat>订单处理状态<tranStat/>
					<bankRem>指令错误信息<bankRem/>
					<amount>订单总金额<amount/>
					<currType>支付币种<currType/>
					<tranTime>返回通知日期时间<tranTime/>
					<PayeeAcct>收款人账号<PayeeAcct/>
					<PayeeName>收款人户名<PayeeName/>
					<JoinFlag>校验联名标志<JoinFlag/>
					<MerJoinFlag>商城联名标志<MerJoinFlag/>
					<CustJoinFlag>客户联名标志<CustJoinFlag/>
					<CustJoinNum>联名会员号<CustJoinNum/>
					<CertID>商户签名证书id<CertID/>
				</out>
			</ICBCAPI>
		  
		 	B2C返回数据格式:
		  	<?xml  version="1.0" encoding="GBK" standalone="no" ?>
			<ICBCAPI>
				<pub>
					<APIName>接口名称</APIName>
					<APIVersion>接口版本号</APIVersion>
			    </pub>
		        <in>
		        	<orderNum>订单号</orderNum>
					<tranDate>交易日期</ tranDate>
					<ShopCode>商家号码</ShopCode>
					<ShopAccount>商城账号</ShopAccount>
				</in>
				<out>
					<tranSerialNum>指令序号<tranSerialNum/>
					<tranStat>订单处理状态<tranStat/>
					<bankRem>指令错误信息<bankRem/>
					<amount>订单总金额<amount/>
					<currType>支付币种<currType/>
					<tranTime>返回通知日期时间<tranTime/>
					<ShopAccount>商城账号<ShopAccount/>
					<PayeeName>商城户名<PayeeName/>
					<JoinFlag>校验联名标志<JoinFlag/>
					<MerJoinFlag>商城联名标志<MerJoinFlag/>
					<CustJoinFlag>客户联名标志<CustJoinFlag/>
					<CustJoinNum>联名会员号<CustJoinNum/>
					<CertID>商户签名证书id<CertID/>
				</out>
			</ICBCAPI>
		 */
		//保存交易记录明细
		IcbcbankOrder order = (IcbcbankOrder)databaseService.findRecordByHql("from IcbcbankOrder IcbcbankOrder where IcbcbankOrder.paymentId=" + payment.getId());
		boolean isNew = (order==null);
		if(isNew) { //没有记录过
			order = new IcbcbankOrder();
			order.setId(UUIDLongGenerator.generateId()); //ID
			order.setPaymentId(payment.getId()); //支付ID
		}
		Element xmlIn = xmlResponse.element("in");
		Element xmlOut = xmlResponse.element("out");
		order.setOrderNum(xmlIn.elementText("orderNum")); //订单号
		try {
			order.setTranDate(DateTimeUtils.parseTimestamp(xmlIn.elementText("tranDate"), "yyyyMMdd")); //交易日期,20120816
		}
		catch (ParseException e) {
			
		}
		order.setShopCode(xmlIn.elementText("ShopCode")); //商家号码
		order.setShopAccount(xmlIn.elementText("ShopAccount")); //商城账号
		order.setTranSerialNum(xmlOut.elementText("tranSerialNum")); //指令序号
		/*
		  B2B:  3：指令处理完成，转账成功
				4：指令处理失败，转账未完成。
				6：指令超过支付人的限额，正在等待主管会计批复。
				7：指令超过支付人的限额，正在等待主管会计第二次批复。
				8：指令超过支付人的限额，被主管会计否决。
				9：银行正在处理（可疑）
 		   B2C: 0-支付成功，未清算
 		    	1-支付成功，已清算
 		    	2-支付失败
 		    	3-支付可疑交易
		 */
		order.setTranStat(xmlOut.elementText("tranStat")); //订单处理状态
		order.setBankRem(xmlOut.elementText("bankRem")); //指令错误信息
		order.setAmount(Integer.parseInt(xmlOut.elementText("amount"))/100.0); //订单总金额
		order.setCurrType(xmlOut.elementText("currType")); //支付币种
		order.setTranTime(xmlOut.elementText("tranTime")); //返回通知日期时间
		order.setPayeeAcct(xmlOut.elementText("PayeeAcct")); //收款人账号
		order.setPayeeName(xmlOut.elementText("PayeeName")); //收款人户名
		order.setJoinFlag(xmlOut.elementText("JoinFlag")); //校验联名标志
		order.setMerJoinFlag(xmlOut.elementText("MerJoinFlag")); //商城联名标志
		order.setCustJoinFlag(xmlOut.elementText("CustJoinFlag")); //客户联名标志
		order.setCustJoinNum(xmlOut.elementText("CustJoinNum")); //联名会员号
		order.setCertId(xmlOut.elementText("CertID")); //商户签名证书ID
		if(isNew) {
			databaseService.saveRecord(order);
		}
		else {
			databaseService.updateRecord(order);
		}
		//返回交易结果
		PaymentResult result = new PaymentResult();
		result.setTransactTime(order.getTranDate()); //交易时间
		result.setTransactMoney(order.getAmount()); //实际交易金额
		result.setFee(0); //手续费
		result.setTransactSn(order.getTranSerialNum()); //支付系统流水号
		result.setBankOrderId(order.getOrderNum()); //银行端订单ID
		String failedReason = null;
		if(!"企业".equals(payment.getAccountType())) { //借记卡,信用卡
			result.setTransactSuccess("0".equals(order.getTranStat()) || "1".equals(order.getTranStat()) ? '1' : '0'); //是否交易成功，订单处理状态，0-支付成功，未清算 1-支付成功，已清算 2-支付失败 3-支付可疑交易
			if("3".equals(order.getTranStat())) {
				failedReason = "支付可疑交易";
			}
		}
		else { //企业
			result.setTransactSuccess("3".equals(order.getTranStat()) ? '1' : '0'); //是否交易成功，订单处理状态，3：指令处理完成，转账成功 4：指令处理失败，转账未完成。 6：指令超过支付人的限额，正在等待主管会计批复。 7：指令超过支付人的限额，正在等待主管会计第二次批复。 8：指令超过支付人的限额，被主管会计否决。 9：银行正在处理（可疑）
			if("4".equals(order.getTranStat())) {
				failedReason = "指令处理失败，转账未完成"; // 6：指令超过支付人的限额，正在等待主管会计批复。 7：指令超过支付人的限额，正在等待主管会计第二次批复。 8：指令超过支付人的限额，被主管会计否决。 9：银行正在处理（可疑）
			}
			else if("6".equals(order.getTranStat())) {
				failedReason = "指令超过支付人的限额，正在等待主管会计批复";
			}
			else if("7".equals(order.getTranStat())) {
				failedReason = "指令超过支付人的限额，正在等待主管会计第二次批复";
			}
			else if("8".equals(order.getTranStat())) {
				failedReason = "指令超过支付人的限额，被主管会计否决";
			}
			else if("9".equals(order.getTranStat())) {
				failedReason = "银行正在处理（可疑）";
			}
		}
		if(failedReason==null) {
			failedReason = order.getBankRem();
		}
		else if(order.getBankRem()!=null) {
			failedReason += "," + order.getBankRem();
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

	/**
	 * @return the paymentNotifyUrl
	 */
	public String getPaymentNotifyUrl() {
		return paymentNotifyUrl;
	}

	/**
	 * @param paymentNotifyUrl the paymentNotifyUrl to set
	 */
	public void setPaymentNotifyUrl(String paymentNotifyUrl) {
		this.paymentNotifyUrl = paymentNotifyUrl;
	}
}