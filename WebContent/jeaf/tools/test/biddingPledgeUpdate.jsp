<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"npztbadmin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	
	try {
		Object biddingService = (Object)Environment.getService("biddingService");
		biddingService.getClass().getMethod("bankTransactionsQuery", null).invoke(biddingService, null);
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
	out.print("completed.");
%>
</body>
</html>