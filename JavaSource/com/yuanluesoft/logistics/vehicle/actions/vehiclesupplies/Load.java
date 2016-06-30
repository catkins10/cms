package com.yuanluesoft.logistics.vehicle.actions.vehiclesupplies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.logistics.usermanage.model.LogisticsSessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	externalAction = true;
		anonymousEnable = true;
		sessionInfoClass = LogisticsSessionInfo.class;
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		PageService pageService = (PageService)getService("pageService");
		pageService.writePage("logistics/vehicle", SessionService.ANONYMOUS.equals(sessionInfo.getLoginName()) ? "vehicleSuppliesAnonymous" : "vehicleSupplies", request, response, false);
        return null;
    }
}