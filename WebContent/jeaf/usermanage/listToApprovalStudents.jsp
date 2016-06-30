<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html:html>
<head>
	<title>待审批学生列表</title>
</head>
<body style="margin:0; padding-left:5px; padding-right:5px">
    <ext:form action="/approvalRegistStudents">
    	<jsp:include page="viewPackage.jsp"/>
    </ext:form>
</body>
</html:html>