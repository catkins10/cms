<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSetTop">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr valign="middle">
			<td nowrap="nowrap">范围：</td>
			<td width="100%"><ext:field property="selectedDirectoryIds"/></td>
		</tr>
		<tr valign="middle">
			<td nowrap="nowrap">截止时间：</td>
			<td width="100%"><ext:field property="expire"/></td>
		</tr>
	</table>
</ext:form>