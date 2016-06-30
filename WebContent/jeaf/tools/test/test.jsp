<%@page import="com.yuanluesoft.jeaf.business.util.FieldUtils"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>

<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	out.println(FieldUtils.getRecordField("com.yuanluesoft.cms.siteresource.pojo.SiteResource", "images", null).getParameter("maxSaveSize"));
%>