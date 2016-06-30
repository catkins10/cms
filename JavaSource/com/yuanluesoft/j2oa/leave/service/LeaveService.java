/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.j2oa.leave.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.j2oa.leave.pojo.Leave;
import com.yuanluesoft.j2oa.leave.pojo.LeaveConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface LeaveService extends BusinessService {
	
    /**
     * 审批请假条
     * @param leave
     * @param pass
     * @throws ServiceException
     */
    public void approvalLeave(Leave leave, boolean pass) throws ServiceException;
    
    /**
     * 销假
     * @param leave
     * @throws ServiceException
     */
    public void terminateLeave(Leave leave) throws ServiceException;
    
    /**
     * 获取请假配置
     * @return
     * @throws ServiceException
     */
    public LeaveConfig getLeaveConfig() throws ServiceException;
    
    /**
     * 获取用户在指定时间段内已审核通过的请假条
     * @param personId
     * @param beginTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List listPersonalLeaves(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException;
}