package com.yuanluesoft.jeaf.sms.client;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;

/**
 * 短信收发客户端
 * @author linchuan
 *
 */
public abstract class SmsClient {
	private int receiveTimePeriod = 20; //接收短信时间间隔,以秒为单位,默认20秒
	private String smsNumber; //短信号码
	private String name; //客户端名称
	
	/**
	 * 获取单条短信最大长度
	 * @return
	 */
	public abstract int getMaxMessageLength();
	
	/**
	 * 发送短信,返回短信ID,用来做短信到达情况查询
	 * @param recevierNumbers
	 * @param senderNumber
	 * @param message
	 * @param receiveTime
	 * @return
	 * @throws ServiceException
	 */
	public abstract String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException;
	
	/**
	 * 检查短信是否已经到达用户手机,返回到达时间,如果短信平台不支持本方法,直接返回当前时间
	 * @param messageId
	 * @param recevierNumber
	 * @return
	 * @throws ServiceException
	 */
	public abstract Timestamp isShortMessageArrived(String messageId, String recevierNumber) throws ServiceException;
		
	/**
	 * 获取接收到的消息
	 * @return
	 * @throws ServiceException
	 */
	protected abstract ReceivedShortMessage readReceivedShortMessage() throws ServiceException;
	
	/**
	 * 获取单条短信发送号码个数上限,默认1个号码
	 * @return
	 */
	public int getSendNumbersLimit() {
		return 1;
	}
	
	/**
	 * 获取号码中允许增加的位数
	 * @return
	 */
	public int getAddDigits() {
		return 20 - (smsNumber==null ? 0 : smsNumber.length());
	}
	
	/**
	 * 获取手机单条短信最大的字数,用于计算实发短信条数,以便计费,默认70个汉字(移动MAS为67),或者140个纯英文
	 * @param singleByteCharacters 是否单字节字符串
	 * @return
	 */
	public int getMessageMaxLengthForCharge(boolean singleByteCharacters) {
		return singleByteCharacters ? 140 : 70;
	}
	
	/**
	 * 接收短信,如果没有短信,则一直等待
	 * @return
	 * @throws ServiceException
	 */
	public ReceivedShortMessage getReceivedShortMessage() throws ServiceException {
		for(;;) {
			try {
				ReceivedShortMessage receivedShortMessage = readReceivedShortMessage();
				if(receivedShortMessage!=null) { //有短信
					return receivedShortMessage;
				}
				//没有短信
				synchronized (this) {
					wait(receiveTimePeriod * 1000); //等待一段时间后再接收
				}
			}
			catch(Error e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 有短信到达,当短信客户端支持事件触发时使用
	 *
	 */
	protected void shortMessageArrived() {
		synchronized (this) {
			notify(); //通知短信接收线程处理短信
		}
	}

	/**
	 * @return the receiveTimePeriod
	 */
	public int getReceiveTimePeriod() {
		return receiveTimePeriod;
	}

	/**
	 * @param receiveTimePeriod the receiveTimePeriod to set
	 */
	public void setReceiveTimePeriod(int receiveTimePeriod) {
		this.receiveTimePeriod = receiveTimePeriod;
	}

	/**
	 * @return the smsNumber
	 */
	public String getSmsNumber() {
		return smsNumber;
	}

	/**
	 * @param smsNumber the smsNumber to set
	 */
	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}