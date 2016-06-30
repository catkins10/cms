/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.jeaf.usermanage.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.soap.SoapService;

/**
 * 
 * @author linchuan
 *
 */
public interface AgentService extends SoapService {
    
    /**
     * 添加代理人
     * @param personId
     * @param agentIds
     * @param beginTime
     * @param endTime
     * @param source
     * @throws ServiceException
     */
    public void setAgents(long personId, String agentIds, Timestamp beginTime, Timestamp endTime, String source) throws ServiceException;
    
    /**
     * 删除代理人
     * @param personId
     * @param source
     * @throws ServiceException
     */
    public void removeAgents(long personId, String source) throws ServiceException;
    
    /**
     * 获取全部代理人(Person)列表
     * @param personIds
     * @return
     * @throws ServiceException
     */
    public List listAgents(String personIds) throws ServiceException;
    
    /**
     * 获取代理人(Person)列表
     * @param personIds
     * @param source
     * @return
     * @throws ServiceException
     */
    public List listAgents(String personIds, String source) throws ServiceException;
    
    /**
     * 检查用户是否是toCheckPersonIds的代理人
     * @param personIds
     * @param toCheckPersonIds
     * @return
     * @throws ServiceException
     */
    public boolean isAgent(String agentPersonIds, String toCheckPersonIds) throws ServiceException;
}