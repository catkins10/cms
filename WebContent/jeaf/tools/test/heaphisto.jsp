<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.lang.management.RuntimeMXBean"%>
<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="java.io.InputStream"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	long t = System.currentTimeMillis();
	RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
	String name = bean.getName();
	int index = name.indexOf('@');
	String pid = name.substring(0, index);
	//这里要区分操作系统
	sun.tools.attach.HotSpotVirtualMachine machine = (sun.tools.attach.HotSpotVirtualMachine)new sun.tools.attach.WindowsAttachProvider().attachVirtualMachine(pid);
	InputStream is = machine.heapHisto(new String[]{"-all"});
	
	int readed;
	byte[] buff = new byte[1024];
	while((readed = is.read(buff)) > 0) {
	    out.println(new String(buff, 0, readed) + "<br/>");
	}
	is.close();
	machine.detach();
	out.println("" + (System.currentTimeMillis() - t));
%>
</body>
</html>