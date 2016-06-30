package com.yuanluesoft.jeaf.document.actions.remotedocument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.document.RemoteDocumentService;

/**
 * 
 * @author chuan
 *
 */
public class DownloadRemoteDocument extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	RemoteDocumentService remoteDocumentService = (RemoteDocumentService)getService("remoteDocumentService");
    	remoteDocumentService.downloadRemoteDocument(request.getParameter("taskId"), response);
    	return null;
    }
}