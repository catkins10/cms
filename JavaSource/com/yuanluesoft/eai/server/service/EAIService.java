/*
 * Created on 2006-5-26
 *
 */
package com.yuanluesoft.eai.server.service;

import java.util.List;

import com.yuanluesoft.eai.server.model.EAI;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.soap.SoapService;

/**
 * EAI服务
 * @author linchuan
 *
 */
public interface EAIService extends SoapService {
    
    /**
     * 获取EAI配置
     * @return
     * @throws ServiceException
     */
    public EAI getEAI() throws ServiceException;
    
    /**
     * 重载EAI配置
     * @throws ServiceException
     */
    public void reloadEAI() throws ServiceException;
    
    /**
     * 根据用户登录名获取EAI,并过滤掉没有访问权限的元素
     * @param userLoginName
     * @return
     * @throws ServiceException
     */
    public com.yuanluesoft.eai.client.model.EAI getEAI(String userLoginName) throws ServiceException;
    
    /**
     * 获取用户对指定应用的访问权限列表
     * @param sessionInfo
     * @param applicationSetName
     * @param applicationName
     * @return
     * @throws ServiceException
     */
    public List listApplicationPrivileges(String userLoginName, String applicationSetName, String applicationName) throws ServiceException;

    /**
     * 获取应用名称
     * @param applicationSetName
     * @param applicationName
     * @return
     * @throws ServiceException
     */
    public String getApplicationTitle(String applicationSetName, String applicationName) throws ServiceException;
    
    /**
     * 获取流程列表
     * @param applicationSetName
     * @param applicationName
     * @return
     * @throws ServiceException
     */
    public List listWorkflows(String applicationSetName, String applicationName) throws ServiceException;
    
    /**
     * 获取用户对链接的权限
     * @param userLoginName
     * @param applicationSetName
     * @param linkName
     * @return
     * @throws ServiceException
     */
    public List listLinkPrivileges(String userLoginName, String linkId) throws ServiceException;
    
    /**
     * 按链接名称获取用户对链接的权限
     * @param userLoginName
     * @param linkName
     * @return
     * @throws ServiceException
     */
    public List listLinkPrivilegesByName(String userLoginName, String linkName) throws ServiceException;
    
    /**
     * 判断用户是否EAI的管理员
     * @param userLoginName
     * @return
     * @throws ServiceException
     */
    public boolean isEAIManager(String userLoginName) throws ServiceException;
    
    /**
     * 判断用户是否有EAI的访问权限
     * @param userLoginName
     * @return
     * @throws ServiceException
     */
    public boolean isEAIVisitor(String userLoginName) throws ServiceException;
    
    /**
     * 获取管理单元访问者列表
     * @param applicationSetName
     * @param applicationName
     * @param manageUnit
     * @return
     * @throws ServiceException
     */
    public List listVisitors(String applicationSetName, String applicationName, String manageUnit) throws ServiceException;
}