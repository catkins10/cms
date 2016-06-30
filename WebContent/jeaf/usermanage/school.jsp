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
<ext:form action="/saveSchool">
	<table cellSpacing="3" cellPadding="3" border="0" style="width:300px" align="center">
		<tr>
			<td width="50px">学校名称：</td>
			<td><html:text property="name" style="required"/></td>
		</tr>
		<tr>
			<td width="50px">学校全称：</td>
			<td><html:text property="fullName"/></td>
		</tr>
		<tr>
			<td width="50px">学校类别：</td>
			<td><html:text property="type"/></td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top:10px" align="center">
				<input type="button" value="提交" class="button" onclick="if(document.getElementsByName('code')[0].value!='')FormUtils.submitForm();">
			</td>
		</tr>
	</table>
</ext:form>
</body>
</html>