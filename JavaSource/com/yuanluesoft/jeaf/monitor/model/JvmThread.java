package com.yuanluesoft.jeaf.monitor.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class JvmThread implements Serializable {
	private String threadName; //名称
	private long threadId; //ID
	private long blockedTime;
	private long blockedCount;
	private long waitedTime;
	private long waitedCount;
	private String lockClassName;
    private int lockIdentityHashCode;
	private String lockName;
	private long lockOwnerId;
	private String lockOwnerName;
	private boolean inNative;
	private boolean suspended;
	private String threadState;
	private String stackTrace; //堆栈追踪
	
	/**
	 * @return the blockedCount
	 */
	public long getBlockedCount() {
		return blockedCount;
	}
	/**
	 * @param blockedCount the blockedCount to set
	 */
	public void setBlockedCount(long blockedCount) {
		this.blockedCount = blockedCount;
	}
	/**
	 * @return the blockedTime
	 */
	public long getBlockedTime() {
		return blockedTime;
	}
	/**
	 * @param blockedTime the blockedTime to set
	 */
	public void setBlockedTime(long blockedTime) {
		this.blockedTime = blockedTime;
	}
	/**
	 * @return the inNative
	 */
	public boolean isInNative() {
		return inNative;
	}
	/**
	 * @param inNative the inNative to set
	 */
	public void setInNative(boolean inNative) {
		this.inNative = inNative;
	}
	/**
	 * @return the lockClassName
	 */
	public String getLockClassName() {
		return lockClassName;
	}
	/**
	 * @param lockClassName the lockClassName to set
	 */
	public void setLockClassName(String lockClassName) {
		this.lockClassName = lockClassName;
	}
	/**
	 * @return the lockIdentityHashCode
	 */
	public int getLockIdentityHashCode() {
		return lockIdentityHashCode;
	}
	/**
	 * @param lockIdentityHashCode the lockIdentityHashCode to set
	 */
	public void setLockIdentityHashCode(int lockIdentityHashCode) {
		this.lockIdentityHashCode = lockIdentityHashCode;
	}
	/**
	 * @return the lockName
	 */
	public String getLockName() {
		return lockName;
	}
	/**
	 * @param lockName the lockName to set
	 */
	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	/**
	 * @return the lockOwnerId
	 */
	public long getLockOwnerId() {
		return lockOwnerId;
	}
	/**
	 * @param lockOwnerId the lockOwnerId to set
	 */
	public void setLockOwnerId(long lockOwnerId) {
		this.lockOwnerId = lockOwnerId;
	}
	/**
	 * @return the lockOwnerName
	 */
	public String getLockOwnerName() {
		return lockOwnerName;
	}
	/**
	 * @param lockOwnerName the lockOwnerName to set
	 */
	public void setLockOwnerName(String lockOwnerName) {
		this.lockOwnerName = lockOwnerName;
	}
	/**
	 * @return the stackTrace
	 */
	public String getStackTrace() {
		return stackTrace;
	}
	/**
	 * @param stackTrace the stackTrace to set
	 */
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	/**
	 * @return the suspended
	 */
	public boolean isSuspended() {
		return suspended;
	}
	/**
	 * @param suspended the suspended to set
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}
	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}
	/**
	 * @param threadName the threadName to set
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	/**
	 * @return the threadState
	 */
	public String getThreadState() {
		return threadState;
	}
	/**
	 * @param threadState the threadState to set
	 */
	public void setThreadState(String threadState) {
		this.threadState = threadState;
	}
	/**
	 * @return the waitedCount
	 */
	public long getWaitedCount() {
		return waitedCount;
	}
	/**
	 * @param waitedCount the waitedCount to set
	 */
	public void setWaitedCount(long waitedCount) {
		this.waitedCount = waitedCount;
	}
	/**
	 * @return the waitedTime
	 */
	public long getWaitedTime() {
		return waitedTime;
	}
	/**
	 * @param waitedTime the waitedTime to set
	 */
	public void setWaitedTime(long waitedTime) {
		this.waitedTime = waitedTime;
	}
}