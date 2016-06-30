package com.yuanluesoft.jeaf.workflow.connectionpool;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.WorkflowClient;

/**
 * 工作流连接池
 * @author linchuan
 *
 */
public interface WorkflowConnectionPool {
	
	/**
	 * 获取工作流客户端
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public WorkflowClient getWorkflowClient(SessionInfo sessionInfo) throws ServiceException;
}