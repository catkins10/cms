package com.yuanluesoft.jeaf.usermanage.actions.retrievegenearchaccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.RetrieveGenearchAccount;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 
 * @author linchuan
 *
 */
public class Retrieve extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
    	RetrieveGenearchAccount retrieveForm = (RetrieveGenearchAccount)form;
    	RegistPersonService registPersonService = (RegistPersonService)getService("registPersonService");
    	//调用开通服务
    	Person genearch = registPersonService.retrieveGenearchAccount(retrieveForm.getClassId(), retrieveForm.getClassFullName(), retrieveForm.getChildName(), retrieveForm.getName(), retrieveForm.getMobile(), retrieveForm.getValidateCode());
    	if(genearch!=null) {
    		retrieveForm.setLoginName(genearch.getLoginName());
    		//密码解密
    		PersonService personService = (PersonService)getService("personService");
    		retrieveForm.setPassword(personService.decryptPassword(genearch.getId(), genearch.getPassword()));
    		retrieveForm.setNewLoginName(retrieveForm.getLoginName());
    	}
        return mapping.findForward("result");
    }

}