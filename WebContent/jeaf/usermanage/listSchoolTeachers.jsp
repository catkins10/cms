<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>

<html:html>
<head>
	<title>我的学校的教师</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/usermanage/js/usermanage.js"></script>
</head>
<body style="margin:0; padding-left:5px; padding-right:5px">
    <ext:form action="/displaySchoolTeacherView">
    	<jsp:include page="viewPackage.jsp"/>
    </ext:form>
</body>
</html:html>
