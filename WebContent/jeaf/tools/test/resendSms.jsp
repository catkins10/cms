<%@page import="java.sql.Timestamp"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@page import="com.yuanluesoft.jeaf.sms.pojo.SmsSend"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	
	try {
		Object smsService = (Object)Environment.getService("smsService");
		//public void sendShortMessage(long senderId, String senderName, long senderUnitId, String smsBusinessName, String receiverIds, String receiverNumbers, String message, Timestamp sendTime, String remark, boolean arriveCheck) throws ServiceException {
		Method method = smsService.getClass().getMethod("sendShortMessage", new Class[]{long.class, String.class, long.class, String.class, String.class, String.class, String.class, Timestamp.class, String.class, boolean.class});
		out.println(method);
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		for(int i=0; ; i+=200) {
			String hql = "from SmsSend SmsSend" +
						 " where SmsSend.senderNumber like '% %'" +
						 " and SmsSend.message like '%龙海%'";
			List messages = databaseService.findRecordsByHql(hql, i, 200);
			if(messages==null || messages.isEmpty()) {
				break;
			}
			for(Iterator iterator = messages.iterator(); iterator.hasNext();) {
				SmsSend sms = (SmsSend)iterator.next();
				out.println(sms.getSenderNumber() + "," + sms.getSenderUnitId() + "," + sms.getMessage());
				method.invoke(smsService, new Object[]{new Long(sms.getSenderId()), sms.getSenderName(), new Long(sms.getSenderUnitId()), "民主评议", null, sms.getReceiverNumber(), sms.getMessage(), null, null, new Boolean(false)});
			}
		}
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
%>
</body>
</html>