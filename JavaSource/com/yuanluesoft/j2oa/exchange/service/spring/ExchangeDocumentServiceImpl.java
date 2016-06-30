package com.yuanluesoft.j2oa.exchange.service.spring;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocumentUndo;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocumentUnit;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage;
import com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgCategory;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
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
public class ExchangeDocumentServiceImpl extends BusinessServiceImpl implements ExchangeDocumentService {
	private OrgService orgService; //组织机构服务
	private MessageService messageService; //消息中心
	private String notifyFormat = "请登录公文交换平台签收《{PARAMETER:subject}》"; //消息通知格式
	private int notifyDays = 2; //通知间隔天数
	private int notifyTimes = 3; //通知次数
	private String notifySender = "公文交换平台"; //通知人

	/**
	 * 初始化
	 *
	 */
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("sendDocument", "发布公文", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
			orgService.appendDirectoryPopedomType("receiveDocument", "签收公文", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService#getDocumentBySourceRecordId(java.lang.String, long)
	 */
	public ExchangeDocument getDocumentBySourceRecordId(String sourceRecordId, long unitId) throws ServiceException {
		String hql = "from ExchangeDocument ExchangeDocument" +
		 			 " where ExchangeDocument.creatorUnitId=" + unitId +
		 			 " and ExchangeDocument.sourceRecordId='" + JdbcUtils.resetQuot(sourceRecordId) + "'";
		return (ExchangeDocument)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(ExchangeDocument.class));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService#issueDocument(com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issueDocument(final ExchangeDocument document, SessionInfo sessionInfo) throws ServiceException {
		final Set exchangeUnits = new HashSet();
		//添加主送单位
		addExchangeUnits(document, exchangeUnits, document.getMainSend(), "主送单位");
		//添加抄送单位
		addExchangeUnits(document, exchangeUnits, document.getCopySend(), "抄送单位");
		//添加其他单位
		addExchangeUnits(document, exchangeUnits, document.getOtherSend(), "其他单位");
		
		//将之前已经签收过且不在新名单中的单位,设置为"其他单位"
		for(Iterator iterator = document.getExchangeUnits()==null ? null : document.getExchangeUnits().iterator(); iterator!=null && iterator.hasNext();) {
			ExchangeDocumentUnit signedUnit = (ExchangeDocumentUnit)iterator.next();
			delete(signedUnit); //删除
			signedUnit.setId(UUIDLongGenerator.generateId());
			signedUnit.setUnitType("其他单位");
			save(signedUnit); //重新保存
			exchangeUnits.add(signedUnit);
		}
		document.setExchangeUnits(exchangeUnits);
		
		//更新公文
		document.setIssue('1'); //设置为已发布
		document.setIssuePerson(sessionInfo.getUserName()); //发布人
		document.setIssuePersonId(sessionInfo.getUserId()); //发布人ID
		document.setIssueTime(DateTimeUtils.now()); //发布时间
		update(document);
		
		//发送通知
		new Thread() {
			public void run() {
				for(Iterator iterator = exchangeUnits.iterator(); iterator.hasNext();) {
					ExchangeDocumentUnit unit = (ExchangeDocumentUnit)iterator.next();
					if(unit.getSignTime()==null) {
						try {
							sendMessageToReceivers(document, unit);
						}
						catch (ServiceException e) {
							Logger.exception(e);
						}
					}
				}
			}
		}.start();
	}
	
	/**
	 * 添加接收单位
	 * @param document
	 * @param exchangeUnits
	 * @param unitIds
	 * @param units
	 * @param unitType
	 * @throws ServiceException
	 */
	private void addExchangeUnits(ExchangeDocument document, Set exchangeUnits, String unitNames, String unitType) throws ServiceException {
		if(unitNames==null || unitNames.equals("")) {
			return;
		}
		//获取组织机构
		List orgs = orgService.listDirectoriesByNames(0, unitNames.replaceAll("[，;；、]", ","), false);
		if(orgs==null || orgs.isEmpty()) {
			return;
		}
		for(int i=orgs.size()-1; i>=0; i--) {
			Org org = (Org)orgs.get(i);
			if(org instanceof OrgCategory) {
				orgs.remove(i);
				i--;
				List childOrgs = orgService.listAllChildDirectories(org.getId(), null, false);
				if(childOrgs!=null && !childOrgs.isEmpty()) {
					orgs.addAll(childOrgs);
				}
			}
		}
		for(Iterator iterator = orgs.iterator(); iterator.hasNext();) {
			Org org = (Org)iterator.next();
			if(!(org instanceof Unit)) { //不是单位
				continue;
			}
			//检查是否本单位,或者已经添加过
			if(org.getId()==document.getCreatorUnitId() || ListUtils.findObjectByProperty(exchangeUnits, "unitId", new Long(org.getId()))!=null) {
				continue;
			}
			ExchangeDocumentUnit unit = new ExchangeDocumentUnit();
			unit.setId(UUIDLongGenerator.generateId()); //ID
			unit.setDocumentId(document.getId()); //公文ID
			unit.setUnitId(org.getId()); //单位ID
			unit.setUnitName(org.getDirectoryName()); //单位名称
			unit.setUnitType(unitType); //单位类别
			//检查是否被签收过
			ExchangeDocumentUnit signedUnit = (ExchangeDocumentUnit)ListUtils.removeObjectByProperty(document.getExchangeUnits(), "unitId", new Long(org.getId()));
			if(signedUnit!=null) {
				unit.setSignTime(signedUnit.getSignTime()); //签收时间
				unit.setSignPerson(signedUnit.getSignPerson()); //签收人
				unit.setSignPersonId(signedUnit.getSignPersonId()); //签收人ID
				delete(signedUnit); //删除签收记录
			}
			save(unit);
			exchangeUnits.add(unit);
		}
	}
	
	/**
	 * 发送通知
	 * @param document
	 * @param unitId
	 */
	private void sendMessageToReceivers(ExchangeDocument document, ExchangeDocumentUnit unit) throws ServiceException {
		if(notifyTimes<=0) {
			return;
		}
		//生成消息
		String message = StringUtils.fillParameters(notifyFormat, false, false, false, "utf-8", document, null, null);
		String href = Environment.getContextPath() + "/j2oa/exchange/admin/document.shtml?id=" + document.getId();
		notifyDays = Math.max(1, notifyDays);
		String notifyCyclicMode = notifyTimes==1 ? MessageService.CYCLIC_MODE_NO_CYCLIC : MessageService.CYCLIC_MODE_BY_DAY;
		Timestamp notifyEnd = DateTimeUtils.add(DateTimeUtils.now(), Calendar.DAY_OF_MONTH, 0);
		//获取公文接收人列表
		List receivers = orgService.listDirectoryVisitors(unit.getUnitId(), "receiveDocument", true, true, 10);
		for(Iterator iterator = receivers==null ? null : receivers.iterator(); iterator!=null && iterator.hasNext();) {
			Record receiver = (Record)iterator.next();
			if(receiver instanceof Person) {
				messageService.sendMessageToPerson(receiver.getId(), message, 0, notifySender, MessageService.MESSAGE_PRIORITY_NORMAL, unit.getId(), href, null, null, notifyCyclicMode, notifyDays, notifyEnd);
			}
			else if(receiver instanceof Org) {
				messageService.sendMessageToOrg(receiver.getId(), true, message, 0, notifySender, MessageService.MESSAGE_PRIORITY_NORMAL, unit.getId(), href, null, null, notifyCyclicMode, notifyDays, notifyEnd);
			}
			else if(receiver instanceof Role) {
				messageService.sendMessageToRole(receiver.getId(), message, 0, notifySender, MessageService.MESSAGE_PRIORITY_NORMAL, unit.getId(), href, null, null, notifyCyclicMode, notifyDays, notifyEnd);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService#unissueDocument(com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void unissueDocument(ExchangeDocument document, String reason, boolean resign, SessionInfo sessionInfo) throws ServiceException {
		if(document.getIssue()!='1') {
			return;
		}
		//记录撤销原因
		ExchangeDocumentUndo undo = new ExchangeDocumentUndo();
		undo.setId(UUIDLongGenerator.generateId()); //ID
		undo.setDocumentId(document.getId()); //公文ID
		undo.setUndoReason(reason); //撤销发布的原因
		undo.setResign(resign ? '1' : '0'); //是否重新签收
		undo.setUndoTime(DateTimeUtils.now()); //撤销时间
		undo.setUndoPerson(sessionInfo.getUserName()); //撤销人
		undo.setUndoPersonId(sessionInfo.getUserId()); //撤销人ID
		getDatabaseService().saveRecord(undo); //保存
		//删除接收单位记录
		for(Iterator iterator = document.getExchangeUnits()==null ? null : document.getExchangeUnits().iterator(); iterator!=null && iterator.hasNext();) {
			ExchangeDocumentUnit unit = (ExchangeDocumentUnit)iterator.next();
			if(resign || unit.getSignTime()==null) { //需要重新签收,或者未签收
				getDatabaseService().deleteRecord(unit); //删除接收单位
				messageService.removeMessages(unit.getId()); //移除消息通知
			}
		}
		document.setIssue('0'); //设置为未发布
		document.setIssueTime(DateTimeUtils.now()); //发布时间
		getDatabaseService().updateRecord(document); //保存公文
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService#signDocument(com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void signDocument(ExchangeDocument document, SessionInfo sessionInfo) throws ServiceException {
		for(Iterator iterator = document.getExchangeUnits().iterator(); iterator.hasNext();) {
			ExchangeDocumentUnit unit = (ExchangeDocumentUnit)iterator.next();
			if(unit.getUnitId()!=sessionInfo.getUnitId()) {
				continue;
			}
			if(orgService.checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) {
				unit.setSignTime(DateTimeUtils.now());
				unit.setSignPerson(sessionInfo.getUserName());
				unit.setSignPersonId(sessionInfo.getUserId());
				update(unit); //更新接收单位
				messageService.removeMessages(unit.getId()); //移除消息通知
			}
			break;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService#getReplyMessage(long)
	 */
	public ExchangeMessage getReplyMessage(long messageId) throws ServiceException {
		String hql = "from ExchangeMessage ExchangeMessage where ExchangeMessage.replyMessageId=" + messageId;
		return (ExchangeMessage)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(ExchangeMessage.class));
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
	 * @return the notifyFormat
	 */
	public String getNotifyFormat() {
		return notifyFormat;
	}

	/**
	 * @param notifyFormat the notifyFormat to set
	 */
	public void setNotifyFormat(String notifyFormat) {
		this.notifyFormat = notifyFormat;
	}

	/**
	 * @return the notifyDays
	 */
	public int getNotifyDays() {
		return notifyDays;
	}

	/**
	 * @param notifyDays the notifyDays to set
	 */
	public void setNotifyDays(int notifyDays) {
		this.notifyDays = notifyDays;
	}

	/**
	 * @return the notifyTimes
	 */
	public int getNotifyTimes() {
		return notifyTimes;
	}

	/**
	 * @param notifyTimes the notifyTimes to set
	 */
	public void setNotifyTimes(int notifyTimes) {
		this.notifyTimes = notifyTimes;
	}

	/**
	 * @return the notifySender
	 */
	public String getNotifySender() {
		return notifySender;
	}

	/**
	 * @param notifySender the notifySender to set
	 */
	public void setNotifySender(String notifySender) {
		this.notifySender = notifySender;
	}
}