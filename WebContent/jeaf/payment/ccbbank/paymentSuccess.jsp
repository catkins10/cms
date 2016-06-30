<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	String orderId = request.getParameter("ORDERID");
	if(orderId==null) {
		orderId = request.getParameter("ORDER_NUMBER");
	}
	String remark = request.getParameter("REMARK1");
	String url;
	if("zfcg".equals(remark)) { 
		url = "http://www.npzbtb.gov.cn/ccb/completePayment.asp?paymentId=" + Long.parseLong(orderId);
	}
	else {
		url = request.getContextPath() + "/jeaf/payment/completePayment.shtml?paymentId=" + Long.parseLong(orderId)  + "&query=" + URLEncoder.encode(new String(request.getQueryString().getBytes("ISO-8859-1"), "gbk"), "utf-8");
	}
	response.sendRedirect(url);
%>