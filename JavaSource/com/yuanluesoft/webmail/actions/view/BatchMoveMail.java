package com.yuanluesoft.webmail.actions.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 * 
 * @author linchuan
 *
 */
public class BatchMoveMail  extends WebmailViewAction {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(request.getParameter("moveto")==null) {
			return mapping.getInputForward();
		}
        long mailboxId = Long.parseLong(request.getParameter("moveto"));
        String selectedIds = viewForm.getViewPackage().getSelectedIds();
        WebMailService webMailService = (WebMailService)getService("webMailService");
        if(selectedIds!=null && !selectedIds.equals("")) {
            String[] ids = selectedIds.split(",");
            for(int i=0; i<ids.length; i++) {
            	webMailService.moveMail(Long.parseLong(ids[i]), mailboxId, sessionInfo);
            }
        }
        viewForm.getViewPackage().setSelectedIds(null);
        return mapping.getInputForward();
	}
}