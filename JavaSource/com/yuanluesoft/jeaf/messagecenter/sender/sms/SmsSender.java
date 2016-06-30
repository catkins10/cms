/*
 * Created on 2005-11-29
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.sms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;
import com.yuanluesoft.jeaf.messagecenter.sender.sms.pojo.SmsCustom;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.sms.service.SmsServiceListener;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class SmsSender extends Sender implements SmsServiceListener {
	private DatabaseService databaseService;
	private SmsService smsService;
	private OrgService orgService; //组织机构服务
	private MessageService messageService; //消息服务
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		//注册短信业务
		smsService.registSmsBusiness("系统消息", false, false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#sendMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message)
	 */
	public boolean sendMessage(Message message, int feedbackDelay) throws SendException {
		try {
			//获取发送人所在的单位
			Org org = message.getSenderId()==0 || message.getSenderId()==SessionService.ANONYMOUS_USERID ? null : orgService.getPersonalUnitOrSchool(message.getSenderId());
			if(org==null || org.getId()==0) { //没有单位
				//获取收件人所在单位
				org = message.getReceivePersonId()==0 || message.getReceivePersonId()==SessionService.ANONYMOUS_USERID ? null : orgService.getPersonalUnitOrSchool(message.getReceivePersonId());
			}
			smsService.sendShortMessage(0, (message.getSenderName()==null ? "消息中心" : message.getSenderName()), org==null ? 0 : org.getId(), "系统消息", "" + message.getReceivePersonId(), null, message.getContent(), null, -1, false, "消息通知:" + message.getReceivePersonId() + "|" + message.getId(), true);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageArrived(com.yuanluesoft.jeaf.sms.pojo.SmsSend)
	 */
	public void onShortMessageArrived(SmsSend sentMessage) throws ServiceException {
		try {
			if(sentMessage.getRemark()==null || !sentMessage.getRemark().startsWith("消息通知:")) {
				return;
			}
			//用户已经接收到消息,反馈到消息中心
			String[] values = sentMessage.getRemark().substring("消息通知:".length()).split("\\x7c");
			messageService.feedbackMessage(Long.parseLong(values[1]), Long.parseLong(values[0]));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#loadPersonalCustom(long)
	 */
	public Object loadPersonalCustom(long personId) throws ServiceException {
		return databaseService.findRecordById(SmsCustom.class.getName(), personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#savePersonalCustom(long, java.lang.Object)
	 */
	public void savePersonalCustom(long personId, Object config) throws ServiceException {
		SmsCustom smsConfig = (SmsCustom)config;
		SmsCustom curConfig = (SmsCustom)loadPersonalCustom(personId);
		if(curConfig==null) {
			smsConfig.setPersonId(personId);
			databaseService.saveRecord(smsConfig);
		}
		else {
			curConfig.setMobile(smsConfig.getMobile());
			databaseService.updateRecord(curConfig);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageReceived(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)
	 */
	public boolean onShortMessageReceived(String senderNumber, String message, Timestamp receiveTime, String receiveNumber, SmsUnitBusiness smsUnitBusiness) throws ServiceException {
		//不处理接收到的短信
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#stopSender()
	 */
	public void stopSender() throws SendException {
		
	}

	/**
	 * @return the smsService
	 */
	public SmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the messageService
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * @param messageService the messageService to set
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
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
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
}