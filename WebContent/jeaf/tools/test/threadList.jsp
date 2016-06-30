<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.Set"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	
	try {
		Set threads = Thread.getAllStackTraces().keySet();
		for(Iterator iterator = threads.iterator(); iterator.hasNext();) {
			Thread thread = (Thread)iterator.next();
			//if(thread.getName().startsWith("Timer-")) {
				out.println("thread: " + thread.getName() + "," + thread.getState() + "<br/>");
				StackTraceElement[] elements = thread.getStackTrace();
				for(int i=0; i<elements.length; i++) {
					out.println("   StackTraceElement: " + elements[i].getClassName() + "," + elements[i].getFileName() + "," + elements[i].getLineNumber() + "," + elements[i].getMethodName() + "<br>");
				}
				out.println("<br/><br/>");
			//}
		}
	}
	catch(Exception e) {
		out.println(e + e.getMessage());
	}
%>
</body>
</html>