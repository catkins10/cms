package com.yuanluesoft.jeaf.payment.actions.payment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.forms.Payment;
import com.yuanluesoft.jeaf.payment.service.PaymentService;

/**
 * 选择商户
 * @author linchuan
 *
 */
public class SelectPaymentMerchant extends PaymentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Payment paymentForm = (Payment)form;
    	PaymentService paymentService = (PaymentService)getService("paymentService");
    	//获取支付方式支持的帐户类型
    	List accountTypes = paymentService.listAccountTypes(paymentForm.getSelectedMerchantId(), paymentForm.isB2cOnly(), paymentForm.isB2bOnly());
    	if(accountTypes.size()==1) { //只有一种帐户类型
    		//直接跳转到银行页面
    		try {
	    		paymentService.redirectToBankPage(paymentForm.getId(), paymentForm.getSelectedMerchantId(), (String)accountTypes.get(0), request, response);
	    		return null;
    		}
    		catch(ServiceException se) {
    			if("paid".equals(se.getMessage())) {
    				return mapping.findForward("paid");
    			}
    			throw se;
    		}
    	}
    	//选择帐户类型
    	return mapping.findForward("selectAccountType");
    }
}