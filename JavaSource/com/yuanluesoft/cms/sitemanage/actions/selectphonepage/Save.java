package com.yuanluesoft.cms.sitemanage.actions.selectphonepage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.model.PhonePageConfig;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(PhonePageConfig.class);
		boolean selectEverytime = "true".equals(businessObject.getExtendParameter("selectEverytime")); //是否每次都选择
		int maxAge = selectEverytime ? -1 : 3600 * 24 * 180; //180天
		RequestUtils.savaRequestPageInfo(response, RequestUtils.getParameterIntValue(request, "pageWidth"), RequestUtils.getParameterIntValue(request, "flashSupport")==1, maxAge);
		response.sendRedirect(RequestUtils.resetRedirectURL(request.getParameter("redirect")));
		return null;
    }
}