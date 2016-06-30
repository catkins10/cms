<%@page import="com.yuanluesoft.jeaf.sms.client.SmsClient"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>

<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	Object smsService = Environment.getService("smsService");
	List clients = (List)PropertyUtils.getProperty(smsService, "smsClients");
	for(Iterator iterator = clients.iterator(); iterator.hasNext();) {
		SmsClient client = (SmsClient)iterator.next();
		out.println(client.getSmsNumber() + "," + client.getReceiveTimePeriod());
	}
%>