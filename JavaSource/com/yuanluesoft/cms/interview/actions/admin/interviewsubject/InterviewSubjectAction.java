package com.yuanluesoft.cms.interview.actions.admin.interviewsubject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.interview.forms.admin.InterviewSubject;
import com.yuanluesoft.cms.interview.pojo.InterviewRole;
import com.yuanluesoft.cms.interview.pojo.InterviewSpeakFlow;
import com.yuanluesoft.cms.interview.pojo.InterviewSubjectRole;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class InterviewSubjectAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		if(record!=null) {
			//检查用户是否主持人或者审核人
			com.yuanluesoft.cms.interview.pojo.InterviewSubject interviewSubject = (com.yuanluesoft.cms.interview.pojo.InterviewSubject)record;
			InterviewService interviewService = (InterviewService)getService("interviewService"); //访谈服务
			//获取用户角色
			String role = interviewService.getInterviewRole(interviewSubject, sessionInfo);
			if("主持人".equals(role)) {
				acl.add("compere");
			}
			else if(role!=null && !role.equals("网友") && !role.equals("嘉宾")) {
				acl.add("approval");
			}
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkAttachmentUploadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAttachmentUploadPrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("compere") || acl.contains("approval")) { //允许主持人或审核人上传附件
			return;
		}
		super.checkAttachmentUploadPrivilege(form, request, record, acl, sessionInfo);
	}


	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("compere") || acl.contains("approval")) { //允许主持人或审核人编辑组件
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		InterviewSubject subjectForm = (InterviewSubject)form;
		InterviewService interviewService = (InterviewService)getService("interviewService");
		//初始化角色成员列表
		List siteInterviewRoles = interviewService.listInterviewRoles(subjectForm.getSiteId());
		if(siteInterviewRoles!=null) {
			Set roles = new HashSet();
			for(Iterator iterator = siteInterviewRoles.iterator(); iterator.hasNext();) {
				InterviewRole interviewRole = (InterviewRole)iterator.next();
				InterviewSubjectRole role = new InterviewSubjectRole();
				PropertyUtils.copyProperties(role, interviewRole);
				role.setId(UUIDLongGenerator.generateId());
				roles.add(role);
			}
			subjectForm.setRoles(roles);
		}
		//初始化发言审核顺序
		InterviewSpeakFlow speakFlow = interviewService.loadSpeakFlow(subjectForm.getSiteId());
		if(speakFlow!=null) {
			subjectForm.setSpeakFlow(speakFlow.getSpeakFlow());
			subjectForm.setCompereSpeakFlow(speakFlow.getCompereSpeakFlow());
			subjectForm.setGuestsSpeakFlow(speakFlow.getGuestsSpeakFlow());
		}
		//初始化主持人
		subjectForm.setCompereIds(sessionInfo.getUserId() + "");
		subjectForm.setCompereNames(sessionInfo.getUserName());
		//设置创建人和创建时间
		subjectForm.setCreator(sessionInfo.getUserName());
		subjectForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		//获取角色列表
		InterviewService interviewService = (InterviewService)getService("interviewService");
		InterviewSubject subjectForm = (InterviewSubject)form;
		com.yuanluesoft.cms.interview.pojo.InterviewSubject interviewSubject = (com.yuanluesoft.cms.interview.pojo.InterviewSubject)record;
		boolean isNew = OPEN_MODE_CREATE.equals(openMode);
		List roles = interviewService.listInterviewRoles(isNew ? subjectForm.getSiteId() : interviewSubject.getSiteId());
		subjectForm.setRoleNames("主持人,嘉宾" + (roles==null || roles.isEmpty() ? "" : "," + ListUtils.join(roles, "role", ",", false)));
		
		//判断访谈是否结束
		if(interviewSubject!=null && interviewSubject.getIsEnding()=='1') {
			subjectForm.setSubForm("interviewSubjectRead.jsp");
		}
		
		//设置TAB列表
		form.getTabs().addTab(-1, "basic", "基本信息", null, true);
		form.getTabs().addTab(-1, "interviewBackground", "访谈背景", null, false);
		form.getTabs().addTab(-1, "interviewGuestsIntro", "嘉宾介绍", null, false);
		if(!isNew) {
			String status = (accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE || acl.contains("compere") || acl.contains("approval") ? "Edit" : "Read"); //允许主持人或审核人编辑组件
			form.getTabs().addTab(-1, "interviewSpeaks", "访谈发言", "interviewSpeaks" + status + ".jsp", false);
			form.getTabs().addTab(-1, "interviewImages", "访谈图片", "interviewImages" + status + ".jsp", false);
			form.getTabs().addTab(-1, "interviewVideos", "访谈视频", "interviewVideos" + status + ".jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.interview.pojo.InterviewSubject interviewSubject = (com.yuanluesoft.cms.interview.pojo.InterviewSubject)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			interviewSubject.setCreator(sessionInfo.getUserName());
			interviewSubject.setCreatorId(sessionInfo.getUserId());
			interviewSubject.setCreated(DateTimeUtils.now());
		}
		//设置角色列表
		String[] roleIds = request.getParameterValues("roleId");
		if(roleIds!=null) {
			Set roles = new HashSet();
			for(int i=0; i<roleIds.length; i++) {
				InterviewSubjectRole role = new InterviewSubjectRole();
				role.setId(Long.parseLong(roleIds[i])); //ID
				role.setSubjectId(interviewSubject.getId()); //主题ID
				role.setRole(request.getParameter("roleName_" + roleIds[i])); //角色名称
				role.setRoleMemberIds(request.getParameter("roleMemberIds_" + roleIds[i])); //人员ID列表
				role.setRoleMemberNames(request.getParameter("roleMemberNames_" + roleIds[i])); //人员姓名列表
				roles.add(role);
			}
			interviewSubject.setRoles(roles);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
