/*
 * Created on 2005-9-14
 *
 */
package com.yuanluesoft.jeaf.opinionmanage.service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface OpinionService extends BusinessService {
	
	/**
	 * 获取自定义的意见类型列表
	 * @param businessClassName
	 * @return
	 * @throws ServiceException
	 */
	public List listCustomOpinionTypes(String businessClassName) throws ServiceException;
	
	/**
	 * 把预设的意见类型保存为自定义意见类型
	 * @param businessClassName
	 * @throws ServiceException
	 */
	public void loadDefaultOpinionTypes(String businessClassName) throws ServiceException;
	
	/**
	 * 添加常用意见
	 * @param personId
	 * @param applicationName
	 * @param opinion
	 * @throws ServiceException
	 */
	public void appendOftenUseOpinion(long personId, String applicationName, String opinion) throws ServiceException;
	
	/**
	 * 删除常用意见
	 * @param personId
	 * @param applicationName
	 * @param opinion
	 * @throws ServiceException
	 */
	public void deleteOftenUseOpinion(long personId, String applicationName, String opinion) throws ServiceException;

	/**
	 * 加载意见
	 * @param pojoClassName
	 * @param opinionId
	 * @return
	 * @throws ServiceException
	 */
	public Opinion loadOpinion(String pojoClassName, long opinionId) throws ServiceException;
	
	/**
	 * 保存意见
	 * @param pojoClassName
	 * @param opinionId 新意见时为0
	 * @param mainRecordId
	 * @param opinionContent
	 * @param opinionType
	 * @param participantId
	 * @param participantName
	 * @param agentId
	 * @param agentName
	 * @param activityId
	 * @param activityName
	 * @param workItemId
	 * @param created
	 * @throws ServiceException
	 */
	public Opinion saveOpinion(String pojoClassName, long opinionId, long mainRecordId, String opinionContent, String opinionType, long participantId, String participantName, long agentId, String agentName, String activityId, String activityName, String workItemId, Timestamp created) throws ServiceException;
	
	/**
	 * 删除意见
	 * @param pojoClassName
	 * @param opinionId
	 * @throws ServiceException
	 */
	public void deleteOpinion(String pojoClassName, long opinionId) throws ServiceException;
	
	/**
	 * 按环节填充意见包
	 * @param opinionPackage
	 * @param opinions
	 * @param mainRecordClassName
	 * @param activityId
	 * @param participantId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void fillOpinionPackageByActivityId(OpinionPackage opinionPackage, Collection opinions, String mainRecordClassName, String activityId, long participantId, SessionInfo sessionInfo) throws ServiceException;
		
	/**
	 * 按工作项填充意见包
	 * @param opinionPackage
	 * @param opinions
	 * @param mainRecordClassName
	 * @param wordItemId
	 * @param participantId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void fillOpinionPackageByWorkItemId(OpinionPackage opinionPackage, Collection opinions, String mainRecordClassName, String wordItemId, long participantId, SessionInfo sessionInfo) throws ServiceException;
}