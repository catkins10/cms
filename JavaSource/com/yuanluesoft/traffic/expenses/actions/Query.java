package com.yuanluesoft.traffic.expenses.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.traffic.expenses.pojo.Expenses;
import com.yuanluesoft.traffic.expenses.service.ExpensesService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Query extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.yuanluesoft.traffic.expenses.forms.Query queryForm = (com.yuanluesoft.traffic.expenses.forms.Query)form;
    	ExpensesService expensesService = (ExpensesService)getService("expensesService");
    	Expenses expenses = expensesService.getExpenses(queryForm.getPlateNumberPrefix(), queryForm.getPlateNumber(), queryForm.getVehicleType());
    	if(expenses!=null) {
    		PropertyUtils.copyProperties(queryForm, expenses);
    	}
        return mapping.getInputForward();
    }
}