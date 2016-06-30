<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
	<head>
		<title><ext:write property="formTitle"/></title>
		<style>
			html,body,form {
				height: 100%;
			}
		</style>
	</head>
	<body style="border-style:none; overflow:hidden;" leftmargin="0" topmargin="0" bottommargin="0" rightmargin="0">
		<ext:form action="<%=((org.apache.struts.action.ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance")).getPath()%>">
			<jsp:include page="templateCommon.jsp"/>
		</ext:form>
	</body>
</html>