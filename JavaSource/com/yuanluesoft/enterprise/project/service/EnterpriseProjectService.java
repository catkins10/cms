package com.yuanluesoft.enterprise.project.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseContractTemplate;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 
 * @author linchuan
 *
 */
public interface EnterpriseProjectService extends BusinessService, WorkflowConfigureCallback {
	
	/**
	 * 获取项目
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public EnterpriseProject getProject(long projectId) throws ServiceException;
	
	/**
	 * 保存合同正文
	 * @param contract
	 * @param request
	 * @throws ServiceException
	 */
	public void saveContractBody(EnterpriseProjectContract contract, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取项目组
	 * @param teamId
	 * @return
	 * @throws ServiceException
	 */
	public EnterpriseProjectTeam getProjectTeam(long teamId) throws ServiceException;

	/**
	 * 获取项目类型列表
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectTypes() throws ServiceException;

	/**
	 * 获取用户可以进入的项目类型
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listEnteredProjectTypeList(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 按模板ID获取模板
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	public Attachment getContractTemplate(long templateId) throws ServiceException;
	
	/**
	 * 保存合同模板
	 * @param contractTemplate
	 * @param request
	 * @throws ServiceException
	 */
	public void saveContractTemplate(EnterpriseContractTemplate contractTemplate, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 生成合同编号
	 * @param projectType
	 * @return
	 * @throws ServiceException
	 */
	public String generateContractNumber(String projectType) throws ServiceException;
	
	/**
	 * 更新项目阶段办理人
	 * @param stageId
	 * @param principalIds
	 * @param principalNames
	 * @param isReal
	 * @throws ServiceException
	 */
	public void updateStagePrincipal(long stageId, String principalIds, String principalNames, boolean isReal) throws ServiceException;
	
	/**
	 * 审核设计
	 * @param team
	 * @param designQuality
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void approvalDesign(EnterpriseProjectTeam team, String designQuality, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 创建项目组
	 * @param projectId
	 * @param stage
	 * @param teamId
	 * @param workContent
	 * @param expectingDate
	 * @param projectTeamManagerIds
	 * @param projectTeamManagerNames
	 * @param projectTeamMemberIds
	 * @param projectTeamMemberNames
	 * @param creatorId
	 * @param creator
	 * @throws ServiceException
	 */
	public void createProjectTeam(long projectId, String stage, long teamId, String workContent, Date expectingDate, String projectTeamManagerIds, String projectTeamManagerNames, String projectTeamMemberIds, String projectTeamMemberNames, long creatorId, String creator) throws ServiceException;
	
	/**
	 * 获取项目阶段列表
	 * @return
	 * @throws ServiceException
	 */
	public List listProjectStages() throws ServiceException;
}