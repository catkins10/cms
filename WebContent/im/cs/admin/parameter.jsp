<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveParameter">
   	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<col valign="middle" align="right">
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap">欢迎辞:</td>
			<td><ext:field property="welcome"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">对话超时时限(分钟):</td>
			<td><ext:field property="chatTimeout"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">评价等级:</td>
			<td><ext:field property="evaluateLevels"/></td>
		</tr>
	</table>
</ext:form>