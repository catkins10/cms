/*
 * Created on 2006-3-31
 *
 */
package com.yuanluesoft.jeaf.lock.service;

import java.sql.Timestamp;

/**
 * 
 * @author linchuan
 *
 */
public interface LockService {
	/**
	 * 加锁记录,如果已经被加锁且加锁人不是当前用户,则抛出异常,如果已经被加锁且加锁人是当前用户,则重新设定加锁时间
	 * @param lockTarget
	 * @param personId
	 * @param personName
	 * @return
	 * @throws LockException
	 */
	public void lock(String lockTarget, long personId, String personName) throws LockException;
	
	/**
	 * 解锁
	 * @param lockTarget
	 * @param personId
	 * @throws LockException
	 */
	public void unlock(String lockTarget, long personId) throws LockException;
	
	/**
	 * 检查记录是否被本人加锁,如果是还需要重置加锁时间
	 * @param lockTarget
	 * @param personId
	 * @return
	 * @throws LockException
	 */
	public boolean isLockByPerson(String lockTarget, long personId) throws LockException;
	
	/**
	 * 获取加锁人姓名
	 * @param lockTarget
	 * @throws LockException
	 */
	public String getLockPersonName(String lockTarget) throws LockException;
	
	/**
	 * 获取加锁超时时间
	 * @param lockTarget
	 * @return
	 * @throws LockException
	 */
	public Timestamp getLockTimeout(String lockTarget) throws LockException;
}
