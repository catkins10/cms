<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>修改密码</title>
	<link href="<%=request.getContextPath()%>/cms/css/application.css" type="text/css" rel="stylesheet">
</head>
<body style="border-style:none; overflow:hidden">
	<%request.setAttribute("writeFormPrompt", "true");%>
	<div class="prompt"><ext:write property="actionResult"/></div>
</body>
</html>