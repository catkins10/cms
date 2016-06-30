<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html:html>
<head>
	<title>登录</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<script>
		function afterLogined() {
			var callback = DialogUtils.getDialogArguments();
			if(callback) {
				callback.call(null);
			}
			DialogUtils.closeDialog();
		}
	</script>
</head>
<body onload="afterLogined()">
</body>
</html:html>