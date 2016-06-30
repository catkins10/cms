package com.yuanluesoft.jeaf.usermanage.replicator;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 用户复制
 * @author xujianxiong
 *
 */
public interface UserReplicateService {
	
	/**
	 * 用户同步
	 * @throws ServiceException
	 */
    public void replicate() throws ServiceException;
    
    /**
     * 注册用户
     * @param person
     * @throws ServiceException
     */
    public void registPerson(Person person) throws ServiceException;
    
    /**
     * 修改用户信息
     * @param person
     * @throws ServiceException
     */
    public void modifyPerson(Person person) throws ServiceException;
    
    /**
     * 删除用户
     * @param personId
     * @throws ServiceException
     */
    public void deletePerson(long personId) throws ServiceException;
    
    /**
     * 注册组织机构
     * @param org
     * @throws ServiceException
     */
    public void registOrg(Org org) throws ServiceException;
    
    /**
     * 修改组织机构信息
     * @param org
     * @throws ServiceException
     */
    public void modifyOrg(Org org) throws ServiceException;
    
    /**
     * 删除组织机构
     * @param orgId
     * @param parentOrgId
     * @throws ServiceException
     */
    public void deleteOrg(long orgId, long parentOrgId) throws ServiceException;
}