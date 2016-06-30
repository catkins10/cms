package com.yuanluesoft.cms.smssubscription.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.smssubscription.model.SmsContentDefinition;
import com.yuanluesoft.cms.smssubscription.pojo.SmsSubscription;
import com.yuanluesoft.cms.smssubscription.service.SmsContentCallback;
import com.yuanluesoft.cms.smssubscription.service.SmsContentService;
import com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SmsSubscriptionServiceImpl extends BusinessServiceImpl implements SmsSubscriptionService {
	private SmsService smsService; //短信服务
	private SiteService siteService; //站点服务
	private String contentServiceNames; //短信内容服务名称列表,用逗号分隔
	private int subscribeMessageLength = 500; //订阅的消息长度,默认60
	private ExchangeClient exchangeClient; //数据交换服务
	
	//私有属性
	private List contentDefinitions = null; //内容定义列表
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		smsService.registSmsBusiness("短信订阅", false, false); //注册短信业务
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		exchangeClient.synchUpdate(record, null, 2000);
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		exchangeClient.synchUpdate(record, null, 2000);
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		exchangeClient.synchDelete(record, null, 2000);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService#getContentDescription(java.lang.String, java.lang.String, long)
	 */
	public String getContentDescription(String contentName, String subscribeParameter, long siteId) throws ServiceException {
		if(subscribeParameter==null || subscribeParameter.isEmpty()) {
			return contentName;
		}
		SmsContentDefinition smsContentDefinition = (SmsContentDefinition)ListUtils.findObjectByProperty(listContentDefinitions(), "name", contentName);
		SmsContentService contentService = (SmsContentService)Environment.getService(smsContentDefinition.getContentServiceName());
		return contentService.getContentDescription(contentName, subscribeParameter, siteId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService#subscribe(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public SmsSubscription subscribe(String subscriberNumber, String contentName, String subscribeParameter, long siteId) throws ServiceException {
		//获取订阅记录
		SmsSubscription subscription = getWebSubscription(subscriberNumber, contentName, subscribeParameter, siteId);
		if(subscription!=null) { //订阅过
			return subscription;
		}
		//创建新的订阅记录
		subscription = new SmsSubscription();
		subscription.setId(UUIDLongGenerator.generateId()); //ID
		subscription.setSiteId(siteId); //隶属站点ID
		subscription.setSubscriberNumber(subscriberNumber); //订阅人号码
		subscription.setServiceId(0); //订阅的服务ID
		subscription.setContentName(contentName); //订阅的服务名称
		subscription.setSubscribeParameter(subscribeParameter); //订阅参数
		subscription.setSubscribeTime(DateTimeUtils.now()); //订阅时间
		save(subscription);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService#unsubscribe(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public SmsSubscription unsubscribe(String subscriberNumber, String contentName, String subscribeParameter, long siteId) throws ServiceException {
		SmsSubscription subscription = getWebSubscription(subscriberNumber, contentName, subscribeParameter, siteId);
		if(subscription==null) {
			return null;
		}
		//退订服务
		subscription.setUnsubscribeTime(DateTimeUtils.now()); //退订时间
		subscription.setEndTime(DateTimeUtils.now()); //订阅失效时间
		update(subscription);
		return subscription;
	}
	
	/**
	 * 获取WEB订阅记录
	 * @param subscriberNumber
	 * @param contentName
	 * @param subscribeParameter
	 * @param siteId
	 * @return
	 */
	private SmsSubscription getWebSubscription(String subscriberNumber, String contentName, String subscribeParameter, long siteId) {
		String hql = "from SmsSubscription SmsSubscription" +
		  			 " where SmsSubscription.siteId=" + siteId +
		  			 " and SmsSubscription.serviceId=0" +
		  			 " and SmsSubscription.subscriberNumber='" + subscriberNumber + "'" +
		  			 " and SmsSubscription.contentName='" + contentName + "'" +
		  			 " and SmsSubscription.subscribeParameter" + (subscribeParameter==null || subscribeParameter.isEmpty() ? " is null" : "='" + JdbcUtils.resetQuot(subscribeParameter) + "'") +
		  			 " and SmsSubscription.unsubscribeTime is null";
		return (SmsSubscription)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageArrived(com.yuanluesoft.jeaf.sms.pojo.SmsSend)
	 */
	public void onShortMessageArrived(SmsSend sentMessage) throws ServiceException {
		//不处理
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageReceived(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)
	 */
	public boolean onShortMessageReceived(String senderNumber, String message, Timestamp receiveTime, String receiveNumber, SmsUnitBusiness smsUnitBusiness) throws ServiceException {
		if(smsUnitBusiness!=null && !"短信订阅".equals(smsUnitBusiness.getBusinessName())) {
			return false;
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("SmsSubscriptionService: process received message, content is " + message + ".");
		}
		String filter = message.replaceAll("['%]", " ").toUpperCase(); 
		//处理接收到的短信
		String hql = "from SmsService SmsService " +
					 " where (SmsService.subscribePrefixRule is not null" +
					 "  and '" + filter + "' like concat(SmsService.subscribePrefixRule, '%'))" +
					 " or (SmsService.unsubscribeRule is not null" +
					 "  and '" + filter + "' like concat(SmsService.unsubscribeRule, '%'))";
		com.yuanluesoft.cms.smssubscription.pojo.SmsService service = (com.yuanluesoft.cms.smssubscription.pojo.SmsService)getDatabaseService().findRecordByHql(hql);
		if(service==null) { //没有匹配的规则
			return false; //不处理
		}
		//获取用户原来的订阅记录
		if(filter.startsWith(service.getSubscribePrefixRule())) { //和订阅规则匹配
			processSubscribeRequest(service, senderNumber, message, receiveTime);
		}
		else { //退订
			processUnsubscribeRequest(service, senderNumber, receiveTime);
		}
		return true;
	}
	
	/**
	 * 处理订阅请求
	 * @param service
	 * @param senderNumber
	 * @param message
	 * @param receiveTime
	 * @throws ServiceException
	 */
	private void processSubscribeRequest(com.yuanluesoft.cms.smssubscription.pojo.SmsService service, String senderNumber, String message, Timestamp receiveTime) throws ServiceException {
		//检查是否即时消息
		SmsContentDefinition contentDefinition = findContentDefinition(service.getContentName(), service.getContentServiceName());
		if(contentDefinition==null) { //服务已经不存在
			return;
		}
		if(SmsContentService.SEND_MODE_NEWS.equals(contentDefinition.getSendMode())) { //有新消息时发送,新闻性质
			//获取订阅记录
			SmsSubscription subscription = getSmsSubscription(service.getId(), senderNumber);
			if(subscription!=null) { //订阅过
				return;
			}
			//创建新的订阅记录
			subscription = new SmsSubscription();
			subscription.setId(UUIDLongGenerator.generateId()); //ID
			subscription.setSiteId(service.getSiteId()); //隶属站点ID
			subscription.setSubscriberNumber(senderNumber); //订阅人号码
			subscription.setServiceId(service.getId()); //订阅的服务ID
			subscription.setContentName(service.getContentName()); //订阅的服务名称
			subscription.setSubscribeTime(receiveTime); //订阅时间
			//unsubscribeTime; //退订时间
			//endTime; //订阅失效时间,由系统设置
			save(subscription);
			
			//给用户发送订阅成功短信,TODO:回复内容做到订阅配置里面
			smsService.sendShortMessage(0, "短信订阅服务", ((WebSite)siteService.getDirectory(service.getSiteId())).getOwnerUnitId(), "短信订阅", null, senderNumber, "您已经成功订阅服务：" + service.getContentName() + "。", null, -1, false, null, false);
		}
		else if(SmsContentService.SEND_MODE_REPLY.equals(contentDefinition.getSendMode())) { //即时消息,立即回复
			String replace = "＃#？?＆&／/＠@！!％%（(）)＋+｜|｛{｝}【[】]－-—-＝=；;：:“\"”\"，,。.‘'’'";
			for(int i=replace.length()-1; i>=0; i-=2) {
				message = message.replaceAll("" + replace.charAt(i-1), "" + replace.charAt(i));
			}
			SmsContentService contentService = (SmsContentService)Environment.getService(service.getContentServiceName());
			Map fieldValueMap = null; //字段值列表
			if(contentDefinition.getContentFields()!=null) {
				fieldValueMap = new HashMap();
				String rule = service.getSubscribePrefixRule() + (service.getSubscribeBodyRule()==null ? "" : service.getSubscribeBodyRule());
				//解析规则
				List ruleParts = new ArrayList();
				String[] ruleSplits = rule.split("<");
				for(int i=0; i<ruleSplits.length; i++) {
					int index = ruleSplits[i].indexOf('>');
					String rulePart = "";
					if(index==-1) {
						rulePart = ruleSplits[i];
					}
					else {
						ruleParts.add("FIELD:" + ruleSplits[i].substring(0, index)); //添加字段名称
						rulePart = ruleSplits[i].substring(index + 1);
					}
					if(!rulePart.equals("")) {
						ruleParts.add(rulePart);
					}
				}
				//从短信内容中解析出字段值
				int beginIndex = ((String)ruleParts.get(0)).length(), endIndex = -1;
				for(int i=1; i<ruleParts.size(); i++) {
					String rulePart = (String)ruleParts.get(i);
					if(!rulePart.startsWith("FIELD:")) { //不是字段
						continue;
					}
					//获取字段值字段
					endIndex = i+1<ruleParts.size() ? message.indexOf((String)ruleParts.get(i+1), beginIndex) : -1;
					if(endIndex==-1) {
						fieldValueMap.put(rulePart.substring("FIELD:".length()), message.substring(beginIndex));
						break;
					}
					else {
						fieldValueMap.put(rulePart.substring("FIELD:".length()), message.substring(beginIndex, endIndex));
						beginIndex = endIndex + ((String)ruleParts.get(i+1)).length();
					}
				}
			}
			String reply = contentService.getSmsReplyContent(service.getContentName(), fieldValueMap, message, senderNumber, service.getSiteId());
			if(reply!=null) {
				smsService.sendShortMessage(0, "短信订阅服务", ((WebSite)siteService.getDirectory(service.getSiteId())).getOwnerUnitId(), "短信订阅", null, senderNumber, reply, null, -1, false, null, false);
			}
			//创建新的订阅记录
			SmsSubscription subscription = new SmsSubscription();
			subscription.setId(UUIDLongGenerator.generateId()); //ID
			subscription.setSiteId(service.getSiteId()); //隶属站点ID
			subscription.setSubscriberNumber(senderNumber); //订阅人号码
			subscription.setServiceId(service.getId()); //订阅的服务ID
			subscription.setContentName(service.getContentName()); //订阅的服务名称
			subscription.setSubscribeTime(receiveTime); //订阅时间
			//unsubscribeTime; //退订时间
			//endTime; //订阅失效时间,由系统设置
			save(subscription);
		}
	}
	
	/**
	 * 处理退订请求
	 * @param service
	 * @param senderNumber
	 * @param receiveTime
	 * @throws ServiceException
	 */
	private void processUnsubscribeRequest(com.yuanluesoft.cms.smssubscription.pojo.SmsService service, String senderNumber, Timestamp receiveTime) throws ServiceException {
		//获取订阅记录
		SmsSubscription subscription = getSmsSubscription(service.getId(), senderNumber);
		if(subscription==null) { //没订阅过
			return;
		}
		//退订服务
		subscription.setUnsubscribeTime(receiveTime); //退订时间
		subscription.setEndTime(receiveTime); //订阅失效时间
		update(subscription);
		
		//给用户发送退订成功短信
		smsService.sendShortMessage(0, "短信订阅服务", ((WebSite)siteService.getDirectory(service.getSiteId())).getOwnerUnitId(), "短信订阅", null, senderNumber, "您已经成功退订服务：" + service.getContentName() + "。", null, -1, false, null, false);
	}
	
	/**
	 * 获取订阅记录
	 * @param serviceId
	 * @param subscriberNumber
	 * @return
	 */
	private SmsSubscription getSmsSubscription(long serviceId, String subscriberNumber) {
		String hql = "from SmsSubscription SmsSubscription" +
		  			 " where SmsSubscription.serviceId=" + serviceId +
		  			 " and SmsSubscription.subscriberNumber=" + subscriberNumber +
		  			 " and SmsSubscription.unsubscribeTime is null";
		return (SmsSubscription)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsService#listContentDefinitions()
	 */
	public List listContentDefinitions() throws ServiceException {
		if(contentDefinitions!=null) {
			return contentDefinitions;
		}
		contentDefinitions = new ArrayList();
		String[] serviceNames = contentServiceNames.split(",");
		for(int i=0; i<serviceNames.length; i++) {
			SmsContentService contentService = (SmsContentService)Environment.getService(serviceNames[i]);
			List definitions = contentService.listSmsContentDefinitions();
			for(Iterator iterator = definitions.iterator(); iterator.hasNext();) {
				SmsContentDefinition definition = (SmsContentDefinition)iterator.next();
				definition.setContentServiceName(serviceNames[i]);
				contentDefinitions.add(definition);
			}
		}
		return contentDefinitions;
	}

	/**
	 * 查找匹配的内容定义
	 * @param contentName
	 * @param contentServiceName
	 * @return
	 * @throws ServiceException
	 */
	private SmsContentDefinition findContentDefinition(String contentName, String contentServiceName) throws ServiceException {
		List contentDefinitions = listContentDefinitions();
		for(Iterator iterator = contentDefinitions.iterator(); iterator.hasNext();) {
			SmsContentDefinition contentDefinition = (SmsContentDefinition)iterator.next();
			if(contentDefinition.getName().equals(contentName) && contentDefinition.getContentServiceName().equals(contentServiceName)) {
				return contentDefinition;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService#valideteRule(java.lang.String, java.lang.String, long)
	 */
	public void valideteRule(String subscribePrefixRule, String unsubscribeRule, long smsServiceId) throws ServiceException {
		subscribePrefixRule = subscribePrefixRule.toLowerCase();
		if(unsubscribeRule!=null && !unsubscribeRule.equals("")) {
			unsubscribeRule = unsubscribeRule.toLowerCase();
			if(subscribePrefixRule.startsWith(unsubscribeRule) || unsubscribeRule.startsWith(subscribePrefixRule)) {
				throw new ServiceException("订阅规则和退订规则重复");
			}
		}
		if(isRuleUsed(subscribePrefixRule, smsServiceId)) {
			throw new ServiceException("订阅规则已经被使用");
		}
		if(isRuleUsed(unsubscribeRule, smsServiceId)) {
			throw new ServiceException("退订规则已经被使用");
		}
	}

	/**
	 * 检查规则是否被使用
	 * @param rule
	 * @param smsServiceId
	 * @return
	 * @throws ServiceException
	 */
	private boolean isRuleUsed(String rule, long smsServiceId) throws ServiceException {
		if(rule==null || rule.equals("")) {
			return false;
		}
		rule = rule.toUpperCase(); //不区分大小写,转换为大写
		String hql = "select SmsService.id" +
					 " from SmsService SmsService " +
					 " where SmsService.id!=" + smsServiceId +
					 " and (SmsService.subscribePrefixRule like '" + JdbcUtils.resetQuot(rule) + "%'" +
					 " or SmsService.unsubscribeRule like '" + JdbcUtils.resetQuot(rule) + "%'" +
					 " or (SmsService.subscribePrefixRule is not null" +
					 "     and '" + JdbcUtils.resetQuot(rule) + "' like concat(SmsService.subscribePrefixRule, '%'))" +
					 " or (SmsService.unsubscribeRule is not null" +
					 "     and '" + JdbcUtils.resetQuot(rule) + "' like concat(SmsService.unsubscribeRule, '%'))" +
					 ")";
		return (getDatabaseService().findRecordByHql(hql)!=null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssubscription.service.SmsSubscriptionService#sendMessageToSubscriber(java.lang.String, java.lang.String, long, java.lang.String, com.yuanluesoft.cms.smssubscription.service.SmsContentCallback)
	 */
	public void sendMessageToSubscriber(final String contentServiceName, final String contentName, final long siteId, final String message, final SmsContentCallback smsContentCallback) throws ServiceException {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					//获取订阅者号码列表
					String hql = "select SmsSubscription.subscriberNumber, SmsSubscription.subscribeParameter" +
								 " from SmsSubscription SmsSubscription" +
								 " where SmsSubscription.siteId=" + siteId + //站点检查
								 " and SmsSubscription.unsubscribeTime is null" + //没有退订
								 " and ((SmsSubscription.serviceId=0" + //WEB订阅
								 "   and SmsSubscription.contentName='" + JdbcUtils.resetQuot(contentName) + "')" + //内容名称检查
								 "  or not (select SmsService.id" +
								 "   from SmsService SmsService" +
								 "	 where SmsService.id=SmsSubscription.serviceId" +
								 "   and SmsService.isValid='1'" + //服务有效
								 "   and SmsService.contentServiceName='" + JdbcUtils.resetQuot(contentServiceName) + "'" + //服务名称检查
								 "   and SmsService.contentName='" + JdbcUtils.resetQuot(contentName) + "') is null)"; //内容名称检查
					String slice = (message.length()<=subscribeMessageLength ? message : message.substring(0, subscribeMessageLength-3) + "...");
					for(int i=0; ; i+=200) { //每次处理200个号码
						List values = getDatabaseService().findRecordsByHql(hql, i, 200);
						if(values==null || values.isEmpty()) {
							break;
						}
						String subscriberNumbers = null;
						for(Iterator iterator = values.iterator(); iterator.hasNext();) {
							Object[] objects = (Object[])iterator.next();
							String subscribeParameter = (String)objects[1]; //订阅参数
							if(subscribeParameter==null || subscribeParameter.isEmpty() || (smsContentCallback!=null && smsContentCallback.isSendable(subscribeParameter))) {
								subscriberNumbers = (subscriberNumbers==null ? "" : subscriberNumbers + ",") + objects[0];
							}
						}
						if(subscriberNumbers!=null) {
							smsService.sendShortMessage(0, "短信订阅服务", ((WebSite)siteService.getDirectory(siteId)).getOwnerUnitId(), "短信订阅", null, subscriberNumbers, slice, null, -1, false, contentName, false);
							if(Logger.isDebugEnabled()) {
								Logger.debug("SmsSubscriptionService: send message to subscriber " + subscriberNumbers + ", content is " + slice);
							}
						}
						if(values.size()<200) {
							break;
						}
					}
				}
				catch(Exception e) {
					Logger.exception(e);
				}
				timer.cancel();
			}
		};
		timer.schedule(task, 10);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("contentDefinitions".equals(itemsName)) { //短信内容定义列表
			List contentDefinitions = listContentDefinitions();
			if(contentDefinitions==null) {
				return null;
			}
			List items = new ArrayList();
			for(int i = 0; i < contentDefinitions.size(); i++) {
				SmsContentDefinition definition = (SmsContentDefinition)contentDefinitions.get(i);
				String json = "{contentServiceName:'" + definition.getContentServiceName() + "'," +
							  " description:'" + (definition.getDescription()==null ? "" : definition.getDescription().replaceAll("\r", "").replaceAll("\n", "\\\\n")) + "'," +
							  " contentFields:'" + (definition.getContentFields()==null ? "" : definition.getContentFields()) + "'}";
				items.add(new String[]{definition.getName(), json});
			}
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the contentServiceNames
	 */
	public String getContentServiceNames() {
		return contentServiceNames;
	}

	/**
	 * @param contentServiceNames the contentServiceNames to set
	 */
	public void setContentServiceNames(String contentServiceNames) {
		this.contentServiceNames = contentServiceNames;
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
	 * @return the subscribeMessageLength
	 */
	public int getSubscribeMessageLength() {
		return subscribeMessageLength;
	}

	/**
	 * @param subscribeMessageLength the subscribeMessageLength to set
	 */
	public void setSubscribeMessageLength(int subscribeMessageLength) {
		this.subscribeMessageLength = Math.max(subscribeMessageLength, 50);
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}