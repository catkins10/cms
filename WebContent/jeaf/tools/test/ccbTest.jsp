<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.yuanluesoft.jeaf.payment.paymentmethod.ccbbank.service.spring.CcbbankPaymentMethodService"%>
<%@page import="com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant"%>
<%@page import="com.yuanluesoft.jeaf.util.DateTimeUtils"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	try {
		Object paymentService = (Object)Environment.getService("paymentService");
		PaymentMerchant paymentMerchant = (PaymentMerchant)paymentService.getClass().getMethod("loadPaymentMerchant", new Class[]{long.class, boolean.class}).invoke(paymentService, new Object[]{new Long(530304142804370000l), Boolean.TRUE});
		CcbbankPaymentMethodService paymentMethodService = (CcbbankPaymentMethodService)Environment.getService("ccbbankPaymentMethodService");
		paymentMethodService.listTransactions6WY101(DateTimeUtils.parseDate("2015-9-28", null), paymentMerchant); 
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
	out.print("completed.");
%>
</body>
</html>