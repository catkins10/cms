package com.yuanluesoft.enterprise.exam.learn.actions.learnmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.exam.learn.service.LearnService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class UpdateLearnTime extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//会话检查
    	SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	response.getWriter().write("NOSESSION");
        	return null;
        }
        LearnService learnService = (LearnService)getService("learnService");
        learnService.updateLearnTime(RequestUtils.getParameterLongValue(request, "id"), sessionInfo);
        response.getWriter().write("SUCCESS");
        return null;
    }
}