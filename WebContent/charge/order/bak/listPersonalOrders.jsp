<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
	<link href="<%=request.getContextPath()%>/edu/css/application.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
	<ext:form action="/displayPersonalOrders">
	   	<b>我订购的服务：</b>
		<jsp:include page="/edu/view/viewPackage.jsp" />
	</ext:form>
</body>
</html:html>