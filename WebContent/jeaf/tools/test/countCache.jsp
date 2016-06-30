<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.Collection"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	
	try {
		Object cache = (Object)Environment.getService("requestCodeCache");
		Collection keys = (Collection)PropertyUtils.getProperty(cache, "keys");
		out.print(keys.size());
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
%>
</body>
</html>