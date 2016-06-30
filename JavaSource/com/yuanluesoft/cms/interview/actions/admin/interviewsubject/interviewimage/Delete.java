package com.yuanluesoft.cms.interview.actions.admin.interviewsubject.interviewimage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends InterviewImageAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeDeleteComponentAction(mapping, form, "interviewImage", "interviewImages", "refreshInterviewSubject", request, response);
    }
}