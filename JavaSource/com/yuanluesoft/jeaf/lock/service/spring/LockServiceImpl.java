/*
 * Created on 2006-3-31
 *
 */
package com.yuanluesoft.jeaf.lock.service.spring;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.lock.model.LockData;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.lock.service.LockService;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class LockServiceImpl implements LockService {
	private Cache cache; //记录加锁缓存recordLockCache
	private int maxLockMinutes; //最长加锁时间,以分钟为单位
	
	private final String CACHE_HEAD = "lock.";
	private Object mutex = new Object();

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.recordlock.service.RecordLockService#lock(long, long)
	 */
	public  void lock(String lockTarget, long personId, String personName) throws LockException {
		synchronized(mutex) {
			try {
				String key = CACHE_HEAD + lockTarget;
				LockData lockData = (LockData)cache.get(key);
				if(lockData==null || //未被加锁
				   lockData.getLockPersonName()==null || lockData.getLockPersonName().isEmpty() || "null".equals(lockData.getLockPersonName())) { //锁定当前记录的用户身份不明
					lockData = new LockData();
					lockData.setLockPersonId(personId);
					lockData.setLockPersonName(personName);
					lockData.setLockTime(System.currentTimeMillis());
					cache.put(key, lockData);
					if(Logger.isTraceEnabled()) {
						Logger.info("LockService: lock " + lockTarget + " by person " + personName + "/" + personId + ".");
					}
				}
				else if(lockData.getLockPersonId()!=personId &&
						System.currentTimeMillis()-lockData.getLockTime() < maxLockMinutes * 60000) { //已被其他人加锁且尚未超时
					throw new LockException("locked by " + lockData.getLockPersonId());
				}
				else { //被自己加锁或者加锁超时
					lockData.setLockPersonId(personId);
					lockData.setLockTime(System.currentTimeMillis());
					if(Logger.isTraceEnabled()) {
						Logger.info("LockService: update lock time of  " + lockTarget + " by person " + personName + "/" + personId + ".");
					}
				}
			}
			catch(CacheException ce) {
				Logger.exception(ce);
				throw new LockException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.recordlock.service.RecordLockService#unlock(long, long)
	 */
	public void unlock(String lockTarget, long personId) throws LockException {
		synchronized(mutex) {
			try {
				String key = CACHE_HEAD + lockTarget;
				LockData lockData = (LockData)cache.get(key);
				if(lockData!=null && lockData.getLockPersonId()==personId) {
					cache.remove(key);
					if(Logger.isTraceEnabled()) {
						Logger.info("LockService: unlock " + lockTarget + " by person " + personId + ".");
					}
				}
			}
			catch(CacheException ce) {
				Logger.exception(ce);
				throw new LockException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.recordlock.service.RecordLockService#isLockByPerson(long, long)
	 */
	public boolean isLockByPerson(String lockTarget, long personId) throws LockException {
		synchronized(mutex) {
			try {
				String key = CACHE_HEAD + lockTarget;
				LockData lockData = (LockData)cache.get(key);
				if(lockData==null) { //未被加锁
					if(Logger.isTraceEnabled()) {
						Logger.info("LockService: nobody lock " + lockTarget + ".");
					}
					return false;
				}
				else if(lockData.getLockPersonId()!=personId) { //已被其他人加锁
					if(Logger.isTraceEnabled()) {
						Logger.info("LockService: " + lockTarget + " is locked by person " + personId + ".");
					}
					return false;
				}
				else { //被自己加锁或者加锁超时
					lockData.setLockTime(System.currentTimeMillis());
					return true;
				}
			}
			catch(CacheException ce) {
				Logger.exception(ce);
				throw new LockException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.recordlock.service.RecordLockService#getLockPersonName(long)
	 */
	public String getLockPersonName(String lockTarget) throws LockException {
		try {
			String key = CACHE_HEAD + lockTarget;
			LockData lockData = (LockData)cache.get(key);
			return lockData==null ? null : lockData.getLockPersonName();
		}
		catch(Exception ce) {
			Logger.exception(ce);
			throw new LockException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.recordlock.service.RecordLockService#getLockTimeout(long)
	 */
	public Timestamp getLockTimeout(String lockTarget) throws LockException {
		try {
			String key = CACHE_HEAD + lockTarget;
			LockData lockData = (LockData)cache.get(key);
			return lockData==null ? null : new Timestamp(lockData.getLockTime() + maxLockMinutes * 60000);
		}
		catch(CacheException ce) {
			Logger.exception(ce);
			throw new LockException();
		}
	}
	/**
	 * @return Returns the cache.
	 */
	public Cache getCache() {
		return cache;
	}
	/**
	 * @param cache The cache to set.
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	/**
	 * @return Returns the maxLockMinutes.
	 */
	public int getMaxLockMinutes() {
		return maxLockMinutes;
	}
	/**
	 * @param maxLockMinutes The maxLockMinutes to set.
	 */
	public void setMaxLockMinutes(int maxLockMinutes) {
		this.maxLockMinutes = maxLockMinutes;
	}
}
