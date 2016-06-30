<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<title>数据导入</title>
	<link href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet" type="text/css" />
	<script src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
</head>
<body style="background-color: #ffffff; margin:5px">
	<ext:form action="/doImportData">
		<ext:subForm/>
	</ext:form>
</body>
</html>