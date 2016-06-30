<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.util.Encoder"%>
<%@page import="java.beans.PropertyDescriptor"%>

<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	Object dataSource = Environment.getService("dataSource");
	PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(dataSource);
	for(int i=0; i<properties.length; i++) {
		try {
			String propertyName = properties[i].getName();
			if(",connection,".indexOf("," + propertyName + ",")==-1) {
				String value = PropertyUtils.getProperty(dataSource, propertyName).toString();
				if(",username,password,".indexOf("," + propertyName + ",")!=-1) {
					value = Encoder.getInstance().desEncode(value, "andy0718", "utf-8", null);
				}
				out.print(propertyName + "=" + value + "<br/>");
			}
		}
		catch(Exception e) {
			
		}
	}
%>
</body>
</html>