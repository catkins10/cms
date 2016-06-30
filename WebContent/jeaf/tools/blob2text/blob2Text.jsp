<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<html:html>
<head>
	<title>blob2text</title>
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
</head>
<body>
<ext:form action="/convert">
	<br><center class="title">blob2text</center><br>
	<table width="100%" style="table-layout:fixed" border="1" cellpadding="0" cellspacing="0" class="table">
		<col valign="middle" width="120px">
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">数据库类型</td>
			<td class="tdcontent">
				<html:radio property="dbmsType" value="mysql" styleClass="radio"/>mysql
				<html:radio property="dbmsType" value="oracle" styleClass="radio"/>oracle
			</td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">数据库服务器IP</td>
			<td class="tdcontent"><html:text property="serverIp" styleClass="field required" value="localhost"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">数据库服务器I端口</td>
			<td class="tdcontent"><html:text property="serverPort" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">数据库名称</td>
			<td class="tdcontent"><html:text property="dbName" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">用户名</td>
			<td class="tdcontent"><html:text property="userName" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">密码</td>
			<td class="tdcontent"><html:password property="password" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">表名称</td>
			<td class="tdcontent"><html:text property="tableName" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">blob字段名称</td>
			<td class="tdcontent"><html:text property="fromBlobField" styleClass="field required"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">text字段名称</td>
			<td class="tdcontent"><html:text property="toTextField" styleClass="field required"/></td>
		</tr>
	</table>
	<br>
	<center>
		<input type="button" value="转换" class="button" onclick="FormUtils.submitForm()">
	</center>
</ext:form>
</body>
</html:html>
