<%@ page contentType="text/html; charset=UTF-8"%><%@page import="java.util.List"%><%@page import="com.yuanluesoft.jeaf.sms.client.mas.SmsClientImpl"%><%@page import="com.yuanluesoft.jeaf.util.Environment"%><%
	request.setCharacterEncoding ("UTF-8"); //提供UTF-8字符集支持
	System.out.println(request.getParameter("msg"));
	Object smsService = (Object)Environment.getService("smsService");
	List clients = (List)smsService.getClass().getMethod("getSmsClients", null).invoke(smsService, null);
	((SmsClientImpl)clients.iterator().next()).processSmsCallbackRequest(request, response);
%>