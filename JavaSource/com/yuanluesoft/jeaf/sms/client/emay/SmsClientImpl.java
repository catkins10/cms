package com.yuanluesoft.jeaf.sms.client.emay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.net.emay.metone.api.Client;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;


/**
 * 
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	private String clientSerial; //加密后的客户端注册号,DES+Base64,密钥emay
	private String clientPassword; //加密后的客户端密码,DES+Base64,密钥emay
	
	private Client client; //亿美短信发送客户端
	
	/**
	 * 客户端登录
	 * @throws ServiceException
	 */
	public void login() throws ServiceException {
		try {
			Logger.info("*************** EmaySmsClient start login ***************");
			//初始化客户端
			client = new Client(Encoder.getInstance().desBase64Decode(clientSerial, "emay", null));
			if(!client.registEx(Encoder.getInstance().desBase64Decode(clientPassword, "emay", null))) {
				throw new ServiceException();
			}
			if(!client.registDetailInfo("千度软件", "千度软件", "059188888888", "13811111111", "yuanluesoft@9191edu.com", "059188888888", "电教大楼", "350001")) {
				throw new ServiceException();
			}
			Logger.info("*************** EmaySmsClient login ***************");
		}
		catch (Exception e) {
			Logger.exception(e);
			Logger.info("*************** EmaySmsClient login failed ***************");
			client = null;
		}
	}
	
	/**
	 * 注销客户端
	 * @throws ServiceException
	 */
	public void logout() {
		try {
			if(client!=null) {
				client.logout();
				Logger.info("*************** EmaySmsClient logout ***************");
			}
		}
		catch(Exception e) {
			
		}
		client = null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		boolean ret;
		if(client==null) {
			try {
				login();
			}
			catch(Exception e) {
				throw new ServiceException();
			}
		}
		if(receiveTime==null || receiveTime.before(DateTimeUtils.now())) { //发送即时短信
			ret = client.sendSMS(new String[] {(String)recevierNumbers.get(0)}, message);
		}
		else { //发送定时短信
			ret = client.sendScheduledSMS(new String[] {(String)recevierNumbers.get(0)}, message, new SimpleDateFormat("yyyyMMddHHmmss").format(receiveTime));
		}
		if(ret) {
			Logger.info("send messege to " + (String)recevierNumbers.get(0) + " success, message content is " + message);
			return null;
		}
		//由于亿美客户端发送短信时,可能无法判断连接异常,所以做余额查询,用来确定连接是否正常
		if(client.getBalance()<=0) { //账户余额查询
			//余额查询失败,重新登录,然后重试短信发送
			logout();
		}
		Logger.info("send messege to " + (String)recevierNumbers.get(0) + " failed, message content is " + message);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#isShortMessageArrived(java.lang.String, java.lang.String)
	 */
	public Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException {
		return DateTimeUtils.now(); //不支持到达查询,直接返回当前时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		return null; //不支持接收
	}

	/**
	 * 获取帐号余额
	 * @return
	 * @throws ServiceException
	 */
	public double getBalance() throws ServiceException {
		if(client!=null) {
			return client.getBalance();
		}
		Client client = null;
		try {
			client = new Client(Encoder.getInstance().desBase64Decode(clientSerial, "emay", null));
			client.registEx(Encoder.getInstance().desBase64Decode(clientPassword, "emay", null));
			return client.getBalance();
		}
		catch(Exception e) {
			throw new ServiceException();
		}
		finally {
			client.logout();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 200;
	}

	/**
	 * @return the clientPassword
	 */
	public String getClientPassword() {
		return clientPassword;
	}

	/**
	 * @param clientPassword the clientPassword to set
	 */
	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}

	/**
	 * @return the clientSerial
	 */
	public String getClientSerial() {
		return clientSerial;
	}

	/**
	 * @param clientSerial the clientSerial to set
	 */
	public void setClientSerial(String clientSerial) {
		this.clientSerial = clientSerial;
	}
}