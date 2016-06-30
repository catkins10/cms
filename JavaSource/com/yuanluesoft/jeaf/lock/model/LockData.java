/*
 * Created on 2006-3-31
 *
 */
package com.yuanluesoft.jeaf.lock.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class LockData implements Serializable, Cloneable {
	private long lockPersonId; //加锁人ID
	private String lockPersonName; //加锁人姓名
	private long lockTime;	//加锁时间
	//TODO:IP
	/**
	 * @return Returns the lockPersonId.
	 */
	public long getLockPersonId() {
		return lockPersonId;
	}
	/**
	 * @param lockPersonId The lockPersonId to set.
	 */
	public void setLockPersonId(long lockPersonId) {
		this.lockPersonId = lockPersonId;
	}
	/**
	 * @return Returns the lockTime.
	 */
	public long getLockTime() {
		return lockTime;
	}
	/**
	 * @param lockTime The lockTime to set.
	 */
	public void setLockTime(long lockTime) {
		this.lockTime = lockTime;
	}
	/**
	 * @return the lockPersonName
	 */
	public String getLockPersonName() {
		return lockPersonName;
	}
	/**
	 * @param lockPersonName the lockPersonName to set
	 */
	public void setLockPersonName(String lockPersonName) {
		this.lockPersonName = lockPersonName;
	}
}
