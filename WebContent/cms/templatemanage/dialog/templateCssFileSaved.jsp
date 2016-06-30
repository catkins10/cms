<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
</head>
<body class="dialogBody" style="border:0; margin:0px">
<ext:form action="/dialog/saveTemplateCssFile" onsubmit="formOnSubmit()">
	<html:hidden property="script"/>
	<script>
		var script = document.getElementsByName('script')[0].value;
		if(script!=null) {
			DialogUtils.getDialogOpener().setTimeout(script, 300);
		}
		DialogUtils.closeDialog();
	</script>
</ext:form>
</body>
</html>