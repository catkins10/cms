package com.yuanluesoft.jeaf.usermanage.actions.myschool;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.MySchoolNavigator;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 * 
 * @author linchuan
 *
 */
public class LoadMySchoolNavigator extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
    	//获取用户任管理员的学校列表
    	List schools = getOrgService().listManagedSchools(sessionInfo.getUserId());
    	if(schools==null || schools.isEmpty()) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	MySchoolNavigator mySchoolNavigator = (MySchoolNavigator)form;
    	mySchoolNavigator.setSchoolName(((Org)schools.get(0)).getDirectoryName());
    	return mapping.findForward("load");
    }
}