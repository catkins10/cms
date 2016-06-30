package com.yuanluesoft.jeaf.payment.actions.payment;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class PaymentAction extends BaseAction {
	
	/**
	 * 重定向到指定页面
	 * @param pageURL
	 * @param response
	 * @throws Exception
	 */
	protected void redirectToPage(String pageURL, HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
     	writer.println("<html><script>");
     	writer.println("window.location.replace('" + pageURL + "');");
     	writer.println("</script></html>");
	}
}
