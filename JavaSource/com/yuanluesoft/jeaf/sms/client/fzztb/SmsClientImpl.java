package com.yuanluesoft.jeaf.sms.client.fzztb;

import java.sql.Timestamp;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.XMLType;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 短信客户端:福州交易中心,使用WEB服务
 * @author linchuan
 *
 */
public class SmsClientImpl extends SmsClient {
	private SoapConnectionPool soapConnectionPool;
	private SoapPassport soapPassport;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#sendMessage(java.util.List, java.lang.String, java.lang.String, java.sql.Timestamp)
	 */
	public String sendMessage(List recevierNumbers, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		try {
			Object[] args = {(String)recevierNumbers.get(0), message};
			String[] argNames = {"v_mobile", "v_smscontent"};
			QName[] argTypes = {XMLType.XSD_STRING, XMLType.XSD_STRING};
			soapConnectionPool.invokeRemoteMethod("SMSWebService.asmx", "SendSMS", XMLType.XSD_INT, null, soapPassport, args, argNames, argTypes, null, null);
			return null;
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
		return DateTimeUtils.now(); //总是认为已经收到
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#readReceivedShortMessage()
	 */
	protected ReceivedShortMessage readReceivedShortMessage() throws ServiceException {
		return null; //不支持接收
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.client.SmsClient#getMaxMessageLength()
	 */
	public int getMaxMessageLength() {
		return 200;
	}

	/**
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the soapPassport
	 */
	public SoapPassport getSoapPassport() {
		return soapPassport;
	}

	/**
	 * @param soapPassport the soapPassport to set
	 */
	public void setSoapPassport(SoapPassport soapPassport) {
		this.soapPassport = soapPassport;
	}
}