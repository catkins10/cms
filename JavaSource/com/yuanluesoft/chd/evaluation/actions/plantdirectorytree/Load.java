package com.yuanluesoft.chd.evaluation.actions.plantdirectorytree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.chd.evaluation.actions.select.SelectAction;
import com.yuanluesoft.chd.evaluation.forms.Select;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends SelectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	externalAction = true;
    	Select selectForm = (Select)form;
    	selectForm.setParentNodeId(RequestUtils.getParameterStringValue(request, "plantId"));
    	return executeOpenDialogAction(mapping, form, request, response);
    }
}