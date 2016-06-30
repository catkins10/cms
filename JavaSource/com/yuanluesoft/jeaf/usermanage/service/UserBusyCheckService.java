package com.yuanluesoft.jeaf.usermanage.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface UserBusyCheckService {
	
    /**
     * 检查用户在指定时间范围里是否忙碌，不忙则返回null，忙则返回事件描述列表
     * @param personId
     * @param beginTime
     * @param endTime
     * @return
     * @throws ServiceException
     */
    public List userBusyCheck(long personId, Timestamp beginTime, Timestamp endTime) throws ServiceException;
}
