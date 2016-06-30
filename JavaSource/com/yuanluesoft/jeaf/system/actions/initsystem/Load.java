package com.yuanluesoft.jeaf.system.actions.initsystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.system.forms.InitSystem;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InitSystem initSystemForm = (InitSystem)form;
		initSystemForm.setFormTitle("系统初始化");
		initSystemForm.getFormActions().addFormAction(-1, "提交", "FormUtils.submitForm();", true);
		return mapping.findForward("load");
	}
}