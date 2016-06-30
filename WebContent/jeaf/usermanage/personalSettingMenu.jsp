<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
</head>
<style>
	
</style>
<body style="overflow:hidden; margin:5px">
	<table cellSpacing="3" cellPadding="3" border="0" style="width:100%">
		<tr><td style="cursor:pointer" onclick="top.frames[2].location='personalData.shtml'">个人资料</td></tr>
		<tr><td style="cursor:pointer" onclick="DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/sso/changePassword.shtml', 430, 260);">修改密码</td></tr>
		<tr><td style="cursor:pointer;display:none" onclick="top.frames[2].location='../charge/account/personalAccount.shtml'">个人账户</td></tr>
		<tr><td style="cursor:pointer;display:none">交易记录</td></tr>
	</table>
</body>
</html>