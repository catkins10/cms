<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic"%>

<html:html>
<head>
	<title>系统提示 - 互动教育网</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
</head>
<body>
	<br>
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:86%" align="center">
		<tr>
			<td style="color:#ff0000; padding:30px; font-size:14px" align="center"><ext:write property="actionResult"/></td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px;display:none">
				<input type="button" class="button" value="关闭窗口" style="width:60px" onclick="top.close();">
			</td>
		</tr>
	</table>
</body>
</html:html>