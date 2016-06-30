package com.yuanluesoft.jeaf.tools.codetest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.model.Transfer;
import com.yuanluesoft.jeaf.util.StringUtils;


/**
 * 
 * @author linchuan
 *
 */
public class CcbTest {
	
	/*
	 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
	 * java -agentlib:d:\cms_balance\WEB-INF\jeaf\security\Decrypt -classpath C:\Users\linchuan\Desktop\yuanluesoft.jar;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
	 */
	public static void main(String[] args) throws Exception {
		/*String[] orderIds = {"70275609156590000"};
		for(int i=0; i<orderIds.length; i++) {
			new CcbTest().retrieveNetPaymentAccount(orderIds[i]);
			new CcbTest().retrieveNetPaymentAccount_5W1012(orderIds[i]);
			new CcbTest().retrieveNetPaymentAccount_5W1012(orderIds[i]);
		}*/
		
		
		/*new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-26", null), false);
		new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-27", null), false);
		new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-28", null), false);
		new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-29", null), false);
		
		new CcbTest().listMerchantTransactions5W10011(DateTimeUtils.parseDate("2013-7-26", null));
		new CcbTest().listMerchantTransactions5W10011(DateTimeUtils.parseDate("2013-7-27", null));
		new CcbTest().listMerchantTransactions5W10011(DateTimeUtils.parseDate("2013-7-28", null));
		new CcbTest().listMerchantTransactions5W10011(DateTimeUtils.parseDate("2013-7-29", null));
*/
		new CcbTest().retrieveNetPaymentAccount("70304749673650000");
		/*
		new CcbTest().listMerchantTransactions5W10012(DateTimeUtils.parseDate("2013-7-26", null), 0);
		new CcbTest().listMerchantTransactions5W10012(DateTimeUtils.parseDate("2013-7-26", null), 1);
		new CcbTest().listMerchantTransactions5W10012(DateTimeUtils.parseDate("2013-7-27", null), 0);
		new CcbTest().listMerchantTransactions5W10012(DateTimeUtils.parseDate("2013-7-27", null), 1);
		new CcbTest().listMerchantTransactions(DateTimeUtils.parseDate("2013-7-26", null));
		new CcbTest().listMerchantTransactions(DateTimeUtils.parseDate("2013-7-27", null));
		
		
		
		new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-26", null), true);
		new CcbTest().listTransactions(DateTimeUtils.parseDate("2013-7-27", null), true);
		*/
		
		//20271492056500000                       
		//System.out.println(System.currentTimeMillis()); //new Timestamp(271492056500l+1094095800000L));
		/*Date date = DateTimeUtils.parseDate("2013-01-01", null);
		for(int i=0; i<50; i++) {
			Map txInfos = new HashMap();
			txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //起始日期 varChar(8) T
			txInfos.put("STARTHOUR", "00"); //开始小时 varChar(2) T
			txInfos.put("STARTMIN", "59"); //开始分钟 varChar(2) T 
			txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //截止日期 varChar(8) T
			txInfos.put("ENDHOUR", "23"); //结束小时 varChar(2) T
			txInfos.put("ENDMIN", "39"); //结束分钟 varChar(2) T
			txInfos.put("KIND", "0"); //流水类型 Char(1) F 0:未结流水,1:已结流水
			//txInfos.put("ORDER", ""); //订单号   varChar(30) F 按订单号查询时，时间段不起作用
			txInfos.put("ACCOUNT", ""); //结算账户号 varChar(30) T 暂不用
			txInfos.put("DEXCEL", "1"); //文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件
			txInfos.put("MONEY", ""); //金额     Decimal(16,2) T 不支持以金额查询
			txInfos.put("NORDERBY", "1"); //排序     Char(1) F 1:交易日期,2:订单号
			txInfos.put("PAGE", "1"); //当前页次 Int F 
			txInfos.put("POS_CODE", "572600130"); //柜台号   varChar(9) T 
			txInfos.put("STATUS", "3"); //流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部 
			Element xmlElement = new CcbTest().sendEbsRequest(true, "5W1002", txInfos, null, null);
			System.out.println(xmlElement.asXML());
		}*/
	}
	
	/**
	 * 转账
	 * @throws Exception
	 */
	protected void transfer() throws Exception {
		//调用外联平台接口：（6W8040）外联批量付款
		List transfers = new ArrayList();	 
		Transfer transfer = new Transfer();
		transfer.setFromUnit("中国建设银行股份有限公司南平分行"); //汇出单位 文本格式,南平中心为“南平市招标投标服务中心”，各县市不同
		transfer.setFromUnitAcount("35001672433052502058"); //汇出帐号 长数值格式，应生成数字串，不能为科学计数法，南平中心为“35001676107052505220”，各县市不同
		transfer.setFromBankFirstCode("350000000"); //汇出行一级分行 数值格式，不能为科学计数法，南平及所辖各县市均为“350000000”
		transfer.setToUnitAccount("7341610182600066994"); //收款帐号 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
		transfer.setToUnit("福建远略软件技术有限公司"); //收款单位 文本格式 数据从单位网银卸出的流水中提取
		transfer.setToBankFirstCode(""); //收款单位一级分行 数值格式，可空（不是“0”）
		transfer.setToUnitBank("中信银行福州华林支行"); //收款单位开户行 文本格式，必填项，数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
		transfer.setToBankCode(""); //收款单位联行号 数值格式，可空（不是“0”）
		transfer.setToUnitOrgCode(""); //收款单位机构号 数值格式，可空（不是“0”）
		transfer.setMoney(0.01); //金额 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
		transfer.setCurrency("人民币"); //数值格式，为“1”，代表人民币
		transfer.setUses("保证金退还:64"); //用途 文本格式，由系统根据标书名称自行填写
		transfers.add(transfer);
		/*
		transfer = new Transfer();
		transfer.setFromUnit("中国建设银行股份有限公司南平分行"); //汇出单位 文本格式,南平中心为“南平市招标投标服务中心”，各县市不同
		transfer.setFromUnitAcount("35001672433052502058"); //汇出帐号 长数值格式，应生成数字串，不能为科学计数法，南平中心为“35001676107052505220”，各县市不同
		transfer.setFromBankFirstCode("350000000"); //汇出行一级分行 数值格式，不能为科学计数法，南平及所辖各县市均为“350000000”
		transfer.setToUnitAccount("6227001824070294645"); //收款帐号 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
		transfer.setToUnit("江进锋"); //收款单位 文本格式 数据从单位网银卸出的流水中提取
		transfer.setToBankFirstCode(""); //收款单位一级分行 数值格式，可空（不是“0”）
		transfer.setToUnitBank("中信建设银行福州东街支行"); //收款单位开户行 文本格式，必填项，数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
		transfer.setToBankCode(""); //收款单位联行号 数值格式，可空（不是“0”）
		transfer.setToUnitOrgCode(""); //收款单位机构号 数值格式，可空（不是“0”）
		transfer.setMoney(0.01); //金额 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
		transfer.setCurrency("人民币"); //数值格式，为“1”，代表人民币
		transfer.setUses("保证金退还个人测试:64"); //用途 文本格式，由系统根据标书名称自行填写
		
		transfers.add(transfer);*/
		
		double amount = 0;
		for(int i=0; i<(transfers==null ? 0 : transfers.size()); i++) {
			transfer = (Transfer)transfers.get(i);
			amount += transfer.getMoney();
		}
		LinkedHashMap txInfos = new LinkedHashMap();
		txInfos.put("AMOUNT", "" + amount); //AMOUNT 总金额 Decimal(16,2) T 如有值，则校验与文件中累加金额是否相符 
		txInfos.put("COUNT", ""); //COUNT 总笔数 Decimal(7,0) T 如有值，则校验与文件中总笔数是否相符  + transfers.size()
		txInfos.put("CHK_RECVNAME", "1"); //CHK_RECVNAME 户名校验 CHAR(1) T 行内转账收款账户户名校验 1:校验 0：不校验 
		txInfos.put("FILE_CTX", generateTransferText(transfers)); //FILE_CTX 付款文件内容 varChar F 说明见下 
		System.out.println(generateTransferText(transfers));
		Element xmlElement = sendEbsRequest(false, "6W8040", txInfos, null, null);
		System.out.println(xmlElement.asXML());
	}
	
	/**
	 * 执行EBS请求
	 * @param isMerchant
	 * @param txCode
	 * @param txInfos
	 * @param signInfo
	 * @param signCert
	 * @param paymentMerchant
	 * @return
	 * @throws Exception
	 */
	protected Element sendEbsRequest(boolean isMerchant, String txCode, Map txInfos, String signInfo, String signCert) throws Exception {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		Socket socket = null;
		try {
			socket = new Socket();
			String ip = isMerchant ? "192.168.0.8" : "127.0.0.1";
			int port = isMerchant ? 12345 : 12346;
			socket.connect(new InetSocketAddress(ip, port), 8000); //连接超时8秒
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" + 
								"	<REQUEST_SN>" + System.currentTimeMillis() + "</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								"	<CUST_ID>" + (isMerchant ? "105350773990001" : "FJ350700199999#0Q") + "</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 FJ135070000425#0P 
								"	<USER_ID>" + (isMerchant ? "135070000425-002" : "WLPT01") + "</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>" + (isMerchant ? "222222" : "8834679") + "</PASSWORD>" + //密码 varChar(32) F 操作员密码  999999
								"	<TX_CODE>" + txCode + "</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN
								"	<TX_INFO>";
			for(Iterator iterator = txInfos.keySet().iterator(); iterator.hasNext();) {
				String propertyName = (String)iterator.next();
				requestXml += 	"		<" + propertyName + ">" + txInfos.get(propertyName) + "</" + propertyName + ">";
			}
			requestXml += 		"	</TX_INFO>";
			if(signInfo!=null) {
				requestXml += "		<SIGN_INFO>" + signInfo + "</SIGN_INFO>" + //签名信息 varChar(254) T
							  "		<SIGNCERT>" + signCert + "</SIGNCERT>"; //签名CA信息 varChar(254) T ,客户采用socket连接时，建行客户端自动添加
			}
			requestXml += 		"</TX>";
			System.out.println(requestXml);
			outputStream.write(requestXml.getBytes("gb2312"));
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
			SAXReader reader = new SAXReader();
			return reader.read(inputStream).getRootElement();
		}
		finally {
			try {
				outputStream.close();
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
	

	public List listMerchantTransactions5W10011(Date day) throws Exception {
		sendEbsRequest(true, "5W1001", new LinkedHashMap(), null, null);
		
		
		//调用外联平台接口：（5W1011）招投标商户线下转账订单查询					
		List transactions = new ArrayList();
		for(int i=1; ; i++) {
			LinkedHashMap txInfos = new LinkedHashMap();
			
			/*
			 * 7	CUST_ON	商户的企业网银签约客户号	varChar(21)	非空		
	
9	START    	起始日期	varChar(8)	非空	YYYYMMDD	
10	END      	截止日期	varChar(8)	非空	YYYYMMDD	
12	PAGE     	当前页次	Int	非空		
15	OTHER_ACCOUNT	对方账户账号	varChar(32)	可空	对方来帐户	
13	MONEY    	金额    	Decimal(16,2)	可空		
14	ORDER    	摘要	varChar(40)	可空	此处填写订单号	
17	INDEX_STRING	定位串	varChar(40)	可空	从返回报文里获取，如果不分页，可传空。第一次查询为空。	

			 */
			
			txInfos.put("CUST_ON", "FJ350700199999#0Q"); //7	CUST_ON	商户的企业网银签约客户号	varChar(21)	非空
			txInfos.put("ACCOUNT", "35001672433052502058"); //8	ACCOUNT  	商户保证金结算账户号	varChar(32)	非空			
			txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(day)); //9	START    	起始日期	varChar(8)	非空	YYYYMMDD	 　 
			txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(day)); //10	END      	截止日期	varChar(8)	非空	YYYYMMDD 　 
			txInfos.put("PAGE", "" + i); //12	PAGE     	当前页次	Int	非空		
			txInfos.put("OTHER_ACCOUNT", ""); //15 OTHER_ACCOUNT	对方账户账号	varChar(32)	可空	对方来帐户	
			txInfos.put("MONEY", ""); //13	MONEY    	金额    	Decimal(16,2)	可空 
			txInfos.put("ORDER", ""); //14	ORDER    	摘要	varChar(40)	可空	此处填写订单号
			txInfos.put("INDEX_STRING", ""); //17	INDEX_STRING	定位串	varChar(40)	可空	从返回报文里获取，如果不分页，可传空。第一次查询为空。 
			Element xmlElement = sendEbsRequest(true, "5W1011", txInfos, null, null);
			System.out.println("Ccbbank EBS 5W1011 response: " + xmlElement.asXML());
			break;
		}
		return transactions;
	}
	
	
	
	public List listMerchantTransactions5W10012(Date day, int kind) throws Exception {
		sendEbsRequest(true, "5W1001", new LinkedHashMap(), null, null);
		
		
		//调用外联平台接口：（5W1012）商户流水查询(招投标专用)					
		List transactions = new ArrayList();
		for(int i=1; ; i++) {
			LinkedHashMap txInfos = new LinkedHashMap();
			txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(day)); //7 START     起始日期 varChar(8) T 　 
			txInfos.put("STARTHOUR", "00"); //8 STARTHOUR 开始小时 varChar(2) T 　 
			txInfos.put("STARTMIN", "00"); //9 STARTMIN  开始分钟 varChar(2) T 　 
			txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(day)); //10 END       截止日期 varChar(8) T 　 
			txInfos.put("ENDHOUR", "23"); //11 ENDHOUR   结束小时 varChar(2) T 　 
			txInfos.put("ENDMIN", "59"); //12 ENDMIN    结束分钟 varChar(2) T 　 
			txInfos.put("KIND", kind + ""); //13 KIND      流水类型 Char(1) F 0:未结流水,1:已结流水 
			txInfos.put("ORDER", ""); //14 ORDER     订单号   varChar(30) F 按订单号查询时，时间段不起作用  70289174828000000
			txInfos.put("ACCOUNT", ""); //15 ACCOUNT   结算账户号 varChar(30) T 暂不用 
			txInfos.put("DEXCEL", "1"); //16 DEXCEL    文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件 
			txInfos.put("MONEY", ""); //17 MONEY     金额     Decimal(16,2) T 不支持以金额查询 
			txInfos.put("NORDERBY", "1"); //18 NORDERBY  排序     Char(1) F 1:交易日期,2:订单号 
			txInfos.put("PAGE", "" + i); //19 PAGE      当前页次 Int F 　 
			txInfos.put("POS_CODE", ""); //20 POS_CODE  柜台号   varChar(9) T 　 
			txInfos.put("STATUS", "3"); //21 STATUS    流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部
			txInfos.put("PAY_METHOD", "1"); //23	PAY_METHOD	支付方式	Char(1)	非空	0-在线支付 1-线下支付 2-全部
			Element xmlElement = sendEbsRequest(true, "5W1012", txInfos, null, null);
			System.out.println("Ccbbank EBS 5W1012 response: " + xmlElement.asXML());
			break;
			/*String returnCode = xmlElement.elementText("RETURN_CODE");
			if(!"000000".equals(returnCode)) {
				throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
			}
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
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(xmlDetail.elementText("TRAN_DATE") + " " + xmlDetail.elementText("TRAN_TIME"), "yyyy/MM/dd HH:mm:ss"));
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
				transaction.setRemark(individual1==null ? individual2 : individual1 + (individual2==null ? "" : individual2)); //备注 INDIVIDUAL1 自定义输出内容1 varChar(99) T  INDIVIDUAL2 自定义输出内容2 varChar(99) T 
				transaction.setTransactionNumber(xmlDetail.elementText("PAY_SEQ_NUM")); //PAY_SEQ_NUM 企业流水号 varChar(65) T 
				transaction.setMyAccount(myAccount); //本方账号
				transaction.setMyAccountName(myAccountName); //本方账户名称
				transaction.setMyBank(myBank); //本方账户开户机构
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}*/
		}
		/*System.out.println(transactions);
		for(Iterator iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction)iterator.next();
			System.out.println(transaction.getTransactionTime() + "," + transaction.getMoney() + "," + transaction.getRemark());
		}*/
		return transactions;
	}
	

	public List listMerchantTransactions(Date day) throws Exception {
		//调用外联平台接口：（5W1002）历史明细查询交易
		List transactions = new ArrayList();
		for(int i=1; ; i++) {
			LinkedHashMap txInfos = new LinkedHashMap();
			txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(day)); //7 START     起始日期 varChar(8) T 　 
			txInfos.put("STARTHOUR", "00"); //8 STARTHOUR 开始小时 varChar(2) T 　 
			txInfos.put("STARTMIN", "00"); //9 STARTMIN  开始分钟 varChar(2) T 　 
			txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(day)); //10 END       截止日期 varChar(8) T 　 
			txInfos.put("ENDHOUR", "23"); //11 ENDHOUR   结束小时 varChar(2) T 　 
			txInfos.put("ENDMIN", "59"); //12 ENDMIN    结束分钟 varChar(2) T 　 
			txInfos.put("KIND", "0"); //13 KIND      流水类型 Char(1) F 0:未结流水,1:已结流水 
			txInfos.put("ORDER", ""); //14 ORDER     订单号   varChar(30) F 按订单号查询时，时间段不起作用 
			txInfos.put("ACCOUNT", ""); //15 ACCOUNT   结算账户号 varChar(30) T 暂不用 
			txInfos.put("DEXCEL", "1"); //16 DEXCEL    文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件 
			txInfos.put("MONEY", ""); //17 MONEY     金额     Decimal(16,2) T 不支持以金额查询 
			txInfos.put("NORDERBY", "1"); //18 NORDERBY  排序     Char(1) F 1:交易日期,2:订单号 
			txInfos.put("PAGE", "" + i); //19 PAGE      当前页次 Int F 　 
			txInfos.put("POS_CODE", ""); //20 POS_CODE  柜台号   varChar(9) T 　 
			txInfos.put("STATUS", "3"); //21 STATUS    流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部 
			Element xmlElement = sendEbsRequest(true, "5W1002", txInfos, null, null);
			System.out.println("Ccbbank EBS 5W1002 response: " + xmlElement.asXML());
			break;
			/*String returnCode = xmlElement.elementText("RETURN_CODE");
			if(!"000000".equals(returnCode)) {
				throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
			}
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
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(xmlDetail.elementText("TRAN_DATE") + " " + xmlDetail.elementText("TRAN_TIME"), "yyyy/MM/dd HH:mm:ss"));
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
				transaction.setRemark(individual1==null ? individual2 : individual1 + (individual2==null ? "" : individual2)); //备注 INDIVIDUAL1 自定义输出内容1 varChar(99) T  INDIVIDUAL2 自定义输出内容2 varChar(99) T 
				transaction.setTransactionNumber(xmlDetail.elementText("PAY_SEQ_NUM")); //PAY_SEQ_NUM 企业流水号 varChar(65) T 
				transaction.setMyAccount(myAccount); //本方账号
				transaction.setMyAccountName(myAccountName); //本方账户名称
				transaction.setMyBank(myBank); //本方账户开户机构
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}*/
		}
		/*System.out.println(transactions);
		for(Iterator iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction)iterator.next();
			System.out.println(transaction.getTransactionTime() + "," + transaction.getMoney() + "," + transaction.getRemark());
		}*/
		return transactions;
	}
	
	
	
	public List listTransactions(Date day, boolean isMerchant) throws Exception {
		//调用外联平台接口：（6W0300）历史明细查询交易
		List transactions = new ArrayList();
		for(int i=1; ; i++) {
			LinkedHashMap txInfos = new LinkedHashMap();
			txInfos.put("ACC_NO", "35001672433052502058"); //帐号 varChar(32) F 查询帐号 35001676107052505220 
			txInfos.put("START_DATE", new SimpleDateFormat("yyyyMMdd").format(day)); //起始日期 YYYYMMDD F 查询起始日期（Char） 
			txInfos.put("END_DATE", new SimpleDateFormat("yyyyMMdd").format(day)); //结束日期 YYYYMMDD F 查询截至日期（Char） 
			txInfos.put("START_PAGE", "" + i); //起始页次 Int F 整数>0
			txInfos.put("POSTSTR", ""); //查询定位串(252定位串) varChar(40) T 分行可选，对应于必输分行则必须输入 
			txInfos.put("CONDITION1", ""); //备注1 varChar(32) T 备注1  NPZTB
			txInfos.put("CONDITION2", ""); //备注2 varChar(32) T 备注2
			Element xmlElement = sendEbsRequest(isMerchant, "6W0300", txInfos, null, null);
			System.out.println("Ccbbank EBS 6W0300 response: " + xmlElement.asXML());
			break;
			/*String returnCode = xmlElement.elementText("RETURN_CODE");
			if(!"000000".equals(returnCode)) {
				throw new ServiceException(xmlElement.elementText("RETURN_MSG"));
			}
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
					transaction.setTransactionTime(DateTimeUtils.parseTimestamp(xmlDetail.elementText("TRAN_DATE") + " " + xmlDetail.elementText("TRAN_TIME"), "yyyy/MM/dd HH:mm:ss"));
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
				transaction.setRemark(individual1==null ? individual2 : individual1 + (individual2==null ? "" : individual2)); //备注 INDIVIDUAL1 自定义输出内容1 varChar(99) T  INDIVIDUAL2 自定义输出内容2 varChar(99) T 
				transaction.setTransactionNumber(xmlDetail.elementText("PAY_SEQ_NUM")); //PAY_SEQ_NUM 企业流水号 varChar(65) T 
				transaction.setMyAccount(myAccount); //本方账号
				transaction.setMyAccountName(myAccountName); //本方账户名称
				transaction.setMyBank(myBank); //本方账户开户机构
				transactions.add(transaction);
			}
			if(curPage>=pageCount) { //已经是最后一页
				break;
			}*/
		}
		/*System.out.println(transactions);
		for(Iterator iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction)iterator.next();
			System.out.println(transaction.getTransactionTime() + "," + transaction.getMoney() + "," + transaction.getRemark());
		}*/
		return transactions;
	}
	
	/**
	 * （6W0300）历史明细查询交易
	 * @throws Exception
	 */
	public void listQuery() throws Exception {
		/*
		 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
		 */
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 12346), 5000);
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" + 
								"	<REQUEST_SN>1</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								//"	<CUST_ID>FJ135070000425#0P</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								//"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								//"	<PASSWORD>999999</PASSWORD>" + //密码 varChar(32) F 操作员密码 
								"	<CUST_ID>FJ350700199999#0Q</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>8834679</PASSWORD>" + //密码 varChar(32) F 操作员密码
								"	<TX_CODE>6W0300</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN 
								"	<TX_INFO>" +
								//"  		<ACC_NO>35001676107052505220</ACC_NO>" +  //帐号 varChar(32) F 查询帐号
								"  		<ACC_NO>35001672433052502058</ACC_NO>" +  //帐号 varChar(32) F 查询帐号
								"		<START_DATE>20130501</START_DATE>" + //起始日期 YYYYMMDD F 查询起始日期（Char） 
								"		<END_DATE>20130508</END_DATE>" + //结束日期 YYYYMMDD F 查询截至日期（Char） 
								"		<START_PAGE>1</START_PAGE>" + //起始页次 Int F 整数>0 
								"		<POSTSTR></POSTSTR>" +  //查询定位串(252定位串) varChar(40) T 分行可选，对应于必输分行则必须输入 
								"		<CONDITION1>npztb</CONDITION1>" + //备注1 varChar(32) T 备注1  NPZTB
								"		<CONDITION2></CONDITION2>" + //备注2 varChar(32) T 备注2 
								"	</TX_INFO>" +
								"</TX>";
			outputStream.write(requestXml.getBytes("gb2312"));
			outputStream.flush();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			//SAXReader reader = new SAXReader();
			//reader.read(inputStream).getRootElement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
		}
 	}
	
	/**
	 * （6WA004）明细账查询交易
	 * @throws Exception
	 */
	public void detailQuery() throws Exception {
		/*
		 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
		 */
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 10018), 5000);
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" +  
								"	<REQUEST_SN>1</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								"	<CUST_ID>FJ135070000425#0P</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>999999</PASSWORD>" + //密码 varChar(32) F 操作员密码 
								"	<TX_CODE>6WA004</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN 
								"	<TX_INFO>" +
								"  		<ACCT_NO>35001676107052505220</ACCT_NO>" + //账号 varChar(32) F 
								"  		<BILL_NO></BILL_NO>" + //对账单编号 varChar(19) T 
								"  		<BEGIN_DATE>20120701</BEGIN_DATE>" +  //开始日期 varChar(8) F yyddmm 
								"  		<END_DATE>20120731</END_DATE>" + //结束日期 varChar(8) F yyddmm 
								"  		<PAGE_NO>1</PAGE_NO>" + //当前页次 NUMBER T 不输入则默认查询第一页 
								"	</TX_INFO>" +
								"</TX>";
			outputStream.write(requestXml.getBytes("gb2312"));
			outputStream.flush();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			//SAXReader reader = new SAXReader();
			//reader.read(inputStream).getRootElement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
		}
 	}
	
	
	/**
	 * （6W1101）账号、户名是否匹配校验交易
	 * @throws Exception
	 */
	public void validate() throws Exception {
		/*
		 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
		 */
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 12346), 5000);
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" +  
								"	<REQUEST_SN>1</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								//"	<CUST_ID>FJ135070000425#0P</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								//"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								//"	<PASSWORD>999999</PASSWORD>" + //密码 varChar(32) F 操作员密码 
								"	<CUST_ID>FJ350700199999#0Q</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>8834679</PASSWORD>" + //密码 varChar(32) F 操作员密码
								"	<TX_CODE>6W1101</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN 
								"	<TX_INFO>" +
								"  		<ACC_NO>35001672433052502058</ACC_NO>" +  //帐号 varChar(32) F 查询帐号
								"  		<ACC_NAME>中国建设银行股份有限公司南平分行</ACC_NAME>" +  //账户名称 varChar(60) F 账户名称 
								"	</TX_INFO>" +
								"</TX>";
			outputStream.write(requestXml.getBytes("gb2312"));
			outputStream.flush();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			//SAXReader reader = new SAXReader();
			//reader.read(inputStream).getRootElement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
		}
 	}
	
	/**
	 * （6W0100）余额查询交易
	 * @throws Exception
	 */
	public void balanceQuery() throws Exception {
		/*
		 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
		 */
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 12346), 5000);
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" +  
								"	<REQUEST_SN>1</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								//"	<CUST_ID>FJ135070000425#0P</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								//"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								//"	<PASSWORD>999999</PASSWORD>" + //密码 varChar(32) F 操作员密码 
								"	<CUST_ID>FJ350700199999#0Q</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>WLPT01</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>8834679</PASSWORD>" + //密码 varChar(32) F 操作员密码
								"	<TX_CODE>6W0100</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN 
								"	<TX_INFO>" +
								"  		<ACC_NO>35001672433052502058</ACC_NO>" +  //帐号 varChar(32) F 查询帐号 
								"	</TX_INFO>" +
								"</TX>";
			outputStream.write(requestXml.getBytes("gb2312"));
			outputStream.flush();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			//SAXReader reader = new SAXReader();
			//reader.read(inputStream).getRootElement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
		}
 	}
	
	/**
	 * （5W1001）B2C外联平台启动连接交易 
	 *
	 */
	public void netPaymentConnect() {
		/*
		 * java -classpath D:\npztb\WEB-INF\classes;D:\npztb\WEB-INF\lib\dom4j-1.5.2.jar com.yuanluesoft.jeaf.tools.codetest.CcbTest
		 */
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("127.0.0.1", 10019), 5000);
			socket.setSoTimeout(30000); //30s
			socket.setReceiveBufferSize(8192);
			socket.setSendBufferSize(8192);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			String requestXml = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>" +  
								"<TX>" +  
								"	<REQUEST_SN>2</REQUEST_SN>" + //请求序列号 varChar(16) F 只可以使用数字 
								"	<CUST_ID>105350793990009</CUST_ID>" + //客户号 varChar(21) F 字符型char，网银客户号 
								"	<USER_ID>69190644101-001</USER_ID>" + //操作员号 varChar(6) F 20051210后必须使用 
								"	<PASSWORD>111111</PASSWORD>" + //密码 varChar(32) F 操作员密码
								"	<TX_CODE>5W1001</TX_CODE>" + //交易码 varChar(6) F 交易请求码 
								"	<LANGUAGE>CN</LANGUAGE>" + //语言 varChar(2) F CN 
								"	<TX_INFO>" +
								"		<REM1/>" + //备注1 varChar(32) T 
								"		<REM2/>" +  //备注2 varChar(32) T
								"	</TX_INFO>" +
								"</TX>";
			
			outputStream.write(requestXml.getBytes("gb2312"));
			outputStream.flush();
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8096];
			int len;
			while((len=inputStream.read(buffer))!=-1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			System.out.println(byteArrayOutputStream.toString());
			//SAXReader reader = new SAXReader();
			//reader.read(inputStream).getRootElement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
			try {
				outputStream.close();
			} 
			catch (Exception e) {
				
			}
		}
 	}
	
	/**
	 * （5W1002）商户支付流水查询,返回付款人帐号
	 * @param orderId
	 * @param paymentMerchant
	 * @return
	 * @throws Exception
	 */
	protected String retrieveNetPaymentAccount(String orderId) throws Exception {
		LinkedHashMap txInfos = new LinkedHashMap();
		//txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //起始日期 varChar(8) T
		//txInfos.put("STARTHOUR", "00"); //开始小时 varChar(2) T
		//txInfos.put("STARTMIN", "59"); //开始分钟 varChar(2) T 
		//txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //截止日期 varChar(8) T
		//txInfos.put("ENDHOUR", "23"); //结束小时 varChar(2) T
		//txInfos.put("ENDMIN", "39"); //结束分钟 varChar(2) T
		txInfos.put("KIND", "1"); //流水类型 Char(1) F 0:未结流水,1:已结流水
		txInfos.put("ORDER", orderId); //订单号   varChar(30) F 按订单号查询时，时间段不起作用
		txInfos.put("ACCOUNT", ""); //结算账户号 varChar(30) T 暂不用
		txInfos.put("DEXCEL", "1"); //文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件
		txInfos.put("MONEY", ""); //金额     Decimal(16,2) T 不支持以金额查询
		txInfos.put("NORDERBY", "1"); //排序     Char(1) F 1:交易日期,2:订单号
		txInfos.put("PAGE", "1"); //当前页次 Int F 
		txInfos.put("POS_CODE", "572600130"); //柜台号   varChar(9) T  047607083 572600130 
		txInfos.put("STATUS", "3"); //流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部 
		txInfos.put("PROJECT_NO", orderId); //项目编号	varChar(60)	非空	招投标商户特有字段	
		txInfos.put("PAY_METHOD", "2"); //支付方式	Char(1)	非空	0-在线支付 1-线下支付 2-全部
		
		Element xmlElement = sendEbsRequest(true, "5W1012", txInfos, null, null);
		System.out.println("Ccbbank EBS 5W1012 response: " + xmlElement.asXML());
		return null;
 	}
	
	/**
	 * （5W1012）商户流水查询(招投标专用),返回付款人帐号
	 * @param orderId
	 * @param paymentMerchant
	 * @return
	 * @throws Exception
	 */
	protected void retrieveNetPaymentAccount_5W1012(String orderId) throws Exception {
		LinkedHashMap txInfos = new LinkedHashMap();
		//txInfos.put("START", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //起始日期 varChar(8) T
		//txInfos.put("STARTHOUR", "00"); //开始小时 varChar(2) T
		//txInfos.put("STARTMIN", "59"); //开始分钟 varChar(2) T 
		//txInfos.put("END", new SimpleDateFormat("yyyyMMdd").format(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, i))); //截止日期 varChar(8) T
		//txInfos.put("ENDHOUR", "23"); //结束小时 varChar(2) T
		//txInfos.put("ENDMIN", "39"); //结束分钟 varChar(2) T
		txInfos.put("KIND", "1"); //流水类型 Char(1) F 0:未结流水,1:已结流水
		txInfos.put("ORDER", orderId); //订单号   varChar(30) F 按订单号查询时，时间段不起作用
		txInfos.put("ACCOUNT", ""); //结算账户号 varChar(30) T 暂不用
		txInfos.put("DEXCEL", "1"); //文件类型 Char(1) F 默认为“1”，1:不压缩,2.压缩成zip文件
		txInfos.put("MONEY", ""); //金额     Decimal(16,2) T 不支持以金额查询
		txInfos.put("NORDERBY", "1"); //排序     Char(1) F 1:交易日期,2:订单号
		txInfos.put("PAGE", "1"); //当前页次 Int F 
		txInfos.put("POS_CODE", "047607083"); //柜台号   varChar(9) T 
		txInfos.put("STATUS", "3"); //流水状态 Char(1) F 0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部 
		txInfos.put("PROJECT_NO", orderId); //项目编号	varChar(60)	非空	招投标商户特有字段	
		txInfos.put("PAY_METHOD", "2"); //支付方式	Char(1)	非空	0-在线支付 1-线下支付 2-全部
		Element xmlElement = sendEbsRequest(true, "5W1012", txInfos, null, null);
		System.out.println("Ccbbank EBS 5W1012 response: " + xmlElement.asXML());
		String returnCode = xmlElement.elementText("RETURN_CODE");
		if(!"000000".equals(returnCode)) {
			return;
		}
	}	

	/**
	 * 生成转账文本
	 * @param transfers
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	private String generateTransferText(List transfers) throws ServiceException {
		String ccbTransfer = "";
		for(int i=0; i<(transfers==null ? 0 : transfers.size()); i++) {
			Transfer transfer = (Transfer)transfers.get(i);
			ccbTransfer += StringUtils.formatNumber(i+1, 3, false) + "|" + //字段一：序号 文本格式，有前导“0” 从001、002、003……按顺序编排
						   transfer.getFromUnit() + "|" + //字段二：汇出单位 文本格式 南平中心为“南平市招标投标服务中心”，各县市不同
						   transfer.getFromUnitAcount() + "|" + //字段三：汇出帐号 长数值格式，应生成数字串，不能为科学计数法 南平中心为“35001676107052505220”，各县市不同
						   "350000000|" + //字段四：汇出行一级分行 数值格式，不能为科学计数法 南平及所辖各县市均为“350000000”
						   transfer.getToUnitAccount() + "|" + //字段五：收款帐号 长数值格式，应生成数字串，不能为科学计数法 数据从单位网银卸出的流水中提取
						   transfer.getToUnit() + "|" + //字段六：收款单位 文本格式 数据从单位网银卸出的流水中提取
						   (transfer.getToBankFirstCode()==null ? "" : transfer.getToBankFirstCode()) + "|" + //字段七：收款单位一级分行 数值格式，可空（不是“0”）
						   transfer.getToUnitBank() + "|" + //字段八：收款单位开户行 文本格式，必填项 数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
						   (transfer.getToBankCode()==null ? "" : transfer.getToBankCode()) + "|" + //字段九：收款单位联行号 数值格式，可空（不是“0”）
						   (transfer.getToUnitOrgCode()==null ? "" : transfer.getToUnitOrgCode()) + "|" + //字段十：收款单位机构号 数值格式，可空（不是“0”）
						   (transfer.getToUnitBank().indexOf("建行")!=-1 || transfer.getToUnitBank().indexOf("建设银行")!=-1 ? "1" : "0") + "|" + //字段十一：行内外 数值格式，客户是建行账户填“1”，其它银行填“0”
						   StringUtils.format(new Double(transfer.getMoney()), "#.##", null) + "|" + //字段十二：金额 长数值格式，应生成数字串，不能为科学计数法 数据从单位网银卸出的流水中提取
						   (transfer.getCurrency().equals("人民币") ? "1" : "0") + "|" + //字段十三：币种 数值格式，为“1”，代表人民币
						   transfer.getUses() + "\n"; //字段十四：用途 文本格式 由系统根据标书名称自行填写
		}
		return ccbTransfer;
	}
}