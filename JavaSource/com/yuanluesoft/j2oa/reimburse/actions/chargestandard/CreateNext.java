package com.yuanluesoft.j2oa.reimburse.actions.chargestandard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.reimburse.forms.ChargeStandardForm;

/**
 * 
 * @author linchuan
 *
 */
public class CreateNext extends ChargeStandardAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeCreateNextAction(mapping, form, request, response);
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
     */
    public void inheritProperties(com.yuanluesoft.jeaf.form.ActionForm newForm, com.yuanluesoft.jeaf.form.ActionForm currentForm) {
        super.inheritProperties(newForm, currentForm);
        ChargeStandardForm newStandardForm = (ChargeStandardForm)newForm;
        ChargeStandardForm currentStandardForm = (ChargeStandardForm)currentForm;
        //继承用户信息
        newStandardForm.setUserId(currentStandardForm.getUserId());
        newStandardForm.setUserName(currentStandardForm.getUserName());
    }
}