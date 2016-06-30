package com.yuanluesoft.jeaf.sms.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.client.SmsClient;
import com.yuanluesoft.jeaf.sms.model.ReceivedShortMessage;
import com.yuanluesoft.jeaf.sms.model.SmsBusiness;
import com.yuanluesoft.jeaf.sms.pojo.SmsReceive;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitConfig;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.sms.service.SmsServiceListener;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SmsServiceImpl extends BusinessServiceImpl implements SmsService {
	private List smsClients; //短信客户端列表,目前只支持一个短信客户端
	private MemberServiceList memberServiceList; //成员服务列表
	private OrgService orgService; //组织机构服务
	private RoleService roleService; //角色服务
	private int arriveCheckDelaySeconds = 60; //短信到达检查延时,以秒为单位
	private int arriveCheckLimitHours = 72; //短信到达检查时限,以小时为单位,超过时限的不再做检查
	private String smsServiceListenerNames; //短信侦听器名称列表,用逗号分隔
	
	private List smsServiceListeners = null; //短信侦听器列表
	private Map receiveTimerTasks = new HashMap(); //短信接收任务
	private Map arriveCheckThreads = new HashMap(); //短信到达检查线程
	private boolean businessMode = false; //是否商业模式
	private List smsBusinesses = new ArrayList(); //短信业务列表
	
	/**
	 * 初始化短信服务
	 * @throws ServiceException
	 */
	public void startup() {
		if(smsClients==null || smsClients.isEmpty()) { //没有短信客户端
			return;
		}
		//创建短信接收线程线程
		for(int i=0; i<smsClients.size(); i++) {
			final SmsClient smsClient = (SmsClient)smsClients.get(i);
			Timer receiveTimer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					Logger.info("************ SMS recevive thread is running. ************");
					try {
						receiveShortMessage(smsClient);
					}
					catch(Exception e) {
						Logger.exception(e);
					}
				}
			};
			receiveTimer.schedule(task, 60000); //60秒后启动
			receiveTimerTasks.put(smsClient.getName(), task);
			
			//启动短信到达检查线程
			Thread thread = new Thread() {
				public void run() {
					try {
						shortMessageArriveCheck(smsClient, this);
					} 
					catch (ServiceException e) {
						Logger.exception(e);
					}
				}
			};
			thread.start();
			arriveCheckThreads.put(smsClient.getName(), thread);
		}
	}
	
	/**
	 * 停止短信服务
	 *
	 */
	public void showdown() {
		if(smsClients==null || smsClients.isEmpty()) { //没有短信客户端
			return;
		}
		for(int i=0; i<smsClients.size(); i++) {
			SmsClient smsClient = (SmsClient)smsClients.get(i);
			try {
				((TimerTask)receiveTimerTasks.get(smsClient.getName())).cancel();
			}
			catch(Exception e) {
				
			}
			try {
				Thread thread = (Thread)arriveCheckThreads.get(smsClient.getName());
				synchronized(thread) {
					thread.interrupt();
				}
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#registSmsBusiness(java.lang.String, boolean, boolean)
	 */
	public void registSmsBusiness(String name, boolean sendPopedomConfig, boolean receivePopedomConfig) {
		if(ListUtils.findObjectByProperty(smsBusinesses, "name", name)!=null) {
			return;
		}
		smsBusinesses.add(new SmsBusiness(name, sendPopedomConfig, receivePopedomConfig));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#getSmsBusiness(java.lang.String)
	 */
	public SmsBusiness getSmsBusiness(String name) {
		return (SmsBusiness)ListUtils.findObjectByProperty(smsBusinesses, "name", name);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateBusiness(com.yuanluesoft.jeaf.database.Record, boolean)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		if(record instanceof SmsUnitBusiness) {
			SmsUnitBusiness smsUnitBusiness = (SmsUnitBusiness)record;
			String hql = "select SmsUnitBusiness.id" +
						 " from SmsUnitBusiness SmsUnitBusiness" +
						 " where SmsUnitBusiness.id!=" + smsUnitBusiness.getId() +
						 " and SmsUnitBusiness.unitConfigId=" + smsUnitBusiness.getUnitConfigId() +
						 " and SmsUnitBusiness.businessName='" + smsUnitBusiness.getBusinessName() + "'";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return ListUtils.generateList(smsUnitBusiness.getBusinessName() + "已经存在，不允许重复配置");
			}
			if(smsUnitBusiness.getSmsNumber()!=null && !smsUnitBusiness.getSmsNumber().isEmpty()) {
				//检查短信号码是否为数字
				for(int i=0; i<smsUnitBusiness.getSmsNumber().length(); i++) {
					if(smsUnitBusiness.getSmsNumber().charAt(i)<'0' || smsUnitBusiness.getSmsNumber().charAt(i)>'9') {
						return ListUtils.generateList("短信号码只允许输入数字");
					}
				}
				//检查短信号码是否超出短信客户端允许的最大长度
				String smsClientName;
				if(smsUnitBusiness.getUnitConfig()!=null) {
					smsClientName = smsUnitBusiness.getUnitConfig().getSmsClientName();
				}
				else {
					smsClientName = (String)getDatabaseService().findRecordByHql("select SmsUnitConfig.smsClientName from SmsUnitConfig SmsUnitConfig where SmsUnitConfig.id=" + smsUnitBusiness.getUnitConfigId());
				}
				for(Iterator iterator = smsClients==null ? null : smsClients.iterator(); iterator!=null && iterator.hasNext();) {
					SmsClient smsClient = (SmsClient)iterator.next();
					if(smsClientName!=null && !smsClientName.isEmpty() && !smsClientName.equals(smsClient.getName())) {
						continue;
					}
					if(smsUnitBusiness.getSmsNumber().length()>smsClient.getAddDigits()) {
						return ListUtils.generateList(smsClient.getAddDigits()==0 ? "不允许设置短信号码" : "短信号码不允许超过" + smsClient.getAddDigits() + "位");
					}
				}
				//检查短信号码是否使用过
				hql = "select SmsUnitBusiness.id" +
					  " from SmsUnitBusiness SmsUnitBusiness left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
					  " where SmsUnitBusiness.id!=" + smsUnitBusiness.getId() +
					  " and SmsUnitConfig.smsClientName='" + smsClientName + "'" +
					  " and SmsUnitBusiness.smsNumber='" + smsUnitBusiness.getSmsNumber() + "'";
				if(getDatabaseService().findRecordByHql(hql)!=null) {
					return ListUtils.generateList("短信号码已经被使用");
				}
			}
		}
		return super.validateBusiness(record, isNew);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(com.yuanluesoft.jeaf.database.Record, boolean)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		if(record instanceof SmsUnitConfig) {
			SmsUnitConfig smsUnitConfig = (SmsUnitConfig)record;
			String hql = "select SmsUnitConfig.id" +
						 " from SmsUnitConfig SmsUnitConfig" +
						 " where SmsUnitConfig.id!=" + smsUnitConfig.getId() +
						 " and SmsUnitConfig.unitId=" + smsUnitConfig.getUnitId();
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return ListUtils.generateList("单位" + smsUnitConfig.getUnitName() + "已经存在，不允许重复配置");
			}
		}
		return super.validateDataIntegrity(record, isNew);
	}

	/**
	 * 接收短信
	 * @param smsClient
	 * @throws ServiceException
	 */
	private void receiveShortMessage(SmsClient smsClient) throws ServiceException {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				ReceivedShortMessage receivedShortMessage = smsClient.getReceivedShortMessage();
				//保存短信接收记录
				SmsReceive smsReceive = new SmsReceive();
				smsReceive.setId(UUIDLongGenerator.generateId()); //ID
				smsReceive.setSenderNumber(receivedShortMessage.getSenderNumber()); //发送人号码
				smsReceive.setMessage(receivedShortMessage.getMessage()); //短信内容
				smsReceive.setReceiveTime(receivedShortMessage.getReceiveTime()); //接收时间
				smsReceive.setReceiverNumber(receivedShortMessage.getReceiverNumber()); //接收号码
				smsReceive.setSmsClientName(smsClient.getName()); //短信客户端名称
				//获取扩展的短信号码
				String extendNumber = receivedShortMessage.getReceiverNumber()==null || smsClient.getSmsNumber()==null ? null : receivedShortMessage.getReceiverNumber().substring(smsClient.getSmsNumber().length());
				//获取短信业务配置
				String hql = "select SmsUnitBusiness" +
							 " from SmsUnitBusiness SmsUnitBusiness left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
							 " where SmsUnitBusiness.smsNumber" + (extendNumber==null || extendNumber.isEmpty() ? " is null" : "='" + extendNumber + "'") +
							 (smsClient.getName()==null ? "" : " and SmsUnitConfig.smsClientName='" + smsClient.getName() + "'");
				List unitBusinessList = getDatabaseService().findRecordsByHql(hql, 0, 2);
				SmsUnitBusiness smsUnitBusiness = null;
				if(unitBusinessList!=null && unitBusinessList.size()==1) { //只有一个
					smsUnitBusiness = (SmsUnitBusiness)unitBusinessList.get(0);
					smsReceive.setBusinessId(smsUnitBusiness.getBusinessId()); //短信业务ID
					smsReceive.setBusinessName(smsUnitBusiness.getBusinessName()); //短信业务名称
					smsReceive.setReceiverUnitId(smsUnitBusiness.getUnitConfig().getUnitId()); //单位ID
					smsReceive.setReceiverUnit(smsUnitBusiness.getUnitConfig().getUnitName()); //单位名称
				}
				getDatabaseService().saveRecord(smsReceive);
				//调用侦听器,处理收到的短信
				for(Iterator iterator = getSmsServiceListeners().iterator(); iterator.hasNext();) {
					SmsServiceListener listener = (SmsServiceListener)iterator.next();
					try {
						if(listener.onShortMessageReceived(receivedShortMessage.getSenderNumber(), receivedShortMessage.getMessage(), receivedShortMessage.getReceiveTime(), receivedShortMessage.getReceiverNumber(), smsUnitBusiness)) {
							break;
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
			catch(Error e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 获取短信侦听器列表
	 * @return
	 */
	private List getSmsServiceListeners() {
		if(smsServiceListeners!=null && !smsServiceListeners.isEmpty()) {
			return smsServiceListeners;
		}
		//初始化短信侦听器列表
		smsServiceListeners = new ArrayList();
		if(smsServiceListenerNames==null || smsServiceListenerNames.isEmpty()) {
			return smsServiceListeners;
		}
		String[] listenerNames = smsServiceListenerNames.split(",");
		for(int i=0; i<listenerNames.length; i++) {
			try {
				SmsServiceListener listener = (SmsServiceListener)Environment.getService(listenerNames[i]);
				if(listener!=null) {
					smsServiceListeners.add(listener);
				}
			}
			catch(Exception e) {
				
			}
		}
		return smsServiceListeners;
	}
	
	/**
	 * 检查短信是否到达用户手机
	 * @param smsClient
	 * @param arriveCheckThread
	 * @throws ServiceException
	 */
	private void shortMessageArriveCheck(SmsClient smsClient, Thread arriveCheckThread) throws ServiceException {
		synchronized(arriveCheckThread) {
			int waitMinutes = 5; //等待分钟数
			while(!arriveCheckThread.isInterrupted()) {
				try {
					if(waitMinutes>0) {
						arriveCheckThread.wait(waitMinutes * 60000); //5分钟检查一次
					}
					long leftTime = arriveCheckDelaySeconds * 1000; //由于短信发送后会触发短信到达检查，故继续等待60秒
					for(long beginTime = System.currentTimeMillis(); leftTime>0; beginTime = System.currentTimeMillis()) {
						arriveCheckThread.wait(leftTime);
						leftTime -= System.currentTimeMillis() - beginTime;
					}
					//检查1天内发送的短信
					String hql = "from SmsSend SmsSend" +
								 " where SmsSend.created>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -arriveCheckLimitHours), null) + ")" +
								 " and SmsSend.arriveTime is null" +
								 " and SmsSend.arriveCheck='1'" +
								 " and SmsSend.smsClientName='" + smsClient.getName() + "'" +
								 " order by SmsSend.created DESC";
					waitMinutes = 5;
					for(int i=0; ; i+=200) {
						List messages = getDatabaseService().findRecordsByHql(hql, i, 200);
						if(messages==null || messages.isEmpty()) {
							break;
						}
						for(Iterator iterator = messages.iterator(); iterator.hasNext();) {
							SmsSend smsSend = (SmsSend)iterator.next();
							Timestamp arriveTime = smsClient.isShortMessageArrived(smsSend.getMessageId(), smsSend.getReceiverNumber());
							if(arriveTime==null) { //短信没有到达
								continue;
							}
							waitMinutes = 0;
							//短信已经到达
							smsSend.setArriveTime(arriveTime); //设置短信到达时间
							getDatabaseService().updateRecord(smsSend);
		
							//调用侦听器,处理短信到达消息
							for(Iterator iteratorListener = getSmsServiceListeners().iterator(); iteratorListener.hasNext();) {
								SmsServiceListener listener = (SmsServiceListener)iteratorListener.next();
								try {
									listener.onShortMessageArrived(smsSend);
								}
								catch(Exception e) {
									Logger.exception(e);
								}
							}
						}
					}
				}
				catch(Error e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					Logger.exception(e);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#sendShortMessage(long, java.lang.String, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, long, boolean, java.lang.String, boolean)
	 */
	public void sendShortMessage(long senderId, String senderName, long senderUnitId, String smsBusinessName, String receiverIds, String receiverNumbers, String message, Timestamp sendTime, long sourceRecordId, boolean checkSent, String remark, boolean arriveCheck) throws ServiceException {
		if(smsClients==null || smsClients.isEmpty()) { //没有在工作的短信客户端
			return;
		}
		if(businessMode) { //商业模式
			arriveCheck = true; //总是要到达检查
		}
		//获取单位配置
		SmsUnitBusiness smsUnitBusiness = getSmsUnitBusiness(senderUnitId, smsBusinessName);
		if(smsUnitBusiness==null) {
			throw new ServiceException("unit business config is not exists");
		}
		//获取短信客户端
		SmsClient smsClient = getSmsClient(smsUnitBusiness);
		if(smsClient==null) {
			throw new ServiceException("sms client is not exists");
		}
		Org senderUnit = orgService.getOrg(senderUnitId);
		if(smsUnitBusiness.getPostfix()!=null && !smsUnitBusiness.getPostfix().isEmpty() && senderUnit!=null && (senderUnit instanceof Unit)) {
			message += smsUnitBusiness.getPostfix().replace("<单位名称>", senderUnit.getDirectoryName()).replace("<业务名称>", smsBusinessName); //添加短信后缀
		}
		List receivers = new ArrayList();
		long taskId = UUIDLongGenerator.generateId();
		if(receiverNumbers!=null && !receiverNumbers.isEmpty()) { //指定了接收号码
			String[] values = receiverNumbers.split(",");
			for(int i=0; i<values.length; i++) {
				String receiverNumber = validateReceiverNumber(taskId, sourceRecordId, values[i], checkSent);
				if(receiverNumber!=null) {
					doSendShortMessage(smsClient, taskId, sourceRecordId, senderId, senderName, senderUnit, smsUnitBusiness, receivers, 0, null, receiverNumber, message, sendTime, arriveCheck, remark);
				}
			}
		}
		else {
			List orgOrRoleIds = new ArrayList(); //部门或者角色ID
			String[] ids = receiverIds.split(",");
			for(int i=0; i<ids.length; i++) {
				Member receiver = memberServiceList.getMember(Long.parseLong(ids[i]));
				if(receiver==null) { //部门或者角色
					orgOrRoleIds.add(ids[i]);
				}
				else { //个人
					String receiverNumber = getReceiverNumber(receiver.getId(), receiver.getMobile(), taskId, sourceRecordId, checkSent);
					if(receiverNumber!=null) {
						doSendShortMessage(smsClient, taskId, sourceRecordId, senderId, senderName, senderUnit, smsUnitBusiness, receivers, receiver.getId(), receiver.getName(), receiverNumber, message, sendTime, arriveCheck, remark);
					}
				}
			}
			//给机构成员发短信
			for(int j=0; !orgOrRoleIds.isEmpty(); j+=200) {
				List persons = orgService.listOrgPersons(ListUtils.join(orgOrRoleIds, ",", false), null, true, false, j, 200);
				for(Iterator iterator = persons==null ? null : persons.iterator(); iterator!=null && iterator.hasNext();) {
					Person person = (Person)iterator.next();
					String receiverNumber = getReceiverNumber(person.getId(), person.getMobile(), taskId, sourceRecordId, checkSent);
					if(receiverNumber!=null) {
						doSendShortMessage(smsClient, taskId, sourceRecordId, senderId, senderName, senderUnit, smsUnitBusiness, receivers, person.getId(), person.getName(), receiverNumber, message, sendTime, arriveCheck, remark);
					}
				}
				if(persons==null || persons.size()<200) {
					break;
				}
			}
			//给角色成员发短信
			List persons = orgOrRoleIds.isEmpty() ? null : roleService.listRoleMembers(ListUtils.join(orgOrRoleIds, ",", false));
			for(Iterator iterator = persons==null ? null : persons.iterator(); iterator!=null && iterator.hasNext();) {
				Person person = (Person)iterator.next();
				String receiverNumber = getReceiverNumber(person.getId(), person.getMobile(), taskId, sourceRecordId, checkSent);
				if(receiverNumber!=null) {
					doSendShortMessage(smsClient, taskId, sourceRecordId, senderId, senderName, senderUnit, smsUnitBusiness, receivers, person.getId(), person.getName(), receiverNumber, message, sendTime, arriveCheck, remark);
				}
			}
		}
		//发送短信给最后一组用户
		doSendShortMessage(smsClient, taskId, sourceRecordId, senderId, senderName, senderUnit, smsUnitBusiness, receivers, 0, null, null, message, sendTime, arriveCheck, remark);
		
		//启动短信到达检查
		if(arriveCheck && (sendTime==null || sendTime.before(DateTimeUtils.now()))) { //需要检查短信是否到达,且短信是立即发送的
			//调用定时器,启动短信检查
			final Thread arriveCheckThread = (Thread)arriveCheckThreads.get(smsClient.getName());
			synchronized(arriveCheckThread) {
				arriveCheckThread.notify(); //启动检查
			}
		}
	}
	
	/**
	 * 获取单位配置
	 * @param senderUnitId
	 * @param smsBusinessName
	 * @return
	 */
	private SmsUnitBusiness getSmsUnitBusiness(long senderUnitId, String smsBusinessName) {
		String hql = "select SmsUnitBusiness" +
					 " from SmsUnitBusiness SmsUnitBusiness left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
					 " where SmsUnitConfig.unitId=" + senderUnitId +
					 " and SmsUnitBusiness.businessName='" + JdbcUtils.resetQuot(smsBusinessName) + "'";
		SmsUnitBusiness smsUnitBusiness = (SmsUnitBusiness)getDatabaseService().findRecordByHql(hql);
		if(smsUnitBusiness==null) {
			if(businessMode) { //商业模式
				return null;
			}
			smsUnitBusiness = new SmsUnitBusiness();
			smsUnitBusiness.setBusinessName(smsUnitBusiness.getBusinessName());
			smsUnitBusiness.setEnabled(1);
			smsUnitBusiness.setUnitConfig(new SmsUnitConfig());
			smsUnitBusiness.getUnitConfig().setEnabled(1);
		}
		return smsUnitBusiness;
	}
	
	/**
	 * 获取短信客户端
	 * @param smsUnitBusiness
	 * @return
	 */
	private SmsClient getSmsClient(SmsUnitBusiness smsUnitBusiness) {
		if(smsUnitBusiness==null) {
			return null;
		}
		SmsClient smsClient = null;
		if(smsUnitBusiness.getEnabled()!=1 || smsUnitBusiness.getUnitConfig().getEnabled()!=1) { //停用
			return null;
		}
		else if(smsUnitBusiness.getUnitConfig().getSmsClientName()!=null) {
			smsClient = (SmsClient)ListUtils.findObjectByProperty(smsClients, "name", smsUnitBusiness.getUnitConfig().getSmsClientName());
		}
		if(smsClient==null) {
			smsClient = (SmsClient)smsClients.get(new Random().nextInt(smsClients.size())); //随机选择短信客户端
		}
		return smsClient;
	}
	
	/**
	 * 按接收人获取手机号码,并检验
	 * @param receiverId
	 * @param receiverMobile
	 * @param taskId
	 * @param sourceRecordId
	 * @param checkSent
	 * @return
	 */
	private String getReceiverNumber(long receiverId, String receiverMobile, long taskId, long sourceRecordId, boolean checkSent) {
		String hql = "select SmsCustom.mobile" +
					 " from SmsCustom SmsCustom" +
					 " where SmsCustom.personId=" + receiverId;
		String receiverNumber = (String)getDatabaseService().findRecordByHql(hql);
		if(receiverNumber==null || receiverNumber.isEmpty()) {
			receiverNumber = receiverMobile;
		}
		return validateReceiverNumber(taskId, sourceRecordId, receiverNumber, checkSent);
	}
	
	/**
	 * 检验手机号码
	 * @param taskId
	 * @param sourceRecordId
	 * @param receiverNumber
	 * @param checkSent
	 * @return
	 * @throws ServiceException
	 */
	private String validateReceiverNumber(long taskId, long sourceRecordId, String receiverNumber, boolean checkSent) {
		if(receiverNumber==null) {
			return null;
		}
		receiverNumber = StringUtils.trim(receiverNumber);
		int len = receiverNumber.length();
		//号码检查
		if(receiverNumber.startsWith("0")) { //0打头的,小灵通,号码位数必须是11或12位
			if(len!=11 && len!=12) {
				return null;
			}
		}
		else if(receiverNumber.startsWith("1")) { //手机
			if(len!=11) { //位数是11
				return null;
			}
		}
		else { //不是手机,也不是小灵通
			return null;
		}
		//检查是否已经发送过
		String hql = "select SmsSend.id" +
					 " from SmsSend SmsSend" +
					 " where SmsSend.taskId=" + taskId +
					 " and SmsSend.receiverNumber='" + JdbcUtils.resetQuot(receiverNumber) + "'";
		if(getDatabaseService().findRecordByHql(hql)!=null) {
			return null;
		}
		//按源记录ID检查是否已经发送过
		if(checkSent && sourceRecordId>0) {
			hql = "select SmsSend.id" +
			 	  " from SmsSend SmsSend" +
			 	  " where SmsSend.sourceRecordId=" + sourceRecordId +
			 	  " and SmsSend.receiverNumber='" + JdbcUtils.resetQuot(receiverNumber) + "'";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return null;
			}
		}
		return receiverNumber;
	}
	
	/**
	 * 执行发送短信
	 * @param smsClient
	 * @param taskId
	 * @param sourceRecordId
	 * @param senderId
	 * @param senderName
	 * @param senderUnit
	 * @param smsUnitBusiness
	 * @param receiverId
	 * @param receiverName
	 * @param receiverNumbers
	 * @param receiverNumber
	 * @param message
	 * @param sendTime
	 * @param arriveCheck
	 * @param remark
	 * @throws ServiceException
	 */
	private void doSendShortMessage(final SmsClient smsClient,  final long taskId, final long sourceRecordId,  final long senderId, final String senderName,  final Org senderUnit,  final SmsUnitBusiness smsUnitBusiness, final List receivers, final long newReceiverId, final String newReceiverName, String newReceiverNumber, final String message,  final Timestamp sendTime,  final boolean arriveCheck,  final String remark) throws ServiceException {
		if(newReceiverNumber==null) { //没有指定新号码
			if(receivers.isEmpty()) {
				return;
			}
		}
		else {
			receivers.add(new Object[] {new Long(newReceiverId), newReceiverName, newReceiverNumber});
			if(receivers.size()<smsClient.getSendNumbersLimit()) { //没有到达号码个数上限
				return;
			}
		}
		List receiverNumbers = new ArrayList();
		for(Iterator iterator = receivers.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			receiverNumbers.add(values[2]);
		}
		String messageId = null;
		int splitCount = 0;
		String senderNumber = getSmsNumber(smsClient, smsUnitBusiness);
		String[] messages = splitMessageByChar(message, smsClient.getMaxMessageLength()); //切割信息
		for(int i=0; i<messages.length; i++) {
			splitCount += getSplitCountForCharge(smsClient, messages[i]);
			messageId = smsClient.sendMessage(receiverNumbers, senderNumber, messages[i], sendTime); //执行发送
		}
		//保存短信发送记录
		for(Iterator iterator = receivers.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			SmsSend smsSend = new SmsSend();
			smsSend.setTaskId(taskId); //任务ID
			smsSend.setSourceRecordId(sourceRecordId<=0 ? UUIDLongGenerator.generateId() : sourceRecordId); //源记录ID
			smsSend.setId(UUIDLongGenerator.generateId()); //ID
			smsSend.setSenderId(senderId); //发送人ID
			smsSend.setSenderName(senderName); //发送人姓名
			smsSend.setSenderUnitId(senderUnit==null ? 0 : senderUnit.getId()); //发送人单位ID
			smsSend.setSenderUnit(senderUnit==null ? null : senderUnit.getDirectoryName()); //发送人单位名称
			smsSend.setBusinessId(smsUnitBusiness.getId()); //短信业务ID
			smsSend.setBusinessName(smsUnitBusiness.getBusinessName()); //短信业务名称
			smsSend.setReceiverId(((Number)values[0]).longValue()); //接收人ID
			smsSend.setReceiverName((String)values[1]); //接收人姓名
			smsSend.setReceiverNumber((String)values[2]); //接收人号码
			smsSend.setMessage(message); //短信内容
			smsSend.setSplitCount(splitCount); //分拆条数
			smsSend.setSenderNumber(senderNumber); //短信发送号码
			smsSend.setCreated(DateTimeUtils.now()); //创建时间
			smsSend.setSendTime(sendTime); //发送时间
			smsSend.setMessageId(messageId); //短信ID
			smsSend.setSmsClientName(smsClient.getName()); //短信客户端名称
			smsSend.setRemark(remark); //备注
			smsSend.setArriveCheck(arriveCheck ? '1' : '0');
			getDatabaseService().saveRecord(smsSend);
		}
		receivers.clear();
	}
	
	/**
	 * 计算计费时短信分拆条数
	 * @param smsClient
	 * @param message
	 * @return
	 */
	private int getSplitCountForCharge(SmsClient smsClient, String message) {
		if(message==null || message.isEmpty()) {
			return 0;
		}
		double maxLength = smsClient.getMessageMaxLengthForCharge(StringUtils.isSingleByteCharacters(message));
		return (int)Math.ceil(message.length()/maxLength);
	}

	/**
	 * 按字拆分短信,不允许超过9条
	 * @param message
	 * @param receiverMobile
	 * @param maxMobileChars
	 * @param maxPHSChars
	 * @return
	 */
	private String[] splitMessageByChar(String message, int maxMessageLength) {
		int len = message.length();
		if(len<=maxMessageLength || maxMessageLength==0) { //小于最大长度或不限制
			return new String[]{message};
		}
		maxMessageLength -= 5; //增加的5个字节是页码,如:(1/2)
		int size = Math.min(9, (len + maxMessageLength - 1) / maxMessageLength); //增加的5个字节是页码,如:(1/2)
		String[] messageArray = new String[size];
		for(int i=0; i<size; i++) {
			messageArray[i] = "(" + (i+1) + "/" + size + ")" + message.substring(i*maxMessageLength, Math.min(len, i*maxMessageLength + maxMessageLength));
		}
		return messageArray;
	}
	
	/**
	 * 按字节拆分短信,不允许超过9条
	 * @param message
	 * @param receiverNumber
	 * @param maxMobileBytes 手机最大发送字节数
	 * @param maxPHSBytes 最大小灵通发送字节数
	 * @return
	 */
	protected String[] splitMessageByByte(String message, String receiverNumber, int maxMobileBytes, int maxPHSBytes) {
		int maxBytes = (receiverNumber.startsWith("1") ? maxMobileBytes : maxPHSBytes);
		if(message.getBytes().length<=maxBytes || maxBytes==0) { //小于最大长度或不限制
			return new String[]{message};
		}
		int len = message.length();
		List messages = new ArrayList();
		int beginIndex = 0;
		int endIndex;
		String singleMessage = "";
		maxBytes -= 5; //增加的5个字节是页码,如:(1/2)
		for( ; beginIndex<len && messages.size()<10; ) {
			endIndex = beginIndex + maxBytes / 2;
			if(endIndex>=len) {
				singleMessage = message.substring(beginIndex);
				messages.add(singleMessage);
				break;
			}
			singleMessage = message.substring(beginIndex, endIndex);
			//检查字节数是否已经到上限
			int left = maxBytes - singleMessage.getBytes().length; 
			if(left > 0) { //还可以增加内容
				beginIndex = endIndex;
				for(int append = 0; endIndex<len; endIndex++) {
					append += message.substring(endIndex, endIndex+1).getBytes().length;
					if(append==left) { //已经到上限
						endIndex++;
						break;
					}
					else if(append>left) { //超出上限,回退
						break;
					}
				}
				if(endIndex>beginIndex) {
					singleMessage += message.substring(beginIndex, Math.min(len, endIndex));
				}
			}
			beginIndex = endIndex;
			messages.add(singleMessage);
		}
		int size = Math.min(messages.size(), 9);
		String[] messageArray = new String[size];
		for(int i=0; i<size; i++) {
			messageArray[i] = "(" + (i+1) + "/" + size + ")" + messages.get(i);
		}
		return messageArray;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#getSmsNumber(long, java.lang.String)
	 */
	public String getSmsNumber(long unitId, String smsBusinessName) throws ServiceException {
		SmsUnitBusiness smsUnitBusiness = getSmsUnitBusiness(unitId, smsBusinessName);
		SmsClient smsClient = getSmsClient(smsUnitBusiness);
		return getSmsNumber(smsClient, smsUnitBusiness);
	}
	
	/**
	 * 获取短信号码
	 * @param smsClient
	 * @param smsUnitBusiness
	 * @return
	 * @throws ServiceException
	 */
	private String getSmsNumber(SmsClient smsClient, SmsUnitBusiness smsUnitBusiness) throws ServiceException {
		if(smsClient==null || smsClient.getSmsNumber()==null) {
			return null;
		}
		return smsClient.getSmsNumber() + (smsUnitBusiness.getSmsNumber()==null || smsClient.getAddDigits()<=0 ? "" : smsUnitBusiness.getSmsNumber().substring(0, Math.min(smsClient.getAddDigits(), smsUnitBusiness.getSmsNumber().length())));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("smsClientName".equals(itemsName)) { //短信客户端名称
			List items = new ArrayList();
			for(Iterator iterator = smsClients==null ? null : smsClients.iterator(); iterator!=null && iterator.hasNext();) {
				SmsClient smsClient = (SmsClient)iterator.next();
				items.add(new String[]{smsClient.getName(), smsClient.getName()});
			}
			return items;
		}
		else if("smsBusinessDefine".equals(itemsName)) { //短信业务定义
			List items = new ArrayList();
			for(Iterator iterator = smsBusinesses.iterator(); iterator.hasNext();) {
				SmsBusiness smsBusiness = (SmsBusiness)iterator.next();
				items.add(new String[]{smsBusiness.getName(), smsBusiness.getName()});
			}
			return items;
		}
		else if("smsBusinessName".equals(itemsName)) { //短信业务名称
			String hql = "select SmsBusiness.name, SmsBusiness.name from SmsBusiness SmsBusiness order by SmsBusiness.name";
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("sendableSmsBusinessName".equals(itemsName)) { //有短信发送权限的短信业务名称
			String hql = "select distinct SmsUnitBusiness.businessName, SmsUnitBusiness.businessName" +
						 " from SmsUnitBusiness SmsUnitBusiness" +
						 " left join SmsUnitBusiness.transactors SmsUnitBusinessPrivilege" +
						 " left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
						 " where SmsUnitConfig.unitId=" + sessionInfo.getUnitId() +
						 " and SmsUnitBusinessPrivilege.accessLevel='" + SMS_SEND_EDITOR + "'" +
						 " and SmsUnitBusinessPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
			return getDatabaseService().findRecordsByHql(hql);
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#getLessUseClientName()
	 */
	public String getLessUseClientName() throws ServiceException {
		if(smsClients==null || smsClients.isEmpty()) { //没有短信客户端
			return null;
		}
		String hql = "select SmsUnitConfig.smsClientName, count(SmsUnitConfig.id)" +
					 " from SmsUnitConfig SmsUnitConfig" +
					 " where SmsUnitConfig.smsClientName is not null" +
					 " group by SmsUnitConfig.smsClientName" +
					 " order by count(SmsUnitConfig.id)";
		List totals = getDatabaseService().findRecordsByHql(hql);
		if(totals==null || totals.isEmpty()) {
			return ((SmsClient)smsClients.get(0)).getName();
		}
		List usedClientNames = new ArrayList();
		for(Iterator iterator = totals.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			usedClientNames.add(values[0]);
		}
		for(int i=0; i<smsClients.size(); i++) {
			SmsClient smsClient = (SmsClient)smsClients.get(i);
			if(usedClientNames.indexOf(smsClient.getName())==-1) {
				return smsClient.getName();
			}
		}
		return (String)usedClientNames.get(0);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#isSmsSent(long, java.lang.String)
	 */
	public boolean isSmsSent(long unitId, String businessName) throws ServiceException {
		String hql = "select SmsSend.id" +
					 " from SmsSend SmsSend" +
					 " where SmsSend.senderUnitId=" + unitId +
					 " and SmsSend.businessName='" + businessName + "'";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#isTransactor(long, java.lang.String, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isTransactor(long unitId, String businessName, char transactorType, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select SmsUnitBusinessPrivilege.id" +
					 " from SmsUnitBusiness SmsUnitBusiness" +
					 " left join SmsUnitBusiness.transactors SmsUnitBusinessPrivilege" +
					 " left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
					 " where SmsUnitConfig.unitId=" + unitId +
					 (businessName==null || businessName.isEmpty() ? "" : " and SmsUnitBusiness.businessName='" + JdbcUtils.resetQuot(businessName) + "'") +
					 " and SmsUnitBusinessPrivilege.accessLevel='" + transactorType + "'" +
					 " and SmsUnitBusinessPrivilege.visitorId in (" + sessionInfo.getUserIds() + ")";
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#listTransactors(long, java.lang.String, char)
	 */
	public List listTransactors(long unitId, String businessName, char transactorType) throws ServiceException {
		String hql = "select SmsUnitBusinessPrivilege" +
					 " from SmsUnitBusiness SmsUnitBusiness" +
					 " left join SmsUnitBusiness.transactors SmsUnitBusinessPrivilege" +
					 " left join SmsUnitBusiness.unitConfig SmsUnitConfig" +
					 " where SmsUnitConfig.unitId=" + unitId +
					 (businessName==null || businessName.isEmpty() ? "" : " and SmsUnitBusiness.businessName='" + JdbcUtils.resetQuot(businessName) + "'") +
					 " and SmsUnitBusinessPrivilege.accessLevel='" + transactorType + "'";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/**
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
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
	 * @return the arriveCheckDelaySeconds
	 */
	public int getArriveCheckDelaySeconds() {
		return arriveCheckDelaySeconds;
	}

	/**
	 * @param arriveCheckDelaySeconds the arriveCheckDelaySeconds to set
	 */
	public void setArriveCheckDelaySeconds(int arriveCheckDelaySeconds) {
		this.arriveCheckDelaySeconds = arriveCheckDelaySeconds;
	}

	/**
	 * @return the arriveCheckLimitHours
	 */
	public int getArriveCheckLimitHours() {
		return arriveCheckLimitHours;
	}

	/**
	 * @param arriveCheckLimitHours the arriveCheckLimitHours to set
	 */
	public void setArriveCheckLimitHours(int arriveCheckLimitHours) {
		this.arriveCheckLimitHours = arriveCheckLimitHours;
	}

	/**
	 * @return the businessMode
	 */
	public boolean isBusinessMode() {
		return businessMode;
	}

	/**
	 * @param businessMode the businessMode to set
	 */
	public void setBusinessMode(boolean businessMode) {
		this.businessMode = businessMode;
	}

	/**
	 * @return the smsServiceListenerNames
	 */
	public String getSmsServiceListenerNames() {
		return smsServiceListenerNames;
	}

	/**
	 * @param smsServiceListenerNames the smsServiceListenerNames to set
	 */
	public void setSmsServiceListenerNames(String smsServiceListenerNames) {
		this.smsServiceListenerNames = smsServiceListenerNames;
	}

	/**
	 * @return the smsClients
	 */
	public List getSmsClients() {
		return smsClients;
	}

	/**
	 * @param smsClients the smsClients to set
	 */
	public void setSmsClients(List smsClients) {
		this.smsClients = smsClients;
	}
}