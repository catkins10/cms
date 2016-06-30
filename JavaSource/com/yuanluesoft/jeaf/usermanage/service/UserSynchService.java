/*
 * Created on 2006-3-13
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
public interface UserSynchService extends SoapService {

	/**
	 * 保存用户
	 * @param id
	 * @param loginName
	 * @param name
	 * @param password
	 * @param mailAddress
	 * @param familyAddress
	 * @param mobile
	 * @param tel
	 * @param telFamily
	 * @param departmentId
	 * @param otherDepartmentIds
	 * @param isHalt
	 * @param deleteDiaable
	 * @param preassign
	 * @throws ServiceException
	 */
	public void savePerson(long id, String loginName, String name, String password, String mailAddress, String familyAddress, String mobile, String tel, String telFamily, long departmentId, String otherDepartmentIds, boolean isHalt, boolean deleteDiaable, boolean preassign, int priority) throws ServiceException;
	/**
	 * 删除用户
	 * @param loginName
	 * @throws ServiceException
	 */
	public void deletePerson(long id) throws ServiceException;
	
	/**
	 * 保存角色
	 * @param id
	 * @param name
	 * @param memberIds
	 * @throws ServiceException
	 */
	public void saveRole(long id, String name, long[] memberIds) throws ServiceException;
	
	/**
	 * 删除角色
	 * @param name
	 * @throws ServiceException
	 */
	public void deleteRole(long id) throws ServiceException;
	
	/**
     * 添加代理人
     * @param personId
     * @param agentIds
     * @param beginTime
     * @param endTime
     * @param source
     * @throws ServiceException
     */
    public void addAgents(long personId, List agentIds, Timestamp beginTime, Timestamp endTime, String source) throws ServiceException;
    
    /**
     * 删除代理人
     * @param personId
     * @param source
     * @throws ServiceException
     */
    public void removeAgents(long personId, String source) throws ServiceException;
}
