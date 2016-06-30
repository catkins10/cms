package com.yuanluesoft.telex.base.actions.total;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.telex.base.service.TelexService;

/**
 * 
 * @author linchuan
 *
 */
public class Total extends TotalAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSubmitAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TelexService telexService = (TelexService)getService("telexService");
        com.yuanluesoft.telex.base.forms.Total totalForm = (com.yuanluesoft.telex.base.forms.Total)form;
       	totalForm.setTotals(telexService.totalTelegram(totalForm.getBeginDate(), totalForm.getEndDate(), totalForm.getSelectedTelegramLevels(), totalForm.getSelectedSecurityLevels(), totalForm.getFromUnitId()));
	}
}