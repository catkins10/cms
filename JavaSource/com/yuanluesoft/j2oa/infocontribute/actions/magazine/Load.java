package com.yuanluesoft.j2oa.infocontribute.actions.magazine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContributeMagazine;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//检查用户对刊物的权限
    	long magazineId = RequestUtils.getParameterLongValue(request, "id");
    	if(getRecordControlService().getAccessLevel(magazineId, InfoContributeMagazine.class.getName(), sessionInfo)<RecordControlService.ACCESS_LEVEL_READONLY) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	//获取刊物文件
    	AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
    	List attachments = attachmentService.list("j2oa/infocontribute", "magazine", magazineId, false, 0, request);
    	//启动下载
    	FileDownloadService fileDownloadService = (FileDownloadService)getService("fileDownloadService");
    	Attachment attachment = (Attachment)attachments.get(0);
    	fileDownloadService.httpDownload(request, response, attachment.getFilePath(), null, false, null);
    	return null;
    }
}