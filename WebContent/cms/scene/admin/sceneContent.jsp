<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title><ext:write property="formTitle"/></title>
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
</head>
<body style="border-style:none; background:#ffffff; margin:5px">
<ext:form action="/admin/saveSceneContent" onsubmit="return formOnSubmit();">
   	<jsp:include page="sceneDirectory.jsp" />
</ext:form>
</body>
</html:html>