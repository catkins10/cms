<%@ page contentType="text/html; charset=UTF-8" %>

<%	
	Exception e = (Exception)request.getAttribute("org.apache.struts.action.EXCEPTION");
	com.yuanluesoft.jeaf.logger.Logger.exception(e);
%>
<html><body>系统错误</body></html>