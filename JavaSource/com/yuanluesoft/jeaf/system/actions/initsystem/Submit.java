package com.yuanluesoft.jeaf.system.actions.initsystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.system.forms.InitSystem;
import com.yuanluesoft.jeaf.system.services.SystemService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Submit extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InitSystem initSystemForm = (InitSystem)form;
		initSystemForm.setFormTitle("系统初始化");
		SystemService systemService = (SystemService)getService("systemService");
		try {
			systemService.initSystem(initSystemForm.getSystemName(), initSystemForm.getManagerName(), initSystemForm.getManagerLoginName(), initSystemForm.getManagerPassword());
			initSystemForm.setActionResult("完成初始化");
		}
		catch(Exception e) {
			Logger.exception(e);
			initSystemForm.setActionResult("初始化失败");
		}
		initSystemForm.getFormActions().addFormAction(-1, "确定", "DialogUtils.closeDialog();", true);
		return mapping.findForward("result");
	}
}