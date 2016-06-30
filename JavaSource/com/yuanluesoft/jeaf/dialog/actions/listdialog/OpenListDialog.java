package com.yuanluesoft.jeaf.dialog.actions.listdialog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.dialog.actions.SelectDialogAction;

/**
 * 
 * @author linchuan
 *
 */
public class OpenListDialog extends SelectDialogAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeOpenDialogAction(mapping, form, request, response);
    }
}