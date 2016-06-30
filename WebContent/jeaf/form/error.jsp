<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
if(com.yuanluesoft.jeaf.util.RequestUtils.getRequestInfo(request).isClientRequest()) {
	response.setStatus(500);
	return;
}
Exception e = (Exception)request.getAttribute("org.apache.struts.action.EXCEPTION");
if(!(e instanceof com.yuanluesoft.jeaf.system.exception.SystemUnregistException)) { %>
	<ext:form siteFormServiceName="userFormService" applicationName="jeaf/usermanage" pageName="error">
		<pre>
<%			com.yuanluesoft.jeaf.logger.Logger.exception(e);
			e.printStackTrace(new java.io.PrintWriter(out)); %>
		</pre>
	</ext:form>
<%	return;
}
if("dialog".equals(request.getParameter("displayMode"))) {
	response.sendRedirect(request.getContextPath() + "/jeaf/system/regist.shtml?displayMode=dialog");
	return;
} %>
<html>
	<head>
		<script src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	</head>
	<body>
		<script language="javascript">
			DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/system/regist.shtml', 480, 200);
		</script>
	</body>
</html>