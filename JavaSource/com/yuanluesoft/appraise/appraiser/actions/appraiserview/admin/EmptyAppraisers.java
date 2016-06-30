package com.yuanluesoft.appraise.appraiser.actions.appraiserview.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.appraise.appraiser.forms.admin.AppraiserView;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class EmptyAppraisers extends AppraiserViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		AppraiserView appraiserView = (AppraiserView)viewForm;
		AppraiserService appraiserService = (AppraiserService)getService("appraiserService");
		appraiserService.setAppraisersDisabled(appraiserView.getOrgId(), RequestUtils.getParameterIntValue(request, "appraiserType"));
		return mapping.getInputForward();
	}
}