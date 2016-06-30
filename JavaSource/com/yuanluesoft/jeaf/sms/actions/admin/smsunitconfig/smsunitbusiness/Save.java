package com.yuanluesoft.jeaf.sms.actions.admin.smsunitconfig.smsunitbusiness;

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
public class Save extends SmsUnitBusinessAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveComponentAction(mapping, form, "unitBusiness", "businessConfigs", "unitConfigId", "refreshSmsUnitConfig", false, request, response);
    }
}