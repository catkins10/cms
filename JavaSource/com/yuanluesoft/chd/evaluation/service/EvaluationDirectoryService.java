package com.yuanluesoft.chd.evaluation.service;

import java.util.List;
import java.util.Set;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantType;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPrerequisites;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationRule;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 评价体系目录服务
 * @author linchuan
 *
 */
public interface EvaluationDirectoryService extends DirectoryService, WorkflowConfigureCallback, InitializeService {

	/**
	 * 获取评价项目各级别对应的分数
	 * @param directoryId
	 * @param rule
	 * @return
	 * @throws ServiceException
	 */
	public Set listRuleScores(long directoryId, ChdEvaluationRule rule) throws ServiceException;
	
	/**
	 * 保存评价项目各级别对应的分数
	 * @param ruleId
	 * @param levelIds
	 * @param minScores
	 * @param maxScrores
	 * @throws ServiceException
	 */
	public void saveRuleScores(long ruleId, String[] levelIds, String[] minScores, String[] maxScrores) throws ServiceException;
	
	/**
	 * 获取必备条件各级别对应的结果
	 * @param plantTypeId
	 * @param prerequisites
	 * @return
	 * @throws ServiceException
	 */
	public Set listPrerequisitesScores(long plantTypeId, ChdEvaluationPrerequisites prerequisites) throws ServiceException;
	
	/**
	 * 保存必备条件各级别对应的结果
	 * @param plantTypeId
	 * @param prerequisitesId
	 * @param levelIds
	 * @param scores
	 * @throws ServiceException
	 */
	public void savePrerequisitesScores(long plantTypeId, long prerequisitesId, String[] levelIds, String[] scores) throws ServiceException;
	
	/**
	 * 保存机组数据模板
	 * @param generatorTemplate
	 * @param plantType
	 * @throws ServiceException
	 */
	public void saveGeneratorTemplate(String generatorTemplate, ChdEvaluationPlantType plantType) throws ServiceException;
	
	/**
	 * 判断是否有属于指定的类型的发电企业
	 * @param type
	 * @throws ServiceException
	 */
	public boolean isPlantTypeUsed(String type) throws ServiceException;
	
	/**
	 * 获取审批流程
	 * @param diretoryId
	 * @param workflowType
	 * @return
	 * @throws ServiceException
	 */
	public String getApprovalWorkflowId(long diretoryId, String workflowType) throws ServiceException;
	
	/**
	 * 获取创星责任人
	 * @param directoryId
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listEvaluationTransactor(long directoryId, int max) throws ServiceException;
	
	/**
	 * 获取创星负责人
	 * @param directoryId
	 * @param plantLeader 是否发电企业的创星负责人
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listEvaluationLeader(long directoryId, boolean plantLeader, int max) throws ServiceException;
}