package com.yuanluesoft.jeaf.messagecenter.actions.messenger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.messagecenter.sender.messenger.pojo.MessengerOnline;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Feedback extends FormAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DatabaseService databaseService = (DatabaseService)getService("databaseService");
        String hql = "from MessengerOnline MessengerOnline" +
        			 " where MessengerOnline.ip='" + request.getRemoteHost() + "'" +
        			 " and MessengerOnline.port=" + request.getParameter("port");
        
        MessengerOnline messengerOnline = (MessengerOnline)databaseService.findRecordByHql(hql);
        ((MessageService)getService("messageService")).feedbackMessage(Long.parseLong(request.getParameter("messageId")), messengerOnline.getPersonId());
        return null;
    }
}