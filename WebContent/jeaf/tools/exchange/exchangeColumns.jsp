<%@page import="java.io.Serializable"%>
<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	if(!request.getServerName().equals("localhost")) {
		out.println("Failed, run on localhost only.");
	}
	else {
		Object exchangeClient = (Object)Environment.getService("exchangeClient");
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		String hql = "from WebDirectory WebDirectory order by WebDirectory.id";
		for(int i=0; ; i+=200) {
			List columns = databaseService.findRecordsByHql(hql, i, 200);
			if(columns==null || columns.isEmpty()) {
				break;
			}
			for(Iterator iterator = columns.iterator(); iterator.hasNext();) {
				Serializable column = (Serializable)iterator.next();
				//public void synchUpdate(Serializable object, String receiverNames, int delayMilliseconds) throws ExchangeException;
				exchangeClient.getClass().getMethod("synchUpdate", new Class[]{Serializable.class, String.class, int.class}).invoke(exchangeClient, new Object[]{column, null, new Integer(2000)});
			}
			if(columns.size()<200) {
				break;
			}
		}
		out.println("completed");
	}
%>