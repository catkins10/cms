<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveRelationLink">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">链接名称：</td>
			<td><ext:field property="relationLink.linkName"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">链接地址：</td>
			<td><ext:field property="relationLink.linkUrl"/></td>
		</tr>
	</table>
</ext:form>