package com.yuanluesoft.enterprise.assess.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 考核视图定义服务
 * @author linchuan
 *
 */
public class AssessViewServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewDefineServiceImpl#listWorkflowEntries(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listWorkflowEntries(String applicationPath, SessionInfo sessionInfo) throws ServiceException {
		List workflowEntries = super.listWorkflowEntries(applicationPath, sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) {
			return null;
		}
		Iterator iterator = workflowEntries.iterator();
		//获取第一个流程入口
		WorkflowEntry workflowEntry = (WorkflowEntry)iterator.next();
		//删除其他流程入口
		while(iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		iterator = workflowEntry.getActivityEntries().iterator();
		iterator.next();
		//删除其他流程入口环节
		while(iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		return workflowEntries;
	}
}