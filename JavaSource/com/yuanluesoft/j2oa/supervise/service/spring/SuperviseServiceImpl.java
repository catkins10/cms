package com.yuanluesoft.j2oa.supervise.service.spring;

import java.sql.Timestamp;

import com.yuanluesoft.j2oa.supervise.pojo.Supervise;
import com.yuanluesoft.j2oa.supervise.pojo.SuperviseTimeLimit;
import com.yuanluesoft.j2oa.supervise.service.SuperviseService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class SuperviseServiceImpl extends BusinessServiceImpl implements SuperviseService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.supervise.service.SuperviseService#updateTimeLimit(com.yuanluesoft.j2oa.supervise.pojo.Supervise, java.sql.Timestamp)
	 */
	public void updateTimeLimit(Supervise supervise, Timestamp timeLimit) throws ServiceException {
		if(supervise.getStatus()!=SUPERVISE_STATUS_NEW || timeLimit==null) { //不是新建状态,不更新
			return;
		}
		if(supervise.getTimeLimits()==null) {
			SuperviseTimeLimit superviseTimeLimit = new SuperviseTimeLimit();
			superviseTimeLimit.setId(UUIDLongGenerator.generateId());
			superviseTimeLimit.setSuperviseId(supervise.getId());
			superviseTimeLimit.setCreated(DateTimeUtils.now());
			superviseTimeLimit.setTimeLimit(timeLimit);
			getDatabaseService().saveRecord(superviseTimeLimit);
			return;
		}
		SuperviseTimeLimit superviseTimeLimit = (SuperviseTimeLimit)supervise.getTimeLimits().iterator().next();
		if(!superviseTimeLimit.getTimeLimit().equals(timeLimit)) {
			superviseTimeLimit.setCreated(DateTimeUtils.now());
			superviseTimeLimit.setTimeLimit(timeLimit);
			getDatabaseService().updateRecord(superviseTimeLimit);
		}
	}
}