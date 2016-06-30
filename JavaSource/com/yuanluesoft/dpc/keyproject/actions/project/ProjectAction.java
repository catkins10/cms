package com.yuanluesoft.dpc.keyproject.actions.project;

import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.keyproject.forms.Project;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestComplete;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProblem;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProgress;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;

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
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
		Project projectForm = (Project)form;
		KeyProject project = (KeyProject)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			project.setCreated(DateTimeUtils.now());
			project.setCreator(sessionInfo.getUserName());
			project.setCreatorId(sessionInfo.getUserId());
			if(project.getFiveYearPlan()!='1') {
				project.setFiveYearPlan('0');
			}
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存项目所在地
		keyProjectService.updateProjectArea(project, projectForm.getArea());
		
		//保存简化报送的数据
		if(projectForm.getDebriefYear()>0) {
			//保存计划完成投资
			KeyProjectInvestComplete projectInvestComplete = null;
			for(Iterator iterator = project.getInvestCompletes()==null ? null : project.getInvestCompletes().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
				if(investComplete.getCompleteYear()==projectForm.getDebriefYear() &&
				   investComplete.getCompleteMonth()==projectForm.getDebriefMonth() &&
				   investComplete.getCompleteTenDay()=='1') {
					projectInvestComplete = investComplete;
					projectInvestComplete.setInvestPlan(projectForm.getDebriefInvestPlan());
					keyProjectService.update(projectInvestComplete);
				}
			}
			if(projectInvestComplete==null) {
				projectInvestComplete = new KeyProjectInvestComplete();
				projectInvestComplete.setId(UUIDLongGenerator.generateId()); //ID
				projectInvestComplete.setProjectId(project.getId()); //项目ID
				projectInvestComplete.setApproverId(0); //审核人ID
				projectInvestComplete.setApprovalTime(null); //审核时间
				projectInvestComplete.setCompleteYear(projectForm.getDebriefYear()); //年份
				projectInvestComplete.setCompleteMonth(projectForm.getDebriefMonth()); //月份
				projectInvestComplete.setCompleteTenDay('1'); //旬,上旬/1、中旬/2、下旬/3
				projectInvestComplete.setInvestPlan(0); //计划完成投资（万元）
				projectInvestComplete.setCompleted('1'); //是否已提交完成情况
				projectInvestComplete.setInvestPlan(projectForm.getDebriefInvestPlan());
				keyProjectService.save(projectInvestComplete);
				if(project.getInvestCompletes()==null) {
					project.setInvestCompletes(new HashSet());
				}
				project.getInvestCompletes().add(projectInvestComplete);
			}
			
			//保存完成投资
			projectInvestComplete = null;
			for(Iterator iterator = project.getInvestCompletes()==null ? null : project.getInvestCompletes().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
				if(investComplete.getCompleteYear()==projectForm.getDebriefYear() &&
				   investComplete.getCompleteMonth()==projectForm.getDebriefMonth() &&
				   investComplete.getCompleteTenDay()==projectForm.getDebriefTenDay()) {
					projectInvestComplete = investComplete;
					projectInvestComplete.setCompleteInvest(projectForm.getDebriefInvestComplete());
					keyProjectService.update(projectInvestComplete);
				}
			}
			if(projectInvestComplete==null) {
				projectInvestComplete = new KeyProjectInvestComplete();
				projectInvestComplete.setId(UUIDLongGenerator.generateId()); //ID
				projectInvestComplete.setProjectId(project.getId()); //项目ID
				projectInvestComplete.setApproverId(0); //审核人ID
				projectInvestComplete.setApprovalTime(null); //审核时间
				projectInvestComplete.setCompleteYear(projectForm.getDebriefYear()); //年份
				projectInvestComplete.setCompleteMonth(projectForm.getDebriefMonth()); //月份
				projectInvestComplete.setCompleteTenDay(projectForm.getDebriefTenDay()); //旬,上旬/1、中旬/2、下旬/3
				projectInvestComplete.setInvestPlan(0); //计划完成投资（万元）
				projectInvestComplete.setCompleted('1'); //是否已提交完成情况
				projectInvestComplete.setCompleteInvest(projectForm.getDebriefInvestComplete());
				keyProjectService.save(projectInvestComplete);
			}
			
			//保存形象进度
			KeyProjectProgress projectProgress = null;
			for(Iterator iterator = project.getProgresses()==null ? null : project.getProgresses().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
				if(progress.getProgressYear()==projectForm.getDebriefYear() &&
				   progress.getProgressMonth()==projectForm.getDebriefMonth() &&
				   progress.getProgressTenDay()==projectForm.getDebriefTenDay()) {
					projectProgress = progress;
					projectProgress.setPlan(projectForm.getDebriefProgress());
					keyProjectService.update(projectProgress);
				}
			}
			if(projectProgress==null) {
				projectProgress = new KeyProjectProgress();
				projectProgress.setId(UUIDLongGenerator.generateId()); //ID
				projectProgress.setProjectId(project.getId()); //项目ID
				projectProgress.setApproverId(0); //审核人ID
				projectProgress.setApprovalTime(null); //审核时间
				projectProgress.setProgressYear(projectForm.getDebriefYear()); //年份
				projectProgress.setProgressMonth(projectForm.getDebriefMonth()); //月份
				projectProgress.setProgressTenDay(projectForm.getDebriefTenDay()); //旬,上旬/1、中旬/2、下旬/3
				projectProgress.setPlan(projectForm.getDebriefProgress()); //安排
				projectProgress.setCompleted('0'); //是否已汇报项目进度
				projectProgress.setProgress(null); //进度
				keyProjectService.save(projectProgress);
			}
			
			//保存存在的问题
			KeyProjectProblem projectProblem = null;
			for(Iterator iterator = project.getProblems()==null ? null : project.getProblems().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectProblem problem = (KeyProjectProblem)iterator.next();
				if(problem.getProblemYear()==projectForm.getDebriefYear() &&
				   problem.getProblemMonth()==projectForm.getDebriefMonth()) {
					projectProblem = problem;
					projectProblem.setProblem(projectForm.getDebriefProblem());
					keyProjectService.update(projectProblem);
				}
			}
			if(projectProblem==null) {
				projectProblem = new KeyProjectProblem();
				projectProblem.setId(UUIDLongGenerator.generateId()); //ID
				projectProblem.setProjectId(project.getId()); //项目ID
				projectProblem.setApproverId(0); //审核人ID
				projectProblem.setApprovalTime(null); //审核时间
				projectProblem.setProblemYear(projectForm.getDebriefYear()); //年份
				projectProblem.setProblemMonth(projectForm.getDebriefMonth()); //月份
				projectProblem.setProblem(projectForm.getDebriefProblem()); //问题描述
				projectProblem.setResponsible(null); //责任单位或责任人
				projectProblem.setTimeLimit(null); //解决时限
				projectProblem.setCompleted('0'); //是否已提交解决情况
				projectProblem.setResult(null); //解决情况
				keyProjectService.save(projectProblem);
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员编辑
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员删除
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		if(form.getSubForm().indexOf("DebriefSimple")!=-1) { //简化的报送界面
			Project projectForm = (Project)form;
			//设置报送时间
			if(projectForm.getDebriefYear()==0) {
				if(projectForm.getInvestCompletes()==null || projectForm.getInvestCompletes().isEmpty()) {
					Date date = DateTimeUtils.date();
					projectForm.setDebriefYear(DateTimeUtils.getYear(date));
					projectForm.setDebriefMonth(DateTimeUtils.getMonth(date) + 1);
					projectForm.setDebriefTenDay((char)('1' + Math.min(2, DateTimeUtils.getMonth(date)%10)));
				}
				else {
					KeyProjectInvestComplete lastInvestComplete = null;
					for(Iterator iterator = projectForm.getInvestCompletes().iterator(); iterator.hasNext();) {
						lastInvestComplete = (KeyProjectInvestComplete)iterator.next();
					}
					projectForm.setDebriefTenDay((char)(lastInvestComplete.getCompleteTenDay()=='3' ? '1' : lastInvestComplete.getCompleteTenDay() + 1));
					projectForm.setDebriefMonth(lastInvestComplete.getCompleteTenDay()<'3' && lastInvestComplete.getCompleteTenDay()>'0' ? lastInvestComplete.getCompleteMonth() : (lastInvestComplete.getCompleteMonth()==12 ? 1 : lastInvestComplete.getCompleteMonth() + 1));
					projectForm.setDebriefYear(projectForm.getDebriefMonth()==lastInvestComplete.getCompleteMonth() || lastInvestComplete.getCompleteMonth()<12 ? lastInvestComplete.getCompleteYear() : lastInvestComplete.getCompleteYear() + 1);
				}
			}
			projectForm.setDebriefInvestPlan(0);
			projectForm.setDebriefInvestComplete(0);
			projectForm.setDebriefProgress(null);
			projectForm.setDebriefProblem(null);
			//设置月计划完成投资
			for(Iterator iterator = projectForm.getInvestCompletes()==null ? null : projectForm.getInvestCompletes().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
				if(investComplete.getCompleteYear()==projectForm.getDebriefYear() &&
				   investComplete.getCompleteMonth()==projectForm.getDebriefMonth() &&
				   investComplete.getCompleteTenDay()=='1') {
					projectForm.setDebriefInvestPlan(investComplete.getInvestPlan());
				}
			}
			//设置完成投资
			for(Iterator iterator = projectForm.getInvestCompletes()==null ? null : projectForm.getInvestCompletes().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectInvestComplete investComplete = (KeyProjectInvestComplete)iterator.next();
				if(investComplete.getCompleteYear()==projectForm.getDebriefYear() &&
				   investComplete.getCompleteMonth()==projectForm.getDebriefMonth() &&
				   investComplete.getCompleteTenDay()==projectForm.getDebriefTenDay()) {
					projectForm.setDebriefInvestComplete(investComplete.getCompleteInvest());
				}
			}
			//设置形象进度
			for(Iterator iterator = projectForm.getProgresses()==null ? null : projectForm.getProgresses().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectProgress progress = (KeyProjectProgress)iterator.next();
				if(progress.getProgressYear()==projectForm.getDebriefYear() &&
				   progress.getProgressMonth()==projectForm.getDebriefMonth() &&
				   progress.getProgressTenDay()==projectForm.getDebriefTenDay()) {
					projectForm.setDebriefProgress(progress.getPlan());
				}
			}
			//设置存在的问题
			for(Iterator iterator = projectForm.getProblems()==null ? null : projectForm.getProblems().iterator(); iterator!=null && iterator.hasNext();) {
				KeyProjectProblem problem = (KeyProjectProblem)iterator.next();
				if(problem.getProblemYear()==projectForm.getDebriefYear() &&
				   problem.getProblemMonth()==projectForm.getDebriefMonth()) {
					projectForm.setDebriefProblem(problem.getProblem());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		KeyProject project = (KeyProject)record;
		Project projectForm = (Project)form;
		KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员编辑
			projectForm.setSubForm("projectEdit.jsp");
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(form.getSubForm().indexOf("DebriefSimple")==-1) { //不是简化的报送界面
			//设置TAB列表
			int tabIndex = 1;
			if("projectEdit.jsp".equals(projectForm.getSubForm())) {
				projectForm.getTabs().addTab(tabIndex++, "debrief", "汇报内容", "tabpage/debrief.jsp", false);
			}
			String[] componentNames = keyProjectService.getComponentNames().split(",");
			for(int i=0; i<componentNames.length; i++) {
				String[] values = componentNames[i].split("\\|");
				if("declares".equals(values[0])) { //申报情况
					if(project==null || project.getDeclares()==null || project.getDeclares().isEmpty()) {
						continue;
					}
				}
				else if("fiveYearPlans".equals(values[0])) { //五年计划
					if(projectForm.getFiveYearPlan()!='1' && (project==null || project.getFiveYearPlan()!='1')) {
						continue;
					}
				}
				projectForm.getTabs().addTab(tabIndex++, values[0], values[1], "tabpage/" + values[0] + ".jsp", false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, java.lang.Object, java.lang.Object, java.lang.String, java.lang.String, java.util.List, char, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(component!=null && (component instanceof KeyProjectComponent) && PropertyUtils.isReadable(component, "completed")) {
			KeyProjectComponent projectComponent = (KeyProjectComponent)component;
			if(projectComponent.getNeedApproval()=='2' && //计划已经审核过
			   !acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && //不是管理员
			   accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE) { //允许编辑
				//需要输入完成情况时,设置子表单为Restricted(有限制的)
				form.setSubForm((PropertyUtils.getProperty(component, "completed") + "").equals("1") || (PropertyUtils.getProperty(form, componentName + ".completed")+"").equals("1") ? "Restricted" : "Read");
			}
		}
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		if(form.getSubForm().indexOf("Edit")==-1) {
			form.getFormActions().removeFormAction("删除");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		try {
			PropertyUtils.setProperty(form, componentName + ".completed", new Character('0'));
		}
		catch(Exception e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //允许管理员修改
		}
		char accessLevel = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		if(accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE) { //编辑状态
			WorkflowInterface workflowInterface;
			try {
				workflowInterface = getWorkflowInterface((WorkflowForm)form, request, record, record==null ? OPEN_MODE_CREATE : OPEN_MODE_EDIT, sessionInfo);
				if("Read".equals(workflowInterface.getSubForm())) { //只读表单,不允许编辑组成部分
					accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
				}
			}
			catch (Exception e) {
				
			}
		}
		if(accessLevel==RecordControlService.ACCESS_LEVEL_EDITABLE && (component instanceof KeyProjectComponent) && ((KeyProjectComponent)component).getNeedApproval()=='0') { //已经审批过
			KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
			return keyProjectService.isApprovalDebrief() ? RecordControlService.ACCESS_LEVEL_READONLY : accessLevel; //如果汇报内容需要审核,则不允许修改
		}
		return accessLevel;
	}
}