package com.yuanluesoft.jeaf.system.actions.regist;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.system.services.SystemService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Regist extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SystemService systemService = (SystemService)getService("systemService");
    	com.yuanluesoft.jeaf.system.forms.Regist registForm = (com.yuanluesoft.jeaf.system.forms.Regist)form;
    	try {
    		Date usefulDate = systemService.registSystem(registForm.getSn());
    		registForm.setActionResult("注册成功" + (usefulDate==null ? "" : ",系统试用截止时间:" + DateTimeUtils.formatDate(usefulDate, null)));
    	}
    	catch(Exception e) {
    		Logger.exception(e);
    		registForm.setActionResult("注册失败");
    	}
    	registForm.getFormActions().addFormAction(-1, "确定", "DialogUtils.closeDialog();", true);
    	return mapping.findForward("result");
    }
}