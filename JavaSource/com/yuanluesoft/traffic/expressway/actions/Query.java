package com.yuanluesoft.traffic.expressway.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.traffic.expressway.pojo.ExpresswayPrice;
import com.yuanluesoft.traffic.expressway.service.ExpresswayService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Query extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.yuanluesoft.traffic.expressway.forms.Query queryForm = (com.yuanluesoft.traffic.expressway.forms.Query)form;
        ExpresswayService expresswayService = (ExpresswayService)getService("expresswayService");
        ExpresswayPrice price = expresswayService.getPrice(queryForm.getEntry(), queryForm.getExit());
    	if(price!=null) {
    		PropertyUtils.copyProperties(queryForm, price);
    	}
        return mapping.getInputForward();
    }
}