package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.dispatch.forms.Dispatch;
import com.yuanluesoft.j2oa.dispatch.service.DispatchTemplateService;
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
public class EditBody extends DispatchAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeOpenRemoteDocumentAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#writeRemoteDocument(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.util.List, com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, char, java.util.List, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void writeRemoteDocument(String documentApplicationName, String documentCommand, Map documentCommandParameter, String documentRelatedApplicationName, String documentRelatedFormName, List documentFiles, com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		Dispatch dispatchForm = (Dispatch)form;
		//获取正文列表
		documentFiles = attachmentService.list("j2oa/dispatch", "body", form.getId(), false, 1, request);
		if(documentFiles==null || documentFiles.isEmpty()) { //
			if(documentCommand.equals(RemoteDocumentService.COMMAND_EDIT_DOCUMENT)) { //编辑文档
				documentCommand = RemoteDocumentService.COMMAND_CREATE_DOCUMENT;
			}
			//获取模板
			DispatchTemplateService dispatchTemplateService = (DispatchTemplateService)getService("dispatchTemplateService");
			documentFiles = ListUtils.generateList(dispatchTemplateService.getWordTemplate(dispatchForm.getDocType(), dispatchForm.getDocMark()));
	    }
		//使用发文标题做文件名
		Attachment document = (Attachment)documentFiles.get(0);
		document.setName(dispatchForm.getSubject() + ".doc");
		if(dispatchForm.getGenerateDate()!=null) { //正式文件已经生成
			documentCommandParameter.put(RemoteDocumentService.COMMAND_PARAMETER_KEEP_FIELD_VALUE, "true"); //增加参数,禁止更新字段
		}
		super.writeRemoteDocument(RemoteDocumentService.APPLICATION_WORD, documentCommand, documentCommandParameter, documentRelatedApplicationName, documentRelatedFormName, documentFiles, form, request, response, sessionInfo);
	}
}