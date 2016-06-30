package com.yuanluesoft.jeaf.sms.client.huawei;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.huawei.eie.api.sm.DBSMProxy;
import com.huawei.eie.api.sm.SmReceiveBean;
import com.huawei.eie.api.sm.SmSendBean;
import com.huawei.eie.api.sm.SmSendResultBean;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 短信客户端:华为信息机
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	private DBSMProxy smProxy; //华为MAS短信代理
	private String masAccount; //华为MAS帐号
	private String masPassword; //华为MAS密码
	private String masSign = "【纪委】"; //签名

	/**
	 * 启动
	 * @throws Exception
	 */
	public void stratup() {
		if(smProxy!=null) {
			showdown(); //注销
		}
		else {
			smProxy = new DBSMProxy();
		}
		try {
			smProxy.initConn(Environment.getWebinfPath().substring(1) + "jeaf/sms/huawei/smApiConf.xml");
			smProxy.login(masAccount, masPassword);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 退出
	 * @throws Exception
	 */
	public void showdown() {
		try {
			smProxy.logout(); //注销
	        smProxy.destroy(); //销毁连接
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		try {
			if(!smProxy.isConnected()) {
				stratup(); //未连接时,重新启动
			}
			SmSendBean shortMessage = new SmSendBean();
			shortMessage.setSmMsgContent(message);
			shortMessage.setMsgFmt("utf-8");
			shortMessage.setSmDestAddrs(new String[]{(String)recevierNumbers.get(0)});
			shortMessage.setSmNeedStateReport(1); //这条消息是否需要状态报告
			shortMessage.setSendType(SmSendBean.SMSEND_TYPE_TEXT);
			if(receiveTime!=null && receiveTime.after(DateTimeUtils.now())) { //指定短信发送时间
				shortMessage.setSmSendTime(new Date(receiveTime.getTime()));
			}
			int[] messageIds = smProxy.sendSm(shortMessage);
			return messageIds[0] + "";
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		try {
			if(!smProxy.isConnected()) {
				stratup(); //未连接时,重新启动
			}
			SmSendResultBean[] results = smProxy.querySmsResult(Integer.parseInt(messageId), null, null, null, null);
			if(results==null || results.length==0) {
				return null;
			}
			return new Timestamp(results[0].getSmRecvTime().getTime()); //smRecvTime:短信被手机终端收到的时间，有状态报告的时候生效，无状态报告的时候同短信入库时间
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		try {
			if(!smProxy.isConnected()) {
				stratup(); //未连接时,重新启动
			}
			SmReceiveBean[] shortMessages = smProxy.getReceivedSms(1, null, null, null, null);
			if(shortMessages==null || shortMessages.length==0) {
				return null;
			}
			if(shortMessages[0].getSmId()==-1) { //接收消息id（唯一标识），为－1的时候表明此消息为无效消息；
				return null;
			}
			ReceivedShortMessage receivedShortMessage = new ReceivedShortMessage();
			receivedShortMessage.setSenderNumber(shortMessages[0].getSmOrgAddr()); //发送人号码, 接收消息的源地址，对应的就是发送消息的移动台
			receivedShortMessage.setMessage(shortMessages[0].getSmMsgContent()); //短信内容
			receivedShortMessage.setReceiveTime(new Timestamp(shortMessages[0].getSmRecvTime().getTime())); //接收时间
			return receivedShortMessage;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 200;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMessageMaxLengthForCharge(boolean)
	 */
	public int getMessageMaxLengthForCharge(boolean singleByteCharacters) {
		return 70 - masSign.length(); //66, 70-"【纪委】'.length, 内容字数计算规则：根据签名长度、长短信统计，如：内容字数<=64字按1条计算，字数<=128字按2条计算
		/*if(singleByteCharacters) { //单字节字符串
			return isPHS ? 116 : 140;
		}
		else {
			return isPHS ? 58 : 67; //MAS,67个汉字
		}*/
	}

	/**
	 * @return the masAccount
	 */
	public String getMasAccount() {
		return masAccount;
	}

	/**
	 * @param masAccount the masAccount to set
	 */
	public void setMasAccount(String masAccount) {
		this.masAccount = masAccount;
	}

	/**
	 * @return the masPassword
	 */
	public String getMasPassword() {
		return masPassword;
	}

	/**
	 * @param masPassword the masPassword to set
	 */
	public void setMasPassword(String masPassword) {
		this.masPassword = masPassword;
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
}