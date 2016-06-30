package com.yuanluesoft.enterprise.quality.actions.document;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.quality.forms.Document;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocument;
import com.yuanluesoft.enterprise.quality.pojo.QualityDocumentBody;
import com.yuanluesoft.enterprise.quality.service.QualityDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class DocumentAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runDocument";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Document documentForm = (Document)form;
		QualityDocument document = (QualityDocument)record;
		//页签设置
		if(OPEN_MODE_CREATE.equals(openMode)) {
			documentForm.getTabs().removeTab("opinion");
			documentForm.getTabs().removeTab("workflowLog");
			return;
		}
		//正文页签
		int tabIndex = 1;
		String status = (accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE ? "Read" : "Edit");
		for(Iterator iterator = document.getBodies()==null ? null : document.getBodies().iterator(); iterator!=null && iterator.hasNext();) {
			QualityDocumentBody documentBody = (QualityDocumentBody)iterator.next();
			Tab tab = documentForm.getTabs().addTab(tabIndex++, "body" + "_" + documentBody.getId(), documentBody.getName(), "documentBody" + status + ".jsp", documentBody.getTemplateId()==RequestUtils.getParameterLongValue(request, "templateId"));
			tab.setAttribute("documentBody", documentBody);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Document documentForm = (Document)form;
		documentForm.setCreator(sessionInfo.getUserName());
		documentForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		QualityDocument document = (QualityDocument)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			document.setCreator(sessionInfo.getUserName());
			document.setCreatorId(sessionInfo.getUserId());
			document.setCreated(DateTimeUtils.now());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		QualityDocumentService qualityDocumentService = (QualityDocumentService)getBusinessService(form.getFormDefine());
		//保存正文
		int i = 0;
		for(Iterator iterator = document.getBodies()==null ? null : document.getBodies().iterator(); iterator!=null && iterator.hasNext();) {
			QualityDocumentBody documentBody = (QualityDocumentBody)iterator.next();
			//获取正文
			String body = request.getParameterValues("body")[i++];
			if(body!=null && !body.isEmpty()) {
				documentBody.setBody(body);
				qualityDocumentService.update(documentBody);
			}
		}
		return record;
	}
}