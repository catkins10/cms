package com.yuanluesoft.enterprise.exam.actions.exampaper.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Print extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//权限控制
    	List acl = getAcl("enterprise/exam", sessionInfo);
    	if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && !acl.contains("manageUnit_create")) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
    	//创建一个答卷
    	ExamService examService = (ExamService)getService("examService");
    	request.setAttribute("record", examService.createExamTest(RequestUtils.getParameterLongValue(request, "id"), sessionInfo));
    	//输出
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("enterprise/exam", "true".equals(request.getParameter("fullPrint")) ? "examPaperFullPrint" : "examPaperPrint", request, response, false);
    	return null;
    }
}