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
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						系统提示
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:86%" align="center">
		<tr>
			<td style="color:#ff0000; padding:30px; font-size:14px" align="center"><ext:write property="actionResult"/></td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px">
				<input type="button" class="button" value="关闭窗口" style="width:60px" onclick="window.close();">
			</td>
		</tr>
	</table>
</body>
</html:html>