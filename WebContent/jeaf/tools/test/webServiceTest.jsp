<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.soap.SoapConnectionPool"%>
<%@page import="com.yuanluesoft.jeaf.soap.SoapPassport"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	SoapConnectionPool soapConnectionPool = new SoapConnectionPool();
	SoapPassport soapPassport = new SoapPassport();
	soapPassport.setUrl("http://127.0.0.1/cms/services/");
	try {
		out.println(soapConnectionPool.invokeRemoteMethod("SsoSessionService", "getLoginToken", soapPassport, new Object[]{}, null));
	}
	catch (Exception e) {
		e.printStackTrace();
	}
%>
</body>
</html>