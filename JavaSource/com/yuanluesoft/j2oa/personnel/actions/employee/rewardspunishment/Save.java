package com.yuanluesoft.j2oa.personnel.actions.employee.rewardspunishment;

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
public class Save extends RewardsPunishmentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveComponentAction(mapping, form, "rewardsPunishment", "rewardsPunishments", "employeeId", "refreshEmployee", false, request, response);
    }
}