package com.yuanluesoft.jeaf.payment.actions.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.service.PaymentService;

/**
 * 
 * @author linchuan
 *
 */
public class Complete extends PaymentAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PaymentService paymentService = (PaymentService)getService("paymentService");
    	Payment payment = paymentService.completePayment(request); //完成支付
    	if("redirect".equals(payment.getPageMode())) { //重定向方式
    		String url = payment.getRedirectOfSuccess();
    		if(payment.getTransactSuccess()!='1' && payment.getRedirectOfFailed()!=null) { //支付不成功
    			url = payment.getRedirectOfFailed();
    		}
    		url += (url.indexOf('?')==-1 ? "?" : "&") + "paymentId=" + payment.getId() +
    			   "&paymentSuccess=" + (payment.getTransactSuccess()=='1' ? "true" : "false");
    		redirectToPage(url, response);
    	}
    	else { //对话框方式或者弹出页面方式
    		//重定向到到paymentFrom所在服务器的根目录的blank.html
    		String from = payment.getPaymentFrom();
    		int index = from.indexOf('/', from.indexOf("://") + 1);
    		if(index!=-1) {
    			from = from.substring(0, index);
    		}
    		from += "/blank.html?paymentId=" + payment.getId() + "&paymentSuccess=" + (payment.getTransactSuccess()=='1' ? "true" : "false");
    		if("dialog".equals(payment.getPageMode())) { //对话框方式
    			from = "<html><script>window.location.replace('" + from + "')</script></html>";
    		}
    		else { //弹出窗口方式
    			from = "<html><script>window.parent.location.replace('" + from + "')</script></html>";
    		}
    		response.getWriter().write(from);
    	}
    	return null;
    }
}