package com.yuanluesoft.enterprise.iso.actions.document;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.iso.forms.Document;
import com.yuanluesoft.enterprise.iso.pojo.IsoDirectory;
import com.yuanluesoft.enterprise.iso.pojo.IsoDocument;
import com.yuanluesoft.enterprise.iso.pojo.IsoDocumentSubjection;
import com.yuanluesoft.enterprise.iso.service.IsoDirectoryService;
import com.yuanluesoft.enterprise.iso.service.IsoDocumentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;
import com.yuanluesoft.workflow.client.model.user.ParticipantRole;

/**
 * 
 * @author yuanlue
 *
 */
public class DocumentAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runDocument";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		IsoDocument document = (IsoDocument)record;
		if(document!=null && document.getIsValid()=='1') { //已发布文件
			IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
			long directoryId = ((IsoDocumentSubjection)document.getSubjections().iterator().next()).getDirectoryId();
        	if(isoDirectoryService.checkPopedom(directoryId, null, sessionInfo)) { //检查用户对目录的权限
        		return RecordControlService.ACCESS_LEVEL_READONLY;
        	}
        	//检查用户对文件的权限
        	try {
				if(getRecordControlService().getAccessLevel(directoryId, IsoDocument.class.getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY) {
					return RecordControlService.ACCESS_LEVEL_READONLY;
				}
			}
        	catch (ServiceException e) {
				
			}
        	throw new PrivilegeException();
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		Document documentForm = (Document)workflowForm;
		
		//获取流程入口
		String createWorkflowId = isoDirectoryService.getWorkflowId(documentForm.getDirectoryId(), "create");
		WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry(createWorkflowId, null, (WorkflowData)record, sessionInfo);
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Document documentForm = (Document)workflowForm;
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		
		IsoDirectory directory = (IsoDirectory)isoDirectoryService.getDirectory(documentForm.getDirectoryId());
		//获取站点编辑或管理员
		List participants = ListUtils.getSubListByProperty(directory.getDirectoryPopedoms(), "popedomName", programmingParticipantId);
		if(participants==null || participants.isEmpty()) {
			return null;
		}
		PersonService personService = (PersonService)getService("personService");
		OrgService orgService = (OrgService)getService("orgService");
		//转换为办理人
		for(int i=0; i<participants.size(); i++) {
			DirectoryPopedom popedom  = (DirectoryPopedom)participants.get(i);
			com.yuanluesoft.jeaf.usermanage.pojo.Person person = personService.getPerson(popedom.getUserId());
			if(person!=null) { //个人
				participants.set(i, new Person("" + person.getId(), person.getName())); //转换为Person模型
				continue;
			}
			Org org = orgService.getOrg(popedom.getUserId());
			if(org!=null) { //部门
				participants.set(i, new ParticipantDepartment("" + org.getId(), org.getDirectoryName())); //转换为部门办理人,允许所有人办理
			}
			else { //角色
				participants.set(i, new ParticipantRole("" + popedom.getUserId(), popedom.getUserName())); //转换为角色办理人,允许所有人办理
			}
		}
		return participants;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Document documentForm = (Document)form;
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		IsoDocumentService isoDocumentService = (IsoDocumentService)getService("isoDocumentService");
		
		documentForm.setWriter(sessionInfo.getUserName());
		documentForm.setWriteDate(DateTimeUtils.date());
		
		//设置所在目录名称
		documentForm.setDirectoryName(isoDirectoryService.getDirectoryFullName(documentForm.getDirectoryId(), "/", null));
		//设置版本号
		documentForm.setVersion(isoDocumentService.getVersionInitialValue());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Document documentForm = (Document)form;
		IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
		//设置所在目录ID和名称
		documentForm.setDirectoryId(((IsoDocumentSubjection)documentForm.getSubjections().iterator().next()).getDirectoryId());
		documentForm.setDirectoryName(isoDirectoryService.getDirectoryFullName(documentForm.getDirectoryId(), "/", null));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Document documentForm = (Document)form;
		IsoDocument document = (IsoDocument)record;
		
		boolean modifyEnabled = false; //是否允许修改
		boolean destroyEnabled = false; //是否允许销毁
		if(document!=null && document.getIsValid()=='1' && document.getIsModify()!='1' && document.getIsDestroy()!='1') {
			IsoDirectoryService isoDirectoryService = (IsoDirectoryService)getService("isoDirectoryService");
			WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
			
			//获取修改流程入口
			String modifyWorkflowId = isoDirectoryService.getWorkflowId(documentForm.getDirectoryId(), "modify");
			modifyEnabled = workflowExploitService.getWorkflowEntry(modifyWorkflowId, null, document, sessionInfo)!=null;
			
			//获取销毁流程入口
			String destroyWorkflowId = isoDirectoryService.getWorkflowId(documentForm.getDirectoryId(), "destroy");
			destroyEnabled = workflowExploitService.getWorkflowEntry(destroyWorkflowId, null, document, sessionInfo)!=null;
		}
		//删除"企业变更"和"企业注销"按钮
		if(!modifyEnabled) {
			ListUtils.removeObjectByProperty(documentForm.getFormActions(), "title", "修改文件");
		}
		if(!destroyEnabled) {
			ListUtils.removeObjectByProperty(documentForm.getFormActions(), "title", "销毁文件");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#setFormTitle(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void setFormTitle(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		if(record==null || !(record instanceof IsoDocument)) {
			super.setFormTitle(form, record, request, sessionInfo);
		}
		else {
			IsoDocument document = (IsoDocument)record;
			form.setFormTitle(document.getName() + " - ISO文件" + (document.getIsModify()=='1' ? "修改" : (document.getIsDestroy()=='1' ? "销毁" : "")));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//更新隶属目录
		IsoDocument document = (IsoDocument)record;
		Document documentForm = (Document)form;
		if(documentForm.getDirectoryName()!=null) {
			IsoDocumentService isoDocumentService = (IsoDocumentService)getService("isoDocumentService");
			isoDocumentService.updateDocumentSubjections(document, OPEN_MODE_CREATE.equals(openMode), "" + documentForm.getDirectoryId());
		}
		return record;
	}
}