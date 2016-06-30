package com.yuanluesoft.cms.onlineservice.interactive.accept.service.spring;

import java.util.List;

import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAccept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.OnlineServiceAcceptMissing;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService;
import com.yuanluesoft.cms.onlineservice.interactive.services.spring.OnlineserviceInteractiveServiceImpl;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceAcceptServiceImpl extends OnlineserviceInteractiveServiceImpl implements OnlineServiceAcceptService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#isPageRebuildable(com.yuanluesoft.cms.publicservice.pojo.PublicService)
	 */
	protected boolean isPageRebuildable(PublicService publicService) throws ServiceException {
		if(publicService instanceof OnlineServiceAccept) {
			OnlineServiceAccept accept = (OnlineServiceAccept)publicService;
			if(accept.getCaseAccept()=='1') { //已经受理
				return true;
			}
		}
		return super.isPageRebuildable(publicService);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService#sendMissingNotify(java.lang.String, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void sendMissingNotify(String missingReason, long acceptId, SessionInfo sessionInfo) throws ServiceException {
		OnlineServiceAcceptMissing acceptMissing = new OnlineServiceAcceptMissing();
		acceptMissing.setId(UUIDLongGenerator.generateId()); //ID
		acceptMissing.setAcceptId(acceptId); //收件ID
		acceptMissing.setDescription(missingReason); //缺件说明
		acceptMissing.setCreated(DateTimeUtils.now()); //创建时间
		acceptMissing.setCreatorId(sessionInfo.getUserId()); //通知人ID
		acceptMissing.setCreator(sessionInfo.getUserName()); //通知人
		getDatabaseService().saveRecord(acceptMissing);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService#clearMissingNotifies(long)
	 */
	public void clearMissingNotifies(long acceptId) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from OnlineServiceAcceptMissing OnlineServiceAcceptMissing where OnlineServiceAcceptMissing.acceptId=" + acceptId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService#importCases(java.util.List)
	 */
	public void importCases(List uploadFiles) throws ServiceException {
		
	}
}