package com.yuanluesoft.jeaf.sms.client.emay.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sms.client.emay.SmsClientImpl;

/**
 * 
 * @author linchuan
 *
 */
public class ShutdownClient extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	((SmsClientImpl)getService("smsClient")).logout();
        return null;
    }
}