/*
 * Created on 2006-6-30
 *
 */
package com.yuanluesoft.j2oa.businesstrip.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.j2oa.businesstrip.pojo.Businesstrip;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface BusinesstripService extends BusinessService {
	
    /**
     * 审批出差
     * @param businessTrip
     * @param workItemId
     * @param pass
     * @param sessionInfo
     * @throws ServiceException
     */
    public void approvalBusinesstrip(Businesstrip businessTrip, String workItemId, boolean pass, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 获取用户在指定时间段内已审核通过的出差申请
     * @param personId
     * @param beginTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List listPersonalBusinesstrips(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException;
}
