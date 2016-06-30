<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveAgent">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
		<col width="60px" valign="middle" align="right">
		<col width="100%" valign="middle">
		<tr>
			<td>用户名：</td>
			<td><ext:field property="userName"/></td>
		</tr>
		<tr valign="middle">
			<td>代理人：</td>
			<td><ext:field property="agentNames"/></td>
		</tr>
	</table>
</ext:form>