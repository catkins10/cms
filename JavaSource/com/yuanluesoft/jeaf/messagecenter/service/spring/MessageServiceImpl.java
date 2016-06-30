/*
 * Created on 2005-11-11
 *
 */
package com.yuanluesoft.jeaf.messagecenter.service.spring;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.model.SendMode;
import com.yuanluesoft.jeaf.messagecenter.model.SendOption;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.security.pojo.RecordPrivilege;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author LinChuan
 *
 */
public class MessageServiceImpl implements MessageService {
	private DistributionService distributionService; //分布式服务
	private SoapPassport serviceSoapPassport; //服务端SOAP身份验证
	private DatabaseService databaseService;
	private OrgService orgService;
	private PersonService personService;
	private RoleService roleService;
	
	private List senders; //发送器列表
	private List sendOptions; //发送选项列表
	
	private Message nextMessageToSend = null; //下一次需要发送的消息
	
	private MessageSendThread messageSendThread = null;
	private Timer wakeupTimer;
	
	/**
	 * 启动消息中心
	 *
	 */
	public void startup() {
		try {
			messageSendThread = new MessageSendThread(this);
			messageSendThread.start();
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 卸载消息中心
	 *
	 */
	public void shutdown() {
		//销毁扫描线程
		try {
			if(messageSendThread!=null) {
				messageSendThread.interrupt();
			}
		}
		catch(Exception e) {
			
		}
		//停止所有发送器
		stopSenders();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#sendMessageToPerson(long, java.lang.String, java.lang.String, char, long, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, int, java.sql.Timestamp)
	 */
	public void sendMessageToPerson(long personId, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException {
		if(!distributionService.isMasterServer(false)) { //当前消息中心不在活动状态
			Serializable[] args = {new Long(personId), message, new Long(senderId), senderName, new Character(priority), new Long(sourceRecordId), href, sendTime, bindSendMode, cyclicMode, new Integer(cyclicTime), cyclicEnd};
			if(doInRemoteCenter("sendMessageToPerson", args)) {
				return;
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("MessageCenter: sendMessageToPerson.");
		}
		createMessageToPerson(personId, message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
		updateMessageIfNeed(sendTime);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#sendMessageToOrg(long, boolean, java.lang.String, java.lang.String, char, long, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, int, java.sql.Timestamp)
	 */
	public void sendMessageToOrg(long orgId, boolean containChildOrg, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException {
		if(!distributionService.isMasterServer(false)) { //当前消息中心不在活动状态
			Serializable[] args = {new Long(orgId), new Boolean(containChildOrg), message, new Long(senderId), senderName, new Character(priority), new Long(sourceRecordId), href, sendTime, bindSendMode, cyclicMode, new Integer(cyclicTime), cyclicEnd};
			if(doInRemoteCenter("sendMessageToOrg", args)) {
				return;
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("MessageCenter: sendMessageToOrg.");
		}
		List personList = orgService.listOrgPersons("" + orgId, "all", containChildOrg, false, 0, 300); //最多给300人发送消息
		if(personList!=null && !personList.isEmpty()) {
			for(Iterator iterator = personList.iterator(); iterator.hasNext();) {
				Person person = (Person)iterator.next();
				createMessageToPerson(person.getId(), message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
			}
			updateMessageIfNeed(sendTime);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#sendMessageToRole(long, java.lang.String, java.lang.String, char, long, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, int, java.sql.Timestamp)
	 */
	public void sendMessageToRole(long roleId, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException {
		if(!distributionService.isMasterServer(false)) { //当前消息中心不在活动状态
			Serializable[] args = {new Long(roleId), message, new Long(senderId), senderName, new Character(priority), new Long(sourceRecordId), href, sendTime, bindSendMode, cyclicMode, new Integer(cyclicTime), cyclicEnd};
			if(doInRemoteCenter("sendMessageToRole", args)) {
				return;
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("MessageCenter: sendMessageToRole.");
		}
		List personList = roleService.listRoleMembers("" + roleId);
		if(personList!=null && !personList.isEmpty()) {
			for(Iterator iterator = personList.iterator(); iterator.hasNext();) {
				Person person = (Person)iterator.next();
				createMessageToPerson(person.getId(), message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
			}
			updateMessageIfNeed(sendTime);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#sendMessageToVisitors(java.util.List, boolean, java.lang.String, java.lang.String, char, long, java.lang.String, java.sql.Timestamp, java.lang.String, java.lang.String, int, java.sql.Timestamp)
	 */
	public void sendMessageToVisitors(List visitors, boolean containChildOrg, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException {
	    if(visitors==null || visitors.isEmpty()) {
	        return;
	    }
		for(Iterator iterator = visitors.iterator(); iterator.hasNext();) {
			RecordPrivilege visitor = (RecordPrivilege)iterator.next();
			if(personService.getPerson(visitor.getVisitorId())!=null) { //个人
				sendMessageToPerson(visitor.getVisitorId(), message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
			}
			else if(orgService.getOrg(visitor.getVisitorId())!=null) { //部门
				sendMessageToOrg(visitor.getVisitorId(), containChildOrg, message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
			}	
			else if(roleService.getRole(visitor.getVisitorId())!=null) { //角色
				sendMessageToRole(visitor.getVisitorId(), message, senderId, senderName, priority, sourceRecordId, href, sendTime, bindSendMode, cyclicMode, cyclicTime, cyclicEnd);
			}
		}
	}
	
	/**
	 * 创建发送到个人的消息
	 * @param personId
	 * @param messageContent
	 * @param senderId
	 * @param senderName
	 * @param priority
	 * @param sourceRecordId
	 * @param href
	 * @param sendTime
	 * @param bindSendMode
	 * @param cyclicMode
	 * @param cyclicTime
	 * @param cyclicEnd
	 * @throws ServiceException
	 */
	private void createMessageToPerson(long personId, String messageContent, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException {
		if(sendTime==null) {
			sendTime = new Timestamp(1121665260000l);
		}
		Message message = null;
		boolean newMessage = false;
		if(sourceRecordId>0) {
			String hql = "from Message Message" +
						 " where Message.sourceRecordId=" + sourceRecordId +
						 " and Message.receivePersonId=" + personId +
						 " and Message.sendTime=TIMESTAMP(" + DateTimeUtils.formatTimestamp(sendTime, null) + ")";
			message = (Message)databaseService.findRecordByHql(hql);
		}
		if(message==null) {
			message = new Message();
			message.setId(UUIDLongGenerator.generateId());
			newMessage = true;
		}
		message.setContent(messageContent); //内容
		message.setFaildCount(0);
		if(bindSendMode!=null && !bindSendMode.equals("")) { //绑定发送方式
			message.setLastSendMode(bindSendMode);
			message.setBindSendMode('1');
		}
		else {
			message.setLastSendMode("");
			message.setBindSendMode('0');
		}
		message.setSenderId(senderId); //发送人ID
		message.setSenderName(senderName); //发送人
		message.setSendTime(sendTime); //通知时间
		message.setSourceRecordId(sourceRecordId); //源记录ID
		message.setPriority(priority<'0'  ? '0' : priority); //优先级
		message.setWebLink(href); //HTTP链接
		message.setReceivePersonId(personId); //接收人ID
		message.setCyclicMode(cyclicTime<=0 ? null : cyclicMode); //循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
		message.setCyclicTime(cyclicTime); //循环周期
		message.setCyclicEnd(cyclicEnd); //截止时间,空表示无限期
		if(newMessage) {
			databaseService.saveRecord(message);
		}
		else {
			databaseService.updateRecord(message);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#removeMessages(long)
	 */
	public void removeMessages(long sourceRecordId) throws ServiceException {
		if(!distributionService.isMasterServer(false)) { //当前消息中心不在活动状态
			Serializable[] args = {new Long(sourceRecordId)};
			if(doInRemoteCenter("removeMessages", args)) {
				return;
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("MessageCenter: removeMessages.");
		}
		databaseService.deleteRecordsByHql("from Message Message where Message.sourceRecordId=" + sourceRecordId);
		updateMessageIfNeed(null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#retrieveMessage(long, long)
	 */
	public Message retrieveMessage(long messageId, long receivePersonId) throws ServiceException {
		String hql = "from Message Message" +
					 " where Message.id=" + messageId +
					 " and Message.receivePersonId=" + receivePersonId;
		return (Message)databaseService.findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.service.MessageService#feedbackMessage(long)
	 */
	public void feedbackMessage(long messageId, long receivePersonId) throws ServiceException {
		if(!distributionService.isMasterServer(false)) { //当前消息中心不在活动状态
			Serializable[] args = {new Long(messageId), new Long(receivePersonId)};
			if(doInRemoteCenter("feedbackMessage", args)) {
				return;
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("MessageCenter: feedbackMessage.");
		}
		Message message = (Message)databaseService.findRecordById(Message.class.getName(), messageId);
		if(message!=null && message.getReceivePersonId()==receivePersonId) {
			endMessage(message);
			updateMessageIfNeed(null);
		}
	}
	
	/**
	 * 结束本次消息发送
	 * @param message
	 */
	private void endMessage(Message message) {
		if(message.getCyclicMode()==null || message.getCyclicTime()<=0) { //不循环
			try {
				databaseService.deleteRecord(message);
			}
			catch(Exception e) {
				
			}
			return;
		}
		//消息循环,设置下一次发送时间
		Timestamp nextSendTime = DateTimeUtils.now();
		int mode;
		if(CYCLIC_MODE_BY_MINUTE.equals(message.getCyclicMode())) { //按分钟
			mode = Calendar.MINUTE;
		}
		else if(CYCLIC_MODE_BY_HOUR.equals(message.getCyclicMode())) { //按小时
			mode = Calendar.HOUR_OF_DAY;
		}
		else if(CYCLIC_MODE_BY_DAY.equals(message.getCyclicMode())) { //按天
			mode = Calendar.DAY_OF_MONTH;
		}
		else if(CYCLIC_MODE_BY_MONTH.equals(message.getCyclicMode())) { //按月
			mode = Calendar.MONTH;
		}
		else {
			databaseService.deleteRecord(message);
			return;
		}
		nextSendTime = DateTimeUtils.add(nextSendTime, mode, message.getCyclicTime());
		//检查截止时间
		if(message.getCyclicEnd()!=null && message.getCyclicEnd().before(nextSendTime)) {
			databaseService.deleteRecord(message);
			return;
		}
		if(message.getBindSendMode()!='1') {
			message.setLastSendMode(null); //上次通知方式
		}
		message.setFaildCount(0); //发送失败次数
		message.setSendTime(nextSendTime); //下次发送时间
		databaseService.updateRecord(message);
	}
	
	/**
	 * 在远程消息中心执行操作
	 * @param methodName
	 * @param args
	 * @return
	 * @throws ServiceException
	 */
	private boolean doInRemoteCenter(String methodName, Serializable[] args) throws ServiceException {
		try {
			distributionService.invokeMethodOnMasterServer("messageService", methodName, args);
			return true;
		}
		catch(Exception e) {
			//发送到远程消息中心失败,把当前消息中心设置为活动,并由自己来发送
			Logger.exception(e);
			setActive();
			return false;
		}
	}
		
	/**
	 * 根据发送时间决定是否需要更新消息队列,当发送时间比队列中最大的消息发送时间大时,则不需要更新
	 * @param sendTime
	 */
	private void updateMessageIfNeed(Timestamp sendTime) throws ServiceException {
		if(sendTime!=null && nextMessageToSend!=null && sendTime.after(nextMessageToSend.getSendTime())) {
			return;
		}
		try {
			wakeupTimer.cancel();
		}
		catch(Exception e) {
			
		}
		//300ms后执行
		wakeupTimer = new Timer();
		wakeupTimer.schedule(new TimerTask() {
			public void run() {
				try {
					synchronized (messageSendThread) {
						setNextMessageToSend();
						messageSendThread.notify();
					}
				}
				catch (Exception e) {
					Logger.exception(e);
				}
				wakeupTimer.cancel(); 
			}
		}, 300); //300ms
	}
	
	/**
	 * 设置下一个需要发送的消息
	 *
	 */
	private void setNextMessageToSend() {
		synchronized (messageSendThread) {
			try {
				nextMessageToSend = (Message)databaseService.findRecordByHql("from Message Message order by Message.sendTime");
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 把当前消息中心设置为活动状态
	 * @throws ServiceException
	 */
	private void setActive() throws ServiceException {
		distributionService.setAsMasterServer();
		synchronized (messageSendThread) {
			messageSendThread.notify();
		}
	}
	
	/**
	 * 发送消息
	 * @param message
	 * @throws Exception
	 */
	private void doSendMessage(Message message) throws ServiceException {
		//获取发送选项
		String priority = "normal";
		switch(message.getPriority()) {
		case MessageService.MESSAGE_PRIORITY_HIRH:
			priority = "high";
			break;
			
		case MessageService.MESSAGE_PRIORITY_LOW:
			priority = "low";
			break;
		}
		sendUseSendOption(message, (SendOption)ListUtils.findObjectByProperty(sendOptions, "priority", priority));
	}
	
	/**
	 * 根据发送选项发送消息
	 * @param message
	 * @param sendOption
	 * @throws Exception
	 */
	private void sendUseSendOption(Message message, SendOption sendOption) throws ServiceException {
		String sendModeName = message.getLastSendMode();
		//获取发送方式
		SendMode sendMode = null;
		if(message.getBindSendMode()!='1' && (sendModeName==null || sendModeName.equals(""))) {
			sendMode = (SendMode)sendOption.getSendModes().get(0);
		}
		else {
			Iterator iterator = sendOption.getSendModes().iterator(); 
			while(iterator.hasNext()) {
				sendMode = (SendMode)iterator.next();
				if(sendMode.getSender().getName().equalsIgnoreCase(sendModeName)) {
					break;
				}
			}
			if(message.getBindSendMode()!='1') { //非绑定发送方式时,获取下一个发送方式
				sendMode = iterator.hasNext() ? (SendMode)iterator.next() : (SendMode)sendOption.getSendModes().get(0);
			}
		}
		if(sendMode==null) { //没找到对应的发送方式
			//删除消息,不再发送
			databaseService.deleteRecord(message);
			return;
		}
		sendModeName = sendMode.getSender().getName();
		message.setLastSendMode(sendModeName);
		boolean isLastSendMode = message.getBindSendMode()=='1' || (sendMode==sendOption.getSendModes().get(sendOption.getSendModes().size() - 1));
		//检查发送时间限制
		if(sendMode.getMinSendHour()!=sendMode.getMaxSendHour()) {
			int hour = DateTimeUtils.getHour(DateTimeUtils.now());
			if(hour<sendMode.getMinSendHour() || hour>sendMode.getMaxSendHour()) { //不在发送时间范围内
				if(!isLastSendMode) { //不是最后一种发送方式
					//尝试用下一种方式发送
					sendUseSendOption(message, sendOption);
				}
				else if(message.getBindSendMode()=='1' || //绑定发送方式
					sendOption.getSendModes().size()==1 || //仅有一种发送方式
					message.getFaildCount()==sendOption.getRetry()-1) { //已经到重发次数上限
					//把发送时间设置成有效的发送时间
					Calendar today = Calendar.getInstance();
					today.set(Calendar.HOUR_OF_DAY, sendMode.getMinSendHour());
					if(hour>sendMode.getMaxSendHour()) { //大于最大时间,则等到第二天发送
						today.add(Calendar.DAY_OF_MONTH, 1);
					}
					message.setSendTime(new Timestamp(today.getTimeInMillis()));
					databaseService.updateRecord(message);
				}
				else { //最后一种发送方式
					//等到重发时间到后,继续下一次发送
					message.setSendTime(new Timestamp(System.currentTimeMillis() + sendOption.getRetryDelay() * 1000));
					databaseService.updateRecord(message);
				}
				return;
			}
		}
		boolean sendResult = false; //发送消息
		//发送信息
		try {
			sendResult = sendMode.getSender().sendMessage(message, sendMode.getFeedbackDelay());
			if(Logger.isTraceEnabled()) {
				Logger.trace("MessageCenter: send message use sender " + sendMode.getSender().getName() + " " + (sendResult ? "success" : "failed") + ", send failed count is " + message.getFaildCount());
			}
		}
		catch(Exception e) {
			
		}
		if(isLastSendMode) { //判断是否最后一种发送方式
			//已经是最后一种发送方式时,检查发送失败次数
			if(message.getFaildCount()==sendOption.getRetry()-1) { //已经达到重试次数上限
				//删除消息,不再发送
				endMessage(message);
				return;
			}
			message.setFaildCount(message.getFaildCount() + 1);
		}
		if(sendResult) { //发送成功
			if(sendMode.getSender().isFeedback()) { //支持反馈,等待用户反馈
				message.setSendTime(new Timestamp(System.currentTimeMillis() + (isLastSendMode ? sendOption.getRetryDelay() : sendMode.getFeedbackDelay()) * 1000));
				databaseService.updateRecord(message);
			}
			else { //不支持反馈,完成消息通知,删除本消息
				endMessage(message);
			}
		}
		else if(isLastSendMode) { //发送失败,判断是否最后一种发送方式
			//等到重发时间到后,继续下一次发送
			message.setSendTime(new Timestamp(System.currentTimeMillis() + sendOption.getRetryDelay() * 1000));
			databaseService.updateRecord(message);
		}
		else { //不是最后一种发送方式,继续尝试用下一种方式发送
			sendUseSendOption(message, sendOption);
		}
	}
	
	/**
	 * 停止所有的发送器
	 *
	 */
	private void stopSenders() {
		for(Iterator iterator = senders.iterator(); iterator.hasNext();) {
			Sender sender = (Sender)iterator.next();
			try {
				sender.stopSender();
			}
			catch(SendException e) {
				
			}
		}
	}
	
	/**
     * 消息队列扫描线程
     * @author linchuan
     *
     */
    private class MessageSendThread extends Thread {
    	private MessageServiceImpl messageService;

		public MessageSendThread(MessageServiceImpl messageService) {
			super();
			this.messageService = messageService;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			synchronized(this) {
				//第一次,设置需要发送的消息
				setNextMessageToSend();
				while(!isInterrupted()) {
					try {
						if(!messageService.distributionService.isMasterServer(false)) { //当前服务器已经不是主服务器
							if(Logger.isTraceEnabled()) {
								Logger.trace("MessageCenter: current server is not master server.");
							}
							messageService.stopSenders();
							for(;;) {
								wait(60000); //等待被唤醒
								if(messageService.distributionService.isMasterServer(true)) { //每隔60秒检查一次当前服务器是否已经成为主服务器
									break;
								}
							}
						}
						for(Timestamp now = DateTimeUtils.now(); nextMessageToSend!=null && !nextMessageToSend.getSendTime().after(now); setNextMessageToSend()) {
							try {
								messageService.doSendMessage(nextMessageToSend); //发送消息
								if(Logger.isTraceEnabled()) {
									Logger.trace("MessageCenter: sent a message that id is " + nextMessageToSend.getId() + ".");
								}
							}
							catch (Exception e) {
								Logger.exception(e);
							}
						}
						if(nextMessageToSend==null) { //没有消息需要发送
							if(Logger.isTraceEnabled()) {
								Logger.trace("MessageCenter: no message to send.");
							}
							wait(); //一直等待
						}
						else { //等待到下一个消息发送的时间
							if(Logger.isTraceEnabled()) {
								Logger.trace("MessageCenter: message that id is " + nextMessageToSend.getId() + " will send at " + DateTimeUtils.formatTimestamp(nextMessageToSend.getSendTime(), null) + ".");
							}
							long wait = nextMessageToSend.getSendTime().getTime() - System.currentTimeMillis();
							if(wait>0) {
								wait(wait);
							}
						}
					}
					catch (Error e) {
						e.printStackTrace();
						Logger.error(e.getMessage());
						try {
							wait(10000); //等待10秒
						}
						catch (InterruptedException ie) {
							
						}
						setNextMessageToSend(); //重新设置需要发送的消息
					}
					catch (Exception e) {
						Logger.exception(e);
						try {
							wait(10000); //等待10秒
						}
						catch (InterruptedException ie) {
							
						}
						setNextMessageToSend(); //重新设置需要发送的消息
					}
				}
			}
		}
    }
	
	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	/**
	 * @return Returns the orgService.
	 */
	public OrgService getOrgService() {
		return orgService;
	}
	
	/**
	 * @param orgService The orgService to set.
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
	
	/**
	 * @return Returns the senders.
	 */
	public List getSenders() {
		return senders;
	}
	
	/**
	 * @param senders The senders to set.
	 */
	public void setSenders(List senders) {
		this.senders = senders;
	}

	/**
	 * @return the sendOptions
	 */
	public List getSendOptions() {
		return sendOptions;
	}

	/**
	 * @param sendOptions the sendOptions to set
	 */
	public void setSendOptions(List sendOptions) {
		this.sendOptions = sendOptions;
	}

	/**
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @return the serviceSoapPassport
	 */
	public SoapPassport getServiceSoapPassport() {
		return serviceSoapPassport;
	}

	/**
	 * @param serviceSoapPassport the serviceSoapPassport to set
	 */
	public void setServiceSoapPassport(SoapPassport serviceSoapPassport) {
		this.serviceSoapPassport = serviceSoapPassport;
	}
}