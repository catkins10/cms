package com.yuanluesoft.enterprise.project.service.spring;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseContractTemplate;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectQuality;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeamMember;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectType;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.document.callback.ProcessWordDocumentCallback;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class EnterpriseProjectServiceImpl extends BusinessServiceImpl implements EnterpriseProjectService {
	private AttachmentService attachmentService; //附件管理
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private NumerationService numerationService; //编号服务
	private RemoteDocumentService remoteDocumentService; //远程文档服务
	
	private String projectStages; //项目设计阶段(工可、初步设计、施工图设计等)列表
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#getProject(long)
	 */
	public EnterpriseProject getProject(long projectId) throws ServiceException {
		return (EnterpriseProject)load(EnterpriseProject.class, projectId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#saveContractBody(com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract, javax.servlet.http.HttpServletRequest)
	 */
	public void saveContractBody(final EnterpriseProjectContract contract, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新正文
				remoteDocumentService.updateRecordFile(documentPath, contract, "contract", null);
				//读取HTML内容
				String htmlBody = remoteDocumentService.retrieveRecordHtmlBody(htmlPagePath, htmlFilesPath, pageWidth, contract, "html", Environment.getContextPath() + "/attachments/enterprise/project/html/" + contract.getId() + "/$1");
				//保存html正文
		    	contract.setBody(htmlBody);
		    	getDatabaseService().updateRecord(contract);
		    }
		});
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#getProjectTeam(long)
	 */
	public EnterpriseProjectTeam getProjectTeam(long teamId) throws ServiceException {
		EnterpriseProjectTeam team = (EnterpriseProjectTeam)load(EnterpriseProjectTeam.class, teamId);
		//设置项目名称
		team.setProjectName((String)getDatabaseService().findRecordByHql("select EnterpriseProject.name from EnterpriseProject EnterpriseProject where EnterpriseProject.id=" + team.getProjectId()));
		return team;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#listProjectTypes()
	 */
	public List listProjectTypes() throws ServiceException {
		return getDatabaseService().findRecordsByHql("from EnterpriseProjectType EnterpriseProjectType order by EnterpriseProjectType.id");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#listEnteredProjectTypeList(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listEnteredProjectTypeList(SessionInfo sessionInfo) throws ServiceException {
		List projectTypes = listProjectTypes();
		if(projectTypes==null || projectTypes.isEmpty()) {
			return null;
		}
		for(Iterator iterator = projectTypes.iterator(); iterator.hasNext();) {
			EnterpriseProjectType projectType = (EnterpriseProjectType)iterator.next();
			if(projectType.getWorkflowId()==null ||
			   projectType.getWorkflowId().equals("") ||
			   workflowExploitService.getWorkflowEntry(projectType.getWorkflowId(), null, null, sessionInfo)==null) {
				iterator.remove();
			}
		}
		return projectTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#getContractTemplate(long)
	 */
	public Attachment getContractTemplate(long templateId) throws ServiceException {
		List templates = attachmentService.list("enterprise/project", "template", templateId, false, 1, null);
		if(templates!=null && !templates.isEmpty()) {
			return (Attachment)templates.get(0);
		}
		//获取默认模板
		Attachment template = new Attachment();
		template.setName("合同.doc");
		template.setFilePath(Environment.getWebinfPath() + "enterprise/project/template/合同.doc");
		return template;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#saveContractTemplate(com.yuanluesoft.enterprise.project.pojo.EnterpriseContractTemplate, javax.servlet.http.HttpServletRequest)
	 */
	public void saveContractTemplate(final EnterpriseContractTemplate contractTemplate, HttpServletRequest request) throws ServiceException {
		//处理上传的文档
		remoteDocumentService.processWordDocument(request, new ProcessWordDocumentCallback() {
			public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException {
				//更新模板
				remoteDocumentService.updateRecordFile(documentPath, contractTemplate, "template", null);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#generateContractNumber(java.lang.String)
	 */
	public String generateContractNumber(String projectType) throws ServiceException {
		String contractNumberRule = (String)getDatabaseService().findRecordByHql("select EnterpriseProjectType.contractNumberRule from EnterpriseProjectType EnterpriseProjectType where EnterpriseProjectType.projectType='" + JdbcUtils.resetQuot(projectType) + "'");
		if(contractNumberRule==null || contractNumberRule.equals("")) {
			return null;
		}
		return numerationService.generateNumeration("设计项目管理", "合同编号", contractNumberRule, false, null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#updateStagePrincipal(long, java.lang.String, java.lang.String, boolean)
	 */
	public void updateStagePrincipal(long stageId, String principalIds, String principalNames, boolean isReal) throws ServiceException {
		/*getDatabaseService().deleteObjectsByHql("from EnterpriseProjectPrincipal EnterpriseProjectPrincipal where EnterpriseProjectPrincipal.stageId=" + stageId + " and EnterpriseProjectPrincipal.isReal" + (isReal ? "=" : "!=") + "'1'");
		String[] ids = principalIds.split(",");
		String[] names = principalNames.split(",");
		for(int i=0; i<ids.length; i++) {
			EnterpriseProjectPrincipal principal = new EnterpriseProjectPrincipal();
			principal.setId(UUIDLongGenerator.generateId()); //ID
			principal.setStageId(stageId); //项目阶段ID,工可、初步设计、施工图设计等
			principal.setPrincipalId(Long.parseLong(ids[i])); //负责人ID
			principal.setPrincipalName(names[i]); //负责人姓名
			principal.setIsReal(isReal ? '1' : '0'); //是否实际负责人
			getDatabaseService().saveObject(principal);
		}*/
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#approvalEnterprise(com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectStage, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void approvalDesign(EnterpriseProjectTeam team, String designQuality, SessionInfo sessionInfo) throws ServiceException {
		//更新设计阶段
		team.setDesignQuality(designQuality);
		getDatabaseService().updateRecord(team);
		//记录设计完成情况
		EnterpriseProjectQuality projectQuality = new EnterpriseProjectQuality();
		projectQuality.setId(UUIDLongGenerator.generateId()); //ID
		projectQuality.setTeamId(team.getProjectId()); //项目ID
		projectQuality.setCompletionDate(team.getCompletionDate()); //设计完成时间,设计完成情况日期（设计人员填写）
		projectQuality.setCompletionDescription(team.getCompletionDescription()); //设计完成情况
		projectQuality.setDesignQuality(designQuality); //设计质量,填写设计质量，分为优秀、良好、合格、不合格、原则性错误	、技术性错误、一般性错误等（总工）
		projectQuality.setApproverId(sessionInfo.getUserId()); //审核人ID
		projectQuality.setApprover(sessionInfo.getUserName()); //审核人
		projectQuality.setApprovalTime(DateTimeUtils.now()); //审核时间
		getDatabaseService().saveRecord(projectQuality);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#createProjectTeam(long, java.lang.String, long, java.lang.String, java.sql.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public void createProjectTeam(long projectId, String stage, long teamId, String workContent, Date expectingDate, String projectTeamManagerIds, String projectTeamManagerNames, String projectTeamMemberIds, String projectTeamMemberNames, long creatorId, String creator) throws ServiceException {
		//删除旧记录
		getDatabaseService().deleteRecordsByHql("from EnterpriseProjectTeam EnterpriseProjectTeam where EnterpriseProjectTeam.id=" + teamId);
		//创建项目组
		EnterpriseProjectTeam team = new EnterpriseProjectTeam();
		team.setId(teamId); //ID
		team.setProjectId(projectId); //项目ID
		team.setStage(stage); //项目阶段,工可、初步设计、施工图设计等
		team.setWorkContent(workContent); //工作内容
		team.setExpectingDate(expectingDate); //计划完成时间
		team.setCreatorId(creatorId); //创建人ID
		team.setCreator(creator); //创建人
		team.setCreated(DateTimeUtils.now()); //创建时间
		getDatabaseService().saveRecord(team);
		//保存项目组负责人
		String[] ids = projectTeamManagerIds.split(",");
		String[] names = projectTeamManagerNames.split(",");
		for(int i=0; i<ids.length; i++) {
			EnterpriseProjectTeamMember member = new EnterpriseProjectTeamMember();
			member.setId(UUIDLongGenerator.generateId()); //ID
			member.setTeamId(team.getId()); //项目组ID
			member.setMemberId(Long.parseLong(ids[i])); //组员ID
			member.setMemberName(names[i]); //组员姓名
			member.setIsManager('1'); //是否项目组负责人
			getDatabaseService().saveRecord(member);
		}
		//保存项目组其他成员
		ids = projectTeamMemberIds.split(",");
		names = projectTeamMemberNames.split(",");
		for(int i=0; i<ids.length; i++) {
			EnterpriseProjectTeamMember member = new EnterpriseProjectTeamMember();
			member.setId(UUIDLongGenerator.generateId()); //ID
			member.setTeamId(team.getId()); //项目组ID
			member.setMemberId(Long.parseLong(ids[i])); //组员ID
			member.setMemberName(names[i]); //组员姓名
			member.setIsManager('0'); //是否项目组负责人
			getDatabaseService().saveRecord(member);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long projectTyped = RequestUtils.getParameterLongValue(notifyRequest, "projectTypeId"); //项目类型ID
		EnterpriseProjectType projectType = (EnterpriseProjectType)getDatabaseService().findRecordById(EnterpriseProjectType.class.getName(), projectTyped);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) {
			projectType.setWorkflowId(null); //流程ID 
			projectType.setWorkflowName(null); //流程名称
		}
		else {
			projectType.setWorkflowId(workflowId); //流程ID 
			projectType.setWorkflowName(workflowPackage.getName()); //流程名称
		}
		getDatabaseService().updateRecord(projectType);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.service.EnterpriseProjectService#listProjectStages()
	 */
	public List listProjectStages() throws ServiceException {
		return ListUtils.generateList(projectStages, ",");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("contract".equals(itemsName)) { //合同
			String hql;
			try {
				hql = "select EnterpriseProjectContract.contractName, EnterpriseProjectContract.id" +
							 " from EnterpriseProjectContract EnterpriseProjectContract" +
							 " where EnterpriseProjectContract.projectId=" + PropertyUtils.getProperty(bean, "id") +
							 " order by EnterpriseProjectContract.contractName";
			}
			catch (Exception e) {
				throw new ServiceException(e);
			}
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("projectType".equals(itemsName)) { //项目类型
			return getDatabaseService().findRecordsByHql("select EnterpriseProjectType.projectType from EnterpriseProjectType EnterpriseProjectType order by EnterpriseProjectType.id");
		}
		else if("contractTemplate".equals(itemsName)) { //合同模板
			String projectType;
			try {
				projectType = (String)getDatabaseService().findRecordByHql("select EnterpriseProject.type from EnterpriseProject EnterpriseProject where EnterpriseProject.id=" + PropertyUtils.getProperty(bean, "id"));
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
			String hql = "select EnterpriseContractTemplate.name,EnterpriseContractTemplate.id" +
						 " from EnterpriseContractTemplate EnterpriseContractTemplate" +
						 " where EnterpriseContractTemplate.appliedProjectTypes like '%," + JdbcUtils.resetQuot(projectType) + ",%'";
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("city".equals(itemsName)) { //城市
			String hql = "select distinct EnterpriseProject.city" +
						 " from EnterpriseProject EnterpriseProject" +
						 " where not EnterpriseProject.city is null" +
						 " order by EnterpriseProject.city";
			return getDatabaseService().findRecordsByHql(hql);
		}
		else if("projectTypeEnterable".equals(itemsName)) { //用户有权限登记的项目类型
			List projectTypes = listEnteredProjectTypeList(sessionInfo);
			if(projectTypes==null || projectTypes.isEmpty()) {
				return null;
			}
			for(int i=projectTypes.size()-1; i>=0; i--) {
				EnterpriseProjectType projectType = (EnterpriseProjectType)projectTypes.get(i);
				projectTypes.set(i, projectType.getProjectType());
			}
			return projectTypes;
		}
		else if("stageImcomplete".equals(itemsName)) { //未完成的项目阶段
			//获取项目阶段
			List stages = listProjectStages();
			//获取已经完成的项目阶段
			String hql;
			try {
				hql = "select EnterpriseProjectTeam.stage" +
				 			 " from EnterpriseProjectTeam EnterpriseProjectTeam" +
				 			 " where EnterpriseProjectTeam.projectId=" + PropertyUtils.getProperty(bean, "id") +
				 			 " and not EnterpriseProjectTeam.completionDate is null";
			}
			catch(Exception e) {
				throw new ServiceException(e);
			}
			List completionStages = getDatabaseService().findRecordsByHql(hql);
			//清除已经完成的阶段
			if(completionStages!=null) {
				for(Iterator iterator=completionStages.iterator(); iterator.hasNext();) {
					String completionStage = (String)iterator.next();
					stages.remove(completionStage);
				}
			}
			return stages;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the numerationService
	 */
	public NumerationService getNumerationService() {
		return numerationService;
	}

	/**
	 * @param numerationService the numerationService to set
	 */
	public void setNumerationService(NumerationService numerationService) {
		this.numerationService = numerationService;
	}

	/**
	 * @return the projectStages
	 */
	public String getProjectStages() {
		return projectStages;
	}

	/**
	 * @param projectStages the projectStages to set
	 */
	public void setProjectStages(String projectStages) {
		this.projectStages = projectStages;
	}

	/**
	 * @return the remoteDocumentService
	 */
	public RemoteDocumentService getRemoteDocumentService() {
		return remoteDocumentService;
	}

	/**
	 * @param remoteDocumentService the remoteDocumentService to set
	 */
	public void setRemoteDocumentService(RemoteDocumentService remoteDocumentService) {
		this.remoteDocumentService = remoteDocumentService;
	}
}