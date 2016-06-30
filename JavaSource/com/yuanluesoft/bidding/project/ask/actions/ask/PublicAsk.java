package com.yuanluesoft.bidding.project.ask.actions.ask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.ask.forms.admin.Ask;

/**
 * 
 * @author linchuan
 *
 */
public class PublicAsk extends AskAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Ask askForm = (Ask)form;
    	return executeSaveAction(mapping, form, request, response, false, null, (askForm.getIsPublic()=='1' ? "发布成功" : "撤销成功"), "outerResult");
    }
}