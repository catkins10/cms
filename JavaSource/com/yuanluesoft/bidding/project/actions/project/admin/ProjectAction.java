package com.yuanluesoft.bidding.project.actions.project.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bidding.biddingroom.service.BiddingRoomService;
import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.project.ask.service.BiddingProjectAskService;
import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgentDraw;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectCity;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectComponent;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectJobholder;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectParameter;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectSupplement;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.resource.Action;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author yuanlue
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
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BiddingProject project = (BiddingProject)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(project!=null) {
			//获取提问列表
			BiddingProjectAskService biddingProjectAskService = (BiddingProjectAskService)getService("biddingProjectAskService");
			List asks = biddingProjectAskService.listProjectAsks(project.getId(), sessionInfo);
			project.setAskQuestions(asks==null ? new LinkedHashSet() : new LinkedHashSet(asks));
		}
		return project;
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
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		if(record!=null) {
			BiddingProject project = (BiddingProject)record;
			if(project.getBidopeningRoomSchedule()!=null && //已经分配过开标室
			   project.getBidopeningRoomSchedule().getPublicPersonId()==sessionInfo.getUserId() && //当前用户是原来的安排人
			   (project.getBidopeningRoomSchedule().getBeginTime()==null || project.getBidopeningRoomSchedule().getBeginTime().after(DateTimeUtils.now()))) { //时间没有到
				acl.add("changeBidopeningRoom"); //给开标室的发布人增加调整开标室的权限
			}
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		BiddingProject project = (BiddingProject)record;
		if("projectAgent".equals(programmingParticipantId) || "agent".equals(programmingParticipantId)) {
			ParticipantDepartment participantDepartment = new ParticipantDepartment();
			if("否".equals(project.getAgentEnable())) { //自行招标
				participantDepartment.setId(project.getOwnerId() + "");
				participantDepartment.setName(project.getOwner());
			}
			else { //代理招标
				Project projectForm = (Project)workflowForm;
				participantDepartment.setId(projectForm.getBiddingAgent().getAgentId() + "");
				participantDepartment.setName(projectForm.getBiddingAgent().getAgentName());
			}
			return ListUtils.generateList(participantDepartment);
		}
		else if(",cityManager,cityProjectCreator,cityProjectApprover,".indexOf("," + programmingParticipantId + ",")!=-1) { //地区管理员,项目登记人员,项目审核人员
			BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			BiddingProjectCity city = biddingProjectParameterService.getCityDetail(project.getCity());
			char accessLevel = BiddingProjectParameterService.BIDDING_CITY_MANAGER;
			if("cityProjectCreator".equals(programmingParticipantId)) { //项目登记人员
				accessLevel = BiddingProjectParameterService.BIDDING_CITY_PROJECT_CREATOR;
			}
			else if("cityProjectApprover".equals(programmingParticipantId)) { //项目审核人员
				accessLevel = BiddingProjectParameterService.BIDDING_CITY_PROJECT_APPROVER;
			}
			return getRecordControlService().listVisitors(city.getId(), BiddingProjectCity.class.getName(), accessLevel);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Project projectForm = (Project)workflowForm;
		if(programmingParticipantId.equals("agent")) {
			if(sessionInfo instanceof BiddingSessionInfo) {
				BiddingSessionInfo biddingSessionInfo = (BiddingSessionInfo)sessionInfo;
				return biddingSessionInfo.isAgent();
			}
		}
		else if(",cityManager,cityProjectCreator,cityProjectApprover,".indexOf("," + programmingParticipantId + ",")!=-1) { //地区管理员,项目登记人员,项目审核人员
			BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			BiddingProjectCity city = biddingProjectParameterService.getCityDetail(projectForm.getCity());
			char accessLevel = BiddingProjectParameterService.BIDDING_CITY_MANAGER;
			if("cityProjectCreator".equals(programmingParticipantId)) { //项目登记人员
				accessLevel = BiddingProjectParameterService.BIDDING_CITY_PROJECT_CREATOR;
			}
			else if("cityProjectApprover".equals(programmingParticipantId)) { //项目审核人员
				accessLevel = BiddingProjectParameterService.BIDDING_CITY_PROJECT_APPROVER;
			}
			return getRecordControlService().isVisitor(city.getId(), BiddingProjectCity.class.getName(), accessLevel, sessionInfo);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains("changeBidopeningRoom")) { //有调整开标室的权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		BiddingProject project = (BiddingProject)record;
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && project.getHalt()=='1') { //管理员、逻辑删除
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(project.getWorkflowInstanceId()==null || project.getWorkflowInstanceId().isEmpty()) { //没有流程实例ID,是导入的项目
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Project projectForm = (Project)form;
		//BiddingParameterService biddingParameterService = (BiddingParameterService)getService("biddingParameterService");
		if(projectForm.getAgentEnable()==null || projectForm.getAgentEnable().equals("")) {
			projectForm.setAgentEnable("是"); //是否委托代理招标
		}
		if(projectForm.getBiddingMode()==null || projectForm.getBiddingMode().equals("")) {
			projectForm.setBiddingMode("公开招标"); //招标方式
		}
		projectForm.setCreated(DateTimeUtils.now()); //创建时间
		if(projectForm.getApprovalMode()==null || projectForm.getApprovalMode().equals("")) {
			projectForm.setApprovalMode("资格后审");
		}
		if(projectForm.getAgentMode()==null || projectForm.getAgentMode().equals("")) {
			projectForm.setAgentMode("随机抽签");
		}
		//初始化组成部分列表
		initProjectComponents(projectForm, null, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Project projectForm = (Project)form;
		BiddingProject project = (BiddingProject)record;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		//如果开标公示允许编辑,则开始同步开标公示
		if(isComponentEditable("bidopening", projectForm, project, request, sessionInfo)) {
			biddingProjectService.synchBidopening(project);
		}
		//如果中标公示允许编辑,则开始同步中标公示
		if(isComponentEditable("pitchon", projectForm, project, request, sessionInfo)) {
			biddingProjectService.synchPitchon(project);
		}
		//如果中标通知书允许编辑,则开始创建中标通知书
		if(isComponentEditable("notice", projectForm, project, request, sessionInfo)) {
			biddingProjectService.generateNotice(project);
		}
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		
		//初始化项目组成部分
		initProjectComponents(projectForm, project, request, sessionInfo);
		
		//如果需要显示报名情况,统计报名信息
		if(ListUtils.findObjectByProperty(projectForm.getTabs(), "id", "bidding")!=null) {
			BiddingService biddingService = (BiddingService)getService("biddingService");
			projectForm.setSignUpTotal(biddingService.totalSignUp(projectForm.getId()));
		}
		
		//调整补充通知
		if(projectForm.getSupplement().getPublicPersonId()>0 && isComponentEditable("supplement", projectForm, project, request, sessionInfo) ) { //补充通知已发布、且可编辑
			projectForm.setSupplement(new BiddingProjectSupplement()); //添加新的补充通知
		}
		
		//设置中标候选企业
		BiddingService biddingService = (BiddingService)getService("biddingService");
		List rankingSignUps = biddingService.listRankingSignUps(project);
		projectForm.setRankingSignUpIds(ListUtils.join(rankingSignUps, "id", ",", false));
		projectForm.setRankingEnterpriseNames(ListUtils.join(rankingSignUps, "enterpriseName", ",", false));
		
		//计算保证金返还金额
		if(ListUtils.findObjectByProperty(projectForm.getTabs(), "id", "pledgeReturnChoose")!=null &&
		   "get".equalsIgnoreCase(request.getMethod())) {
			biddingService.retrievePledgeReturnMoney(project);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Project projectForm = (Project)form;
		BiddingProject project = (BiddingProject)record;
		if(accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE || (project!=null && project.getRegistTime()!=null)) {
			ListUtils.removeObjectByProperty(projectForm.getFormActions(), "title", "完成登记");
		}
		BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
		
		//初始化列表
		if(OPEN_MODE_CREATE.equals(openMode)) {
			createTabs(projectForm, project, openMode, acl, null, request, sessionInfo);
			return;
		}
		
		//上下班时间
		if(project.getCity()!=null) {
			BiddingProjectCity city = biddingProjectParameterService.getCityDetail(project.getCity());
			if(city!=null) {
				projectForm.setWorkBeginAm(city.getWorkBeginAm()); //上午上班时间
				projectForm.setWorkEndAm(city.getWorkEndAm()); //上午下班时间
				projectForm.setWorkBeginPm(city.getWorkBeginPm()); //下午上班时间
				projectForm.setWorkEndPm(city.getWorkEndPm()); //下午下班时间
			}
		}
		//获取流程界面
		WorkflowInterface workflowInterface = getWorkflowInterface(projectForm, request, record, openMode, sessionInfo);
		
		//判断是否需要对招标文件盖章
		if(workflowInterface!=null) {
			projectForm.setNeedBiddingDocumentsStamp(ListUtils.findObjectByProperty(workflowInterface.getActions(), "name", "招标文件盖章")!=null);
		}
		
		//设置页签列表
		createTabs(projectForm, project, openMode, acl, workflowInterface, request, sessionInfo);
		
		//有人提问,设置提问发起点列表
		if(project.getAskQuestions()!=null && !project.getAskQuestions().isEmpty()) {
			List askFroms = new ArrayList();
			List componentFields = FieldUtils.listRecordFields(BiddingProject.class.getName(), "component", null, null, null, true, false, false, false, 1);
			for(Iterator iterator = componentFields.iterator(); iterator.hasNext();) {
				Field componentField = (Field)iterator.next();
				askFroms.add(0, componentField.getTitle());
			}
			projectForm.setAskFroms(askFroms);
		}
		
		//获取前期资料列表
		if(isComponentEditable("prophase", projectForm, project, request, sessionInfo) || (project.getProphases()!=null && !project.getProphases().isEmpty())) {
			projectForm.setProphaseFiles(biddingProjectParameterService.listProjectFileItems(project.getProjectCategory(), project.getProjectProcedure(), project.getCity(), "前期资料"));
			projectForm.setNeedFullFiles(biddingProjectParameterService.isNeedFullProjectFiles(project.getProjectCategory(), project.getProjectProcedure(), project.getCity(), "前期资料"));
		}
		
		//获取归档资料列表
		if(isComponentEditable("archive", projectForm, project, request, sessionInfo) || (project.getArchives()!=null && !project.getArchives().isEmpty())) {
			projectForm.setArchiveFiles(biddingProjectParameterService.listProjectFileItems(project.getProjectCategory(), project.getProjectProcedure(), project.getCity(), "归档资料"));
			projectForm.setNeedFullFiles(biddingProjectParameterService.isNeedFullProjectFiles(project.getProjectCategory(), project.getProjectProcedure(), project.getCity(), "归档资料"));
		}
		
		if(project.getBidopeningRoomSchedule()!=null && //已经分配过开标室
		   project.getBidopeningRoomSchedule().getBeginTime()!=null &&
		   !project.getBidopeningRoomSchedule().getBeginTime().after(DateTimeUtils.now())) { //时间已到
			form.getFormActions().removeFormAction("调整开标室"); //不允许调整开标室
		}
		
		if(project.getHalt()=='1') { //逻辑删除
			form.getFormActions().removeFormAction("作废");
			form.getFormActions().removeFormAction("保存");
			form.setSubForm(form.getSubForm().replace("Edit", "Read"));
		}
		else {
			form.getFormActions().removeFormAction("永久删除");
			form.getFormActions().removeFormAction("撤销删除");
		}
		
		//检查是否实名报名
		if("是".equals(project.getIsRealNameSignUp())) {
			//中标候选人,只允许选择
			Field rankingEnterpriseNamesField = projectForm.getFormDefine().getField("rankingEnterpriseNames");
			rankingEnterpriseNamesField.setParameter("selectOnly", "true");
			rankingEnterpriseNamesField.setRequired(true);
		}
	}
	
	/**
	 * 根据流程操作配置判断对应的编辑权限
	 * @param componentName
	 * @param projectForm
	 * @param project
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected boolean isComponentEditable(String componentName, Project projectForm, BiddingProject project, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		//获取工作流界面
		WorkflowInterface workflowInterface = getWorkflowInterface(projectForm, request, project, getOpenMode(projectForm, request), sessionInfo);
		if(workflowInterface==null || workflowInterface.getActions()==null || workflowInterface.getActions().isEmpty()) {
			return false;
		}
		Field componentField = FieldUtils.getRecordField(BiddingProject.class.getName(), componentName, request);
		if(componentField==null) {
			return false;
		}
		//获取对应的流程按钮
		String editActions = (String)componentField.getParameter("editActions");
		if(editActions==null || editActions.isEmpty()) {
			return false;
		}
		String[] actions = editActions.split(",");
		for(int i=0; i<actions.length; i++) {
			if(ListUtils.findObjectByProperty(workflowInterface.getActions(), "name", actions[i])!=null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置页签列表
	 * @param projectForm
	 * @param project
	 * @param openMode
	 * @param acl
	 * @param workflowInterface
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	private void createTabs(Project projectForm, BiddingProject project, String openMode, List acl, WorkflowInterface workflowInterface, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		//初始化TAB列表
		projectForm.getTabs().clear();
		
		//添加"登记"
		projectForm.getTabs().addTab(-1, "create", "登记", "tabpages/" + (OPEN_MODE_CREATE.equals(openMode) ? "projectCreate.jsp" : projectForm.getSubForm()), true);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			return;
		}
		
		BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
		//获取区域的英文名称
		String cityEnglishName = biddingProjectParameterService.getCityEnglishName(project.getCity());
		//获取项目类别的英文名称
		String projectCategoryEnglishName = biddingProjectParameterService.getProjectCategoryEnglishName(project.getProjectCategory());
		//获取招标内容的英文名称
		String projectProcedureEnglishName = biddingProjectParameterService.getProjectProcedureEnglishName(project.getProjectProcedure());
		
		//添加项目组成部分
		boolean tabSelected = false;
		List componentFields = FieldUtils.listRecordFields(BiddingProject.class.getName(), "component", null, null, null, true, false, false, false, 1);
		for(Iterator iterator = componentFields.iterator(); iterator.hasNext();) {
			Field componentField = (Field)iterator.next();
			String componentName = componentField.getName();
			if("kc".equals(componentName)) {
				continue;
			}
			if("agentDraw".equals(componentName) && ("否".equals(project.getAgentEnable()) || !"随机抽签".equals(project.getAgentMode()))) {
				continue;
			}
			//判断是否处于编辑状态
			if(isComponentEditable(componentName, projectForm, project, request, sessionInfo)) {
				projectForm.getTabs().addTab(-1, componentName, componentField.getTitle(), getTabJspFile(componentName + "Edit.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), !tabSelected);
				tabSelected = true;
				continue;
			}
			try {
				//判断是否有对应的数据
				if(PropertyUtils.getProperty(project, componentName)!=null) {
					if(componentField.getTitle().equals("招标文件") && !checkTabPrivilege(projectForm, "manageUnit_biddingDocumentReader", acl)) { //没有招标文件的查看权限
						continue;
					}
					projectForm.getTabs().addTab(-1, componentName, componentField.getTitle(), getTabJspFile(componentName + "Read.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), !tabSelected);
					continue;
				}
			}
			catch(Exception e) {

			}
		}
		if(project!=null) {
			//如果开标室时间时间已经到了,在"开标"前增加"报名情况",如果没有"开标",则在最后追加
			BiddingService biddingService = (BiddingService)getService("biddingService");
			boolean isPledgeVisible = biddingService.isPledgeVisible(project, sessionInfo);
			//if(isPledgeVisible) {
			if(project.getPlan()!=null && project.getPlan().getBuyDocumentEnd()!=null && project.getPlan().getBuyDocumentEnd().before(DateTimeUtils.now())) { //已经过了购买标书截止时间
				projectForm.getTabs().insertTab("bidopening", "bidding", "投标", getTabJspFile("bidding.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), false);
				//检查是否实名报名
				if("是".equals(project.getIsRealNameSignUp())) {
					projectForm.getTabs().insertTab("bidopening", "biddingJobholder", "参与人员", getTabJspFile("biddingJobholder.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), false);
				}
			}
			
			if(project.getAskQuestions()!=null && !project.getAskQuestions().isEmpty()) {
				projectForm.getTabs().addTab(-1, "ask", "提问", getTabJspFile("ask.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), false);
			}
			
			if(isPledgeVisible) { //允许查看报名详情
				if(workflowInterface!=null && ListUtils.findObjectByProperty(workflowInterface.getActions(), "name", "完善投标人账户信息")!=null) {
					request.getSession().setAttribute("complementProjectId", new Long(projectForm.getId()));
					projectForm.getTabs().addTab(-1, "accountsComplement", "投标人账户", getTabJspFile("pledgeAccountsComplement.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), false);
				}
				if(ListUtils.findObjectByProperty(projectForm.getFormActions(), "title", "完成保证金确认")!=null) {
					biddingService.updatePledgeStatus(project); //查询交易记录
					projectForm.getTabs().addTab(-1, "pledgeConfirm", "保证金", getTabJspFile("pledgeConfirm.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), true);
				}
				else if(ListUtils.findObjectByProperty(projectForm.getFormActions(), "title", "完成保证金返还名单确认")!=null) {
					projectForm.getTabs().addTab(-1, "pledgeReturnChoose", "保证金", getTabJspFile("pledgeReturnChoose.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), true);
				}
				else if(ListUtils.findObjectByProperty(projectForm.getFormActions(), "title", "完成保证金返还")!=null) {
					projectForm.getTabs().addTab(-1, "pledgeReturnTransfer", "保证金", getTabJspFile("pledgeReturnTransfer.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), true);
					//检查是否需要显示转载按钮
					boolean show = false;
					for(Iterator iterator = project.getSignUps()==null ? null : project.getSignUps().iterator(); !show && iterator!=null && iterator.hasNext();) {
						BiddingSignUp signUp = (BiddingSignUp)iterator.next();
						if(signUp.getPledgePaymentTime()==null || signUp.getPledgeConfirm()!='1' || signUp.getPledgeReturnTime()!=null || signUp.getPledgeReturnEnabled()!='1') {
							continue;
						}
						show = signUp.getPledgeReturnTransferTime()==null || //转账时间为空
							   (signUp.getPledgeReturnTransferError()!=null && !signUp.getPledgeReturnTransferError().isEmpty()); //转账失败原因不为空
					}
					request.setAttribute("showTransferButton", "true");
				}
				else {
					projectForm.getTabs().addTab(-1, "pledge", "保证金", getTabJspFile("pledge.jsp", cityEnglishName, projectCategoryEnglishName, projectProcedureEnglishName, request), false);
				}
			}
			
			addOpinionTab(projectForm); //办理意见
			if(getWorkflowInterface(projectForm, request, project, openMode, sessionInfo)!=null) {
				addWorkflowLogTab(projectForm); //流程记录
			}
		}
	}
	
	/**
	 * 获取TAB页签的JSP文件
	 * @param jspFileName
	 * @param cityEnglishName
	 * @param projectCategoryEnglishName
	 * @param projectProcedureEnglishName
	 * @param request
	 * @return
	 */
	private String getTabJspFile(String jspFileName, String cityEnglishName, String projectCategoryEnglishName, String projectProcedureEnglishName, HttpServletRequest request) {
		if(cityEnglishName!=null && !cityEnglishName.isEmpty()) {
			String basePath = Environment.getWebAppPath() + "/bidding/project" + (RequestUtils.getRequestURL(request, false).indexOf("/admin/")==-1 ? "" : "/admin") + "/";
			String jspFullPath = "tabpages/" + cityEnglishName.toLowerCase();
			for(int index = jspFullPath.length(); index!=-1; index = jspFullPath.lastIndexOf('/', index-1)) {
				//检查是否有为招标内容设计的JSP
				String jspPath = jspFullPath.substring(0, index) +
								 (projectCategoryEnglishName==null ? "" : "/" + projectCategoryEnglishName.toLowerCase()) +
								 (projectProcedureEnglishName==null ? "" : "/" + projectProcedureEnglishName.toLowerCase()) +
								 "/" + jspFileName;
				if(FileUtils.isExists(basePath + jspPath)) {
					return jspPath;
				}
				
				//检查是否有为项目分类设计的JSP
				jspPath = jspFullPath.substring(0, index) +
						  (projectCategoryEnglishName==null ? "" : "/" + projectCategoryEnglishName.toLowerCase()) +
						  "/" + jspFileName;
				if(FileUtils.isExists(basePath + jspPath)) {
					return jspPath;
				}
				
				//检查是否有区域设计的JSP
				jspPath = jspFullPath.substring(0, index) +
						  "/" + jspFileName;
				if(FileUtils.isExists(basePath + jspPath)) {
					return jspPath;
				}
			}
		}
		return "tabpages/" + jspFileName;
	}
	
	/**
	 * 检查用户的tab页签的权限
	 * @param projectForm
	 * @param manageUnitName
	 * @param acl
	 * @return
	 * @throws Exception
	 */
	private boolean checkTabPrivilege(Project projectForm, String manageUnitName, List acl) throws Exception {
		if(acl.contains(manageUnitName)) {
			return true;
		}
		//检查是否有做授权
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		List visitors = accessControlService.listVisitors(projectForm.getFormDefine().getApplicationName(), manageUnitName);
		return (visitors==null || visitors.isEmpty()); //没有授权记录,认为是允许查看
	}

	/**
	 * 初始化工程的组成部分
	 * @param projectForm
	 * @param project
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void initProjectComponents(Project projectForm, BiddingProject project, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		List componentFields = FieldUtils.listRecordFields(BiddingProject.class.getName(), "component", null, null, null, true, false, false, false, 1);
		BiddingProjectParameter parameter = null;
		for(Iterator iterator = componentFields.iterator(); iterator.hasNext();) {
			Field componentField = (Field)iterator.next();
			Object component = project==null ? null : PropertyUtils.getProperty(project, componentField.getName()); //获取组成部分
			if(component!=null) {
				continue; //组成部分不为空,不需要初始化
			}
			if(!isComponentEditable(componentField.getName(), projectForm, project, request, sessionInfo)) { //不可编辑
				continue;
			}
			if(parameter==null) {
				BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
				parameter = (project==null ? new BiddingProjectParameter() : biddingProjectParameterService.getParameter(project.getProjectCategory(), project.getProjectProcedure(), project.getCity()));
				if(parameter==null) {
					parameter = new BiddingProjectParameter();
				}
			}
			if("agentDraw".equals(componentField.getName())) { //代理抽签公告
				BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
				//设置代理抽签时间
				//Timestamp time = DateTimeUtils.now();
				//time = DateTimeUtils.add(time, Calendar.DAY_OF_MONTH, parameter.getAgentDrawDays());
				//time = DateTimeUtils.set(time, Calendar.HOUR_OF_DAY, 9);
				//time = DateTimeUtils.set(time, Calendar.MINUTE, 30);
				//time = DateTimeUtils.set(time, Calendar.SECOND, 0);
				//projectForm.getAgentDraw().setDrawTime(time);
				projectForm.getAgentDraw().setPublicLimit(parameter.getAgentDrawDays()); //设置公示期限
				projectForm.getAgentDraw().setContent(project.getProjectProcedure());
				//获取地区配置
				BiddingProjectCity area = biddingProjectParameterService.getCityDetail(project.getCity());
				if(area!=null) {
					projectForm.getAgentDraw().setMoney(area.getAgentChargeStandard());
					projectForm.getAgentDraw().setRemark(area.getAgentDrawRemark());
					//设置代理抽签地点
					projectForm.getAgentDraw().setDrawAddress(area.getDrawAddress());
				}
			}
			else if("biddingAgent".equals(componentField.getName())) { //中选代理
				projectForm.getBiddingAgent().setPublicLimit(parameter.getAgentResultDays()); //设置公示期限
			}
			else if("tender".equals(componentField.getName())) { //招标公告
				projectForm.getTender().setPublicLimit(parameter.getSignUpDays()); //设置公示期限
			}
			else if("plan".equals(componentField.getName())) { //招标安排
				projectForm.getPlan().setPublicPitchonDays(parameter.getPitchonDays()); //中标结果公示时间
				projectForm.getPlan().setNoticeDays(parameter.getNoticeDays()); //发放中标通知书时间
				projectForm.getPlan().setArchiveDays(parameter.getArchiveDays()); //文件备案时间
				//初始化各类信息发布地点或媒体
				BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
				BiddingProjectCity area = biddingProjectParameterService.getCityDetail(project.getCity());
				if(area!=null) {
					projectForm.getPlan().setInviteMedia(area.getInviteMedia()); //发布招标邀请书媒体,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
					projectForm.getPlan().setBuyDocumentAddress(area.getBuyDocumentAddress()); //购买招标文件地址
					projectForm.getPlan().setAskMedia(area.getAskMedia()); //招标文件质疑地点,福州市建设工程交易管理中心公告栏；福州建设工程招标投标网（网址：http://www.fzztb.rog）或福建招标与采购网（http://www.fjbid.gov.cn）
					projectForm.getPlan().setSubmitAddress(area.getSubmitAddress()); //投标文件的递交地点,福州市建设工程交易管理中心
					projectForm.getPlan().setBidopeningAddress(area.getBidopeningAddress()); //开标地点,福州市建设工程交易管理中心
					projectForm.getPlan().setPitchonAddress(area.getPitchonAddress()); //确定中标人地点,福州市建设工程交易管理中心
					projectForm.getPlan().setPublicPitchonMedia(area.getPublicPitchonMedia()); //中标结果公示媒体
					projectForm.getPlan().setNoticeAddress(area.getNoticeAddress()); //发放中标通知书地点,招标人
					projectForm.getPlan().setArchiveAddress(area.getArchiveAddress()); //书面报告备案地点,招标人
				}
			}
			else if("material".equals(componentField.getName())) { //招标文件实质性内容
				projectForm.getMaterial().setPublicLimit(parameter.getSignUpDays()); //设置公示期限
			}
			else if("pitchon".equals(componentField.getName())) { //中标结果公示
				if(project.getPlans()!=null && !project.getPlans().isEmpty()) {
					BiddingProjectPlan plan = (BiddingProjectPlan)project.getPlans().iterator().next();
					projectForm.getPitchon().setPublicLimit(plan.getPublicPitchonDays());
				}
				else {
					projectForm.getPitchon().setPublicLimit(parameter.getPitchonDays()); //设置公示期限
				}
			}
			else if("preapproval".equals(componentField.getName())) { //预审公示
				projectForm.getPreapproval().setPublicLimit(parameter.getPreapprovalDays()); //设置公示期限
			}
			else if("bidopening".equals(componentField.getName())) { //开标公示
				projectForm.getBidopening().setPublicLimit(parameter.getBidopeningDays()); //设置公示期限
			}
			else if("bidopeningRoomSchedule".equals(componentField.getName())) { //开标室分配
				if(project.getPlans()!=null && !project.getPlans().isEmpty()) {
					BiddingProjectPlan plan = (BiddingProjectPlan)project.getPlans().iterator().next();
					Timestamp time = plan.getBidopeningTime();
					projectForm.getBidopeningRoomSchedule().setBeginTime(time);
					//设置结束时间为上午下班时间
					if(projectForm.getWorkEndAm()!=null && !projectForm.getWorkEndAm().equals("")) {
						Timestamp workEndAm = DateTimeUtils.parseTimestamp(projectForm.getWorkEndAm(), "HH:mm");
						time = DateTimeUtils.set(time, Calendar.HOUR_OF_DAY, DateTimeUtils.getHour(workEndAm));
						time = DateTimeUtils.set(time, Calendar.MINUTE, DateTimeUtils.getMinute(workEndAm));
					}
					else {
						time = DateTimeUtils.set(time, Calendar.HOUR_OF_DAY, 12);
						time = DateTimeUtils.set(time, Calendar.MINUTE, 0);
					}
					time = DateTimeUtils.set(time, Calendar.SECOND, 0);
					projectForm.getBidopeningRoomSchedule().setEndTime(time);
				}
			}
			else if("evaluatingRoomSchedule".equals(componentField.getName())) { //评标室分配
				Timestamp time = DateTimeUtils.now();
				projectForm.getEvaluatingRoomSchedule().setBeginTime(time);
				projectForm.getEvaluatingRoomSchedule().setEndTime(DateTimeUtils.add(time, Calendar.HOUR_OF_DAY, 5)); //TODO:读取配置
			}
			else if("useFee".equals(componentField.getName())) { //场地费
				projectForm.getUseFee().setPayTime(DateTimeUtils.now()); //设置缴费时间
				//projectForm.getUseFee().setBillingTime(DateTimeUtils.now()); //设置开票时间
			}
			else if("pay".equals(componentField.getName())) { //缴费
				projectForm.getPay().setPayTime(DateTimeUtils.now()); //设置缴费时间
			}
			else if("declare".equals(componentField.getName())) { //报建
				projectForm.getDeclare().setOwner(project.getOwner()); //建设单位
				projectForm.getDeclare().setOwnerType(project.getOwnerType()); //建设单位性质,全民
				projectForm.getDeclare().setOwnerRepresentative(project.getOwnerRepresentative()); //建设单位法人代表
				projectForm.getDeclare().setOwnerLinkman(project.getOwnerLinkman()); //建设单位联系人
				projectForm.getDeclare().setOwnerTel(project.getOwnerTel()); //建设单位联系电话
				projectForm.getDeclare().setDeclaringProjectName(project.getProjectName());
				projectForm.getDeclare().setProjectAddress(project.getProjectAddress()); //建设地点
				projectForm.getDeclare().setProjectProperty("生产"); //项目性质,生产/非生产
				projectForm.getDeclare().setReceiveTime(DateTimeUtils.now()); //收理时间
				//从招标公告中获取信息
				if(project.getTenders()!=null && !project.getTenders().isEmpty()) {
					BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
					projectForm.getDeclare().setProjectAddress(tender.getProjectAddress()); //建设地点
					//private Date proposalDate; //批准时间
					//private String invest; //投资规模
					projectForm.getDeclare().setScale(tender.getProjectScale()); //工程规模
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		Project projectForm = (Project)workflowForm;
		if(projectForm.getProjectCategory()==null || projectForm.getProjectCategory().equals("") || //项目类别为空 
		   projectForm.getProjectProcedure()==null || projectForm.getProjectProcedure().equals("") || //招标内容为空
		   projectForm.getCity()==null || projectForm.getCity().equals("")) { //所在城市
			return null;
		}
		//获取工作流配置
		BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
		long workflowId = biddingProjectParameterService.getWorkflowId(projectForm.getProjectCategory(), projectForm.getProjectProcedure(), projectForm.getCity());
		if(workflowId<=0) {
			throw new PrivilegeException();
		}
		//获取流程入口
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry("" + workflowId, participantCallback, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			projectForm.setError("没有和当前项目匹配的流程或者没有对应流程的办理权限");
			throw new ValidateException();
		}
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRecord(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record generateRecord(ActionForm form, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		BiddingProject project = (BiddingProject)super.generateRecord(form, openMode, request, sessionInfo);
		String[] jobholderCategory = request.getParameterValues("jobholderCategory");
		if(jobholderCategory!=null) {
			String[] jobholderQualifications = request.getParameterValues("jobholderQualifications");
			String[] jobholderNumber = request.getParameterValues("jobholderNumber");
			Project projectForm = (Project)form;
			projectForm.setJobholders(new LinkedHashSet());
			for(int i=0; i<jobholderCategory.length; i++) {
				BiddingProjectJobholder jobholder = new BiddingProjectJobholder();
				jobholder.setProjectId(project.getId()); //项目ID
				jobholder.setJobholderCategory(jobholderCategory[i]); //人员类别
				jobholder.setQualifications(jobholderQualifications[i]); //资质等级
				jobholder.setJobholderNumber(Integer.parseInt(jobholderNumber[i])); //需求数量
				projectForm.getJobholders().add(jobholder);
			}
			project.setJobholders(projectForm.getJobholders());
		}
		return project;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		BiddingProject project = (BiddingProject)record;
		Project projectForm = (Project)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			project.setCreated(DateTimeUtils.now()); //创建时间
			//检查是否实名报名
			BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			project.setIsRealNameSignUp(biddingProjectParameterService.isRealNameSignUp(project.getCity(), project.getProjectCategory(), project.getProjectProcedure()) ? "是" : "否");
		}
		if(project.getPledgeBank()!=null) { //保证金开户行
			project.setPledgeBank(project.getPledgeBank().replaceAll("&nbsp;", "").trim());
		}
		if(project.getPledgeAccountName()!=null) { //保证金账户名称
			project.setPledgeAccountName(project.getPledgeAccountName().replaceAll("&nbsp;", "").trim());
		}
		if(project.getPledgeAccount()!=null) { //保证金账号
			project.setPledgeAccount(project.getPledgeAccount().replaceAll("&nbsp;", "").trim());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(request.getAttribute("componentSaved")!=null) {
			return record;
		}
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		
		List componentFields = FieldUtils.listRecordFields(BiddingProject.class.getName(), "component", null, null, null, true, false, false, false, 1);
		for(Iterator iterator = componentFields.iterator(); iterator.hasNext();) {
			Field componentField = (Field)iterator.next();
			try {
				saveProjectComponent(componentField.getName(), projectForm, project, request);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		if("否".equals(project.getAgentEnable())) { //不需要代理
			biddingProjectService.removeProjectComponent(projectForm.getBiddingAgent()); //删除中选代理
			biddingProjectService.removeProjectComponent(projectForm.getAgentDraw()); //删除代理抽签公告
		}
		else if("直接指定".equals(project.getAgentMode())) {
			biddingProjectService.removeProjectComponent(projectForm.getAgentDraw()); //删除代理抽签公告
		}
		else if(project.getAgentDraws()==null || project.getAgentDraws().isEmpty() ||
				((BiddingProjectAgentDraw)project.getAgentDraws().iterator().next()).getPublicBeginTime()==null) { //抽签公告没有发布
			biddingProjectService.removeProjectComponent(projectForm.getBiddingAgent()); //删除中选代理
		}
		//保存投标人账户
		BiddingService biddingService = (BiddingService)getService("biddingService");
		biddingService.accountsComplement(project, request);
		
		//保存中标候选企业
		if(projectForm.getRankingEnterpriseNames()!=null) {
			biddingService.saveSignUpRanking(project, projectForm.getRankingSignUpIds());
		}
		request.setAttribute("componentSaved", "1");
		return record;
	}
	
	/**
	 * 保存工程的组成部分
	 * @param projectForm
	 * @param project
	 * @param request
	 * @throws Exception
	 */
	protected void saveProjectComponent(String componentName, Project projectForm, BiddingProject project, HttpServletRequest request) throws Exception {
		//获取表单字段列表
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		//获取ID
		long id = ((Long)PropertyUtils.getProperty(projectForm, componentName + ".id")).longValue();
		BiddingProjectComponent component;
		if(id==0) { //新记录
			component = (BiddingProjectComponent)PropertyUtils.getProperty(projectForm, componentName);
		}
		else { //旧记录
			Set set = (Set)PropertyUtils.getProperty(project, componentName + "s");
			component = (BiddingProjectComponent)set.iterator().next();
		}
		boolean submitted = false;
		//把提交的内容更新到原来的pojo中
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			if(!parameterName.startsWith(componentName + ".")) { //检查是否有提交组成部分的字段
				continue;
			}
			String shortName = parameterName.substring(componentName.length() + 1);
			if(!shortName.equals("id") && PropertyUtils.isWriteable(component, shortName) && PropertyUtils.isReadable(projectForm, parameterName)) {
				PropertyUtils.setProperty(component, shortName, PropertyUtils.getProperty(projectForm, parameterName));
				//检查是否隐藏字段
				Field field = FieldUtils.getFormField(projectForm.getFormDefine(), parameterName, request);
				if(field==null || !"hidden".equals(field.getInputMode())) { //不是隐藏字段
					submitted = true;
				}
			}
		}
		if(!submitted) { //用户没有提交组成部分的字段
			return;
		}
		if(id>0) { //旧记录
			biddingProjectService.saveProjectComponent(project.getId(), project.getProjectName(), project.getBiddingMode(), component);
			return;
		}
		//新记录
		if(component instanceof BiddingProjectAgent) {
			if(((BiddingProjectAgent)component).getAgentId()==0) { //没有指定代理,不保存
				return;
			}
		}
		else if(component instanceof BiddingProjectAgentDraw) { //代理抽签
			if("否".equals(project.getAgentEnable()) || !"随机抽签".equals(project.getAgentMode())) { //不需要抽签,不保存
				return;
			}
			BiddingProjectAgentDraw agentDraw = (BiddingProjectAgentDraw)component;
			BiddingProjectParameterService biddingProjectParameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			//获取地区配置
			BiddingProjectCity area = biddingProjectParameterService.getCityDetail(project.getCity());
			if(area!=null) {
				agentDraw.setMoney(area.getAgentChargeStandard()); //取费标准
				agentDraw.setRemark(area.getAgentDrawRemark()); //备注
				agentDraw.setDrawAddress(area.getDrawAddress()); //设置代理抽签地点
			}
		}
		else if(component instanceof BiddingProjectSupplement) { //补充通知
			((BiddingProjectSupplement)component).setCreated(DateTimeUtils.now());
		}
		//直接保存
		biddingProjectService.saveProjectComponent(projectForm.getId(), project.getProjectName(), project.getBiddingMode(), component);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if("true".equals(request.getParameter("physical"))) { //物理删除
			super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		}
		else { //逻辑删除
			BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
			BiddingProject project = (BiddingProject)record;
			project.setHalt('1');
			biddingProjectService.update(project);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listUndoneActions(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listUndoneActions(WorkflowForm workflowForm, Record record, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		List undoneActions = super.listUndoneActions(workflowForm, record, openMode, request, sessionInfo);
		if(undoneActions==null || undoneActions.isEmpty()) {
			return null;
		}
		Project projectForm = (Project)workflowForm;
		BiddingProject project = (BiddingProject)record;
		for(Iterator iterator = undoneActions.iterator(); iterator.hasNext();) {
			Action action = (Action)iterator.next();
			//判断是否盖过章
			if(action.getName().equals("招标文件盖章")) {
				if(projectForm.isBiddingDocumentsStamp()) {
					iterator.remove();
				}
			}
			else if(action.getName().equals("完善投标人账户信息")) {
				boolean complement = true;
				for(Iterator iteratorSignUp = project.getSignUps()==null ? null : project.getSignUps().iterator(); iteratorSignUp!=null && iteratorSignUp.hasNext();) {
					BiddingSignUp signUp = (BiddingSignUp)iteratorSignUp.next();
					if(signUp.getPledgePaymentTime()!=null && //保证金已缴
					   (signUp.getEnterpriseName()==null || signUp.getEnterpriseName().isEmpty() || //企业名称为空
					    signUp.getEnterpriseBank()==null || signUp.getEnterpriseBank().isEmpty() || //开户行为空
					    signUp.getEnterpriseAccount()==null || signUp.getEnterpriseAccount().isEmpty())) { //帐号为空
						complement = false;
						break;
					}
				}
				if(complement) { //检查是否已经完成
					getWorkflowExploitService().completeAction("" + workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), "完善投标人账户信息", sessionInfo);
					iterator.remove();
				}
			}
		}
		return undoneActions;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#createWorklfowMessage(java.lang.Object, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm)
	 */
	protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
		WorkflowMessage workflowMessage = super.createWorklfowMessage(record, workflowForm);
		BiddingProject project = (BiddingProject)record;
		workflowMessage.setContent(project.getProjectName() + " " + project.getProjectNumber());
		return workflowMessage;
	}
	
	/**
	 * 校验开标室安排
	 * @param projectForm
	 * @throws ValidateException
	 * @throws ServiceException
	 * @throws SystemUnregistException
	 */
	protected void validateBidopeningRoomSchedule(Project projectForm)  throws ValidateException, ServiceException, SystemUnregistException {
		if(projectForm.getBidopeningRoomSchedule().getBeginTime()==null || projectForm.getBidopeningRoomSchedule().getEndTime()==null) {
			projectForm.setError("开始使用时间或者结束使用时间不能为空");
			throw new ValidateException();
		}
		if(projectForm.getBidopeningRoomSchedule().getRoomName()==null || projectForm.getBidopeningRoomSchedule().getRoomName().isEmpty()) {
			projectForm.setError("开标室不能为空。");
			throw new ValidateException();
		}
		if(projectForm.getBidopeningRoomSchedule().getBeginTime().before(DateTimeUtils.now())) {
			projectForm.setError("开始使用时间不允许早于当前时间");
			throw new ValidateException();
		}
		if(projectForm.getPlan()!=null &&
		   projectForm.getPlan().getBuyDocumentEnd()!=null &&
		   projectForm.getBidopeningRoomSchedule().getBeginTime().before(projectForm.getPlan().getBuyDocumentEnd())) {
			projectForm.setError("开始使用时间不允许早于购买标书截止时间");
			throw new ValidateException();
		}
		long diff = projectForm.getBidopeningRoomSchedule().getEndTime().getTime() - projectForm.getBidopeningRoomSchedule().getBeginTime().getTime();
		if(diff<=0) {
			projectForm.setError("开始使用时间不能早于结束使用时间。");
			throw new ValidateException();
		}
		if(diff > 24*3600*1000) {
			projectForm.setError("结束使用时间不允许超过开始使用时间24小时");
			throw new ValidateException();
		}
		if(DateTimeUtils.getHour(projectForm.getBidopeningRoomSchedule().getBeginTime())<7) {
			projectForm.setError("开始使用时间不正确");
			throw new ValidateException();
		}
		BiddingRoomService biddingRoomService = (BiddingRoomService)getService("biddingRoomService");
		if(!biddingRoomService.isFreeRoom(projectForm.getBidopeningRoomSchedule().getRoomId(), projectForm.getId(), projectForm.getBidopeningRoomSchedule().getBeginTime(), projectForm.getBidopeningRoomSchedule().getEndTime())) {
			projectForm.setError("开标室已经被占用");
			throw new ValidateException();
		}
	}
}