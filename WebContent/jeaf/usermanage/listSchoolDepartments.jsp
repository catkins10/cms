<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html:html>
<head>
	<title>部门/教研组列表</title>
</head>
<body style="margin:0; padding-left:5px; padding-right:5px">
    <ext:form action="/displaySchoolDepartmentView">
    	<jsp:include page="viewPackage.jsp"/>
    </ext:form>
</body>
</html:html>