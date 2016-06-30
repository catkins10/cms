/*
 * Created on 2005-11-26
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.msn;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import rath.msnm.MSNMessenger;
import rath.msnm.SwitchboardSession;
import rath.msnm.UserStatus;
import rath.msnm.entity.MsnFriend;
import rath.msnm.msg.MimeMessage;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;
import com.yuanluesoft.jeaf.messagecenter.sender.msn.pojo.MsnCustom;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class MsnSender extends Sender {
	private DatabaseService databaseService;
	private final int MSN_WAIT = 30; //msn登录等待时间 
	private final int SEND_DELAY = 120; //发送延时,120秒内未完成发送,则删除消息

	private String msnLoginName; //msn登录用户名
	private String msnPassword;  //msn密码
	private String msnFriendlyName; //显示的用户信息
	private String autoReply; //自动应答的内容
	private MessageService messageService;
	
	private MSNMessenger msn = new MSNMessenger();
	private MsnListener msnListener = new MsnListener(this);
	private List msnMessages = new ArrayList();
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#sendMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message)
	 */
	public boolean sendMessage(Message message, int feedbackDelay) throws SendException {
		if(msnLoginName==null || msnPassword==null) {
			return false;
		}
		synchronized(msnMessages) {
			//追加消息
			MsnCustom msnCustom;
			try {
				msnCustom = (MsnCustom)loadPersonalCustom(message.getReceivePersonId());
				if(msnCustom==null || msnCustom.getMsnLoginName()==null || msnCustom.getMsnLoginName().equals("")) {
					return false;
				}
				if(msnCustom.getIsFriendly()!='1') { //未加入到朋友列表
					addFriend(msnCustom.getMsnLoginName()); //添加到朋友列表
					//已经加为朋友,更新配置
					msnCustom.setIsFriendly('1');
					savePersonalCustom(msnCustom.getPersonId(), msnCustom);
				}
			}
			catch(Exception e) {
				Logger.exception(e);
				return false;
			}
			MsnMessage msnMessage = new MsnMessage();
			msnMessage.setMessageId(message.getId()); //消息ID
			msnMessage.setContent(message.getContent()); //消息内容
			msnMessage.setPriority(message.getPriority());
			msnMessage.setFeedbackDelay(feedbackDelay); //延迟
			msnMessage.setState(MsnMessage.MESSAGE_STATE_NORMAL); //消息状态
			msnMessage.setSendTime(null);  //发送时间
			msnMessage.setCreated(DateTimeUtils.now()); //创建时间
			msnMessage.setReceiverId(message.getReceivePersonId());
			msnMessage.setReceiverMsnName(msnCustom.getMsnLoginName()); //接收人
			msnMessages.add(0, msnMessage);
			try {
				callReceivers();
			}
			catch (Exception e) {
				Logger.exception(e);
				return false;
			}
			return true;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#stopSender()
	 */
	public void stopSender() throws SendException {
		msnMessages.clear();
		msn.logout();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#loadPersonalCustom(long)
	 */
	public Object loadPersonalCustom(long personId) throws ServiceException {
		return databaseService.findRecordById(MsnCustom.class.getName(), personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#savePersonalCustom(long, java.lang.Object)
	 */
	public void savePersonalCustom(long personId, Object config) throws ServiceException {
		MsnCustom msnConfig = (MsnCustom)config;
		MsnCustom curConfig = (MsnCustom)loadPersonalCustom(personId);
		if(curConfig==null) {
			msnConfig.setPersonId(personId);
			msnConfig.setIsFriendly('0');
			databaseService.saveRecord(msnConfig);
		}
		else {
			if(msnConfig.getMsnLoginName()==null || !msnConfig.getMsnLoginName().equals(curConfig.getMsnLoginName())) {
				curConfig.setIsFriendly('0');
			}
			else {
				curConfig.setIsFriendly(msnConfig.getIsFriendly());
			}
			curConfig.setMsnLoginName(msnConfig.getMsnLoginName());
			databaseService.updateRecord(curConfig);
		}
	}

	/**
	 * 请求与消息接收人建立会话
	 * @return
	 */
	public void callReceivers() throws Exception {
		synchronized(msnMessages) {
			if(msnMessages.isEmpty()) {
				return;
			}
			msnLogin();
			for(Iterator iterator = msnMessages.iterator(); iterator.hasNext();) {
				MsnMessage msnMessage = (MsnMessage)iterator.next();
				Calendar calendar = Calendar.getInstance();
				switch(msnMessage.getState()) {
				case MsnMessage.MESSAGE_STATE_SENT:
					calendar.add(Calendar.SECOND, -msnMessage.getFeedbackDelay());
					if(calendar.after(msnMessage.getSendTime())) { //超出反馈时间
						iterator.remove();
					}
					break;
					
				case MsnMessage.MESSAGE_STATE_CALLED:
					calendar.add(Calendar.SECOND, -SEND_DELAY);
					if(calendar.after(msnMessage.getCreated())) { //发送延迟时间内未完成发送
						iterator.remove();
					}
					break;
					
				case MsnMessage.MESSAGE_STATE_NORMAL:
				    //创建会话,以发送消息
					if(Logger.isDebugEnabled()) {
						Logger.debug("MessageCenter MSN Sender: call user " + msnMessage.getReceiverMsnName() + ".");
					}
					new MsnCallThread(msn, msnMessage.getReceiverMsnName()).start();
					msnMessage.setState(MsnMessage.MESSAGE_STATE_CALLED);
				}
			}
		}
	}
	
	/**
	 * 增加朋友
	 * @param msnCustom
	 * @throws Exception
	 */
	private void addFriend(String msnLoginName) throws Exception {
		msnLogin();
		synchronized(this) {
			msn.addFriend(msnLoginName);
			wait(MSN_WAIT * 1000); //等待完成添加用户
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("MessageCenter MSN Sender: add friend " + msnLoginName + ".");
		}
	}
	
	/**
	 * MSN登录
	 * @return
	 */
	private synchronized void msnLogin() throws Exception {
		if(msn.isLoggedIn()) {
			return;
		}
		msn.setInitialStatus(UserStatus.ONLINE);
		msn.addMsnListener(msnListener);
		msn.login(msnLoginName, msnPassword);
		synchronized(this) {
			wait(MSN_WAIT * 1000); //等待20秒
		}
		if(msn.isLoggedIn()) {
			//设置MSN显示用户名
			msn.setMyFriendlyName(msnFriendlyName);
			if(Logger.isDebugEnabled()) {
				Logger.debug("MessageCenter MSN Sender: login.");
			}
			return;
		}
		throw new Exception("MSN login time out.");
	}
	
	/**
	 * 发送消息给接收人
	 * @param receiverName
	 * @throws Exception
	 */
	public void sendMessageToReceiver(String receiverName) throws Exception {
		synchronized(msnMessages) {
			if(msnMessages.isEmpty()) {
				return;
			}
			for(Iterator iterator = msnMessages.iterator(); iterator.hasNext();) {
				MsnMessage msnMessage = (MsnMessage)iterator.next();
				if(msnMessage.getState()!=MsnMessage.MESSAGE_STATE_SENT && msnMessage.getReceiverMsnName().equalsIgnoreCase(receiverName)) {
					if(sendMessageToReceiver(receiverName, msnMessage.getContent(), msnMessage.getPriority())) {
						msnMessage.setState(MsnMessage.MESSAGE_STATE_SENT);
						msnMessage.setSendTime(DateTimeUtils.now());
					}
				}
			}
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("MessageCenter MSN Sender: send message to " + receiverName + ".");
		}
	}
	
	/**
	 * 发送消息给接收人
	 * @param friendName
	 * @param content
	 */
	public boolean sendMessageToReceiver(String receiverName, String content, char priority) throws Exception {
		MimeMessage newMsg = new MimeMessage(content);
		newMsg.setFontColor(priority==MessageService.MESSAGE_PRIORITY_HIRH ? Color.RED : Color.BLACK);
		newMsg.setKind(MimeMessage.KIND_MESSAGE);
		for(int i=0; i<3; i++) {
			if(msn.sendMessage(receiverName, newMsg)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 当用户会话创建完成
	 * @param session
	 * @param friend
	 * @throws Exception
	 */
	public void onJoinSession(SwitchboardSession session, MsnFriend friend) throws Exception {
		//发送消息内容
		sendMessageToReceiver(friend.getLoginName());
	}
	
	/**
	 * 用户退出会话
	 * @param session
	 * @throws Exception
	 */
	public void onSessionEnded(SwitchboardSession session) throws Exception {
		feedback(session.getMsnFriend().getLoginName());
	}
	
	/**
	 * 收到用户输入的消息
	 * @param session
	 * @param friend
	 * @param message
	 * @throws Exception
	 */
	public void onMessageReceived(SwitchboardSession session, MsnFriend friend, MimeMessage message) throws Exception {
		feedback(session.getMsnFriend().getLoginName());
		sendMessageToReceiver(friend.getLoginName(), autoReply.replaceAll("\\x7bUSERNAME\\x7d", friend.getFriendlyName()), MessageService.MESSAGE_PRIORITY_NORMAL);
	}
	
	/**
	 * 发送“反馈”消息到消息中心
	 * @param friendName
	 * @throws Exception
	 */
	private void feedback(String friendName) throws Exception {
		//发送“反馈”消息到消息中心
		synchronized(msnMessages) {
			for(Iterator iterator = msnMessages.iterator(); iterator.hasNext();) {
				MsnMessage msnMessage = (MsnMessage)iterator.next();
				if(msnMessage.getState()==MsnMessage.MESSAGE_STATE_SENT &&
				   msnMessage.getReceiverMsnName().equals(friendName)) {
					messageService.feedbackMessage(msnMessage.getMessageId(), msnMessage.getReceiverId());
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * 呼叫线程
	 * @author linchuan
	 *
	 */
	private class MsnCallThread extends Thread {
		private MSNMessenger msn;
		private String receiverName;
		
		public MsnCallThread(MSNMessenger msn, String receiverName) {
			this.msn = msn;
			this.receiverName = receiverName;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				msn.doCall(receiverName);
				msn.doCallWait(receiverName);
			}
			catch (Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * @return Returns the messageService.
	 */
	public MessageService getMessageService() {
		return messageService;
	}
	/**
	 * @param messageService The messageService to set.
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	/**
	 * @return the msnFriendlyName
	 */
	public String getMsnFriendlyName() {
		return msnFriendlyName;
	}

	/**
	 * @param msnFriendlyName the msnFriendlyName to set
	 */
	public void setMsnFriendlyName(String msnFriendlyName) {
		this.msnFriendlyName = msnFriendlyName;
	}

	/**
	 * @return the msnLoginName
	 */
	public String getMsnLoginName() {
		return msnLoginName;
	}

	/**
	 * @param msnLoginName the msnLoginName to set
	 */
	public void setMsnLoginName(String msnLoginName) {
		this.msnLoginName = msnLoginName;
	}

	/**
	 * @return the msnMessages
	 */
	public List getMsnMessages() {
		return msnMessages;
	}

	/**
	 * @param msnMessages the msnMessages to set
	 */
	public void setMsnMessages(List msnMessages) {
		this.msnMessages = msnMessages;
	}

	/**
	 * @return the msnPassword
	 */
	public String getMsnPassword() {
		return msnPassword;
	}

	/**
	 * @param msnPassword the msnPassword to set
	 */
	public void setMsnPassword(String msnPassword) {
		this.msnPassword = msnPassword;
	}

	/**
	 * @return the autoReply
	 */
	public String getAutoReply() {
		return autoReply;
	}

	/**
	 * @param autoReply the autoReply to set
	 */
	public void setAutoReply(String autoReply) {
		this.autoReply = autoReply;
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

}
