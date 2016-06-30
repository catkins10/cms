<%@page import="com.yuanluesoft.jeaf.patch.updatesiteresource.UpdateOtherColumns"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	new UpdateOtherColumns().updateOtherColumns();
	out.print("completed.");
%>