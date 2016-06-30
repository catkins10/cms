package com.yuanluesoft.bidding.project.actions.project;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.bidding.project.forms.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.resource.Action;

/**
 * 
 * @author yuanlue
 *
 */
public class ProjectAction extends com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction {

	public ProjectAction() {
		super();
		sessionInfoClass = BiddingSessionInfo.class; //设置会话类型
		alwaysAutoSend = true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return new Link(Environment.getWebApplicationSafeUrl() +  "/bidding/enterprise/login.shtml" + (siteId>0 ? "?siteId=" + siteId : ""), "utf-8");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Project projectForm = (Project)form;
		TabList tabs = projectForm.getTabs();
		if(tabs==null) {
			return;
		}
		//删除"流程记录"页签,如果用户有上传招标文件的权限,删除"实质性内容"和"招标公告"页签
		for(Iterator iterator = tabs.iterator(); iterator.hasNext();) {
			Tab tab = (Tab)iterator.next();
			if(tab.getName().equals("流程记录") || tab.getName().equals("保证金")) {
				iterator.remove();
			}
			if(isComponentEditable("document", projectForm, (BiddingProject)record, request, sessionInfo)) { //有上传招标文件的权限
			   if(tab.getName().equals("安排") || tab.getName().equals("实质性内容") || tab.getName().equals("招标公告")) {
				   iterator.remove();
			   }
			   else if(tab.getName().equals("招标文件")) {
				   tab.setSelected(true);
			   }
			}
			else if(tab.getName().equals("招标文件")) { //没有上传招标文件的权限
				iterator.remove();
			}
		}
		//用户如果有发放中标通知书的权限,则把"中标通知"页签设置为选中
		if(ListUtils.findObjectByProperty(projectForm.getFormActions(), "title", "发放中标通知书")!=null) {
			tabs.setTabSelected("notice");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		if(((BiddingSessionInfo)sessionInfo).isAgent()) { //是代理
			Project projectForm = (Project)form;
			projectForm.setAgentEnable("是");
			projectForm.setAgentMode("直接指定");
			EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
			BiddingEnterprise agent = enterpriseService.getEnterprise(sessionInfo.getDepartmentId());
			projectForm.getBiddingAgent().setAgentId(agent.getId()); //企业ID
			projectForm.getBiddingAgent().setAgentName(agent.getName()); //企业名称
			projectForm.getBiddingAgent().setAgentRepresentative(agent.getLegalRepresentative()); //法人代表
			projectForm.getBiddingAgent().setAgentLinkman(agent.getLinkman()); //联系人
			projectForm.getBiddingAgent().setAgentTel(agent.getTel()); //联系电话
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction#listUndoneActions(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listUndoneActions(WorkflowForm workflowForm, Record record, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		List undoneActions = super.listUndoneActions(workflowForm, record, openMode, request, sessionInfo);
		if(undoneActions==null || undoneActions.isEmpty()) {
			return null;
		}
		for(Iterator iterator = undoneActions.iterator(); iterator.hasNext();) {
			Action action = (Action)iterator.next();
			workflowForm.setError(action.getPrompt());
		}
		throw new ValidateException();
	}
}