package com.yuanluesoft.j2oa.reimburse.actions.reimburse.charge;

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
public class Save extends ChargeAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return executeSaveComponentAction(mapping, form, "charge", "charges", "reimburseId", "refreshReimburse", false, request, response);
    }
}