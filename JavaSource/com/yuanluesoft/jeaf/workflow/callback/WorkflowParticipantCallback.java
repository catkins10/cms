package com.yuanluesoft.jeaf.workflow.callback;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 办理人回调
 * @author linchuan
 *
 */
public interface WorkflowParticipantCallback {
	
	/**
	 * 获取编程决定的办理人列表,可以直接返回Person(pojo/model)/Org/Role/Element(userId, userName)类型的用户,由工作流应用服务负责转换
	 * @param programmingParticipantId
	 * @param programmingParticipantName
	 * @param workflowData
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 重置办理人列表
	 * @param participants 当前办理人
	 * @param anyoneParticipate 是否任何人都可以是办理人
	 * @param workflowData
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 判断用户是否属于编程决定的办理人,获取流程入口时调用
	 * @param programmingParticipantId
	 * @param programmingParticipantName
	 * @param workflowData
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException;
}