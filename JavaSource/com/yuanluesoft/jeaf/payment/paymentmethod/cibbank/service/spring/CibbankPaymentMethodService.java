package com.yuanluesoft.jeaf.payment.paymentmethod.cibbank.service.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.crypt.Des.DesUtil;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.payment.model.PaymentMethodParameterDefine;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService;
import com.yuanluesoft.jeaf.payment.paymentmethod.cibbank.model.CibEnterprisePaymentOrder;
import com.yuanluesoft.jeaf.payment.paymentmethod.cibbank.pojo.CibbankOrder;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 兴业银行支付服务
 * @author linchuan
 *
 */
public class CibbankPaymentMethodService implements PaymentMethodService {
	private DatabaseService databaseService;
	private PageService pageService;
	
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
		return Environment.getContextPath() + "/jeaf/payment/cibbank/logo/cibbank.jpg";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getName()
	 */
	public String getName() {
		return "兴业银行";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#getParameterDefines()
	 */
	public List getParameterDefines() {
		List parameterDefines = new ArrayList();
		parameterDefines.add(new PaymentMethodParameterDefine("个人网银支付URL", "paymentURL", null, "https://payment.cib.com.cn/payment/entry.action", false));
		parameterDefines.add(new PaymentMethodParameterDefine("个人网银支付查询URL", "paymentQueryURL", null, "https://payment.cib.com.cn/payment/api/old", false));
		parameterDefines.add(new PaymentMethodParameterDefine("个人网银商户ID", "merchantId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("个人网银商户公钥", "merchantMacKey", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银代码", "clientId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银登录用户名", "clientUserName", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银登录密码", "clientUserPassword", null, null, true));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银用户密码问题", "paymentVerifyName", "企业创建B2B订单时提醒用户输入的密码的描述,如:XXX平台网址", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银用户密码", "paymentVerifyCode", "企业创建B2B订单时用户要输入的密码", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方名称", "sellName", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银买方名称的描述", "buyerName", "如：投标企业", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银是否行内账户", "interBank", "Y/N，Y-表示兴业银行，N-它行，当账户属性为“Y”时,要求收款账号与收款账户名称必须匹配", null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方帐号", "sellAccountId", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方帐户名", "sellAccountName", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方开户行", "sellBank", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方开户行编码", "sellBankNum", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银卖方开户行所在地", "sellBankCity", null, null, false));
		parameterDefines.add(new PaymentMethodParameterDefine("企业网银企业银行入口", "enterpriseBankURL", "WEB服务器端口", "https://220.250.30.210:7032/eb3/", false));
		return parameterDefines;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.service.PaymentMethodService#redirectToBankPage(com.yuanluesoft.jeaf.payment.pojo.Payment, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		if("企业".equals(payment.getAccountType())) {
			redirectToEnterprisePayment(payment, paymentMerchant, paymentCompleteCallbackURL, request, response);
		}
		else { //个人
			redirectToPersonalPayment(payment, paymentMerchant, paymentCompleteCallbackURL, response);
		}
	}
	
	/**
	 * 打开个人支付页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToPersonalPayment(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletResponse response) throws ServiceException {
		try {
			String merchantURL = paymentCompleteCallbackURL;
			String macContent = paymentMerchant.getParameterValue("merchantId") +
							    Long.toString(payment.getId(), 32) +
							    DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMdd") +
							    payment.getMoney() +
							    "1" +
							    "CNY" +
							    merchantURL +
							    "merchant";
			String mac = DesUtil.genMac(paymentMerchant.getParameterValue("merchantMacKey"), macContent);
			String url = paymentMerchant.getParameterValue("paymentURL") +
						 "?NetPaymentMerchantID=" + paymentMerchant.getParameterValue("merchantId") + //1.商户编号
						 "&NetPaymentMerchantTraceNo=" + Long.toString(payment.getId(), 32) + //2.订单号
						 "&orderDate=" + DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyyMMdd") + //3.订单生成日期
						 "&AmountNumber=" + payment.getMoney() + //4.订单金额
						 "&notifyType=1" + //5.商户通知类型,赋值（0），交易成功网银通知商户 赋值(1) 商户也可登录网银商户后台管理页面进行清算
						 "&orderCurrencyCode=CNY" + //6.订单币种
						 "&merchantURL=" + URLEncoder.encode(merchantURL, "gb2312") + //7.订单返回的URL
						 "&paymethod=merchant" + //8.支付方式,立即支付 赋值(merchant)，现场支付 赋值(locale)	目前仅支持立即支付
						 "&merchantMac=" + mac; //9.订单MAC
			 //输出页面
			 response.sendRedirect(url);
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 打开企业支付页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToEnterprisePayment(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		Document foxRequest = createFoxRequestDocument(paymentMerchant);
		Element foxElement = foxRequest.getRootElement();

		Element element = foxElement.addElement("SECURITIES_MSGSRQV1").addElement("ORDERTRNRQ");
		element.addElement("TRNUID").setText(Long.toString(payment.getId(), 32)); //订单ID
		
		element = element.addElement("ORDERRQ").addElement("ORDERINFO");
		element.addElement("ORDERDATE").setText(DateTimeUtils.formatTimestamp(payment.getCreated(), "yyyy-MM-dd"));
		element.addElement("ORDERNUM").setText(payment.getApplicationOrderId());
		element.addElement("ORERAMOUNT").setText("" + payment.getMoney());
		element.addElement("TRADEDESC").setText(payment.getPaymentReason()); //支付目的
		element.addElement("VERIFYNAME").setText(paymentMerchant.getParameterValue("paymentVerifyName"));
		element.addElement("VERIFYCODE").setText(paymentMerchant.getParameterValue("paymentVerifyCode"));
		element.addElement("SELLNAME").setText(paymentMerchant.getParameterValue("sellName"));
		element.addElement("BUYERNUM").setText("000000");
		element.addElement("BUYERNAME").setText(paymentMerchant.getParameterValue("buyerName"));
		element.addElement("REMARK").setText(payment.getPaymentReason());
		
		element = element.addElement("ACCTTO");
		element.addAttribute("INTERBANK", paymentMerchant.getParameterValue("interBank"));
		element.addElement("ACCTID").setText(paymentMerchant.getParameterValue("sellAccountId"));
		element.addElement("NAME").setText(paymentMerchant.getParameterValue("sellAccountName"));
		element.addElement("BANKDESC").setText(paymentMerchant.getParameterValue("sellBank"));
		element.addElement("BANKNUM").setText(paymentMerchant.getParameterValue("sellBankNum"));
		element.addElement("CITY").setText(paymentMerchant.getParameterValue("sellBankCity"));
		
		if(Logger.isDebugEnabled()) {
			try {
				Logger.debug(new String(foxRequest.asXML().getBytes("gbk"), "utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Document foxResponse = executeFoxMethod(foxElement.getDocument());
		if(Logger.isDebugEnabled()) {
			try {
				Logger.debug(new String(foxResponse.asXML().getBytes("gbk"), "utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//解析返回值
		foxElement = foxResponse.getRootElement().element("SECURITIES_MSGSRSV1");
		if(foxElement==null) {
			throw new ServiceException("create order failed");
		}
		foxElement = foxElement.element("ORDERTRNRS");
		if(!(Long.toString(payment.getId(), 32)).equals(foxElement.elementText("TRNUID"))) { //核对订单ID 
			throw new ServiceException("create order failed");
		}
		//检查状态
		element = foxElement.element("STATUS");
		if(!"0".equals(element.elementText("CODE"))) { //错误代码：0-没有错误 非0-错误代码，最大5
			throw new ServiceException("create order failed, error is " + element.elementText("MESSAGE"));
		}
		//核对返回值
		element = foxElement.element("ORDERRS").element("ORDERINFO");
		if(!payment.getApplicationOrderId().equals(element.elementText("ORDERNUM"))) { //核对订单编号
			throw new ServiceException("create order failed");
		}
		if(payment.getMoney()!=Double.parseDouble(element.elementText("ORERAMOUNT"))) { //核对订单金额
			throw new ServiceException("create order failed");
		}
		CibEnterprisePaymentOrder enterpriseOrder = new CibEnterprisePaymentOrder();
		enterpriseOrder.setPaymentVerifyName(paymentMerchant.getParameterValue("paymentVerifyName")); //企业创建B2B订单时提醒用户输入的密码的描述
		enterpriseOrder.setPaymentVerifyCode(paymentMerchant.getParameterValue("paymentVerifyCode")); //企业创建B2B订单时用户要输入的密码
		enterpriseOrder.setSellAccountId(paymentMerchant.getParameterValue("sellAccountId")); //卖方帐号
		enterpriseOrder.setSellAccountName(paymentMerchant.getParameterValue("sellAccountName")); //卖方帐户名
		enterpriseOrder.setSellBank(paymentMerchant.getParameterValue("sellBank")); //卖方开户行
		enterpriseOrder.setApplicationOrderId(payment.getApplicationOrderId()); //订单号
		enterpriseOrder.setPaymentReason(payment.getPaymentReason()); //支付原因
		enterpriseOrder.setMoney(payment.getMoney()); //金额
		enterpriseOrder.setEnterpriseBankURL(paymentMerchant.getParameterValue("enterpriseBankURL")); //企业银行
		enterpriseOrder.setPaymentCompleteCallbackURL(paymentCompleteCallbackURL);
		request.setAttribute("record", enterpriseOrder); //给sitePage传递数据
		//输出页面
		try {
			pageService.writePage("jeaf/payment/cibbank", "enterprisePayment", request, response, false);
		}
		catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 创建一个FOX请求XML文档
	 * @return
	 * @throws ServiceException
	 */
	public Document createFoxRequestDocument(PaymentMerchant paymentMerchant) throws ServiceException {
		Document foxRequest = DocumentHelper.createDocument();
		//foxRequest.processingInstruction("xml").setValue("standalone", "yes");
		foxRequest.addProcessingInstruction("FOX", "FOXHEADER=\"100\" VERSION=\"100\" SECURITY=\"NONE\" LANG=CHS");
		Element foxElement = foxRequest.addElement("FOX");
		//增加登录请求SONRQ
		Element element = foxElement.addElement("SIGNONMSGSRQV1").addElement("SONRQ");
		element.addElement("DTCLIENT").setText(DateTimeUtils.formatTimestamp(DateTimeUtils.now(), "yyyy-MM-dd HH:mm:ss"));
		element.addElement("CID").setText(paymentMerchant.getParameterValue("clientId"));
		element.addElement("USERID").setText(paymentMerchant.getParameterValue("clientUserName"));
		element.addElement("USERPASS").setText(paymentMerchant.getParameterValue("clientUserPassword"));
		element.addElement("GENUSERKEY").setText("N");
		element.addElement("LANGUAGE").setText("CHS");
		element.addElement("APPID").setText("YLSOFT");
		element.addElement("APPVER").setText("0100");
		return foxRequest;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPayment(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant)
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException {
		//获取交易记录
		CibbankOrder order = (CibbankOrder)databaseService.findRecordByHql("from CibbankOrder CibbankOrder where CibbankOrder.paymentId=" + payment.getId());
		if(order==null || order.getPaymentSuccess()!='1') { //交易没有成功
			try {
				//重新检查交易情况
				if("个人".equals(payment.getAccountType())) {
					order = queryPersonalOrder(payment, paymentMerchant, order);
				}
				else if("企业".equals(payment.getAccountType())) {
					order = queryEnterpriseOrder(payment, paymentMerchant, order);
				}
			}
			catch(Exception e) {
				Logger.exception(e);
				return null;
			}
		}
		if(order==null || order.getOrderDate()==null) { //当前记录的状态是交易没有完成
			return null;
		}
		PaymentResult result = new PaymentResult();
		result.setTransactTime(order.getOrderDate()); //交易时间
		result.setTransactMoney(order.getTransactAmount()); //实际交易金额
		result.setFee(0); //手续费
		result.setTransactSn(order.getTransactId()); //支付系统流水号
		result.setTransactSuccess(order.getPaymentSuccess()); //是否交易成功
		result.setBankOrderId(order.getOrderNum()); //银行端订单ID
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.payment.paymentmethod.PaymentMethodService#queryPaymentByCallbackURL(com.yuanluesoft.jeaf.payment.pojo.Payment, com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant, java.lang.String)
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException {
		return null; //不需要实现
	}

	/**
	 * 查询个人支付情况
	 * @param payment
	 * @param order
	 * @throws ServiceException
	 */
	private CibbankOrder queryPersonalOrder(Payment payment, PaymentMerchant paymentMerchant, CibbankOrder order) throws ServiceException {
		String ver = DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyyMM") + "OBQ1.0"; //.版本号 ‘200808OBQ1.0’	年月＋ OBQ(order batch query)＋版本号	200808OBQ1.0
		String macContent = paymentMerchant.getParameterValue("merchantId") +
							ver +
						    "1" +
						    Long.toString(payment.getId(), 32) +
						    "9" +
						    "9";
		String mac = DesUtil.genMac(paymentMerchant.getParameterValue("merchantMacKey"), macContent);
		//请求查询交易记录
		String request = paymentMerchant.getParameterValue("paymentQueryURL") +
						 "?merchantId=" + paymentMerchant.getParameterValue("merchantId") + //1.商户编号
						 "&ver=" + ver + //2.版本号 ‘200808OBQ1.0’	年月＋ OBQ(order batch query)＋版本号	200808OBQ1.0
						 "&queryMethod=1" + //3.查询模式,指定订单查询或者是指定时间段查询,1：按订单号查询 2：按时间段查询
						 "&orderNo=" + Long.toString(payment.getId(), 32) + //4.订单号,如果queryMethod =1，则不为空；如果queryMethod =2，则可以为空	
						 "&settleStatus=9" + //5.结算状态, 默认值可赋9, 0：尚未结算 1：全额结算 2：逾期还款 3：预授权 4：解除授权 5：撤销 6：退款 8：部分结算 9：全部情况
						 "&acctype=9" + //一、按时间查：0：借记卡 1: 信用卡 二、 按订单号查：9：借记卡/信用卡
						 "&beginDate=" + //7.起始时间 如果queryMethod =1，则可以为空；如果queryMethod =2，则不可以空 时间格式：(共17位) 20080808 20:00:00（日期与时间之间留一空格）
						 "&endDate=" + //8.截止时间,如果queryMethod =1，则可以为空；如果queryMethod =2，则不可以空.从起始时间往后的时间段 目前不能超过30分钟	时间格式:与起始时间相同,二者时间的跨度必须<=30分钟
						 "&mac=" + mac; //9.校验码MAC,做数字签名，以保证数据的完整性
		String ret;
		try {
			ret = HttpUtils.doPost(request, null, null, null).getResponseBody();
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("CibbankPaymentMethodService query personal order result: " + ret);
		}
		if(ret==null) {
			throw new ServiceException();
		}
		//解析xml文件,获取需要的信息
		SAXReader reader = new SAXReader();
		StringReader strReader = new StringReader(ret);
		Element rootElement = null;
		try {
			rootElement = reader.read(strReader).getRootElement();
		} 
		catch (DocumentException e) {
			throw(new ServiceException("SAXReader Exception"));
		}
		String resultCode = rootElement.elementText("resultCode"); //返回码 4位 0000 查询成功(有数据) 0001 查询无数据 0002 商户校验失败 0003 报文信息不完整 0004 查询时间范围超限 0005 报文mac校验错误 9999 其它错误
		if("0001".equals(resultCode)) { //0001 查询无数据
			return null;
		}
		if(!"0000".equals(resultCode)) {
			throw new ServiceException(resultCode);
		}
		//REC
		String rec = rootElement.elementText("rec").trim();
		//内容
		String content = rootElement.element("body").elementText("content").trim(); //交易日期|网银交易流水号|商户代号|商户订单号|(支付卡类别号)_交易帐号后四位|订单金额|订单状态|银行流水号
		//MAC
		mac = rootElement.elementText("mac").trim();
		
		String[] fieldValues = content.split("\\x7c");
		
		//数字签名校验
		content = resultCode + rec;
		for(int i=0; i<fieldValues.length; i++) {
			content += fieldValues[i].trim();
		}
		if(!DesUtil.checkMac(paymentMerchant.getParameterValue("merchantMacKey"), content, mac)) {
			throw new ServiceException("check mac failed.");
		}
		
		//解析返回的字段
		boolean isNew = (order==null);
		if(isNew) {
			order = new CibbankOrder();
			order.setId(UUIDLongGenerator.generateId());
			order.setPaymentId(payment.getId());
		}
		try {
			order.setOrderDate(DateTimeUtils.parseTimestamp(fieldValues[0], "yyyyMMdd"));
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		//交易日期
		order.setMemo(fieldValues[1]); //网银交易流水号
		order.setToAccountId(fieldValues[2]); //商户代号
		order.setOrderNum(fieldValues[3]); //商户订单号
		order.setFromAccountId(fieldValues[4]); //(支付卡类别号)_交易帐号后四位,其中(支付卡类别号)_交易帐号后四位中，支付卡类别“0”为借记卡，“1”为信用卡
		order.setTransactAmount(Double.parseDouble(fieldValues[5])); //订单金额
		order.setOrderStatus(fieldValues[6]); //订单状态, 企业网银 0-未支付 1-买方处理中 2-支付完成 3-订单撤销 4-订单过期
											  //信用卡 0：支付成功 1：撤销成功 2：退款成功
											  //借记卡 0：尚未结算 1：全额结算 2：逾期还款 3：预授权(仅用于移动支付) 4：解除授权(仅用于移动支付) 5：撤销 6：退款 8：部分结算
		order.setTransactId(fieldValues[7]); //银行流水号
		//检查支付是否成功
		if(fieldValues[4].startsWith("0")) { //借记卡
			//借记卡 0：尚未结算 1：全额结算 2：逾期还款 3：预授权(仅用于移动支付) 4：解除授权(仅用于移动支付) 5：撤销 6：退款 8：部分结算
			order.setPaymentSuccess(",0,1,2,3,4,".indexOf("," + fieldValues[6] + ",")!=-1 ? '1' : '0');
		}
		else if(fieldValues[4].startsWith("1")) { //信用卡
			//信用卡 0：支付成功 1：撤销成功 2：退款成功
			order.setPaymentSuccess(fieldValues[6].equals("0") ? '1' : '0');
		}
		if(isNew) {
			getDatabaseService().saveRecord(order); //保存订单
		}
		else {
			getDatabaseService().updateRecord(order); //更新订单
		}
		return order;
	}
	
	/**
	 * 新的查询接口
	 * @param payment
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	protected CibbankOrder newQueryPersonalOrder(Payment payment, PaymentMerchant paymentMerchant, CibbankOrder order) throws ServiceException {
		/*String ver = DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyyMM") + "OBQ1.0"; //.版本号 ‘200808OBQ1.0’	年月＋ OBQ(order batch query)＋版本号	200808OBQ1.0
		String macContent = merchantId +
							ver +
						    "1" +
						    Long.toString(payment.getId(), 32) +
						    "9" +
						    "9";
		String mac = DesUtil.genMac(merchantMacKey, macContent);*/
		String macContent = "servicecib.netpay.order.get" +
							"ver01" +
						    "mrch_no" + paymentMerchant.getParameterValue("merchantId") +
						    "ord_no" + Long.toString(payment.getId(), 32) +
						    paymentMerchant.getParameterValue("merchantMacKey");
		System.out.println(macContent);
		String mac = md5(macContent);
		//请求查询交易记录
		String request = paymentMerchant.getParameterValue("paymentQueryURL") +
						 "?service=cib.netpay.order.get" + //服务名称 
						 "&ver=01" + //接口版本号，固定01
						 "&mrch_no=" + paymentMerchant.getParameterValue("merchantId") + //商户代号，定长6位
						 "&ord_no=" + Long.toString(payment.getId(), 32) + //订单编号，同一商户号下，所有订单的编号必须唯一
						 "&mac=" + mac; //校验码
						 /*paymentQueryURL +
						 "?merchantId=" + merchantId + //1.商户编号
						 "&ver=" + ver + //2.版本号 ‘200808OBQ1.0’	年月＋ OBQ(order batch query)＋版本号	200808OBQ1.0
						 "&queryMethod=1" + //3.查询模式,指定订单查询或者是指定时间段查询,1：按订单号查询 2：按时间段查询
						 "&orderNo=" + Long.toString(payment.getId(), 32) + //4.订单号,如果queryMethod =1，则不为空；如果queryMethod =2，则可以为空	
						 "&settleStatus=9" + //5.结算状态, 默认值可赋9, 0：尚未结算 1：全额结算 2：逾期还款 3：预授权 4：解除授权 5：撤销 6：退款 8：部分结算 9：全部情况
						 "&acctype=9" + //一、按时间查：0：借记卡 1: 信用卡 二、 按订单号查：9：借记卡/信用卡
						 "&beginDate=" + //7.起始时间 如果queryMethod =1，则可以为空；如果queryMethod =2，则不可以空 时间格式：(共17位) 20080808 20:00:00（日期与时间之间留一空格）
						 "&endDate=" + //8.截止时间,如果queryMethod =1，则可以为空；如果queryMethod =2，则不可以空.从起始时间往后的时间段 目前不能超过30分钟	时间格式:与起始时间相同,二者时间的跨度必须<=30分钟
						 "&mac=" + mac; //9.校验码MAC,做数字签名，以保证数据的完整性*/
		//NameValuePair[] values = new NameValuePair[5];
		/*values[0] = new NameValuePair("service", "cib.netpay.order.get");
		values[1] = new NameValuePair("ver", "01");
		values[2] = new NameValuePair("mrch_no", merchantId);
		values[3] = new NameValuePair("ord_no", Long.toString(payment.getId(), 32));
		values[4] = new NameValuePair("mac", mac);*/
		System.out.println(mac);
		String ret;
		try {
			ret = HttpUtils.doPost(request, null, null, null).getResponseBody();
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("CibbankPaymentMethodService query personal order result: " + ret);
		}
		if(ret==null) {
			throw new ServiceException();
		}
		//解析xml文件,获取需要的信息
		SAXReader reader = new SAXReader();
		StringReader strReader = new StringReader(ret);
		Element rootElement = null;
		try {
			rootElement = reader.read(strReader).getRootElement();
		} 
		catch (DocumentException e) {
			throw(new ServiceException("SAXReader Exception"));
		}
		
		String resultCode = rootElement.elementText("resultCode"); //返回码 4位 0000 查询成功(有数据) 0001 查询无数据 0002 商户校验失败 0003 报文信息不完整 0004 查询时间范围超限 0005 报文mac校验错误 9999 其它错误
		if("0001".equals(resultCode)) {
			return null;
		}
		if(!"0000".equals(resultCode)) {
			throw new ServiceException(resultCode); //4位 0000 查询成功(有数据) 0001 查询无数据 0002 商户校验失败 0003 报文信息不完整 0004 查询时间范围超限 0005 报文mac校验错误 9999 其它错误
		}
		//REC
		String rec = rootElement.elementText("rec").trim();
		//内容
		String content = rootElement.element("body").elementText("content").trim(); //交易日期|网银交易流水号|商户代号|商户订单号|(支付卡类别号)_交易帐号后四位|订单金额|订单状态|银行流水号
		//MAC
		mac = rootElement.elementText("mac").trim();
		
		String[] fieldValues = content.split("\\x7c");
		
		//数字签名校验
		content = resultCode + rec;
		for(int i=0; i<fieldValues.length; i++) {
			content += fieldValues[i].trim();
		}
		if(!DesUtil.checkMac(paymentMerchant.getParameterValue("merchantMacKey"), content, mac)) {
			throw new ServiceException("check mac failed.");
		}
		
		//解析返回的字段
		boolean isNew = (order==null);
		if(isNew) {
			order = new CibbankOrder();
			order.setId(UUIDLongGenerator.generateId());
			order.setPaymentId(payment.getId());
		}
		try {
			order.setOrderDate(DateTimeUtils.parseTimestamp(fieldValues[0], "yyyyMMdd"));
		}
		catch (ParseException e) {
			throw new ServiceException();
		}
		//交易日期
		order.setMemo(fieldValues[1]); //网银交易流水号
		order.setToAccountId(fieldValues[2]); //商户代号
		order.setOrderNum(fieldValues[3]); //商户订单号
		order.setFromAccountId(fieldValues[4]); //(支付卡类别号)_交易帐号后四位,其中(支付卡类别号)_交易帐号后四位中，支付卡类别“0”为借记卡，“1”为信用卡
		order.setTransactAmount(Double.parseDouble(fieldValues[5])); //订单金额
		order.setOrderStatus(fieldValues[6]); //订单状态, 企业网银 0-未支付 1-买方处理中 2-支付完成 3-订单撤销 4-订单过期
											  //信用卡 0：支付成功 1：撤销成功 2：退款成功
											  //借记卡 0：尚未结算 1：全额结算 2：逾期还款 3：预授权(仅用于移动支付) 4：解除授权(仅用于移动支付) 5：撤销 6：退款 8：部分结算
		order.setTransactId(fieldValues[7]); //银行流水号
		//检查支付是否成功
		if(fieldValues[4].startsWith("0")) { //借记卡
			//借记卡 0：尚未结算 1：全额结算 2：逾期还款 3：预授权(仅用于移动支付) 4：解除授权(仅用于移动支付) 5：撤销 6：退款 8：部分结算
			order.setPaymentSuccess(",0,1,2,3,4,".indexOf("," + fieldValues[6] + ",")!=-1 ? '1' : '0');
		}
		else if(fieldValues[4].startsWith("1")) { //信用卡
			//信用卡 0：支付成功 1：撤销成功 2：退款成功
			order.setPaymentSuccess(fieldValues[6].equals("0") ? '1' : '0');
		}
		if(isNew) {
			getDatabaseService().saveRecord(order); //保存订单
		}
		else {
			getDatabaseService().updateRecord(order); //更新订单
		}
		return order;
	}
	
	/**
	 * MD5编码
	 * @param str
	 * @return
	 */
	private String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();
		}
		catch(NoSuchAlgorithmException e) {
			return null;
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 查询企业支付情况
	 * @param payment
	 * @param order
	 * @throws ServiceException
	 */
	private CibbankOrder queryEnterpriseOrder(Payment payment, PaymentMerchant paymentMerchant, CibbankOrder order) throws ServiceException {
		Document foxRequest = createFoxRequestDocument(paymentMerchant);
		Element foxElement = foxRequest.getRootElement();

		Element element = foxElement.addElement("SECURITIES_MSGSRQV1").addElement("ORDERTRNRQ");
		element.addElement("TRNUID").setText(Long.toString(payment.getId(), 32)); //订单ID
		
		if(Logger.isDebugEnabled()) {
			try {
				Logger.debug(new String(foxRequest.asXML().getBytes("gbk"), "utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Document foxResponse = executeFoxMethod(foxElement.getDocument());
		if(Logger.isDebugEnabled()) {
			try {
				Logger.debug(new String(foxResponse.asXML().getBytes("gbk"), "utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//解析返回值
		foxElement = foxResponse.getRootElement().element("SECURITIES_MSGSRSV1");
		if(element==null) {
			throw new ServiceException("order query failed");
		}
		foxElement = foxElement.element("ORDERTRNRS");
		if(!(Long.toString(payment.getId(), 32)).equals(foxElement.elementText("TRNUID"))) { //核对订单ID 
			throw new ServiceException("order query failed");
		}
		//检查状态
		element = foxElement.element("STATUS");
		if(!"0".equals(element.elementText("CODE"))) { //错误代码：0-没有错误 非0-错误代码，最大5
			throw new ServiceException("order query failed, error is " + element.elementText("MESSAGE"));
		}
		element = foxElement.element("ORDERRS").element("ORDERINFO");
		//核对返回值
		if(!payment.getApplicationOrderId().equals(element.elementText("ORDERNUM"))) { //核对订单编号
			throw new ServiceException("order query failed");
		}
		if(payment.getMoney()!=Double.parseDouble(element.elementText("ORERAMOUNT"))) { //核对订单金额
			throw new ServiceException("order query failed");
		}
		String orderStatus = element.elementText("ORDERSTATUS");
		boolean isNew = (order==null);
		if(isNew) {
			order = new CibbankOrder();
			order.setId(UUIDLongGenerator.generateId());
			order.setPaymentId(payment.getId());
		}
		order.setOrderStatus(orderStatus);  //订单状态,0-未支付 1-买方处理中 2-支付完成 3-订单撤销 4-订单过期
		if(!"2".equals(orderStatus)) { //检查订单状态, 企业网银 0-未支付 1-买方处理中 2-支付完成 3-订单撤销 4-订单过期
			if(isNew) {
				getDatabaseService().saveRecord(order); //保存订单
			}
			else {
				getDatabaseService().updateRecord(order); //更新订单
			}
			return order;
		}
		element = element.element("XFERINFO");
		//获取支付情况
		order.setTransactId(Long.toString(payment.getId(), 32)); //交易流水号,最大30
		try {
			order.setOrderDate(DateTimeUtils.parseTimestamp(element.elementText("ORDERDATE"), "yyyy-MM-dd"));
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		//订单日期,格式为：yyyy-MM-dd
		order.setOrderNum(payment.getApplicationOrderId()); //订单号,最长30
		order.setOrderAmount(payment.getMoney()); //订单金额,最长整数位最长15位，小数2位
		order.setPaymentSuccess('1'); //是否成功,由于订单状态在不同情况下解释不同，特此增加本字段
		
		//设置支付人信息
		Element formElement = element.element("ACCTFROM");
		order.setFromAccountId(formElement.elementText("ACCTID")); //支付账户代号,最大30
		order.setFromAccountName(formElement.elementText("NAME")); //支付账户户名,最大50
		order.setFromCity(formElement.elementText("CITY")); //支付人所在城市
		
		//设置收款人信息
		Element toElement = element.element("ACCTTO");
		order.setToAccountId(toElement.elementText("ACCTID")); //收款账号,最大32
		order.setToAccountName(toElement.elementText("NAME")); //收款账户名称,最大50
		order.setToBankName(toElement.elementText("BANKDESC")); //收款人开户行名称,最大50，它行时必回
		//order.setToBankNum(toElement.elementText("")); //收报行号,长度=12
		order.setToCity(toElement.elementText("CITY")); //兑付城市,最大30,它行时必回
		
		order.setChequeNum(element.elementText("CHEQUENUM")); //凭证号,8位
		order.setTransactAmount(Double.parseDouble(element.elementText("TRNAMT"))); //支付金额,整数位最长15位，小数2位
		order.setPurpose(element.elementText("PURPOSE")); //用途,最大30
		order.setMemo(element.elementText("MEMO")); //备注,最大60
		try {
			order.setDueDate(DateTimeUtils.parseTimestamp(element.elementText("DTDUE"), "yyyy-MM-dd")); //结算日期,格式为：yyyy-MM-dd
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		if(isNew) {
			getDatabaseService().saveRecord(order); //保存订单
		}
		else {
			getDatabaseService().updateRecord(order); //更新订单
		}
		return order;
	}
	
	/**
	 * 提交FOX请求
	 * @param foxRequest
	 * @return
	 * @throws ServiceException
	 */
	public Document executeFoxMethod(Document foxRequest) throws ServiceException {
		java.net.HttpURLConnection connection = null;
		InputStream in = null;
		InputStreamReader reader = null;
		OutputStream out = null;
		XMLWriter writer = null;
		try {
			java.net.URL url = new java.net.URL("http://127.0.0.1:8007/online/FOX4Securities");
			connection = (java.net.HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(30000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			try {
				connection.connect();
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			//发送数据
			out = connection.getOutputStream();
			writer = new XMLWriter(out, new OutputFormat("", true, "GB2312"));
			writer.write(foxRequest);
			
			//接收返回值
			in = connection.getInputStream();
			reader = new java.io.InputStreamReader(in , "GB2312");
			//解析XML
			SAXReader xmlReader = new SAXReader();
			return xmlReader.read(reader);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
			try {
				in.close();
			}
			catch(Exception e) {
				
			}
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch(Exception e) {
				
			}
			try {
				connection.disconnect();
			}
			catch(Exception e) {
				
			}
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

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
	
	public static void main(String[] args) throws Exception {
		Payment payment = new Payment();
		payment.setId(70222925226310000l);
		payment.setMoney(200.0f);
		payment.setCreated(DateTimeUtils.now());
		payment.setApplicationOrderId("B20099876543211");
		payment.setPaymentReason("投标保证金（顺昌一中教学楼）");
		payment.setCreated(DateTimeUtils.now());
		//new CibbankPaymentMethodService().newQueryPersonalOrder(payment, new CibbankOrder());
		//new CibbankPaymentMethodService().redirectToPersonalPayment(payment, "/", null);
		//new CibbankPaymentMethodService().queryEnterpriseOrder(payment, new CibbankOrder());
	}
}