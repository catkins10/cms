package com.yuanluesoft.jeaf.opinionmanage.actions.opiniontype.loaddefault;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.opinionmanage.forms.LoadDefault;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Complete extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, true);
    	}
    	LoadDefault loadDefaultForm = (LoadDefault)form;
    	BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(loadDefaultForm.getBusinessClassName());
		List acl = getAcl(businessObject.getApplicationName(), sessionInfo);
		if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), true);
		}
		OpinionService opinionService = (OpinionService)getService("opinionService");
		opinionService.loadDefaultOpinionTypes(loadDefaultForm.getBusinessClassName());
		return closeDialogAndRefreshOpener(request, response);
    }
}