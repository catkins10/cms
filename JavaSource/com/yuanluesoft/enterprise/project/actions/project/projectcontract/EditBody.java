package com.yuanluesoft.enterprise.project.actions.project.projectcontract;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectContract;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author chuan
 *
 */
public class EditBody extends ProjectContractAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenComponentRemoteDocumentAction(mapping, form, "contract", request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#writeRemoteDocument(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.util.List, com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, char, java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String documentRelatedApplicationName, String documentRelatedFormName, List documentFiles, com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ProjectContract contractForm = (ProjectContract)form;
		if(RemoteDocumentService.COMMAND_CREATE_DOCUMENT.equals(documentCommand)) {
			EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
			documentFiles = ListUtils.generateList(enterpriseProjectService.getContractTemplate(contractForm.getSelectedContractTemplateId()));
		}
		else {
			AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
			documentFiles = attachmentService.list("enterprise/project", "contract", contractForm.getContract().getId(), false, 1, request);
		}
		//使用标题做文件名
		Attachment document = (Attachment)documentFiles.get(0);
		document.setName(contractForm.getContract().getContractName() + ".doc");
		super.writeRemoteDocument(RemoteDocumentService.APPLICATION_WORD, documentCommand, documentCommandParameter, documentRelatedApplicationName, documentRelatedFormName, documentFiles, form, request, response, sessionInfo);
	}
}