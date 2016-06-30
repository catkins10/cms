package com.yuanluesoft.enterprise.exam.learn.actions.learnmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.exam.learn.pages.LearnPageService;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//会话检查
    	SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        //权限控制
        if(getRecordControlService().getAccessLevel(RequestUtils.getParameterLongValue(request, "id"), LearnMission.class.getName(), sessionInfo)<RecordControlService.ACCESS_LEVEL_READONLY) {
        	return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
        }
        LearnPageService pageService = (LearnPageService)getService("learnPageService");
        pageService.writePage("enterprise/exam/learn", "learnMission", request, response, false);
    	return null;
    }
}