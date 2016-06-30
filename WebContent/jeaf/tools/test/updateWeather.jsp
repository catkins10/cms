<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.weather.service.WeatherService"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	WeatherService weatherService = (WeatherService)Environment.getService("weatherService");
	weatherService.updateWeather();
%>
</body>
</html>