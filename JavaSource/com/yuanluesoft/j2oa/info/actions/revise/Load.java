package com.yuanluesoft.j2oa.info.actions.revise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.info.forms.ReviseInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends ReviseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward actionForward = executeLoadAction(mapping, form, request, response);
    	ReviseInfo reviseForm = (ReviseInfo)form;
    	if(!OPEN_MODE_CREATE.equals(getOpenMode(reviseForm, request))) {
    		response.sendRedirect((reviseForm.getIsReceive()==1 ? "infoReceive" : "info") + ".shtml?act=edit&id=" + reviseForm.getInfoId() + (reviseForm.getEditTime()==null ? "&tabSelected=revises" : ""));
    		return null;
    	}
    	return actionForward;
    }
}