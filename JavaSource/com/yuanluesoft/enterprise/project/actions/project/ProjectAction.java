package com.yuanluesoft.enterprise.project.actions.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.assess.service.AssessService;
import com.yuanluesoft.enterprise.project.forms.Project;
import com.yuanluesoft.enterprise.project.model.IsoFile;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectContract;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectType;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.enterprise.workreport.service.WorkReportService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.instance.ActivityInstance;
import com.yuanluesoft.workflow.client.model.instance.ProcessInstance;
import com.yuanluesoft.workflow.client.model.instance.WorkflowInstance;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runProject";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Project projectForm = (Project)form;
		if(projectForm.getIso()==0) {
			projectForm.setIso('0');
		}
		projectForm.setCreated(DateTimeUtils.now()); //创建时间
		projectForm.setCreator(sessionInfo.getUserName());
		if(projectForm.getIsBidding()==0) {
			projectForm.setIsBidding('0');
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Project projectForm = (Project)form;
		//设置项目组的绩效考核、工作报告列表
		if(projectForm.getTeams()!=null && !projectForm.getTeams().isEmpty()) {
			AssessService assessService = (AssessService)getService("assessService");
			WorkReportService workReportService = (WorkReportService)getService("workReportService");
			for(Iterator iterator = projectForm.getTeams().iterator(); iterator.hasNext();) {
				EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
				team.setAssesses(assessService.listProjectTeamAssesses(team.getId(), sessionInfo, 0, 10)); //获取最近的10次考核
				team.setWorkReports(workReportService.listProjectTeamWorkReports(team.getId(), sessionInfo, 0, 10)); //获取最近的50个汇报
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Project projectForm = (Project)form;
		EnterpriseProject project = (EnterpriseProject)record;
		//页签设置
		projectForm.getTabs().clear();
		if(OPEN_MODE_CREATE.equals(openMode)) {
			projectForm.getTabs().addTab(-1, "projectCreate", "基本信息", "tabpages/projectCreate.jsp", true);
			return;
		}
		if(form.getSubForm().indexOf("projectCreate")!=-1) { //登记
			projectForm.getTabs().addTab(-1, "projectCreate", "基本信息", "tabpages/projectCreate.jsp", true);
		}
		else {
			projectForm.getTabs().addTab(-1, "projectRead", "基本信息", "tabpages/projectRead.jsp", true);
		}
		boolean contractEdit = false;
		boolean designEdit = false;
		//合同页签
		if(form.getSubForm().indexOf("ContractCreate")!=-1) { //合同起草
			projectForm.getTabs().addTab(-1, "contractCreate", "合同", "tabpages/contractCreate.jsp", true);
			contractEdit = true;
		}
		else if(form.getSubForm().indexOf("ContractApproval")!=-1) { //合同审核
			projectForm.getTabs().addTab(-1, "contractApproval", "合同", "tabpages/contractApproval.jsp", true);
			contractEdit = true;
		}
		else if(acl.contains("manageUnit_contractQuery") && project.getContracts()!=null && !project.getContracts().isEmpty()) { //有合同查看权限且有合同
			projectForm.getTabs().addTab(-1, "contractQuery", "合同", "tabpages/contractQuery.jsp", true);
		}
		//设计页签
		if(project.getTeams()!=null && !project.getTeams().isEmpty()) {
			if(form.getSubForm().indexOf("Design")!=-1) { //项目设计
				designEdit = true;
			}
			if(designEdit || acl.contains("manageUnit_designQuery")) { //处于设计阶段,或者有设计的查看权限
				for(Iterator iterator=project.getTeams().iterator(); iterator.hasNext();) {
					EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
					//有设计编辑权限,且是最后一个设计阶段时,显示编辑页面
					String status = (designEdit && !iterator.hasNext() ? "Edit" : "Read");
					Tab tab = projectForm.getTabs().addTab(-1, "projectTeam" + status + "_" + team.getId() + "_", team.getStage(), "tabpages/projectTeam" + status + ".jsp", !contractEdit);
					tab.setAttribute("projectTeam", team); //设置项目组
					//设置子TAB列表
					TabList tabs = new TabList();
					tabs.addTab(-1, "teamBasic" + team.getId(), "项目组信息", null, true);
					tabs.addTab(-1, "teamPlan" + team.getId(), "工作安排", "projectTeamPlan.jsp", false);
					tabs.addTab(-1, "teamWorkReport" + team.getId(), "工作汇报", "projectTeamWorkReport.jsp", false);
					tabs.addTab(-1, "teamAssess" + team.getId(), "绩效考核", "projectTeamAssess.jsp", false);
					tab.setAttribute("projectTeamTabs", tabs);
					//判断是否项目组负责人
					team.setManager(isTeamManager(team, sessionInfo));
					//判断是否项目组成员
					team.setMember(isTeamMember(team, sessionInfo));
					//设置为当前的项目组,记录最后一个项目组
					projectForm.setProjectTeam(team);
				}
			}
		}
		//收费页签
		if(acl.contains("manageUnit_accounting")) { //收费人员
			projectForm.getTabs().addTab(-1, "accounting", "收费", "tabpages/accounting.jsp", !contractEdit && !designEdit);
		}
		else if(acl.contains("manageUnit_accountingQuery") &&
				((project.getCollects()!=null && !project.getCollects().isEmpty()) ||
				 (project.getPaies()!=null && !project.getPaies().isEmpty()))) { //收费查询,且有收费记录
			projectForm.getTabs().addTab(-1, "accountingQuery", "收费", "tabpages/accountingQuery.jsp", !contractEdit && !designEdit);
		}
		if(project.getIso()=='1') { //ISO贯标
			projectForm.getTabs().addTab(-1, "isoFile", "ISO文件", "tabpages/isoFile" + (contractEdit || designEdit ? "Edit" : "Read") + ".jsp", false);
			//设置当前允许上传的ISO文件类型
			projectForm.setIsoFileType(contractEdit ? "contract" : (designEdit ? "design" : null));
			//设置ISO文件列表
			AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
			List attachmentTypes = attachmentService.listTypes(projectForm.getFormDefine().getApplicationName(), projectForm.getId());
			if(attachmentTypes!=null) {
				List isoFiles = new ArrayList();
				WorkflowInstance workflowInstance = getWorkflowInterface(projectForm, request, record, openMode, sessionInfo).getWorkflowInstance(); //流程记录
				ProcessInstance processInstance = (ProcessInstance)workflowInstance.getProcessInstanceList().get(0);
				for(Iterator iterator = processInstance.getActivityInstanceList().iterator(); iterator.hasNext();) {
					ActivityInstance activityInstance = (ActivityInstance)iterator.next();
					for(Iterator iteratorAttachmentType = attachmentTypes.iterator(); iteratorAttachmentType.hasNext();) {
						String attachmentType = (String)iteratorAttachmentType.next();
						if(!attachmentType.startsWith(activityInstance.getActivityDefinitionId())) {
							continue;
						}
						//检查对应的合同附件是否存在
						if(attachmentType.equals(activityInstance.getActivityDefinitionId() + "_" + "contract")) { //合同处理环节
							if(contractEdit || acl.contains("manageUnit_contractQuery")) { //有合同查看权限
								isoFiles.add(new IsoFile(activityInstance, null, attachmentType));
							}
						}
						else if(designEdit || acl.contains("manageUnit_designQuery")) { //有设计查看权限
							isoFiles.add(new IsoFile(activityInstance, (EnterpriseProjectTeam)ListUtils.findObjectByProperty(project.getTeams(), "stage", attachmentType.split("_")[2]), attachmentType));
						}
						iteratorAttachmentType.remove();
					}
				}
				//按项目组创建时间排序
				Collections.sort(isoFiles, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						IsoFile isoFile0 = (IsoFile)arg0;
						IsoFile isoFile1 = (IsoFile)arg1;
						if(isoFile0.getProjectTeam()==null || isoFile1.getProjectTeam()==null) {
							return 0;
						}
						else if(isoFile0.getProjectTeam().getCreated().equals(isoFile1.getProjectTeam().getCreated())) {
							return 0;
						}
						else if(isoFile0.getProjectTeam().getCreated().before(isoFile1.getProjectTeam().getCreated())) {
							return -1;
						}
						else {
							return 1;
						}
					}
				});
				projectForm.setIsoFiles(isoFiles);
			}
		}
		addOpinionTab(projectForm); //办理意见
		addWorkflowLogTab(projectForm); //流程记录

		//根据用户权限删除办理意见
		/*if(projectForm.getOpinionPackage().getOpinionList()!=null) {
			for(Iterator iterator = projectForm.getOpinionPackage().getOpinionList().iterator(); iterator.hasNext();) {
				Opinion opinion = (Opinion)iterator.next();
				if(opinion.getOpinionType()!=null && opinion.getOpinionType().indexOf("合同")!=-1) { //合同意见
					if(!contractEdit && !acl.contains("manageUnit_contractQuery")) { //没有合同查看权限
						iterator.remove();
					}
				}
				else if(!designEdit && !acl.contains("manageUnit_designQuery")) { //没有设计查看权限
					iterator.remove();
				}
			}
		}*/
	}
	
	/**
	 * 判断是否项目组负责人
	 * @param team
	 * @param sessionInfo
	 * @return
	 */
	protected boolean isTeamManager(EnterpriseProjectTeam team, SessionInfo sessionInfo) {
		return ListUtils.findObjectByProperty(ListUtils.getSubListByProperty(team.getMembers(), "isManager", new Character('1')), "memberId", new Long(sessionInfo.getUserId()))!=null;
	}
	
	/**
	 * 判断是否项目组成员
	 * @param team
	 * @param sessionInfo
	 * @return
	 */
	protected boolean isTeamMember(EnterpriseProjectTeam team, SessionInfo sessionInfo) {
		return ListUtils.findObjectByProperty(team.getMembers(), "memberId", new Long(sessionInfo.getUserId()))!=null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		Project projectForm = (Project)workflowForm;
		if(projectForm.getType()==null || projectForm.getType().equals("")) { //项目类别为空
			return null;
		}
		//获取工作流配置
		EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
		EnterpriseProjectType projectType = (EnterpriseProjectType)ListUtils.findObjectByProperty(enterpriseProjectService.listProjectTypes(), "projectType", projectForm.getType());
		if(projectType==null || projectType.getWorkflowName()==null || projectType.getWorkflowName().equals("")) {
			throw new PrivilegeException();
		}
		//获取流程
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(projectType.getWorkflowId(), null, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			projectForm.setError("没有和当前项目匹配的流程或者没有对应流程的办理权限");
			throw new ValidateException();
		}
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkflowRun(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkflowRun(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, SessionInfo sessionInfo) throws Exception {
		super.beforeWorkflowRun(workflowForm, request, response, record, sessionInfo);
		//判断设计是否已经全部完成
		EnterpriseProject project = (EnterpriseProject)record;
		Project projectForm = (Project)workflowForm;
		project.setDesignCompleted(projectForm.getIsDesignCompleted()=='1' ? "是" : "");
		EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
		//获取项目阶段
		List stages = enterpriseProjectService.listProjectStages();
		if(project.getTeams()!=null && project.getTeams().size()>=stages.size()) {
			boolean designCompleted = true;
			//检查项目组任务是否都已经完成
			for(Iterator iterator = project.getTeams().iterator(); iterator.hasNext();) {
				EnterpriseProjectTeam team = (EnterpriseProjectTeam)iterator.next();
				if(team.getCompletionDate()==null) {
					designCompleted = false;
					break;
				}
			}
			if(designCompleted) {
				project.setDesignCompleted("是");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		EnterpriseProjectTeam team = getCurrentTeam((EnterpriseProject)record);
		if(team==null) {
			return null;
		}
		return team.listProgrammingParticipants(programmingParticipantId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EnterpriseProject project = (EnterpriseProject)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			project.setCreated(DateTimeUtils.now()); //创建时间
			project.setCreator(sessionInfo.getUserName());
			project.setCreatorId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/**
	 * 更新合同的盖章/签字/归档时间
	 * @param project
	 * @param dateFieldName
	 * @param request
	 * @throws Exception
	 */
	protected void updateContractDate(ActionForm form, EnterpriseProject project, String dateFieldName, HttpServletRequest request) throws Exception {
		try {
			String[] dateValues = request.getParameterValues(dateFieldName);
			EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
			int index = 0;
			for(Iterator iterator = project.getContracts().iterator(); iterator.hasNext();) {
				EnterpriseProjectContract contract = (EnterpriseProjectContract)iterator.next();
				if(PropertyUtils.getProperty(contract, dateFieldName)==null) {
					PropertyUtils.setProperty(contract, dateFieldName, DateTimeUtils.parseDate(dateValues[index++], null));
					enterpriseProjectService.update(contract);
				}
			}
		}
		catch(Exception e) {
			form.setError("操作失败，请重试。");
			throw new ValidateException();
		}
	}
	
	/**
	 * 获取当前项目组
	 * @param project
	 * @return
	 */
	protected EnterpriseProjectTeam getCurrentTeam(EnterpriseProject project) {
		if(project.getTeams()==null) {
			return null;
		}
		EnterpriseProjectTeam team = null;
		for(Iterator iterator = project.getTeams().iterator(); iterator.hasNext();) {
			team = (EnterpriseProjectTeam)iterator.next();
		}
		return team;
	}
}