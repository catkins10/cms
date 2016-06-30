<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveMenuItem">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap">名称：</td>
			<td><ext:field property="menuItem.name"/></td>
		</tr>
		<ext:equal value="1" property="menuItem.type">
			<tr>
				<td nowrap="nowrap">链接地址：</td>
				<td><ext:field property="menuItem.url"/></td>
			</tr>
		</ext:equal>
	</table>
</ext:form>