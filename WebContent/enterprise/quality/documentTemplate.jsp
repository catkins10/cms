<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>
		<ext:empty name="documentTemplate" property="templateName">模板配置</ext:empty>
		<ext:notEmpty name="documentTemplate" property="templateName"><ext:write name="documentTemplate" property="templateName"/> - 模板</ext:notEmpty>
	</title>
</head>
<body style="border-style:none; overflow:hidden;" leftmargin="0" topmargin="0" bottommargin="0" rightmargin="0">
<ext:form action="/saveDocumentTemplate" onsubmit="if(formOnSubmit)return formOnSubmit();">
	<jsp:include page="/cms/templatemanage/templateCommon.jsp"/>
</ext:form>
</body>
</html:html>