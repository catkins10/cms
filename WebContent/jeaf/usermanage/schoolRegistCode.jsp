<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<html>
<head>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
</head>
<style>
	
</style>
<body style="overflow:hidden; margin:5px">
<ext:form action="/saveSchoolRegistCode">
	<table cellSpacing="3" cellPadding="3" border="0" style="width:300px" align="center">
		<tr>
			<td width="50px">验证码：</td>
			<td><html:password property="code"/></td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top:10px" align="center">
				<input type="button" value="提交" class="button" onclick="if(document.getElementsByName('code')[0].value!='')FormUtils.submitForm();">
			</td>
		</tr>
	</table>
	<html:hidden property="schoolId"/>
</ext:form>
</body>
</html>