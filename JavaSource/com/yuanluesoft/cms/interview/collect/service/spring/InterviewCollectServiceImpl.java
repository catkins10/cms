package com.yuanluesoft.cms.interview.collect.service.spring;

import com.yuanluesoft.cms.interview.collect.pojo.InterviewCollectPrologue;
import com.yuanluesoft.cms.interview.collect.service.InterviewCollectService;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewCollectServiceImpl extends PublicServiceImpl implements InterviewCollectService  {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.interview.collect.service.InterviewCollectService#getCollectPrologue(long)
	 */
	public InterviewCollectPrologue getCollectPrologue(long siteId) throws ServiceException {
		return (InterviewCollectPrologue)getDatabaseService().findRecordByHql("from InterviewCollectPrologue InterviewCollectPrologue where InterviewCollectPrologue.siteId=" + siteId);
	}
}