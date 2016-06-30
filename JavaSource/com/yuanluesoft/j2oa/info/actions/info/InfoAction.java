package com.yuanluesoft.j2oa.info.actions.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.info.forms.Info;
import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefinePrivilege;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class InfoAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName(com.yuanluesoft.jeaf.workflow.form.WorkflowForm)
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runInfo";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Info infoForm = (Info)form;
		InfoFilter infoFilter = (InfoFilter)record;
		//判断用户是否刊物的编辑或者主编
		try {
			if(infoFilter!=null && infoFilter.getMagazineDefineId()>0) {
				infoForm.setMagazineEditor(getRecordControlService().getAccessLevel(infoFilter.getMagazineDefineId(), InfoMagazineDefine.class.getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY);
			}
			else {
				InfoService infoService = (InfoService)getService("infoService");
				infoForm.setMagazineEditor(infoService.isMagazineEditor(sessionInfo));
			}
		}
		catch (ServiceException e) {
			
		}
		if(infoForm.isMagazineEditor()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		InfoFilter infoFilter = (InfoFilter)record;
		if((infoFilter.getStatus()>=InfoService.INFO_STATUS_TYPESET && infoFilter.getSupplementTime()==null) || infoFilter.getIsBeCombined()==1) {
			throw new PrivilegeException();
		}
		if(infoFilter.getSupplementTime()!=null && //补录
		   ((infoFilter.getSendHighers()!=null && !infoFilter.getSendHighers().isEmpty()) || //上报情况不为空
		    (infoFilter.getInstructs()!=null && !infoFilter.getInstructs().isEmpty()))) { //领导批不为空
			throw new PrivilegeException();
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Record record = super.loadRecord(form, formDefine, id, sessionInfo, request);
		Info infoForm = (Info)form;
		if(record==null && infoForm.getCombineInfoIds()!=null && !infoForm.getCombineInfoIds().isEmpty()) {
			InfoService infoService = (InfoService)getService("infoService");
			record = infoService.combineInfos(infoForm.getCombineInfoIds(), sessionInfo);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		InfoFilter info = (InfoFilter)record;
		InfoService infoService = (InfoService)getService("infoService");
		if(OPEN_MODE_CREATE.equals(openMode)) { //补录
			info.setInfoReceiveId(info.getInfoReceive().getId());
			info.setSupplementPerson(sessionInfo.getUserName());
			info.setSupplementPersonId(sessionInfo.getUserId());
			info.setSupplementTime(DateTimeUtils.now());
			info.setIssueTime(DateTimeUtils.now());
			info.setStatus(InfoService.INFO_STATUS_ISSUE);
			info.getInfoReceive().setFilterTime(DateTimeUtils.now());
			infoService.save(info.getInfoReceive());
			//授权
			List editors = getRecordControlService().listVisitors(info.getMagazineDefineId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY);
			List chiefEditors = getRecordControlService().listVisitors(info.getMagazineDefineId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_EDITABLE);
			if(editors==null) {
				editors = new ArrayList();
			}
			if(chiefEditors!=null && !chiefEditors.isEmpty()) {
				editors.addAll(chiefEditors);
			}
			for(Iterator iterator = editors.iterator(); iterator.hasNext();) {
				InfoMagazineDefinePrivilege visitor = (InfoMagazineDefinePrivilege)iterator.next();
				getRecordControlService().appendVisitor(info.getId(), InfoFilter.class.getName(), visitor.getVisitorId(), RecordControlService.ACCESS_LEVEL_READONLY);
			}
		}
		else if(info.getSupplementTime()!=null) { //补录
			infoService.update(info.getInfoReceive());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Info infoForm = (Info)form;
		InfoFilter info = (InfoFilter)record;
		InfoService infoService = (InfoService)getService("infoService");
		if(info.getCombineInfoIds()!=null && !info.getCombineInfoIds().isEmpty()) {
			infoForm.setWorkflowInstanceId(null); //避免流程被删除
		}
		super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		if(info.getCombineInfoIds()!=null && !info.getCombineInfoIds().isEmpty()) {
			infoService.uncombineInfos(info.getCombineInfoIds(), sessionInfo);
		}
		if(info.getSupplementTime()!=null) { //补录
			infoService.delete(info.getInfoReceive());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		long magazineDefineId = ((InfoFilter)record).getMagazineDefineId();
		return getRecordControlService().listVisitors(magazineDefineId, InfoMagazineDefine.class.getName(), "magazineEditor".equals(programmingParticipantId) ? RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Info infoForm = (Info)form;
		infoForm.getInfoReceive().setId(UUIDLongGenerator.generateId()); //ID
		infoForm.setSupplementPerson(sessionInfo.getUserName());
		infoForm.setSupplementTime(DateTimeUtils.now());
		infoForm.setIssueTime(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Info infoForm = (Info)form;
		InfoService infoService = (InfoService)getService("infoService");
		//获取来稿正文
		infoForm.getInfoReceive().setLazyBody(null);
		infoForm.getInfoReceive().setBody(infoService.getReceiveInfoBody(infoForm.getInfoReceive().getId()));
		//获取合并的稿件列表
		if(infoForm.getCombineInfoIds()!=null && !infoForm.getCombineInfoIds().isEmpty()) {
			infoForm.setCombineInfos(infoService.listInfos(infoForm.getCombineInfoIds()));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		if(workflowInstanceCompleted) {
			InfoFilter infoFilter = (InfoFilter)record;
			if(infoFilter.getStatus()==InfoService.INFO_STATUS_TO_APPROVAL) {
				infoFilter.setStatus(InfoService.INFO_STATUS_NO_USE);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Info infoForm = (Info)form;
		InfoFilter infoFilter = (InfoFilter)record;
		if(infoFilter!=null) {
			int index = 1;
			if(infoFilter.getSupplementTime()==null) { //不是补录的
				infoForm.getTabs().addTab(index++, "original", "原文", "infoOriginal.jsp", false);
			}
			if(infoFilter.getStatus()>=InfoService.INFO_STATUS_TO_TYPESET) {
				infoForm.getTabs().addTab(index++, "sendHighers", "报送情况", "infoSendHighers.jsp", false);
				infoForm.getTabs().addTab(index++, "instructs", "领导批示", "infoInstructs.jsp", false);
			}
			if(infoFilter.getRevises()!=null && !infoFilter.getRevises().isEmpty()) {
				infoForm.getTabs().addTab(index++, "revises", "退改稿", "infoRevises.jsp", false);
			}
		}
		infoForm.getOpinionPackage().setWriteOpinionActionName("writeInfoOpinion");
		if(infoFilter==null || //新记录
		   infoFilter.getIsCombined()==1 || //多条合一
		   ListUtils.findObjectByProperty(infoFilter.getRevises(), "editorId", new Long(0))!=null) { //获取已经退了,但没有修改完成
			infoForm.getFormActions().removeFormAction("退改稿");
		}
		if(infoFilter!=null && infoFilter.getIsBeCombined()==1) { //被合并
			infoForm.setSubForm("infoRead.jsp");
			for(Iterator iterator = infoForm.getFormActions().iterator(); iterator.hasNext();) {
				FormAction action = (FormAction)iterator.next();
				if(!"关闭".equals(action.getTitle())) {
					iterator.remove();
				}
			}
		}
		if(OPEN_MODE_CREATE.equals(openMode) || infoFilter.getSupplementTime()!=null) { //补录
			infoForm.setSubForm(infoForm.getSubForm().replaceFirst("infoEdit.jsp", "infoSupplement.jsp"));
		}
	}
}