package com.yuanluesoft.workflow.client.pool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.workflow.client.WorkflowClient;
import com.yuanluesoft.workflow.client.exception.WorkflowException;
import com.yuanluesoft.workflow.client.model.wapi.ConnectInfo;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConnectionPool {
	private HashMap workflowConnections = new HashMap();
	private ClearTimeoutConnectionsThread clearTimeoutConnectionsThread; //线程:清除超时连接
	
	public WorkflowConnectionPool() {
		if(Logger.isDebugEnabled()) {
			Logger.debug("WorkflowConnectionPool: started");
		}
		clearTimeoutConnectionsThread = new ClearTimeoutConnectionsThread();
		clearTimeoutConnectionsThread.start();
	}
	
	/**
	 * 获取工作流客户端
	 * @param connectInfo
	 * @return
	 * @throws ServiceException
	 */
	public WorkflowClient getWorkflowClient(ConnectInfo connectInfo) throws ServiceException {
		synchronized(workflowConnections) {
			//按进程查找客户端
			WorkflowConnection workflowConnection = (WorkflowConnection)workflowConnections.get(new Long(Thread.currentThread().getId()));
			if(workflowConnection==null) {
				workflowConnection = new WorkflowConnection();
				workflowConnection.setWorkflowClient((WorkflowClient)Environment.getService("workflowClient"));
				workflowConnections.put(new Long(Thread.currentThread().getId()), workflowConnection);
				if(Logger.isDebugEnabled()) {
					Logger.debug("WorkflowConnectionPool: create new workflow client for thread " + Thread.currentThread().getId() + ", " + workflowConnections.size() + " conections in pool.");
				}
			}
			workflowConnection.setLastUsed(DateTimeUtils.now()); //设置最后使用时间
			if(workflowConnection.getCurrentUserId()!=connectInfo.getUserId()) { //当前正在连接的用户不是需要的用户
				try {
					if(workflowConnection.getCurrentUserId()!=0) {
						workflowConnection.getWorkflowClient().disconnect(); //断开原来的连接
					}
					workflowConnection.getWorkflowClient().connect(connectInfo);
				}
				catch (WorkflowException e) {
					Logger.exception(e);
					throw new ServiceException();
				}
			}
			return workflowConnection.workflowClient;
		}
	}
	
	/**
	 * 工作流连接
	 * @author linchuan
	 *
	 */
	private class WorkflowConnection {
		private WorkflowClient workflowClient; //客户端
		private Timestamp lastUsed; //最后使用时间
		private long currentUserId; //当前连接的用户ID
		/**
		 * @return the currentUserId
		 */
		public long getCurrentUserId() {
			return currentUserId;
		}
		/**
		 * @param currentUserId the currentUserId to set
		 */
		public void setCurrentUserId(long currentUserId) {
			this.currentUserId = currentUserId;
		}
		/**
		 * @return the lastUsed
		 */
		public Timestamp getLastUsed() {
			return lastUsed;
		}
		/**
		 * @param lastUsed the lastUsed to set
		 */
		public void setLastUsed(Timestamp lastUsed) {
			this.lastUsed = lastUsed;
		}
		/**
		 * @return the workflowClient
		 */
		public WorkflowClient getWorkflowClient() {
			return workflowClient;
		}
		/**
		 * @param workflowClient the workflowClient to set
		 */
		public void setWorkflowClient(WorkflowClient workflowClient) {
			this.workflowClient = workflowClient;
		}
	}
	
	/**
	 * 线程:清除超时连接
	 * @author linchuan
	 *
	 */
	private class ClearTimeoutConnectionsThread extends Thread {
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while(!interrupted()) {
				boolean clearAll = false;
				try {
					Thread.sleep(3600000); //每次清除间隔1小时
				} 
				catch (InterruptedException e) {
					clearAll = true;
				}
				if(Logger.isDebugEnabled()) {
					Logger.debug("WorkflowConnectionPool: clear timeout connections.");
				}
				synchronized(workflowConnections) {
					Timestamp time = DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -1);
					List timeoutThreadIds = new ArrayList();
					for(Iterator iterator = workflowConnections.keySet().iterator(); iterator.hasNext();) {
						Long threadId = (Long)iterator.next();
						WorkflowConnection workflowConnection = (WorkflowConnection)workflowConnections.get(threadId);
						if(clearAll || workflowConnection.getLastUsed().before(time)) { //超时
							timeoutThreadIds.add(threadId);
							try {
								workflowConnection.getWorkflowClient().disconnect(); //关闭连接
							}
							catch (WorkflowException e) {
								Logger.exception(e);
							}
						}
					}
					//从队列中删除
					for(Iterator iterator = timeoutThreadIds.iterator(); iterator.hasNext();) {
						Long threadId = (Long)iterator.next();
						workflowConnections.remove(threadId);
					}
					if(Logger.isDebugEnabled()) {
						Logger.debug("WorkflowConnectionPool: clear " + timeoutThreadIds.size() + " connections, " + workflowConnections.size() + " connections left.");
					}
				}
			}
		}
	}
}