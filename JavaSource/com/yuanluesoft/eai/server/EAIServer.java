package com.yuanluesoft.eai.server;

import java.util.List;

import com.yuanluesoft.eai.client.exception.EAIException;
import com.yuanluesoft.eai.client.model.EAI;


/**
 * EAI服务器
 * @author linchuan
 *
 */
public interface EAIServer {

	/**
	 * 根据用户ID获取EAI
	 * @param personId
	 * @return
	 * @throws EAIException
	 */
	public EAI getEAI(String userLoginName) throws EAIException;
	
    /**
     * 是否EAI管理员
     * @param userLoginName
     * @return
     * @throws EAIException
     */
    public boolean isEAIManager(String userLoginName) throws EAIException;
    
    /**
     * 是否EAI访问者
     * @param userLoginName
     * @return
     * @throws EAIException
     */
	public boolean isEAIVisitor(String userLoginName) throws EAIException;
	
    /**
     * 获取应用名称
     * @param applicationPath
     * @return
     * @throws EAIException
     */
    public String getApplicationTitle(String applicationSetName, String applicationName) throws EAIException;
    
    /**
     * 获取用户对应用的权限
     * @param userLoginName
     * @param applicationPath
     * @return
     * @throws ServiceException
     */
    public List listApplicationPrivileges(String userLoginName, String applicationSetName, String applicationName) throws EAIException;
    
    /**
     * 获取有指定权限的用户列表
     * @param applicationName
     * @param manageUnit
     * @return
     * @throws EAIException
     */
    public List listVisitors(String applicationSetName, String applicationName, String manageUnit) throws EAIException;
    
    /**
     * 获取用户对链接的权限
     * @param userLoginName
     * @param linkName
     * @return
     * @throws EAIException
     */
    public List listLinkPrivileges(String userLoginName, String linkName) throws EAIException;
    
    /**
     * 获取流程列表
     * @param applicationName
     * @return
     * @throws EAIException
     */
    public List listWorkflows(String applicationSetName, String applicationName) throws EAIException;
    
    /**
     * 更新应用集合
     * @param applicationSetXML
     * @throws EAIException
     */
    public void updateApplicationSet(String applicationSetName, String applicationSetXML) throws EAIException;
}