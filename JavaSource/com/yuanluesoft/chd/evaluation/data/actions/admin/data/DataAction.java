/*
 * Created on 2006-5-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.chd.evaluation.data.actions.admin.data;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.chd.evaluation.data.forms.admin.Data;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationData;
import com.yuanluesoft.chd.evaluation.data.pojo.ChdEvaluationDataSubjection;
import com.yuanluesoft.chd.evaluation.data.service.EvaluationDataService;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 *
 * @author linchuan
 *
 */
public class DataAction extends WorkflowAction {
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runData";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#retrieveWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取流程配置
		Data dataForm = (Data)workflowForm;
        EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
        String workflowId = evaluationDirectoryService.getApprovalWorkflowId(dataForm.getDirectoryId(), "dataWorkflow");
        if(workflowId==null || workflowId.isEmpty()) {
			throw new Exception("Approval workflows are not exists.");
		}
		//按ID查找流程
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(workflowId, null, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			throw new Exception("Workflow entry not exists.");
		}
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Data dataForm = (Data)workflowForm;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		return "evaluationTransactor".equals(programmingParticipantId) ? evaluationDirectoryService.listEvaluationTransactor(dataForm.getDirectoryId(), 0) : evaluationDirectoryService.listEvaluationLeader(dataForm.getDirectoryId(), "plantEvaluationLeader".equals(programmingParticipantId), 0);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        Data dataForm = (Data)form;
        EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
        if(OPEN_MODE_CREATE.equals(openMode)) { //新建
        	if(evaluationDirectoryService.checkPopedom(dataForm.getDirectoryId(), "transactor", sessionInfo)) {
        		return RecordControlService.ACCESS_LEVEL_EDITABLE;
        	}
        	else {
        		throw new PrivilegeException();
        	}
        }
        try {
        	return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
        }
        catch(Exception e) {
           	ChdEvaluationData data = (ChdEvaluationData)record;
    		//设置目录ID
    		dataForm.setDirectoryId(((ChdEvaluationDataSubjection)data.getSubjections().iterator().next()).getDirectoryId());
    		if(!evaluationDirectoryService.checkPopedom(dataForm.getDirectoryId(), "all", sessionInfo)) {
            	throw new PrivilegeException();
            }
            return RecordControlService.ACCESS_LEVEL_READONLY;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ChdEvaluationData data = (ChdEvaluationData)record;
		if(data!=null) {
			EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
			//用户如果是否管理员或者负责人,允许删除
			ChdEvaluationDataSubjection subjection = (ChdEvaluationDataSubjection)data.getSubjections().iterator().next();
			if(evaluationDirectoryService.checkPopedom(subjection.getDirectoryId(), "manager,leader", sessionInfo)) {
				return;
			}
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Data dataForm = (Data)form;
        EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
	    //设置所在目录全称
        dataForm.setDirectoryName(evaluationDirectoryService.getDirectoryFullName(dataForm.getDirectoryId(), "/", "plantDetail"));
        //创建人和创建时间
        dataForm.setCreated(DateTimeUtils.now());
        dataForm.setCreator(sessionInfo.getUserName());
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Data fileForm = (Data)form;
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		//设置目录ID
		fileForm.setDirectoryId(((ChdEvaluationDataSubjection)fileForm.getSubjections().iterator().next()).getDirectoryId());
        //设置所在目录全称
		fileForm.setDirectoryName(evaluationDirectoryService.getDirectoryFullName(fileForm.getDirectoryId(), "/", "plantDetail"));
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ChdEvaluationData data = (ChdEvaluationData)record;
		Data dataForm = (Data)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			data.setCreatorId(sessionInfo.getUserId());
	    	data.setCreator(sessionInfo.getUserName());
	    	data.setCreated(DateTimeUtils.now());
        }
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			//保存隶属关系
			EvaluationDataService evaluationDataService = (EvaluationDataService)getService("chdEvaluationDataService");
			evaluationDataService.updateDataSubjections(data, OPEN_MODE_CREATE.equals(openMode), "" + dataForm.getDirectoryId());
		}
		return record;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		Data dataForm = (Data)workflowForm;
		EvaluationDataService evaluationDataService = (EvaluationDataService)getService("chdEvaluationDataService");
		evaluationDataService.clearDataTodo(dataForm.getDirectoryId());
	}
}