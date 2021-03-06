package com.yuanluesoft.jeaf.sms.client.mas;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.HttpResponse;

/**
 * 
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	//应用平台:统一信息公开平台,集团编号:5916000024,付费号码:13860847058,端口号:106573060012483,企业代码:467831,服务密码:A138138@aa
	//平台管理地址 http://112.5.183.34:8060/  账号：yuanlue  密码：yl0789 cpKey：E2E662AFFBDF61DBEB722160A222097C
	private String serverUrl = "http://112.5.183.34:8060";
	private String userID = "4";
	private String cpKey = "E2E662AFFBDF61DBEB722160A222097C";
	private String masSign = ""; //"【纪委】"; //签名,诏安纠风办(10657306696004),纪委
	private String apiCode; //"jfb"; //通道编号,纠风办jfb,纪委jw
	private Cache smsCache; //短信缓存,用来处理接收到的短信和短信到达通知
	
	//私有属性
	private Object receiveMutex = new Object();

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		try {
			if(apiCode==null) {
				return null;
			}
			long smsId = UUIDLongGenerator.generateId();
			String extID = null; //扩展号码, 扩展号码+端口号(106573060012483)不能超过21位, 纪委目前能使用的是6位
			String smsNumber = getSmsNumber();
			if(senderNumber!=null && smsNumber!=null && senderNumber.startsWith(smsNumber)) {
				extID = senderNumber.substring(smsNumber.length());
			}
			NameValuePair[] values = {
				new NameValuePair("userID", userID), //userID	用户编号	是	int	SP提供，登录平台获取
				new NameValuePair("cpKey", cpKey), //cpKey	用户密钥	是	35	SP提供，登录平台获取（可修改）
				new NameValuePair("uPhone", ListUtils.join(recevierNumbers, ",", true)), //uPhone	手机号码	是	text	1)	多个号码以半角逗号“,”隔开 2) 单次提交限1万个号码
				new NameValuePair("content", message), //content	发送内容	是	1000	1)	Utf-8编码 2)	HttpEncode编码转换
				new NameValuePair("extID", extID), //extID	扩展号码	否	3	CP扩展长号码，最长3位
				new NameValuePair("cpSubID", "" + smsId), //cpSubID	任务编号	否	40	1)	此参数由CP自行定义,由数字或字母组成，每次生成保证唯一值。2)	无需同步状态报告，此参数填空  3)	提交单个号码，根据cpSubID作匹配。4)	提交多个号码，应记录每个手机号码的序列号，详情请阅读《状态报告接口》
				new NameValuePair("apiCode", apiCode), //apiCode 通道编号	否	10	默认为空，系统默认选择网关通道 区分多个通道发送，从平台获取编号
				new NameValuePair("pushTime", receiveTime==null ? null : DateTimeUtils.formatTimestamp(receiveTime, "yyyy-MM-dd HH:mm:ss")) //pushTime	发送时间	否	datetime	1)	为空时，立即发送(以系统时间为准) 2)	定时发送，填写时间格式：yyyy-mm-dd hh:mm:ss  3)	Utf-8编码  4)	HttpEncode编码转换
			};
			/*
			  	0：提交成功
				8301：userID为空
				8302：uPhone手机号码为空
				8303：content发送内容为空
				8201：用户通道未分配
				8202：通道异常或暂停
				8203：发送内容(content)字数超出限制
				8204：用户ID(userID)不存在
				8205：发送内容含有非法字符
				8206：账号已被锁定
				8207：cpKey验证失败
				8208：短信条数不足
				8209：提交号码(uPhone)个数超出限制
				8210：通道暂停，网关无法连接
				8211：通道异常，欠费、未免白等原因
				9999：其它原因
			 */
			HttpResponse response = HttpUtils.doPost(serverUrl + "/protFace/subSmsApi.aspx", "utf-8", values, null); //旧地址：/protFace/Api_Sub.aspx
			if(!"0".equals(response.getResponseBody())) {
				throw new ServiceException(response.getResponseBody());
			}
			return "" + smsId;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		try {
			//从缓存中获取短信接收记录
			String key = getSmsNumber() + "_arrive_" + messageId + "_" + recevierNumber;
			HashMap values = (HashMap)smsCache.get(key);
			if(values!=null) { //缓存中有记录
				smsCache.remove(key); //清除记录
				if("0".equals(values.get("rptCode"))) {
					return (Timestamp)values.get("rptTime");
				}
			}
			
			/*
			 * 状态报告接口（自动获取）
			 * 格式 http://127.0.0.1/protFace/getRpt.aspx?userID=1&cpKey=ABCDE&cpSubID=001,002,003
			 * userID	用户编号	是	30	SP提供，登录平台获取
			 * cpKey	用户密钥	是	50	SP提供，登录平台获取（可修改）
			 * cpSubID	任务编号	是	500	提交号码时产生的任务编号（唯一值） 多个任务编号以半角逗号“,”隔开 限制20个任务编号
			 *
			 *返回参数
			 * uPhone	手机号码	是	15	手机号码
			 * cpSubID	任务编号	是	40	CP提交号码时产生的任务编号（唯一值）
			 * cpSubSN	手机号码序列号	是	Int	1)	单个号码提交，默认=1 2)	提交多个号码时，分解产生的序列号，用cpSubID和cpSubSN做匹配
			 * rptCode	状态报告	是	10	0=发送成功，其它参见CMPP2.0
			 * rptTime	回执时间	是	datetime	移动网关返回时间 yyyy-mm-dd hh:mm:ss
			 * 响应正确格式 uPhone, cpSubID, cpSubSN, rptCode, rptTime#
			 */
			/*
			 * 不能同时支持主动查询和通知两种模式
			 * String url = serverUrl + "/protFace/getRpt.aspx" +
						 "?userID=" + userID +
						 "&cpKey=" + cpKey +
						 "&cpSubID=" + messageId;
			String[] responseValues = HttpUtils.getHttpContent(new String[]{url}, true, null, 30000).getResponseBody().split(",");
			if(messageId.equals(responseValues[1].trim()) && "0".equals(responseValues[3].trim())) {
				return DateTimeUtils.parseTimestamp(responseValues[3].trim().replaceAll("#", ""), "yyyy-MM-dd HH:mm:ss");
			}*/
			return null;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		synchronized(receiveMutex) {
			try {
				Collection keys = smsCache.getKeys();
				for(Iterator iterator = (keys==null ? null : keys.iterator()); iterator!=null && iterator.hasNext();) {
					String key = (String)iterator.next();
					if(key.startsWith(getSmsNumber() + "_receive_")) {
						HashMap values = (HashMap)smsCache.get(key);
						smsCache.remove(key); //从缓存中删除
						ReceivedShortMessage message = new ReceivedShortMessage();
						message.setSenderNumber((String)values.get("uPhone")); //发送人号码
						message.setReceiverNumber((String)values.get("spNo")); //接收人号码
						message.setMessage((String)values.get("reContent")); //短信内容
						message.setReceiveTime((Timestamp)values.get("reTime")); //接收时间
						return message;
					}
				}
				return null;
			}
			catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getSendNumbersLimit()
	 */
	public int getSendNumbersLimit() {
		return 1000; //允许发送1000个号码
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		//return 800;
		return 1000 - masSign.length();
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMessageMaxLengthForCharge(boolean)
	 */
	public int getMessageMaxLengthForCharge(boolean singleByteCharacters) {
		return 70 - masSign.length(); //66, 70-"【纪委】'.length, 内容字数计算规则：根据签名长度、长短信统计，如：内容字数<=64字按1条计算，字数<=128字按2条计算
	}
	
	/**
	 * 处理短信网关回调请求
	 * @param request
	 * @param response
	 */
	public void processSmsCallbackRequest(HttpServletRequest request, HttpServletResponse response) {
		String msg = null;
		try {
			String remoteAddress = request.getRemoteAddr();
			if(serverUrl.indexOf(remoteAddress)==-1 && !remoteAddress.equals("127.0.0.1")) { //校验IP
				throw new Exception();
			}
			msg = request.getParameter("msg");
			if(msg!=null && !msg.isEmpty()) { //{"nCount":2,"syncType":1,"syncData":[{"userID":"1","apiCode":"XT","spNo":"10657001","uPhone":"13800000001","moID":4784,"reContent":"测试test","reTime":"2014/10/8 18:35:43"},{"userID":"1","apiCode":"XT","spNo":"10657001","uPhone":"13800000002","moID":4785,"reContent":"上行消息","reTime":"2014/10/8 18:35:47"}]}
				JSONObject json = (JSONObject)new JSONParser().parse(msg);
				int count = Integer.parseInt("" + json.get("nCount")); //nCount	推送总数	是	int	共有几个json对象
				int syncType = Integer.parseInt("" + json.get("syncType")); //syncType	推送类型	是	int	0=状态报告，1=上行消息
				JSONArray syncData = (JSONArray)json.get("syncData"); //syncData	推送数据	是	--	详见syncData说明
				for(int i=0; i<count; i++) {
					JSONObject data = (JSONObject)syncData.get(i);
					String spNo = "" + data.get("spNo");
					SmsClientImpl client = getClient(spNo);
					if(client==null) {
						continue;
					}
					if(syncType==0) { //状态报告
						//userID	用户ID	否	int	用户编号
						//apiCode	通道编号	是	10	系统默认
						//spNo	服务号码	是	30	手机收到的短信号码
						//uPhone	手机号码	是	20	
						//cpSubID	任务编号	是	40	CP提交号码时自定义的任务编号
						//cpSubSN	手机号码序列号	是	10	批量发送多个号码时，分解产生的序列号(1,2,3,…)，用cpSubID和cpSubSN两个条件做匹配
						//rptCode	状态报告	是	10	详见“回执编码列表”
						//rptTime	回执时间	是	20	移动网关返回时间 GB2312编码
						client.processSmsArriveRequest("" + data.get("cpSubID"), 
													   "" + data.get("uPhone"), //手机号码
													   "" + data.get("rptCode"), //0=发送成功
													   "" + data.get("rptTime"));
					}
					else { //上行消息
						//userID	用户ID	否	int	用户编号
						//apiCode	通道编号	是	10	系统默认通道编号
						//spNo	服务号码	是	30	手机收到的短信号码
						//uPhone	手机号码	是	20	
						//moID	上行编号	是	Int	如果收到相同上行编号，视为重复发送，可以放弃处理。
						//reContent	回复内容	是	70	
						//reTime	回复时间	是	20	格式yyyy-mm-dd hh:mm:ss
						client.processSmsReceiveRequest("" + data.get("reContent"),
														"" + data.get("uPhone"),
														spNo, 
														"" + data.get("reTime"));
					}
				}
			}
			else {
				String spNo = request.getParameter("spNO");
				SmsClientImpl client = getClient(spNo);
				msg = URLDecoder.decode(request.getQueryString(), "gbk");
				String cpSubID;
				String reContent;
				if((cpSubID=StringUtils.getPropertyValue(msg, "cpSubID"))!=null) { //状态报告
					/*
					 * 状态报告接口（主动推送）
					 * 格式 http://127.0.0.1/Report.aspx?uPhone=13800000001&cpSubID=1&cpSubSN=1&rptCode=0&rptTime=2009-11-3+12%3A11%3A35
					 * apiCode	通道编号	是	10	系统默认通道编号
					 * spNO	服务号码	是	30	手机收到的短信号码
					 * uPhone	手机号码	是	20	
					 * cpSubID	任务编号	是	40	返回CP提交号码时产生的任务编号（唯一值）
					 * cpSubSN	手机号码序列号	是	int	1) 单个号码提交，默认=1 2) 提交多个号码时，分解产生的序列号，用cpSubID和cpSubSN做匹配
					 * rptCode	状态报告	是	10	0=发送成功
					 * rptTime	回执时间	是	datetime	移动网关返回时间 GB2312编码
					 * 短信回执响应 0：接收成功（由CP响应，则SP视为同步失败）
					 */
					client.processSmsArriveRequest(cpSubID, 
												   StringUtils.getPropertyValue(msg, "uPhone"), //手机号码
												   StringUtils.getPropertyValue(msg, "rptCode"), //0=发送成功
												   StringUtils.getPropertyValue(msg, "rptTime"));
				}
				else if((reContent=StringUtils.getPropertyValue(msg, "reContent"))!=null) { //短信上行
					/*
					 * 短信上行接口（主动推送）
					 * 格式 http://127.0.0.1/Receive.aspx?uPhone=13800000001&extID=188&reContent=%B2%E2%CA%D4%BB%D8%B8%B4&reTime=2009-11-4+15%3A06%3A31
					 * apiCode	通道编号	是	10	系统默认通道编号
					 * spNO	服务号码	是	30	手机收到的短信号码
					 * uPhone	手机号码	是	20	
					 * extID	手机尾号	是	10	
					 * reContent	回复内容	是	70	GB2312编码
					 * reTime	回复时间	是	datetime	GB2312编码
					 * 响应代码 0：接收成功（由CP响应，否则SP视为同步失败）
					 */
					client.processSmsReceiveRequest(reContent,
													StringUtils.getPropertyValue(msg, "uPhone"),
													spNo, 
													StringUtils.getPropertyValue(msg, "reTime"));
				}
			}
			response.getWriter().write("0"); //响应代码 0：接收成功
		}
		catch(Exception e) {
			Logger.error(msg);
			Logger.exception(e);
			try {
				response.getWriter().write("error");
			}
			catch (IOException ioe) {
				
			}
		}
	}
	
	/**
	 * 处理短信到达请求
	 * @param cpSubID
	 * @param uPhone
	 * @param rptCode
	 * @param rptTime
	 * @throws Exception
	 */
	public void processSmsArriveRequest(String cpSubID, String uPhone, String rptCode, String rptTime) throws Exception {
		HashMap values = new HashMap();
		values.put("rptCode", rptCode); //0=发送成功
		values.put("rptTime", DateTimeUtils.parseTimestamp(rptTime.replace('/', '-'), "yyyy-MM-dd HH:mm:ss"));
		smsCache.put(getSmsNumber() + "_arrive_" + cpSubID + "_" + uPhone, values);
	}
	
	/**
	 * 处理短信接收请求
	 * @param reContent
	 * @param uPhone
	 * @param spNo
	 * @param reTime
	 * @throws Exception
	 */
	public void processSmsReceiveRequest(String reContent, String uPhone, String spNo, String reTime) throws Exception {
		HashMap values = new HashMap();
		values.put("uPhone", uPhone);
		values.put("spNo", spNo);
		values.put("reContent", reContent);
		values.put("reTime", DateTimeUtils.parseTimestamp(reTime.replace('/', '-'), "yyyy-MM-dd HH:mm:ss"));
		smsCache.put(getSmsNumber() + "_receive_" + UUIDLongGenerator.generateId(), values);
		synchronized(receiveMutex) {
			receiveMutex.notify(); //通知短信接收方法,处理短信
		}
	}
	
	/**
	 * 获取客户端
	 * @param spNo
	 * @return
	 */
	private SmsClientImpl getClient(String spNo) throws ServiceException {
		SmsService smsService = (SmsService)Environment.getService("smsService");
		for(Iterator iterator = smsService.getSmsClients().iterator(); iterator.hasNext();) {
			SmsClient smsClient = (SmsClient)iterator.next();
			if((smsClient instanceof SmsClientImpl) && smsClient.getSmsNumber()!=null && spNo.startsWith(smsClient.getSmsNumber())) {
				return (SmsClientImpl)smsClient;
			}
		}
		return null;
	}
	
	/**
	 * @return the smsCache
	 */
	public Cache getSmsCache() {
		return smsCache;
	}

	/**
	 * @param smsCache the smsCache to set
	 */
	public void setSmsCache(Cache smsCache) {
		this.smsCache = smsCache;
	}

	/**
	 * @return the cpKey
	 */
	public String getCpKey() {
		return cpKey;
	}

	/**
	 * @param cpKey the cpKey to set
	 */
	public void setCpKey(String cpKey) {
		this.cpKey = cpKey;
	}

	/**
	 * @return the masSign
	 */
	public String getMasSign() {
		return masSign;
	}

	/**
	 * @param masSign the masSign to set
	 */
	public void setMasSign(String masSign) {
		this.masSign = masSign;
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the apiCode
	 */
	public String getApiCode() {
		return apiCode;
	}

	/**
	 * @param apiCode the apiCode to set
	 */
	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}
}