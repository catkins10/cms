package com.yuanluesoft.jeaf.usermanage.actions.admin.synchallperson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 
 * @author linchuan
 *
 */
public class Synch extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取会话
    	SessionInfo sessionInfo = getSessionInfo(request, response);
        //检查用户对根组织的管理权限
    	OrgService orgService = (OrgService)getService("orgService");
    	if(!orgService.checkPopedom(0, "manager", sessionInfo)) {
    		throw new PrivilegeException(); //没有权限
    	}
    	PersonService personService = (PersonService)getService("personService");
    	personService.synchAllPerson();
    	response.setCharacterEncoding("utf-8");
    	response.getWriter().write("synch complete！");
        return null;
    }
}