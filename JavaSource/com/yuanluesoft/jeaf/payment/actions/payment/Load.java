package com.yuanluesoft.jeaf.payment.actions.payment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.forms.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.payment.service.PaymentService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends PaymentAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//创建支付记录
    	Payment paymentForm = (Payment)form;
    	PaymentService paymentService = (PaymentService)getService("paymentService");
    	com.yuanluesoft.jeaf.payment.pojo.Payment payment = paymentService.createPayment(paymentForm.getApplicationName(), paymentForm.getOrderId(), paymentForm.getPayerId(), paymentForm.getPayerName(), paymentForm.getPaymentReason(), paymentForm.getMoney(), paymentForm.getProviderId(), paymentForm.getProviderName(), paymentForm.getProviderMoney(), paymentForm.getPageMode(), paymentForm.getFrom(), paymentForm.getRedirectOfSuccess(), paymentForm.getRedirectOfFailed(), request.getRemoteHost());
		paymentForm.setId(payment.getId());
    	if(payment.getTransactSuccess()=='1') { //已经支付过
    		return mapping.findForward("paid");
    	}
    	//获取商户列表
    	List paymentMerchants = paymentService.listPaymentMerchants(paymentForm.getMerchantIds(), paymentForm.isB2cOnly(), paymentForm.isB2bOnly());
    	if(paymentMerchants==null || paymentMerchants.isEmpty()) {
    		throw new Exception("no payment merchant found");
    	}
    	if(paymentMerchants.size()>1) { //不只一个商户
    		return mapping.findForward("chooseMerchant");
    	}
    	PaymentMerchant paymentMerchant = (PaymentMerchant)paymentMerchants.get(0);
		paymentForm.setSelectedMerchantId(paymentMerchant.getId());
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
    	long siteId = RequestUtils.getParameterLongValue(request, "siteId");
    	response.sendRedirect(request.getContextPath() + "/jeaf/payment/selectPaymentMerchant.shtml?id=" + paymentForm.getId() + "&selectedMerchantId=" + paymentMerchant.getId() + "&b2cOnly=" + paymentForm.isB2cOnly() + "&b2bOnly=" + paymentForm.isB2bOnly() + (siteId==0 ? "" : "&siteId=" + siteId));
    	return null;
    }
}