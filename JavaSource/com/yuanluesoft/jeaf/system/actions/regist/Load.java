package com.yuanluesoft.jeaf.system.actions.regist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.system.forms.Regist;
import com.yuanluesoft.jeaf.system.services.SystemService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SystemService systemService = (SystemService)getService("systemService");
    	Regist registForm = (Regist)form;
    	registForm.setCode(systemService.generateRegistCode());
    	registForm.setFormTitle("系统注册");
    	registForm.getFormActions().addFormAction(-1, "完成注册", "doRegist()", true);
    	return mapping.findForward("load");
    }
}