package com.yuanluesoft.jeaf.validatecode.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class GenerateValidateCodeImage extends BaseAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
		validateCodeService.generateValidateCodeImage(request, response);
		return null;
	}
}