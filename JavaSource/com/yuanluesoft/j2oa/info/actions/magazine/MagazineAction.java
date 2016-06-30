package com.yuanluesoft.j2oa.info.actions.magazine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.info.forms.Magazine;
import com.yuanluesoft.j2oa.info.model.MagazineColumn;
import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazine;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoWorkflow;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.model.WorkflowEntry;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;

/**
 * 
 * @author linchuan
 *
 */
public class MagazineAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName(com.yuanluesoft.jeaf.workflow.form.WorkflowForm)
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runMagazine";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, boolean, com.yuanluesoft.jeaf.workflow.actions.WorkflowAction.WorkflowActionParticipantCallback, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		InfoService infoService = (InfoService)getService("infoService");
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		InfoWorkflow infoWorkflow = infoService.getInfoWorkflow(InfoService.WORKFLOW_TYPE_MAGAZINE);
		com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry("" + infoWorkflow.getWorkflowId(), null, null, sessionInfo);
		if(workflowEntry==null) {
			throw new Exception("no workflow entry found");
		}
		Element activity = (Element)workflowEntry.getActivityEntries().get(0);
		return new WorkflowEntry("" + infoWorkflow.getWorkflowId(), activity.getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		long magazineDefineId = record==null ? ((Magazine)workflowForm).getDefineId() : ((InfoMagazine)record).getDefineId();
		return getRecordControlService().listVisitors(magazineDefineId, InfoMagazineDefine.class.getName(), "magazineEditor".equals(programmingParticipantId) ? RecordControlService.ACCESS_LEVEL_READONLY : RecordControlService.ACCESS_LEVEL_EDITABLE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Magazine magazineForm = (Magazine)form;
		InfoService infoService = (InfoService)getService("infoService");
		InfoMagazineDefine magazineDefine = (InfoMagazineDefine)infoService.load(InfoMagazineDefine.class, magazineForm.getDefineId());
		magazineForm.setName(magazineDefine.getMagazineName()); //刊物名称
		magazineForm.setSn(magazineDefine.getNextSn()); //期号
		magazineForm.setSnTotal(magazineDefine.getSnTotal() + 1); //总期号
		generateMagazineColumns(magazineForm, magazineDefine); //生成刊物栏目列表
		//设为刊物分发范围
		magazineForm.setIssueRanges(getRecordControlService().getVisitors(magazineForm.getDefineId(), InfoMagazineDefine.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Magazine magazineForm = (Magazine)form;
		InfoService infoService = (InfoService)getService("infoService");
		//读取正文
		for(Iterator iterator = magazineForm.getUseInfos()==null ? null : magazineForm.getUseInfos().iterator(); iterator!=null && iterator.hasNext();) {
			InfoFilter info = (InfoFilter)iterator.next();
			info.setLazyBody(null);
			info.setBody(infoService.getFilterInfoBody(info.getId()));
		}
		//生成刊物栏目列表
	 	InfoMagazineDefine magazineDefine = (InfoMagazineDefine)infoService.load(InfoMagazineDefine.class, magazineForm.getDefineId());
	 	generateMagazineColumns(magazineForm, magazineDefine); //生成刊物栏目列表
	 	//设置分发范围
	 	magazineForm.setIssueRanges(getRecordControlService().getVisitors(magazineForm.getId(), InfoMagazine.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD));
	}
	
	/**
	 * 生成刊物栏目列表
	 * @param magazineForm
	 * @param magazineDefine
	 */
	private void generateMagazineColumns(Magazine magazineForm, InfoMagazineDefine magazineDefine) throws Exception {
		magazineForm.setMagazineColumns(new ArrayList());
		String[] columns = (magazineDefine.getColumns()==null ? "" : magazineDefine.getColumns()).split(",");
		for(int i=0; i<columns.length; i++) {
			MagazineColumn magazineColumn = new MagazineColumn();
			magazineForm.getMagazineColumns().add(magazineColumn);
			magazineColumn.setColumnName(columns[i]); //栏目名称
			//获取信息列表
			Collection infos = columns.length==1 ? magazineForm.getUseInfos() : (Collection)ListUtils.getSubListByProperty(magazineForm.getUseInfos(), "magazineColumn", columns[i]);
			//设置信息(非简讯)列表
			magazineColumn.setColumnUseInfos(ListUtils.getSubListByProperty(infos, "isBrief", new Integer(0)));
			//设置简讯列表
			magazineColumn.setColumnUseBriefs(ListUtils.getSubListByProperty(infos, "isBrief", new Integer(1)));
		}
		//获取待排版稿件,供WORD编辑时使用
		InfoService infoService = (InfoService)getService("infoService");
		magazineForm.setToTypesetInfos(infoService.listToTypesetInfos(magazineForm.getDefineId(), false, 100));
		magazineForm.setToTypesetBriefs(infoService.listToTypesetInfos(magazineForm.getDefineId(), true, 100));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(1, "infos", "稿件录用", "magazineInfos.jsp", true);
		form.getTabs().addTab(2, "body", "正文", "magazineBody.jsp", !OPEN_MODE_CREATE.equals(openMode));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Magazine magazineForm = (Magazine)form;
		//保存分发范围
		if(magazineForm.getIssueRanges().getVisitorNames()!=null) {
			RecordControlService recordControlService = getRecordControlService();
			recordControlService.updateVisitors(magazineForm.getId(), InfoMagazine.class.getName(), magazineForm.getIssueRanges(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
	}
}