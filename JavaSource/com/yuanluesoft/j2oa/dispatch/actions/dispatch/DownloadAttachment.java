package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DownloadAttachment extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	long id = RequestUtils.getParameterLongValue(request, "id");
    	char accessLevel = getRecordControlService().getRegistedRecordAccessLevel(id, request.getSession());
		if(accessLevel<RecordControlService.ACCESS_LEVEL_READONLY) {
			return null;
		}
		AttachmentService attachmentService = (AttachmentService)getBean("attachmentService");
		FileDownloadService fileDownloadService = (FileDownloadService)getBean("fileDownloadService");
		Attachment attachment = (Attachment)ListUtils.findObjectByProperty(attachmentService.list("j2oa/dispatch", "html", id, false, 0, request), "name", request.getParameter("fileName"));
		fileDownloadService.httpDownload(request, response, attachment.getFilePath(), null, false, null);
		return null;
    }
}