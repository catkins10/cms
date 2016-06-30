package com.yuanluesoft.jeaf.usermanage.actions.retrievegenearchaccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.RetrieveGenearchAccount;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyAccount extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	RetrieveGenearchAccount retrieveForm = (RetrieveGenearchAccount)form;
		RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
		try {
			registPersonService.modifyGenearchAccount(retrieveForm.getLoginName(), retrieveForm.getPassword(), retrieveForm.getNewLoginName(), retrieveForm.getNewPassword());
		}
		catch(ServiceException se) {
	    	//检查用户名是否重复
			if(RegistPersonService.ERROR_LOGINNAME_IN_USE.equals(se.getMessage())) {
				retrieveForm.setError("用户名已经被占用");
				return mapping.getInputForward();
			}
		}
		retrieveForm.setActionResult("账户修改成功！");
        return mapping.findForward("result");
    }
}