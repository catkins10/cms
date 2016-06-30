package com.yuanluesoft.cms.leadermail.actions.leadermail.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.leadermail.service.LeaderMailService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SetPublishOption extends LeaderMailAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LeaderMailService leaderMailService = (LeaderMailService)getService("leaderMailService");
    	return executeSetPublishOptionAction(leaderMailService.isAlwaysPublishAll(), mapping, form, request, response);
    }
}