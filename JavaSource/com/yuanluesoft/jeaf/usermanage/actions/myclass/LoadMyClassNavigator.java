package com.yuanluesoft.jeaf.usermanage.actions.myclass;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.MyClassNavigator;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class LoadMyClassNavigator extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
    	OrgService orgService = getOrgService();
		//获取我任班主任的班级
		List classesInCharge = orgService.listClassesInCharge(sessionInfo.getUserId());
		if(classesInCharge==null || classesInCharge.isEmpty()) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	MyClassNavigator myClassNavigator = (MyClassNavigator)form;
    	Org schoolClass = (Org)classesInCharge.get(0);
    	myClassNavigator.setClassId(schoolClass.getId());
    	myClassNavigator.setClassName(schoolClass.getDirectoryName());
    	return mapping.findForward("load");
    }
}