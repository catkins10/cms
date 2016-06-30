package com.yuanluesoft.j2oa.bulletin.service.spring;

import java.util.List;

import com.yuanluesoft.j2oa.bulletin.pojo.Bulletin;
import com.yuanluesoft.j2oa.bulletin.service.BulletinService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class BulletinServiceImpl extends BusinessServiceImpl implements BulletinService {
	private RecordControlService recordControlService; //记录控制服务
	private MessageService messageService; //消息通知服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.bulletin.service.BulletinService#issueBulletin(com.yuanluesoft.j2oa.bulletin.pojo.Bulletin, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issueBulletin(Bulletin bulletin, SessionInfo sessionInfo) throws ServiceException {
		bulletin.setIssued('1');
		bulletin.setIssueTime(DateTimeUtils.now());
		String href = Environment.getContextPath() + "/j2oa/bulletin/bulletin.shtml?id=" + bulletin.getId();
		if(recordControlService.listVisitors(bulletin.getId(), Bulletin.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD)==null) { //允许所有人访问
			recordControlService.appendVisitor(bulletin.getId(), Bulletin.class.getName(), 0, RecordControlService.ACCESS_LEVEL_READONLY);
			//发送消息
			//messageService.sendMessageToOrg(0, true, "公告:" + bulletin.getSubject(), sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, bulletin.getId(), href, null, null, null, 0, null);
		}
		else {
			List readers = recordControlService.copyVisitors(bulletin.getId(), bulletin.getId(), RecordControlService.ACCESS_LEVEL_PREREAD, RecordControlService.ACCESS_LEVEL_READONLY, Bulletin.class.getName());
			//发送消息
			messageService.sendMessageToVisitors(readers, true, "公告:" + bulletin.getSubject(), sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, bulletin.getId(), href, null, null, null, 0, null);
		}
		update(bulletin);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.bulletin.service.BulletinService#unissueBulletin(com.yuanluesoft.j2oa.bulletin.pojo.Bulletin, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void unissueBulletin(Bulletin bulletin, SessionInfo sessionInfo) throws ServiceException {
		bulletin.setIssued('0');
		update(bulletin);
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
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}
}