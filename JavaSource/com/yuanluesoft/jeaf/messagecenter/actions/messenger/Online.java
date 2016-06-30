package com.yuanluesoft.jeaf.messagecenter.actions.messenger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.messagecenter.sender.messenger.pojo.MessengerOnline;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Online extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
    	SessionInfo sessionInfo = (SessionInfo)session.getAttribute("SessionInfo");
		DatabaseService databaseService = (DatabaseService)getBean("databaseService");
        MessengerOnline pojoMessengerOnline = (MessengerOnline)databaseService.findRecordById(MessengerOnline.class.getName(), sessionInfo.getUserId());
        boolean isNew = false;
        if(pojoMessengerOnline==null) {
        	pojoMessengerOnline = new MessengerOnline();
        	pojoMessengerOnline.setPersonId(sessionInfo.getUserId());
        	isNew = true;
        }
        pojoMessengerOnline.setIp(request.getRemoteHost());
        pojoMessengerOnline.setPort(Integer.parseInt(request.getParameter("port")));
        if(isNew) {
        	databaseService.saveRecord(pojoMessengerOnline);
        }
        else {
        	databaseService.updateRecord(pojoMessengerOnline);
        }
        response.getWriter().write("" + pojoMessengerOnline.getPort());
        return null;
    }
}