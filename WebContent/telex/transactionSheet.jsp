<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveTransactionSheet">
   	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td>名称：</td>
			<td><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">电报类型：</td>
			<td><ext:field property="telegramType"/></td>
		</tr>
		<tr>
			<td valign="top">办理单：</td>
			<td><ext:field property="body"/></td>
		</tr>
	</table>
</ext:form>