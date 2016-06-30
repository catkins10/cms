package com.yuanluesoft.jeaf.payment.actions.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.forms.Payment;
import com.yuanluesoft.jeaf.payment.service.PaymentService;

/**
 * 选择帐户类型
 * @author linchuan
 *
 */
public class SelectAccountType extends PaymentAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Payment paymentForm = (Payment)form;
    	PaymentService paymentService = (PaymentService)getService("paymentService");
		//跳转到银行页面
    	try {
    		paymentService.redirectToBankPage(paymentForm.getId(), paymentForm.getSelectedMerchantId(), paymentForm.getSelectedAccountType(), request, response);
    		return null;
    	}
		catch(ServiceException se) {
			if("paid".equals(se.getMessage())) {
				return mapping.findForward("paid");
			}
			throw se;
		}
    }
}